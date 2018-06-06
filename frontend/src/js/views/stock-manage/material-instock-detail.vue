<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.MATERIAL_INSTOCK.BILL_NO')" prop="billNo">{{ item.billNo || $t('field.NOT_AVAILABLE')  }}</form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.BILL_TIME')" prop="billTime">{{ item.billTimeLocal || $t('field.NOT_AVAILABLE') }}</form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.FROM_PRINCIPAL')" prop="fromPrincipal">
                                <common-select type="admin" v-model="item.fromPrincipal" :disabled="item.materialSource===1"></common-select>
                            </form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.WAREHOUSE_PRINCIPAL')" prop="warehousePrincipal">
                                <common-select type="admin" v-model="item.warehousePrincipal" disabled></common-select>
                            </form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_SOURCE')" prop="materialSource">
                                <i-select v-model="item.materialSource" :disabled="item.billId!==0" style="width:100%">
                                    <i-option v-for="item in materialSourceList" :value="item.value" :key="item.value">{{ item.descript }}</i-option>
                                </i-select>
                            </form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.RELATED_BILL')" prop="relatedBill" v-if="item.materialSource===1">
                                <common-select type="return-material" v-model="item.relatedBill" :query-parameters="{state:2,onlyNotInstock:true}" v-if="item.billId===0" @on-change="returnMaterialBillSelectChange"></common-select>
                                <span v-else>{{ item.relatedBillNo }}</span>
                            </form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.BILL_STATE')" prop="billState">{{ item.billStateCn || $t('field.NOT_AVAILABLE') }}</form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.REMARK')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                        </div>
                    </div>
                     <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.MATERIAL_INSTOCK.DETAIL_INFO') }}&nbsp;&nbsp;<span v-if="editable&&item.materialSource!==1">（<a class="remark" @click="addMaterial"><icon type="plus"></icon> {{ $t('common.ADD')+$t('field.MATERIAL_INSTOCK.DETAIL_INFO') }}</a>）</span></div>
                        <div class="panel-body">
                            <i-table border :columns="columnList" :data="this.item.materialList"></i-table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="editable">{{ $t('common.SAVE') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="audit" v-if="materialInstockAuditPermission&&item.billId!==0&&item.billState===1">{{ $t('common.AUDIT') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="unaudit" v-if="materialInstockAuditPermission&&item.billId!==0&&item.billState===2">{{ $t('common.UNAUDIT') }}</i-button>
            <i-button class="operate-btn" type="success" shape="circle" @click="finish" v-if="materialInstockFinishPermission&&item.billId!==0&&item.billState===2">{{ $t('common.FINISH') }}</i-button>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="saveMaterial" :loading="true">
            <i-form ref="formValidate2" :model="modal.item" :rules="rules2" :label-width="90">
                <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL')" prop="materialId">
                    <common-select type="material" v-model="modal.item.materialId" @on-change="materialSelectChange" :disabled="item.materialSource===1"></common-select>
                </form-item>
                <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_QUANTITY')" prop="quantity"><input-number v-model="modal.item.quantity" :min="1" style="width:100%" :disabled="item.materialSource===1"></input-number></form-item>
                <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_PRINCIPAL')" prop="principal">
                    <common-select type="admin" v-model="modal.item.principal" @on-change="principalSelectChange"></common-select>
                </form-item>
                <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_WAREHOUSE')" prop="warehouse">
                    <common-select type="warehouse" v-model="modal.item.warehouse" @on-change="warehouseSelectChange"></common-select>
                </form-item>
                <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_PLACE')" prop="place"><i-input v-model="modal.item.place" type="textarea"></i-input></form-item>
                <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_REMARK')" prop="remark"><i-input v-model="modal.item.remark" type="textarea"></i-input></form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission'

import util from '../../libs/util.js';

import commonSelect from '../../components/common-select';

import returnMaterialService from '../../service/return-material';
import materialInstockService from '../../service/material-instock';

export default {
    mixins: [ Permission ],
    data() {
        return {
            item: {
                materialList: [],
            },
            modal: {
                title: 'title',
                item: {
                    materialId: '',
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
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_INSTOCK.FROM_PRINCIPAL'), trigger: 'change' }
                ],
                materialSource: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_INSTOCK.MATERIAL_SOURCE'), trigger: 'change' }
                ]
            }
        },
        rules2() {
            return {
                materialId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_INSTOCK.MATERIAL'), trigger: 'change' }
                ],
                quantity: [
                    { type: 'number', required: true, message: this.$t('field.MATERIAL_INSTOCK.MATERIAL_QUANTITY')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                principal: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_INSTOCK.MATERIAL_PRINCIPAL'), trigger: 'change' }
                ],
                warehouse: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_INSTOCK.MATERIAL_WAREHOUSE'), trigger: 'change' }
                ]
            }
        },
        materialSourceList() {
            return [
                { value: 1, descript: this.$t('field.MATERIAL_INSTOCK_MATERIAL_SOURCE.1') },
                { value: 2, descript: this.$t('field.MATERIAL_INSTOCK_MATERIAL_SOURCE.2') },
            ]
        },
        editable() {
            return (this.materialInstockAddPermission && this.$route.params.id === 'add' && this.item.billId === 0) || (this.materialInstockAddPermission && this.item.billId !==0 && this.item.billState === 1);
        },
        columnList() {
            let result = [
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_NO'), key: 'materialNo' },
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_NAME'), key: 'materialName' },
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_QUANTITY'), key: 'quantity' },
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_PRINCIPAL'), key: 'principalName' },
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_WAREHOUSE'), key: 'warehouseName' },
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_PLACE'), key: 'place' },
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_REMARK'), key: 'remark' },
            ];
            if (this.editable) {
                if (this.item.materialSource !== 1) {
                    result.push({ 
                        title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                            return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                                this.editMaterial(row) 
                            }), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => { 
                                this.removeMaterial(params.index) 
                            })]);
                        } 
                    });
                } else {
                    result.push({ 
                        title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                            return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                                this.editMaterial(row) 
                            })]);
                        } 
                    });
                }
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
                this.$router.replace('/material-instock');
            }
        },
        setDefault() {
            this.item = {
                billId: 0,
                fromPrincipal: '',
                warehousePrincipal: this.$store.state.app.loginAdmin.adminId,
                materialSource: 1,
                relatedBill: '',
                remark:'',
                materialList:[]
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.billId === 0) return;
            let result = await materialInstockService.getById(this.item.billId);
            if (result.status === 200) {
                this.item = result.data;
                this.item.billTimeLocal = util.formatTimestamp(this.item.billTime, "yyyy-MM-dd hh:mm:ss");
                this.item.billStateCn = this.$t('field.MATERIAL_INSTOCK_STATE.' + Number(this.item.billState));
            } else {
                this.$router.replace('/material-instock');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let obj = Object.assign({}, this.item, { materialList: JSON.stringify(this.item.materialList) });
                    if (this.$route.params.id === 'add'&& this.item.billId === 0) {
                        let result = await materialInstockService.add(obj);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            var item = result.data;
                            this.$router.replace('/material-instock/' + item.billId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await materialInstockService.update(obj);
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
                    let result = await materialInstockService.audit(this.item.billId);
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
                    let result = await materialInstockService.unaudit(this.item.billId);
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
                    let result = await materialInstockService.finish(this.item.billId);
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
            this.modal.title = this.$t('common.ADD') + this.$t('field.MATERIAL_INSTOCK.DETAIL_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = -1;
            this.modal.item.materialId = '';
            this.modal.item.quantity = 1;
            this.modal.item.principal = this.$store.state.app.loginAdmin.adminId;
            this.modal.item.warehouse = '';
            this.modal.item.place = '';
            this.modal.item.remark = '';
            this.modal.visible = true;
        },
        editMaterial(item) {
            this.modal.title = this.$t('common.EDIT') + this.$t('field.MATERIAL_INSTOCK.DETAIL_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = item._index;
            this.modal.item.materialId = item.materialId;
            this.modal.item.quantity = item.quantity;
            this.modal.item.principal = item.principal;
            this.modal.item.warehouse = item.warehouse;
            this.modal.item.place = item.place;
            this.modal.item.remark = item.remark;
            this.modal.item.materialNo = item.materialNo;
            this.modal.item.materialName = item.materialName;
            this.modal.item.principalName = item.principalName;
            this.modal.item.warehouseName = item.warehouseName;
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
                            principal: this.modal.item.principal,
                            principalName: this.modal.item.principalName,
                            warehouse: this.modal.item.warehouse,
                            warehouseName: this.modal.item.warehouseName,
                            place: this.modal.item.place,
                            remark: this.modal.item.remark,  
                        });
                    } else {
                        this.item.materialList[this.modal.item._index].materialId = this.modal.item.materialId;
                        this.item.materialList[this.modal.item._index].materialNo = this.modal.item.materialNo;
                        this.item.materialList[this.modal.item._index].materialName = this.modal.item.materialName;
                        this.item.materialList[this.modal.item._index].quantity = this.modal.item.quantity;
                        this.item.materialList[this.modal.item._index].principal = this.modal.item.principal;
                        this.item.materialList[this.modal.item._index].principalName = this.modal.item.principalName;
                        this.item.materialList[this.modal.item._index].warehouse = this.modal.item.warehouse;
                        this.item.materialList[this.modal.item._index].warehouseName = this.modal.item.warehouseName;
                        this.item.materialList[this.modal.item._index].place = this.modal.item.place;
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
        async returnMaterialBillSelectChange(item) {
            if (item === null) return;
            let result = await returnMaterialService.getById(item.billId);
            if (result.status === 200) {
                this.item.fromPrincipal = result.data.fromPrincipal;
                var items = result.data.materialList;
                this.item.materialList.splice(0, this.item.materialList.length);
                items.forEach((row) => {
                    this.item.materialList.push({
                        materialId: row.materialId,
                        materialNo: row.materialNo,
                        materialName: row.materialName,
                        quantity: row.quantity,
                        principal: this.$store.state.app.loginAdmin.adminId,
                        principalName: this.$store.state.app.loginAdmin.trueName,
                        warehouse: '',
                        warehouseName: '',
                        place: '',
                        remark: '',  
                    });
                });
            }
        },
        materialSelectChange(item) {
            if (item === null) {
                this.modal.item.materialNo = ''
                this.modal.item.materialName = '';
            } else {
                this.modal.item.materialNo = item.materialNo;
                this.modal.item.materialName = item.materialName;
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
