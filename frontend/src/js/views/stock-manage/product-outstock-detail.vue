<template>
    <div class="main-panel">
       <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.PROOUTSTOCK.PROOUTSTOCK_ID')" prop="billNo"><i-input v-model="item.billNo"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.PROOUTSTOCK_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.PROOUTSTOCK.PROOUTSTOCK_TIME')" prop="productOutstockTime"><i-input v-model="item.productOutstockTime" :disabled=true></i-input></form-item>                            
                            <form-item :label="$t('field.PROOUTSTOCK.PROOUTSTOCK_PERSON')">
                            	<Select v-model="selectedPrincipal" style="width:200px">
									<Option v-for="item in allAdmins" :value="item.adminId" :key="item.adminId">{{ item.trueName }}</Option>
								</Select>
							</form-item>
                            <form-item :label="$t('field.PROOUTSTOCK.STOCK_PERSON')"><i-input :value="this.$store.state.app.loginAdmin.trueName" :disabled=true></i-input></form-item>
                            <form-item :label="$t('field.PROOUTSTOCK.PRO_SOURCE')">
                            	<Select v-model="selectedSource" style="width:200px">
									<Option v-for="item in allSources" :value="item.label" :key="item.str">{{ item.str }}</Option>
								</Select>
							</form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.PROOUT_STATUS_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.PROOUTSTOCK.STATUS')" prop="status"><radio-group v-model="item.status"><radio v-for="item in statusList" :key="item.value" :label="item.value" :disabled=true>{{ item.descript }}</radio></radio-group></form-item>
                        </div>
                    </div>
                     <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.PROOUTSTOCK.DETAIL_INFO') }}&nbsp;&nbsp;<span v-if="editable">（<a class="remark" @click="addProduct"><icon type="plus"></icon> {{ $t('common.ADD')+$t('field.PROOUTSTOCK.DETAIL_INFO') }}</a>）</span></div>
                        <div class="panel-body">
                            <i-table border :columns="columnList" :data="this.item.productIdList"></i-table>
                        </div>
                    </div>
                </i-form>
            </div>
       </div>
       <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(productOutstockAddPermission&&$route.params.id==='add')||(productOutstockUpdatePermission)">{{ $t('common.SAVE') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="audit" v-if="productOutstockAuditPermission&&item.billId!==0&&item.status===1">{{ $t('common.AUDIT') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="unaudit" v-if="productOutstockAuditPermission&&item.billId!==0&&item.status===2">{{ $t('common.UNAUDIT') }}</i-button>
        </div>
         <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="saveProduct" :loading="true">
            <i-form ref="formValidate2" :model="modal.item" :rules="rules" :label-width="90">
                <form-item :label="$t('field.PROOUTSTOCK.PRODUCT')" prop="productId">
                    <i-select v-model="modal.item.productId">
                        <i-option v-for="item in productIdList" :value="item.productId" :key="item.productId">{{ item.productNo + ' - ' +item.productName }}</i-option>
                    </i-select>
                </form-item>
                <form-item :label="$t('field.PROOUTSTOCK.PRODUCT_NUM')" prop="quantity"><input-number v-model="modal.item.quantity" :min="1" style="width:100%"></input-number></form-item>
                <form-item :label="$t('field.PROOUTSTOCK.PRODUCT_PRIN')"><i-input v-model="modal.item.toPrincipalName" :min="1" style="width:100%" :disabled=true></i-input></form-item>
                <form-item :label="$t('field.PROOUTSTOCK.PRODUCT_WARE')">
					<Select v-model="modal.item.warehouse" style="width:200px">
						<Option v-for="item in allWarehouses" :value="item.warehouseId" :key="item.warehouseId">{{ item.warehouseName }}</Option>
					</Select>
				</form-item>
                
                <form-item :label="$t('field.PROOUTSTOCK.PRODUCT_PLACE')" prop="place"><i-input v-model="modal.item.place" type="textarea"></i-input></form-item>
                <form-item :label="$t('field.PROOUTSTOCK.PRODUCT_REMARK')" prop="remark"><i-input v-model="modal.item.remark" type="textarea"></i-input></form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission'
import productoutstockService from '../../service/product-outstock';
import util from '../../libs/util.js';

export default {
    mixins: [Permission],
    data() {
        return {
            item: {
                productIdList: [],
            },
            modal: {
                title: 'title',
                item: {},
                visible: false
            },
            productIdList: [],
            allSources: [{str: '发货出库', label: 1}],
            selectedSource: '',
            selectedPrincipal: '',
            allAdmins: [],
            allWarehouses: [],
            resultList: [],
        }
    },
    computed: {
        rules() {
            return {
                billNo: [
                    { required: true, trigger: 'blur' }
                ],
            }
        },
        statusList() {
            return [
                { value: 1, descript: this.$t('field.PROOUT_STATUS.1') },
                { value: 2, descript: this.$t('field.PROOUT_STATUS.2') },
                { value: 3, descript: this.$t('field.PROOUT_STATUS.3') },
            ]
        },
         editable() {
            return (this.productOutstockAddPermission && this.$route.params.id === 'add' && this.item.billId === 0) || (this.productOutstockAddPermission && this.item.billId !==0 && this.item.status === 1);
        },
        columnList() {
            let result = [
                { title: this.$t('field.PROOUTSTOCK.PRODUCT_NO'), key: 'productNo' },
                { title: this.$t('field.PROOUTSTOCK.PRODUCT_NAME'), key: 'productName' },
                { title: this.$t('field.PROOUTSTOCK.PRODUCT_NUM'), key: 'quantity' },
                { title: this.$t('field.PROOUTSTOCK.PRODUCT_PRIN'), key: 'toPrincipalName' },
                { title: this.$t('field.PROOUTSTOCK.PRODUCT_WARE'), key: 'warehouseName' },
                { title: this.$t('field.PROOUTSTOCK.PRODUCT_PLACE'), key: 'place' },
                { title: this.$t('field.PROOUTSTOCK.PRODUCT_REMARK'), key: 'remark' },
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
    },
    methods: {
        initData() {
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.billId = Number(this.$route.params.id);
                this.getById();
            } else if (this.$route.params.id === 'add') {
                this.setDefault();
            } else {
                this.$router.replace('/product-outstock');
            }
        },
        getDate() {
            let date = new Date();
            let sep = "-";
            let month = date.getMonth() + 1;
            let day = date.getDate();
            let currentDate = date.getFullYear() + sep + month + sep + day;
            return currentDate;
        },
        setDefault() {
            this.item = {
                billId: 0,
                billNo: '123', 
                toPrincipal: 0,
                warehousePrincipal: this.$store.state.app.loginAdmin.adminId,
                productWhereabouts: 1,
                relatedBill: 0,
                status: 1,
                remark:'',
                productIdList:[],
                productOutstockTime: this.getDate(),
            }
        },
        audit() {
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await productoutstockService.audit(this.item.billId);
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
                    let result = await productoutstockService.unaudit(this.item.billId);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.initData();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        async getProductIdList() {
            let result = await productoutstockService.getProductIdList();
            if (result.status === 200) {
                this.productIdList = result.data;
            }
        },
        async getAdmins() {
            let result = await productoutstockService.getAdmins()
            if (result.status === 200) {
                this.allAdmins = result.data;
            }
        },
        async getWarehouses() {
            let result = await productoutstockService.getWarehouses()
            if (result.status === 200) {
                this.allWarehouses = result.data;
            }
        },
        addProduct() {
            this.modal.title = this.$t('common.ADD') + this.$t('field.PROOUTSTOCK.DETAIL_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = -1;
            this.modal.item.productId = '';
            this.modal.item.quantity = 1;
            this.modal.item.principal = 1;
            this.modal.item.warehouse = 1;
            this.modal.item.place = '';
            this.modal.item.remark = '';
            this.modal.visible = true;
            this.modal.item.toPrincipalName = this.$store.state.app.loginAdmin.trueName;
        },
        editProduct(item) {
            this.modal.title = this.$t('common.EDIT') + this.$t('field.PROOUTSTOCK.DETAIL_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = item._index;
            this.modal.item.productId = item.productId;
            this.modal.item.quantity = item.quantity;
            this.modal.item.principal = item.principal;
            this.modal.item.warehouse = item.warehouse;
            this.modal.item.place = item.place;
            this.modal.item.remark = item.remark;
            this.modal.item.toPrincipalName = item.toPrincipalName
            this.modal.visible = true;
        },
        saveProduct() {
        this.$refs.formValidate2.validate(async (valid) => {
            if (valid) {
                this.productIdList.forEach((item) => {
                    if (item.productId === this.modal.item.productId) {
                        this.modal.item.productNo = item.productNo;
                        this.modal.item.productName = item.productName;
                    }
                });
                
                if (this.modal.item._index === -1) {
                        
                        this.item.productIdList.push({
                            productId: this.modal.item.productId,
                            productNo: this.modal.item.productNo,
                            productName: this.modal.item.productName,
                            quantity: this.modal.item.quantity,
                            principal: this.$store.state.app.loginAdmin.adminId,
                            warehouse: this.modal.item.warehouse,
                            place: this.modal.item.place,
                            remark: this.modal.item.remark,  
                        });
                    } else {
                        this.item.productIdList[this.modal.item._index].productId = this.modal.item.productId;
                        this.item.productIdList[this.modal.item._index].productNo = this.modal.item.productNo;
                        this.item.productIdList[this.modal.item._index].productName = this.modal.item.productName;
                        this.item.productIdList[this.modal.item._index].quantity = this.modal.item.quantity;
                        this.item.productIdList[this.modal.item._index].principal = this.$store.state.app.loginAdmin.adminId;
                        this.item.productIdList[this.modal.item._index].warehouse = this.modal.item.warehouse;
                        this.item.productIdList[this.modal.item._index].place = this.modal.item.place;
                        this.item.productIdList[this.modal.item._index].remark = this.modal.item.remark;
                    }
                    this.modal.visible = false;
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.abortLoading();
                }
            });
            this.item.productIdList.forEach((i) => {
                    this.allAdmins.forEach((item) => {
                    if (item.adminId === i.principal) {
                        i.toPrincipalName = item.trueName;
                    }
                    });
                    this.allWarehouses.forEach((item) => {
                    if (item.warehouseId === i.warehouse) {
                        i.warehouseName = item.warehouseName;
                    }
                    });
                });

        },
        removeProduct(index) {
            if (!this.editable) return;
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: () => {
                    this.item.productIdList.splice(index, 1);
                }
            });
        },
        async getresult(){
            this.resultList = []
            this.item.productIdList.forEach((i) => {
                        this.resultList.push({
                            productId: i.productId,
                            productNo: i.productNo,
                            productName: i.productName,
                            quantity: i.quantity,
                            principal: i.principal,
                            warehouse: i.warehouse,
                            place: i.place,
                            remark: i.remark,  
                        });
                    });
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    this.item.selectedSource = this.selectedSource;
                    this.item.selectedPrincipal = this.selectedPrincipal;
                    this.item.toPrincipal = this.selectedPrincipal;
                    this.getresult()

                    let obj = Object.assign({}, this.item, { productIdList: JSON.stringify(this.resultList) });
                    if (this.$route.params.id === 'add'&& this.item.billId === 0) {
                        let result = await productoutstockService.add(obj);
                        
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            var item = result.data;
                            this.$router.replace('/product-outstock/' + item.billId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await productoutstockService.update(obj);
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
        
        async getById() {
            if (this.$route.params.id === 'add' && this.item.billId === 0) return;
            let result = await productoutstockService.getById(this.item.billId);
            if (result.status === 200) {
                this.item = result.data;
                this.item.productOutstockTime = new Date().toLocaleDateString(this.item.createAt);
                this.selectedSource = this.item.productWhereabouts;
                this.selectedPrincipal = this.item.toPrincipal;
                if (this.item.sysDefault) {  
                    this.$router.replace('/product-outstock');
                }
                this.item.status = Number(this.item.billState);
            } else {
                this.$router.replace('/product-outstock');
            }
        },
    },
    
    created() {
        this.setDefault();
        this.initData();
        this.getProductIdList();
        this.getAdmins();
        this.getWarehouses();
    },
     watch: {
        '$route'(to, from) {
            this.initData();
        }
    }
}
</script>
