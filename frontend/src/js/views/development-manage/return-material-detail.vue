<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.RETURN_MATERIAL.BILL_NO')" prop="billNo">{{ item.billNo || $t('field.NOT_AVAILABLE')  }}</form-item>
                            <form-item :label="$t('field.RETURN_MATERIAL.BILL_TIME')" prop="billTime">{{ item.billTimeLocal || $t('field.NOT_AVAILABLE') }}</form-item>
                            <form-item :label="$t('field.RETURN_MATERIAL.FROM_PRINCIPAL')" prop="fromPrincipal">
                                <common-select type="admin" v-model="item.fromPrincipal" disabled></common-select>
                            </form-item>
                            <form-item :label="$t('field.RETURN_MATERIAL.WAREHOUSE_PRINCIPAL')" prop="warehousePrincipal">{{ item.warehousePrincipalName || $t('field.NOT_AVAILABLE') }}</form-item>
                            <form-item :label="$t('field.RETURN_MATERIAL.RETURN_REASON')" prop="returnReason">
                                <i-select v-model="item.returnReason" disabled style="width:100%">
                                    <i-option v-for="item in returnReasonList" :value="item.value" :key="item.value">{{ item.descript }}</i-option>
                                </i-select>
                            </form-item>
                            <form-item :label="$t('field.RETURN_MATERIAL.BILL_STATE')" prop="billState">{{ item.billStateCn || $t('field.NOT_AVAILABLE') }}</form-item>
                            <form-item :label="$t('field.RETURN_MATERIAL.REMARK')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.RETURN_MATERIAL.MATERIAL_INFO') }}&nbsp;&nbsp;<span v-if="editable">（<a class="remark" @click="addMaterial"><icon type="plus"></icon> {{ $t('common.ADD')+$t('field.RETURN_MATERIAL.MATERIAL_INFO') }}</a>）</span></div>
                        <div class="panel-body">
                            <i-table border :columns="columnList" :data="this.item.materialList"></i-table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="editable">{{ $t('common.SAVE') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="audit" v-if="developmentReturnAuditPermission&&item.billId!==0&&item.billState===1">{{ $t('common.AUDIT') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="unaudit" v-if="developmentReturnAuditPermission&&item.billId!==0&&item.billState===2">{{ $t('common.UNAUDIT') }}</i-button>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="saveMaterial" :loading="true">
            <i-form ref="formValidate2" :model="modal.item" :rules="rules2" :label-width="90">
                <form-item :label="$t('field.RETURN_MATERIAL.MATERIAL')" prop="materialId">
                    <common-select type="material" v-model="modal.item.materialId" @on-change="materialSelectChange"></common-select>
                </form-item>
                <form-item :label="$t('field.RETURN_MATERIAL.QUANTITY')" prop="quantity"><input-number v-model="modal.item.quantity" :min="1" style="width:100%"></input-number></form-item>
                <form-item :label="$t('field.RETURN_MATERIAL.REMARK')" prop="remark"><i-input v-model="modal.item.remark" type="textarea"></i-input></form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import commonSelect from '../../components/common-select';

import developmentReturnService from '../../service/development-return';

export default {
    mixins: [ Permission ],
    data() {
        return {
            item: {
                materialList: []
            },
            modal: {
                title: 'title',
                item: {
                    materialId: ''
                },
                visible: false
            }
        }
    },
    computed: {
        rules() {
            return {
            }
        },
        rules2() {
            return {
                materialId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.RETURN_MATERIAL.MATERIAL'), trigger: 'change' }
                ],
                quantity: [
                    { type: 'number', required: true, message: this.$t('field.RETURN_MATERIAL.QUANTITY')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ]
            }
        },
        returnReasonList() {
            return [
                { value: 2, descript: this.$t('field.RETURN_REASON.REASON2') },
            ]
        },
        editable() {
            return (this.developmentReturnAddPermission && this.$route.params.id === 'add' && this.item.billId === 0) || (this.developmentReturnUpdatePermission && this.item.billId !==0 && this.item.billState === 1);
        },
        columnList() {
            let result = [
                { title: this.$t('field.MATERIAL.MATERIAL_NO'), key: 'materialNo' },
                { title: this.$t('field.MATERIAL.MATERIAL_NAME'), key: 'materialName' },
                { title: this.$t('field.MATERIAL.QUANTITY'), key: 'quantity' },
                { title: this.$t('field.MATERIAL.REMARK'), key: 'remark' }
            ];
            if (this.editable) {
                result.push({
                    title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            this.editMaterial(row)
                        }), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => {
                            this.removeMaterial(params.index)
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
            // 路由检查
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.billId = Number(this.$route.params.id);
                this.getById();
            } else if (this.$route.params.id === 'add') {
                this.setDefault();
            } else {
                this.$router.replace('/development-return');
            }
        },
        setDefault() {
            this.item = {
                billId: 0,
                fromPrincipal: this.$store.state.app.loginAdmin.adminId,
                returnReason: 2,
                remark: '',
                materialList: []
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.billId === 0) return;
            let result = await developmentReturnService.getBill(this.item.billId);
            if (result.status === 200) {
                this.item = result.data;
                this.item.billStateCn = this.$t('field.BILL_STATE.STATE' + Number(this.item.billState));
                this.item.billTimeLocal = util.formatTimestamp(this.item.billTime, "yyyy-MM-dd hh:mm:ss");
            } else {
                this.$router.replace('/development-return');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let obj = Object.assign({}, this.item, { materialList: JSON.stringify(this.item.materialList) });
                    if (this.$route.params.id === 'add' && this.item.billId === 0) {
                        let result = await developmentReturnService.addBill(obj);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            var item = result.data;
                            this.$router.replace('/development-return/' + item.billId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await developmentReturnService.updateBill(obj);
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
                    let result = await developmentReturnService.auditBill(this.item.billId);
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
                    let result = await developmentReturnService.unauditBill(this.item.billId);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.initData();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        addMaterial() {
            this.modal.title = this.$t('common.ADD') + this.$t('field.RETURN_MATERIAL.MATERIAL_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = -1;
            this.modal.item.materialId = '';
            this.modal.item.quantity = 1;
            this.modal.item.remark = '';
            this.modal.visible = true;
        },
        editMaterial(item) {
            this.modal.title = this.$t('common.EDIT') + this.$t('field.RETURN_MATERIAL.MATERIAL_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = item._index;
            this.modal.item.materialId = item.materialId;
            this.modal.item.quantity = item.quantity;
            this.modal.item.remark = item.remark;
            this.modal.item.materialNo = item.materialNo;
            this.modal.item.materialName = item.materialName;
            this.modal.visible = true;
        },
        saveMaterial() {
            this.$refs.formValidate2.validate(async (valid) => {
                if (valid) {
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
                    }
                    this.modal.visible = false;
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.abortLoading();
                }
            });
        },
        removeMaterial(index) {
            if (!this.editable) return;
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: () => {
                    this.item.materialList.splice(index, 1);
                }
            });
        },
        materialSelectChange(item) {
            if (item === null) {
                this.modal.item.materialNo = ''
                this.modal.item.materialName = '';
            } else {
                this.modal.item.materialNo = item.materialNo;
                this.modal.item.materialName = item.materialName;
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