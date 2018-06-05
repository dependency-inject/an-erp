<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <div>
                                <div class="info-item"><label>{{ $t('field.ORDER.BILL_NO') }}： </label>{{ item.billNo }}</div>
                                <div class="info-item"><label>{{ $t('field.ORDER.SALES_MAN') }}： </label>{{ item.salesName }}</div>
                                <div class="info-item"><label>{{ $t('field.ORDER.BILL_TIME') }}： </label>{{ item.billTimeLocal }}</div>
                                <div class="info-item"><label>{{ $t('field.ORDER.BILL_AMOUNT') }}： </label>{{ billAmount }}</div>
                                <div class="info-item"><label>{{ $t('field.ORDER.BILL_STATE') }}： </label>{{ item.billStateCn }}</div>
                            </div>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.ELSE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.ORDER.CLIENT')" prop="clientId">
                                <common-select type="client" v-model="item.clientId" :disabled="item.billId!==0"></common-select>
                            </form-item>
                            <form-item :label="$t('field.ORDER.CONTACT')" prop="contact"><i-input v-model="item.contact"></i-input></form-item>
                            <form-item :label="$t('field.ORDER.CONTACT_PHONE')" prop="contactPhone"><i-input v-model="item.contactPhone"></i-input></form-item>
                            <form-item :label="$t('field.ORDER.REMARK')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.ORDER.PRODUCT_INFO') }}&nbsp;&nbsp;<span v-if="editable">（<a class="remark" @click="addProduct"><icon type="plus"></icon> {{ $t('common.ADD')+$t('field.ORDER.PRODUCT_INFO') }}</a>）</span></div>
                        <div class="panel-body">
                            <i-table border :columns="columnList" :data="item.productList"></i-table>
                        </div>
                    </div>
                    <div class="chief-panel" v-if="item.billState===3||item.billState===4">
                        <div class="panel-header">{{ $t('field.ORDER.MATERIAL_REQUIRED_INFO') }}</div>
                        <div class="panel-body">
                            <i-table border :columns="columnList2" :data="materialList"></i-table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="editable">{{ $t('common.SAVE') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="audit" v-if="orderAuditPermission&&item.billId!==0&&item.billState===1">{{ $t('common.AUDIT') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="unaudit" v-if="orderAuditPermission&&item.billId!==0&&item.billState===2">{{ $t('common.UNAUDIT') }}</i-button>
            <i-button class="operate-btn" type="success" shape="circle" @click="produce" v-if="orderProducePermission&&item.billId!==0&&item.billState===2">{{ $t('common.PRODUCE') }}</i-button>
            <i-button class="operate-btn" type="success" shape="circle" @click="delivery" v-if="orderDeliveryPermission&&item.billId!==0&&item.billState===3">{{ $t('common.DELIVERY') }}</i-button>
            <i-button class="operate-btn" type="error" shape="circle" @click="cancel" v-if="orderCancelPermission&&item.billId!==0&&item.billState===3">{{ $t('common.CANCEL') }}</i-button>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="saveProduct" :loading="true">
            <i-form ref="formValidate2" :model="modal.item" :rules="rules2" :label-width="90">
                <form-item :label="$t('field.ORDER.PRODUCT')" prop="productId">
                    <common-select type="product" v-model="modal.item.productId" :query-parameters="{closed:0}" @on-change="productSelectChange"></common-select>
                </form-item>
                <form-item :label="$t('field.ORDER.QUANTITY')" prop="quantity"><input-number v-model="modal.item.quantity" :min="1" style="width:100%"></input-number></form-item>
                <form-item :label="$t('field.ORDER.REMARK')" prop="remark"><i-input v-model="modal.item.remark" type="textarea"></i-input></form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import commonSelect from '../../components/common-select';

import orderService from '../../service/order';

export default {
    mixins: [ Permission ],
    data() {
        return {
            item: {
                productList: []
            },
            modal: {
                title: 'title',
                item: {
                    productId: ''
                },
                visible: false
            },
            clientList: [],
            materialList: []
        }
    },
    computed: {
        rules() {
            return {
                clientId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.ORDER.CLIENT'), trigger: 'change' }
                ]
            }
        },
        rules2() {
            return {
                productId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.ORDER.PRODUCT'), trigger: 'change' }
                ],
                quantity: [
                    { type: 'number', required: true, message: this.$t('field.ORDER.QUANTITY')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ]
            }
        },
        loginAdmin() {
            return this.$store.state.app.loginAdmin;
        },
        editable() {
            return (this.orderAddPermission && this.$route.params.id === 'add' && this.item.billId === 0) || (this.orderUpdatePermission && this.item.billId !==0 && this.item.billState === 1);
        },
        columnList() {
            let result = [
                { title: this.$t('field.ORDER.PRODUCT_NO'), key: 'productNo'},
                { title: this.$t('field.ORDER.PRODUCT_NAME'), key: 'productName'},
                { title: this.$t('field.ORDER.QUANTITY'), key: 'quantity'},
                { title: this.$t('field.ORDER.PRICE'), key: 'price'},
                { title: this.$t('field.ORDER.TOTAL'), key: 'total'},
                { title: this.$t('field.ORDER.REMARK'), key: 'remark'},
            ];
            if (this.editable) {
                result.push({ 
                    title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            this.editProduct(row) 
                        }), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => { 
                            this.removeProduct(params.index) 
                        })]);
                    } 
                });
            }
            return result;
        },
        columnList2() {
            return [
                { title: this.$t('field.ORDER.MATERIAL_NO'), key: 'materialNo'},
                { title: this.$t('field.ORDER.MATERIAL_NAME'), key: 'materialName'},
                { title: this.$t('field.ORDER.MATERIAL_QUANTITY'), key: 'quantity'},
                { title: this.$t('field.ORDER.PRODUCT_NO'), key: 'productNo'},
                { title: this.$t('field.ORDER.MATERIAL_PROPERTY'), key: 'materialProperty'},
                { title: this.$t('field.ORDER.REMARK'), key: 'productMaterialRemark'},
            ];
        },
        billAmount() {
            let result = 0;
            this.item.productList.forEach((item) => {
                result += (item.price * item.quantity);
            });
            return result;
        }
    },
    components: { commonSelect },
    methods: {
        initData() {
            // 路由检查           
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.billId = Number(this.$route.params.id);
                this.getById();
            } else if (this.$route.params.id === 'add') {
                this.setDefault();
            } else {
                this.$router.replace('/order');
            }
        },
        setDefault() {
            this.item = {
                billId: 0,
                salesName: this.loginAdmin.trueName,
                clientId: '',
                contact: '',
                contactPhone: '',
                remark: '',
                productList: []
            }
        },
        async getById() {
            let result = await orderService.getById(this.item.billId);
            if (result.status === 200) {
                this.item = result.data
                this.item.billStateCn = this.$t('field.ORDER_STATE.' + Number(this.item.billState));
                this.item.billTimeLocal = util.formatTimestamp(this.item.billTime, "yyyy-MM-dd hh:mm:ss");
                if (this.item.billState === 3 || this.item.billState === 4)
                    this.getMaterialRequired();
            } else {
                this.$router.replace('/order');
            }
        },
        async getMaterialRequired() {
            let result = await orderService.getMaterialRequired(this.item.billId);
            if (result.status === 200) {
                this.materialList = result.data;
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let obj = Object.assign({}, this.item, { billAmount: this.billAmount, productList: JSON.stringify(this.item.productList) });
                    if (this.$route.params.id === 'add' && this.item.billId === 0) {
                        let result = await orderService.add(obj);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            var item = result.data;
                            this.$router.replace('/order/' + item.billId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await orderService.update(obj);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            this.initData();
                        } else {
                            this.$Message.error(result.data);
                        }
                    }
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                }
            });
        },
        audit() {
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await orderService.audit(this.item.billId);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.initData();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        unaudit() {
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await orderService.unaudit(this.item.billId);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.initData();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        produce() {
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await orderService.produce(this.item.billId);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.initData();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        delivery() {
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await orderService.delivery(this.item.billId);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.initData();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        cancel() {
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await orderService.cancel(this.item.billId);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.initData();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        async getClientList() {
            let result = await orderService.getClientList();
            if (result.status === 200) {
                this.clientList = result.data;
            }
        },
        addProduct() {
            this.modal.title = this.$t('common.ADD') + this.$t('field.ORDER.PRODUCT_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = -1;
            this.modal.item.productId = '';
            this.modal.item.quantity = 1;
            this.modal.item.remark = '';
            this.modal.visible = true;
        },
        editProduct(item) {
            this.modal.title = this.$t('common.EDIT') + this.$t('field.ORDER.PRODUCT_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = item._index;
            this.modal.item.productId = item.productId;
            this.modal.item.quantity = item.quantity;
            this.modal.item.remark = item.remark;
            this.modal.item.productNo = item.productNo;
            this.modal.item.productName = item.productName;
            this.modal.item.price = item.price;
            this.modal.visible = true;
        },
        saveProduct() {
            this.$refs.formValidate2.validate(async (valid) => {
                if (valid) {
                    if (this.modal.item._index === -1) {
                        this.item.productList.push({
                            productId: this.modal.item.productId,
                            productNo: this.modal.item.productNo,
                            productName: this.modal.item.productName,
                            quantity: this.modal.item.quantity,
                            price: this.modal.item.price,
                            total: this.modal.item.price * this.modal.item.quantity,
                            remark: this.modal.item.remark
                        });
                    } else {
                        this.item.productList[this.modal.item._index].productId = this.modal.item.productId;
                        this.item.productList[this.modal.item._index].productNo = this.modal.item.productNo;
                        this.item.productList[this.modal.item._index].productName = this.modal.item.productName;
                        this.item.productList[this.modal.item._index].quantity = this.modal.item.quantity;
                        this.item.productList[this.modal.item._index].price = this.modal.item.price;
                        this.item.productList[this.modal.item._index].total = this.modal.item.price * this.modal.item.quantity;
                        this.item.productList[this.modal.item._index].remark = this.modal.item.remark;
                    }
                    this.modal.visible = false;
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.abortLoading();
                }
            });
        },
        removeProduct(index) {
            if (!this.editable) return;
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: () => {
                    this.item.productList.splice(index, 1);
                }
            });
        },
        productSelectChange(item) {
            if (item === null) {
                this.modal.item.productNo = ''
                this.modal.item.productName = '';
                this.modal.item.price = 0;
            } else {
                this.modal.item.productNo = item.productNo;
                this.modal.item.productName = item.productName;
                this.modal.item.price = item.price;
            }
        }
    },
    created() {
        this.setDefault();
        this.getClientList();
        this.initData();
    },
    watch: {
        '$route'(to, from) {
            this.initData();
        }
    }
}
</script>
