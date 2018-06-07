<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.PRODUCT_INSTOCK.BILL_NO')" prop="billNo">{{ item.billNo || $t('field.NOT_AVAILABLE')  }}</form-item>
                            <form-item :label="$t('field.PRODUCT_INSTOCK.BILL_TIME')" prop="billTime">{{ item.billTimeLocal || $t('field.NOT_AVAILABLE') }}</form-item>
                            <form-item :label="$t('field.PRODUCT_INSTOCK.FROM_PRINCIPAL')" prop="fromPrincipal">
                                <common-select type="admin" v-model="item.fromPrincipal"></common-select>
                            </form-item>
                            <form-item :label="$t('field.PRODUCT_INSTOCK.WAREHOUSE_PRINCIPAL')" prop="warehousePrincipal">
                                <common-select type="admin" v-model="item.warehousePrincipal" disabled></common-select>
                            </form-item>
                            <form-item :label="$t('field.PRODUCT_INSTOCK.PRODUCT_SOURCE')" prop="productSource">
                                <i-select v-model="item.productSource" :disabled="item.billId!==0" transfer style="width:100%">
                                    <i-option v-for="item in productSourceList" :value="item.value" :key="item.value">{{ item.descript }}</i-option>
                                </i-select>
                            </form-item>
                            <form-item :label="$t('field.PRODUCT_INSTOCK.BILL_STATE')" prop="billState">{{ item.billStateCn || $t('field.NOT_AVAILABLE') }}</form-item>
                            <form-item :label="$t('field.PRODUCT_INSTOCK.REMARK')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                        </div>
                    </div>
                     <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.PRODUCT_INSTOCK.DETAIL_INFO') }}&nbsp;&nbsp;<span v-if="editable">（<a class="remark" @click="addProduct"><icon type="plus"></icon> {{ $t('common.ADD')+$t('field.PRODUCT_INSTOCK.DETAIL_INFO') }}</a>）</span></div>
                        <div class="panel-body">
                            <i-table border :columns="columnList" :data="this.item.productList"></i-table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="editable">{{ $t('common.SAVE') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="audit" v-if="productInstockAuditPermission&&item.billId!==0&&item.billState===1">{{ $t('common.AUDIT') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="unaudit" v-if="productInstockAuditPermission&&item.billId!==0&&item.billState===2">{{ $t('common.UNAUDIT') }}</i-button>
            <i-button class="operate-btn" type="success" shape="circle" @click="finish" v-if="productInstockFinishPermission&&item.billId!==0&&item.billState===2">{{ $t('common.FINISH') }}</i-button>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" @on-ok="saveProduct" :loading="true">
            <i-form ref="formValidate2" :model="modal.item" :rules="rules2" :label-width="90">
                <form-item :label="$t('field.PRODUCT_INSTOCK.PRODUCT')" prop="productId">
                    <common-select type="product" v-model="modal.item.productId" @on-change="productSelectChange"></common-select>
                </form-item>
                <form-item :label="$t('field.PRODUCT_INSTOCK.PRODUCT_QUANTITY')" prop="quantity"><input-number v-model="modal.item.quantity" :min="1" style="width:100%"></input-number></form-item>
                <form-item :label="$t('field.PRODUCT_INSTOCK.PRODUCT_PRINCIPAL')" prop="principal">
                    <common-select type="admin" v-model="modal.item.principal" @on-change="principalSelectChange"></common-select>
                </form-item>
                <form-item :label="$t('field.PRODUCT_INSTOCK.PRODUCT_WAREHOUSE')" prop="warehouse">
					<common-select type="warehouse" v-model="modal.item.warehouse" @on-change="warehouseSelectChange"></common-select>
				</form-item>
                <form-item :label="$t('field.PRODUCT_INSTOCK.PRODUCT_PLACE')" prop="place"><i-input v-model="modal.item.place" type="textarea"></i-input></form-item>
                <form-item :label="$t('field.PRODUCT_INSTOCK.PRODUCT_REMARK')" prop="remark"><i-input v-model="modal.item.remark" type="textarea"></i-input></form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission'

import util from '../../libs/util.js';

import commonSelect from '../../components/common-select';

import productInstockService from '../../service/product-instock';

export default {
    name: 'product-instock-detail',
    mixins: [ Permission ],
    data() {
        return {
            item: {
                productList: [],
            },
            modal: {
                title: 'title',
                item: {
                    productId: '',
                    principal: '',
                    warehouse: ''
                },
                visible: false
            }
        }
    },
    computed: {
        rules() {
            return {
                fromPrincipal: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.PRODUCT_INSTOCK.FROM_PRINCIPAL'), trigger: 'change' }
                ],
                productSource: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.PRODUCT_INSTOCK.PRODUCT_SOURCE'), trigger: 'change' }
                ]
            }
        },
        rules2() {
            return {
                productId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.PRODUCT_INSTOCK.PRODUCT'), trigger: 'change' }
                ],
                quantity: [
                    { type: 'number', required: true, message: this.$t('field.PRODUCT_INSTOCK.PRODUCT_QUANTITY')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                principal: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.PRODUCT_INSTOCK.PRODUCT_PRINCIPAL'), trigger: 'change' }
                ],
                warehouse: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.PRODUCT_INSTOCK.PRODUCT_WAREHOUSE'), trigger: 'change' }
                ]
            }
        },
        productSourceList() {
            return [
                { value: 1, descript: this.$t('field.PRODUCT_INSTOCK_PRODUCT_SOURCE.1') },
            ]
        },
        editable() {
            return (this.productInstockAddPermission && this.$route.params.id === 'add' && this.item.billId === 0) || (this.productInstockAddPermission && this.item.billId !==0 && this.item.billState === 1);
        },
        columnList() {
            let result = [
                { title: this.$t('field.PRODUCT_INSTOCK.PRODUCT_NO'), key: 'productNo' },
                { title: this.$t('field.PRODUCT_INSTOCK.PRODUCT_NAME'), key: 'productName' },
                { title: this.$t('field.PRODUCT_INSTOCK.PRODUCT_QUANTITY'), key: 'quantity' },
                { title: this.$t('field.PRODUCT_INSTOCK.PRODUCT_PRINCIPAL'), key: 'principalName' },
                { title: this.$t('field.PRODUCT_INSTOCK.PRODUCT_WAREHOUSE'), key: 'warehouseName' },
                { title: this.$t('field.PRODUCT_INSTOCK.PRODUCT_PLACE'), key: 'place' },
                { title: this.$t('field.PRODUCT_INSTOCK.PRODUCT_REMARK'), key: 'remark' },
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
        }
    },
    components: { commonSelect },
    methods: {
        initData() {
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.billId = Number(this.$route.params.id);
                this.getById();
            } else if (this.$route.params.id === 'add') {
                this.setDefault();
            } else {
                this.$router.replace('/product-instock');
            }
        },
        setDefault() {
            this.item = {
                billId: 0,
                fromPrincipal: '',
                warehousePrincipal: this.$store.state.app.loginAdmin.adminId,
                productSource: 1,
                remark:'',
                productList:[]
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.billId === 0) return;
            let result = await productInstockService.getById(this.item.billId);
            if (result.status === 200) {
                this.item = result.data;
                this.item.billTimeLocal = util.formatTimestamp(this.item.billTime, "yyyy-MM-dd hh:mm:ss");
                this.item.billStateCn = this.$t('field.PRODUCT_INSTOCK_STATE.' + Number(this.item.billState));
            } else {
                this.$router.replace('/product-instock');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let obj = Object.assign({}, this.item, { productList: JSON.stringify(this.item.productList) });
                    if (this.$route.params.id === 'add'&& this.item.billId === 0) {
                        let result = await productInstockService.add(obj);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            var item = result.data;
                            this.$router.replace('/product-instock/' + item.billId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await productInstockService.update(obj);
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
                    let result = await productInstockService.audit(this.item.billId);
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
                    let result = await productInstockService.unaudit(this.item.billId);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.initData();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        finish() {
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await productInstockService.finish(this.item.billId);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.initData();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        addProduct() {
            this.modal.title = this.$t('common.ADD') + this.$t('field.PRODUCT_INSTOCK.DETAIL_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = -1;
            this.modal.item.productId = '';
            this.modal.item.quantity = 1;
            this.modal.item.principal = this.$store.state.app.loginAdmin.adminId;
            this.modal.item.warehouse = '';
            this.modal.item.place = '';
            this.modal.item.remark = '';
            this.modal.visible = true;
        },
        editProduct(item) {
            this.modal.title = this.$t('common.EDIT') + this.$t('field.PRODUCT_INSTOCK.DETAIL_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = item._index;
            this.modal.item.productId = item.productId;
            this.modal.item.quantity = item.quantity;
            this.modal.item.principal = item.principal;
            this.modal.item.warehouse = item.warehouse;
            this.modal.item.place = item.place;
            this.modal.item.remark = item.remark;
            this.modal.item.productNo = item.productNo;
            this.modal.item.productName = item.productName;
            this.modal.item.principalName = item.principalName;
            this.modal.item.warehouseName = item.warehouseName;
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
                            principal: this.modal.item.principal,
                            principalName: this.modal.item.principalName,
                            warehouse: this.modal.item.warehouse,
                            warehouseName: this.modal.item.warehouseName,
                            place: this.modal.item.place,
                            remark: this.modal.item.remark,  
                        });
                    } else {
                        this.item.productList[this.modal.item._index].productId = this.modal.item.productId;
                        this.item.productList[this.modal.item._index].productNo = this.modal.item.productNo;
                        this.item.productList[this.modal.item._index].productName = this.modal.item.productName;
                        this.item.productList[this.modal.item._index].quantity = this.modal.item.quantity;
                        this.item.productList[this.modal.item._index].principal = this.modal.item.principal;
                        this.item.productList[this.modal.item._index].principalName = this.modal.item.principalName;
                        this.item.productList[this.modal.item._index].warehouse = this.modal.item.warehouse;
                        this.item.productList[this.modal.item._index].warehouseName = this.modal.item.warehouseName;
                        this.item.productList[this.modal.item._index].place = this.modal.item.place;
                        this.item.productList[this.modal.item._index].remark = this.modal.item.remark;
                    }
                    this.modal.visible = false;
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.buttonLoading = false;
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
            } else {
                this.modal.item.productNo = item.productNo;
                this.modal.item.productName = item.productName;
            }
        },
        principalSelectChange(item) {
            if (item === null) {
                this.modal.item.principalName = '';
            } else {
                this.modal.item.principalName = item.trueName;
            }
        },
        warehouseSelectChange(item) {
            if (item === null) {
                this.modal.item.warehouseName = '';
            } else {
                this.modal.item.warehouseName = item.warehouseName;
            }
        }
    },
    created() {
        this.setDefault();
        this.initData();
    },
    watch: {
        '$route'(to, from) {
            this.initData();
        }
    }
}
</script>
