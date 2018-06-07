<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.BILL_NO')" prop="billNo">{{ item.billNo || $t('field.NOT_AVAILABLE')  }}</form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.BILL_TIME')" prop="billTime">{{ item.billTimeLocal || $t('field.NOT_AVAILABLE') }}</form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.TO_PRINCIPAL')" prop="toPrincipal">
                                <common-select type="admin" v-model="item.toPrincipal" :disabled="item.materialWhereabouts===1"></common-select>
                            </form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.WAREHOUSE_PRINCIPAL')" prop="warehousePrincipal">
                                <common-select type="admin" v-model="item.warehousePrincipal" disabled></common-select>
                            </form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_WHEREABOUTS')" prop="materialWhereabouts">
                                <i-select v-model="item.materialWhereabouts" :disabled="item.billId!==0" transfer style="width:100%">
                                    <i-option v-for="item in materialWhereaboutsList" :value="item.value" :key="item.value">{{ item.descript }}</i-option>
                                </i-select>
                            </form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.RELATED_BILL')" prop="relatedBill" v-if="item.materialWhereabouts===1">
                                <common-select type="draw-material" v-model="item.relatedBill" :query-parameters="{state:2,onlyNotOutstock:true}" v-if="item.billId===0" @on-change="drawMaterialBillSelectChange"></common-select>
                                <span v-else>{{ item.relatedBillNo }}</span>
                            </form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.BILL_STATE')" prop="billState">{{ item.billStateCn || $t('field.NOT_AVAILABLE') }}</form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.REMARK')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                        </div>
                    </div>
                     <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.MATERIAL_OUTSTOCK.DETAIL_INFO') }}&nbsp;&nbsp;<span v-if="editable&&item.materialWhereabouts!==1">（<a class="remark" @click="addMaterial"><icon type="plus"></icon> {{ $t('common.ADD')+$t('field.MATERIAL_OUTSTOCK.DETAIL_INFO') }}</a>）</span></div>
                        <div class="panel-body">
                            <i-table border :columns="columnList" :data="this.item.materialList"></i-table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="editable">{{ $t('common.SAVE') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="audit" v-if="materialOutstockAuditPermission&&item.billId!==0&&item.billState===1">{{ $t('common.AUDIT') }}</i-button>
            <i-button class="operate-btn" type="info" shape="circle" @click="unaudit" v-if="materialOutstockAuditPermission&&item.billId!==0&&item.billState===2">{{ $t('common.UNAUDIT') }}</i-button>
            <i-button class="operate-btn" type="success" shape="circle" @click="finish" v-if="materialOutstockFinishPermission&&item.billId!==0&&item.billState===2">{{ $t('common.FINISH') }}</i-button>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="saveMaterial" :loading="true">
            <i-form ref="formValidate2" :model="modal.item" :rules="rules2" :label-width="90">
                <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL')" prop="materialId">
                    <common-select type="material" v-model="modal.item.materialId" @on-change="materialSelectChange" :disabled="item.materialWhereabouts===1"></common-select>
                </form-item>
                <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_QUANTITY')" prop="quantity"><input-number v-model="modal.item.quantity" :min="1" style="width:100%" :disabled="item.materialWhereabouts===1"></input-number></form-item>
                <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_PRINCIPAL')" prop="principal">
                    <common-select type="admin" v-model="modal.item.principal" @on-change="principalSelectChange"></common-select>
                </form-item>
                <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_WAREHOUSE')" prop="warehouse">
                    <common-select type="warehouse" v-model="modal.item.warehouse" @on-change="warehouseSelectChange"></common-select>
                </form-item>
                <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_PLACE')" prop="place"><i-input v-model="modal.item.place" type="textarea"></i-input></form-item>
                <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_REMARK')" prop="remark"><i-input v-model="modal.item.remark" type="textarea"></i-input></form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission'

import util from '../../libs/util.js';

import commonSelect from '../../components/common-select';

import drawMaterialService from '../../service/draw-material';
import materialOutstockService from '../../service/material-outstock';

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
                toPrincipal: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_OUTSTOCK.TO_PRINCIPAL'), trigger: 'change' }
                ],
                materialWhereabouts: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_WHEREABOUTS'), trigger: 'change' }
                ],
                relatedBill: [
                    { type: 'number', required: this.item.materialWhereabouts===1, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_OUTSTOCK.RELATED_BILL'), trigger: 'change' }
                ]
            }
        },
        rules2() {
            return {
                materialId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_OUTSTOCK.MATERIAL'), trigger: 'change' }
                ],
                quantity: [
                    { type: 'number', required: true, message: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_QUANTITY')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                principal: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_PRINCIPAL'), trigger: 'change' }
                ],
                warehouse: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_WAREHOUSE'), trigger: 'change' }
                ]
            }
        },
        materialWhereaboutsList() {
            return [
                { value: 1, descript: this.$t('field.MATERIAL_OUTSTOCK_MATERIAL_WHEREABOUTS.1') },
            ]
        },
        editable() {
            return (this.materialOutstockAddPermission && this.$route.params.id === 'add' && this.item.billId === 0) || (this.materialOutstockAddPermission && this.item.billId !==0 && this.item.billState === 1);
        },
        columnList() {
            let result = [
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_NO'), key: 'materialNo' },
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_NAME'), key: 'materialName' },
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_QUANTITY'), key: 'quantity' },
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_PRINCIPAL'), key: 'principalName' },
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_WAREHOUSE'), key: 'warehouseName' },
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_PLACE'), key: 'place' },
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_REMARK'), key: 'remark' },
            ];
            if (this.editable) {
                if (this.item.materialWhereabouts !== 1) {
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
                this.$router.replace('/material-outstock');
            }
        },
        setDefault() {
            this.item = {
                billId: 0,
                toPrincipal: '',
                warehousePrincipal: this.$store.state.app.loginAdmin.adminId,
                materialWhereabouts: 1,
                relatedBill: '',
                remark:'',
                materialList:[]
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.billId === 0) return;
            let result = await materialOutstockService.getById(this.item.billId);
            if (result.status === 200) {
                this.item = result.data;
                this.item.billTimeLocal = util.formatTimestamp(this.item.billTime, "yyyy-MM-dd hh:mm:ss");
                this.item.billStateCn = this.$t('field.MATERIAL_OUTSTOCK_STATE.' + Number(this.item.billState));
            } else {
                this.$router.replace('/material-outstock');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let obj = Object.assign({}, this.item, { materialList: JSON.stringify(this.item.materialList) });
                    if (this.$route.params.id === 'add'&& this.item.billId === 0) {
                        let result = await materialOutstockService.add(obj);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            var item = result.data;
                            this.$router.replace('/material-outstock/' + item.billId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await materialOutstockService.update(obj);
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
                    let result = await materialOutstockService.audit(this.item.billId);
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
                    let result = await materialOutstockService.unaudit(this.item.billId);
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
                    let result = await materialOutstockService.finish(this.item.billId);
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
            this.modal.title = this.$t('common.ADD') + this.$t('field.MATERIAL_OUTSTOCK.DETAIL_INFO');
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
            this.modal.title = this.$t('common.EDIT') + this.$t('field.MATERIAL_OUTSTOCK.DETAIL_INFO');
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
            if (!this.editable || this.item.materialWhereabouts === 1) return;
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: () => {
                    this.item.materialList.splice(index, 1);
                }
            });
        },
        async drawMaterialBillSelectChange(item) {
            if (item === null) return;
            let result = await drawMaterialService.getById(item.billId);
            if (result.status === 200) {
                this.item.toPrincipal = result.data.toPrincipal;
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
