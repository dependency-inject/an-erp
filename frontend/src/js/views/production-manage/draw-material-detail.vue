<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <div>
                                <div class="info-item"><label>{{ $t('field.DRAW_MATERIAL.BILL_NO') }}： </label>{{ item.billNo }}</div>
                                <div class="info-item"><label>{{ $t('field.DRAW_MATERIAL.TO_PRINCIPAL') }}： </label>{{ item.fromPrincipalName }}</div>
                                <div class="info-item"><label>{{ $t('field.DRAW_MATERIAL.WAREHOUSE_PRINCIPAL') }}： </label>{{ item.warehousePrincipalName }}</div>
                                <div class="info-item"><label>{{ $t('field.DRAW_MATERIAL.BILL_TIME') }}： </label>{{ item.billTimeLocal }}</div>
                                <div class="info-item"><label>{{ $t('field.DRAW_MATERIAL.DRAW_REASON') }}： </label>{{ item.drawReasonCn }}</div>
                                <div class="info-item"><label>{{ $t('field.DRAW_MATERIAL.BILL_STATE') }}： </label>{{ item.billStateCn }}</div>
                            </div>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.ELSE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.DRAW_MATERIAL.REMARK')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.DRAW_MATERIAL.MATERIAL_INFO') }}&nbsp;&nbsp;<span v-if="editable">（<a class="remark" @click="addMaterial"><icon type="plus"></icon> {{ $t('common.ADD')+$t('field.DRAW_MATERIAL.ORDER') }}</a>）</span></div>
                        <div class="panel-body">
                            <i-table border :columns="columnList" :data="this.item.materialList"></i-table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="editable">{{ $t('common.SAVE') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="audit" v-if="productionDrawAuditPermission&&item.billId!==0&&item.billState===1">{{ $t('common.AUDIT') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="unaudit" v-if="productionDrawAuditPermission&&item.billId!==0&&item.billState===2">{{ $t('common.UNAUDIT') }}</i-button>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="saveMaterial" :loading="true">
            <i-form ref="formValidate2" :model="modal.item" :rules="rules2" :label-width="90">
                <form-item :label="$t('field.DRAW_MATERIAL.ORDER')" prop="orderbillId">
                    <i-select v-model="modal.item.orderbillId">
                        <i-option v-for="item in orderbillList" :value="item.billProductId" :key="item.billProductId">{{ item.billProductId + ' - ' +item.quantity }}</i-option>
                    </i-select>
                </form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
    import Permission from '../../mixins/permission';

    import util from '../../libs/util.js';

    import productionDrawService from '../../service/production-draw';

    export default {
        mixins: [ Permission ],
        data() {
            return {
                item: {
                    materialList: []
                },
                modal: {
                    title: 'title',
                    item: {},
                    visible: false
                },
                orderbillList: []
            }
        },
        computed: {
            rules() {
                return {
                }
            },
            rules2() {
                return {
                    // materialId: [
                    //     { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.RETURN_MATERIAL.MATERIAL'), trigger: 'change' }
                    // ],
                    // quantity: [
                    //     { type: 'number', required: true, message: this.$t('field.RETURN_MATERIAL.QUANTITY')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                    // ]
                }
            },
            loginAdmin() {
                return this.$store.state.app.loginAdmin;
            },
            editable() {
                return (this.productionDrawAddPermission && this.$route.params.id === 'add' && this.item.billId === 0) || (this.productionDrawUpdatePermission && this.item.billId !==0 && this.item.billState === 1);
            },
            columnList() {
                let result = [
                    { title: this.$t('field.MATERIAL.MATERIAL_NO'), key: 'materialNo' },
                    { title: this.$t('field.MATERIAL.MATERIAL_NAME'), key: 'materialName' },
                    { title: this.$t('field.MATERIAL.QUANTITY'), key: 'quantity' }
                ];
                return result;
            }
        },
        methods: {
            initData() {
                // 路由检查
                if (!isNaN(Number(this.$route.params.id))) {
                    this.item.billId = Number(this.$route.params.id);
                    this.getById();
                } else if (this.$route.params.id === 'add') {
                    this.setDefault();
                } else {
                    this.$router.replace('/production-draw');
                }
            },
            setDefault() {
                this.item = {
                    billId: 0,
                    fromPrincipalName: this.loginAdmin.trueName,
                    drawReasonCn: this.$t('field.DRAW_REASON.REASON1'),
                    remark: '',
                    materialList: []
                }
            },
            async getById() {
                let result = await productionDrawService.getBill(this.item.billId);
                console.log('getById');
                console.log(result);
                if (result.status === 200) {
                    this.item = result.data;
                    this.item.billStateCn = this.$t('field.BILL_STATE.STATE' + Number(this.item.billState));
                    this.item.billTimeLocal = util.formatTimestamp(this.item.billTime, "yyyy-MM-dd hh:mm:ss");
                    this.item.drawReasonCn = this.$t('field.DRAW_REASON.REASON' + Number(this.item.drawReason));
                }
            },
            save() {
                this.$refs.formValidate.validate(async (valid) => {
                    if (valid) {
                        let obj = Object.assign({}, this.item, { materialList: JSON.stringify(this.item.materialList) });
                        if (this.$route.params.id === 'add' && this.item.billId === 0) {
                            let result = await productionDrawService.addBill(obj);
                            if (result.status === 200) {
                                this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                                var item = result.data;
                                this.$router.replace('/production-draw/' + item.billId);
                            } else {
                                this.$Message.error(result.data);
                            }
                        } else {
                            let result = await productionDrawService.updateBill(obj);
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
                        let result = await productionDrawService.auditBill(this.item.billId);
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
                        let result = await productionDrawService.unauditBill(this.item.billId);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                            this.initData();
                        } else {
                            this.$Message.error(result.data);
                        }
                    }
                });
            },
            async getorderbillList() {
                let result = await productionDrawService.getAllOrderBillProduct();
                if (result.status === 200) {
                    this.orderbillList = result.data;
                    console.log('this.orderbillList');
                    console.log(this.orderbillList);
                }
            },
            addMaterial() {
                this.modal.title = '新增订单相关物料';
                this.$refs.formValidate2.resetFields();
                this.modal.item._index = -1;
                this.modal.item.orderbillId = '';
                this.modal.item.quantity = 1;
                this.modal.item.remark = '';
                this.modal.visible = true;
            },
            saveMaterial() {
                this.$refs.formValidate2.validate(async (valid) => {
                    if (valid) {
                        let result = await productionDrawService.getAllMaterial(this.modal.item.orderbillId);
                        console.log(result.data);
                        var new_all_mate_data = this.item.materialList.concat(result.data);
                        this.item.materialList=new_all_mate_data;
                        //this.item.materialList.push(result.data);
                        /*this.orderbilllList.forEach((item) => {
                            if (item.materialId === this.modal.item.materialId) {
                                this.modal.item.materialNo = item.materialNo;
                                this.modal.item.materialName = item.materialName;
                            }
                        });
                        if (this.modal.item._index === -1) {
                            this.item.materialList.push({
                                materialId: this.modal.item.materialId,
                                materialNo: this.modal.item.materialNo,
                                materialName: this.modal.item.materialName,
                                quantity: this.modal.item.quantity,
                                remark: this.modal.item.remark
                            });
                        } else {
                            this.item.materialList[this.modal.item._index].materialId = this.modal.item.materialId;
                            this.item.materialList[this.modal.item._index].materialNo = this.modal.item.materialNo;
                            this.item.materialList[this.modal.item._index].materialName = this.modal.item.materialName;
                            this.item.materialList[this.modal.item._index].quantity = this.modal.item.quantity;
                            this.item.materialList[this.modal.item._index].remark = this.modal.item.remark;
                        }*/
                        this.modal.visible = false;
                    } else {
                        this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                        this.$refs.modal.abortLoading();
                    }
                });
            }
        },
        created() {
            this.setDefault();
            this.initData();
            this.getorderbillList();
        },
        watch: {
            '$route'(to, from) {
                this.initData();
            }
        }
    }
</script>
