<template>
    <div class="main-panel">
    	<div class="main-panel-content2">
    		<div class="panel-container">
    			<i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
    				<div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_NO')" prop="billNo"><i-input v-model="item.billNo"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_TITLE')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_TIME')" prop="materialOutstockTime"><date-picker v-model="item.materialOutstockTime" v-on:change="picked"></date-picker></form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_TRANSFER')">
                            	<Select v-model="selectedPrincipal" style="width:200px">
									<Option v-for="item in allAdmins" :value="item.adminId" :key="item.adminId">{{ item.trueName }}</Option>
								</Select>
							</form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_CHARGE')"><i-input :value="this.$store.state.app.loginAdmin.trueName" :disabled=true></i-input></form-item>
                            
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_SOURCE')">
                            	<Select v-model="selectedSource" style="width:200px">
									<Option v-for="item in allSources" :value="item.label" :key="item.str">{{ item.str }}</Option>
								</Select>
							</form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_STATE')" prop="materialOutstockState">
                            	{{materialOutstockStateList[item.materialOutstockState - 1].descript}}
                            </form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_RELATED')" prop="relatedBill"><i-input v-model="item.relatedBill"></i-input></form-item>
                        </div>
                        <div class="chief-panel">
							<div class="panel-header">{{ $t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_SELECT') }}
								<i-button class="operate-btn" type="primary" shape="circle" @click="addMaterial" v-if="$route.params.id === 'add'">{{ $t('common.ADD') }}</i-button>
							</div>
							<div class="panel-body">
								<i-table :height="tableHeight" ref="table" :columns="columnList" :data="materials"></i-table>
							</div>
						</div>
                   </div>
    			</i-form>
    		</div>
    	</div>
    	<Modal v-model="modal" :title="title" @on-ok="ok" @on-cancel="cancel" width="700">
    		<i-form :label-width="90" inline>
				<!--selectedMaterial等于option里的value-->
				<form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_NAME')">
					<Select v-model="selectedMaterial" style="width:200px">
						<Option v-for="item in allMaterials" :value="item.materialId" :key="item.materialId">{{ item.materialName }}</Option>
					</Select>
				</form-item>
				<form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_WAREHOUSE')">
					<Select v-model="selectedWarehouse" style="width:200px">
						<Option v-for="item in allWarehouses" :value="item.warehouseId" :key="item.warehouseId" :placeholder="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_WAREHOUSE')">{{ item.warehouseName }}</Option>
					</Select>
				</form-item>
				<form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_COUNT')">
					<i-input v-model="materialCount" :placeholder="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_COUNT')"></i-input>
				</form-item>
				<form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_PRINCIPAL')">
					<i-input v-model="materialPrincipal" :placeholder="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_PRINCIPAL')" :disabled=true></i-input>
				</form-item>
				<form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_PLACE')">
					<i-input v-model="materialPlace" :placeholder="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_PLACE')"></i-input>
				</form-item>
				<form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_REMARK')">
					<i-input v-model="materialRemark" :placeholder="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_REMARK')"></i-input>
				</form-item>
			</i-form>
    	</Modal>
    	<div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(materialOutstockAddPermission&&$route.params.id==='add'&&item.billId===0)||(materialOutstockUpdatePermission&&item.billId!==0)">{{ $t('common.SAVE') }}</i-button>
        	<i-button class="operate-btn" type="primary" shape="circle" @click="audit" v-if="item.materialOutstockState==1 && materialOutstockAuditPermission">{{ $t('common.AUDIT') }}</i-button>
            <i-button class="operate-btn" type="primary" shape="circle" @click="unaudit" v-if="item.materialOutstockState==2 && materialOutstockAuditPermission">{{ $t('common.UNAUDIT') }}</i-button>
            <i-button class="operate-btn" type="primary" shape="circle" @click="finish" v-if="item.materialOutstockState==2 && materialOutstockFinishPermission">{{ $t('完成') }}</i-button>
    	</div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import permissionTable from '../../components/permission-table';

import materialOutstockService from '../../service/material-outstock';

export default {
	mixins: [ Permission ],
    data() {
        return {
            item: {},
            modal: false,
            title: '添加物料',
            materials: [],
            allMaterials: [],
            allWarehouses: [],
            allAdmins: [],
            allSources: [{str: '领料出库', label: 1}],
            selectedMaterial: '',
            selectedWarehouse: '',
            selectedPrincipal: '',
            selectedSource: '',
            materialCount: '',
            materialPrincipal: '',
            materialPlace: '',
            materialRemark: '',
            permissionIdList: ''
        }
    },
    components: { permissionTable },
    computed: {
    	rules() {
            return {
                billNo: [
                    { required: true, message: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_NO')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                materialOutstockState: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_STATE'), trigger: 'change' }
                ],
            }
        },
        materialOutstockStateList() {
            return [
                { value: 1, descript: this.$t('field.MATERIAL_STOCK_STATE.1') },
                { value: 2, descript: this.$t('field.MATERIAL_STOCK_STATE.2') },
                { value: 3, descript: this.$t('field.MATERIAL_STOCK_STATE.3') },
            ]
        },
        columnList() {
            return [
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_NAME'), key: 'materialName'},
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_COUNT'), key: 'quantity'},
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_WAREHOUSE'), key: 'warehouseName'},
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_PRINCIPAL'), key: 'principalName'},
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_PLACE'), key: 'place'},
                { title: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_REMARK'), key: 'remark'},
                
            ]
        },
        tableHeight() {
            return document.documentElement.clientHeight - 400;
        },
    },
    methods: {
    	initData() {
            // 路由检查
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.billId = Number(this.$route.params.id);
                this.getById();
                this.getMaterial();
            } else if (this.$route.params.id === 'add') {
                this.setDefault();
                this.materialPrincipal = this.$store.state.app.loginAdmin.trueName;
                this.getMaterials();
                this.getWarehouses();
                this.getAdmins();
            } else {
                this.$router.replace('/material-outstock');
            }
        },
        setDefault() {
            this.item = {
            	billId: 0,
                billNo: '',
                remark: '',
                materialOutstockTime: new Date(),
                warehousePrincipal: this.$store.state.app.loginAdmin.adminId,
                materialSource: 1,
                materialOutstockState: 1,
                relatedBill: '',
                materials: [],
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.billId === 0) return;
            let result = await materialOutstockService.getById(this.item.billId);
            if (result.status === 200) {
            	this.getAdmins();
                this.item = result.data;
                this.item.materialOutstockTime = new Date().toLocaleDateString(this.item.billTime);
                this.selectedPrincipal = this.item.toPrincipal;
                this.selectedSource = this.item.materialWhereabouts;
                if (this.item.sysDefault) { // 系统默认用户不可查看
                    this.$router.replace('/material-outstock');
                }
                this.item.materialOutstockState = Number(this.item.billState);
            } else {
                this.$router.replace('/material-outstock');
            }
        },
        async getMaterial() {
            let result = await materialOutstockService.getMaterial(this.item.billId)
            if (result.status === 200) {
                this.materials = result.data;
            }
        },
        async getMaterials() {
            let result = await materialOutstockService.getMaterials()
            if (result.status === 200) {
                this.allMaterials = result.data;
            }
        },
        async getAdmins() {
            let result = await materialOutstockService.getAdmins()
            if (result.status === 200) {
                this.allAdmins = result.data;
            }
        },
        async getWarehouses() {
            let result = await materialOutstockService.getWarehouses()
            if (result.status === 200) {
                this.allWarehouses = result.data;
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                	this.item.selectedPrincipal = this.selectedPrincipal;
                    this.item.selectedSource = this.selectedSource;
                    if (this.$route.params.id === 'add' && this.item.billId === 0) {
                    	this.item.materials = JSON.stringify(this.materials);
                        let result = await materialOutstockService.add(this.item);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS')+this.$t('common.DOT'));
                            var item = result.data;
                            this.$router.replace('/material-outstock/' + item.billId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await materialOutstockService.update(this.item);
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
        async audit() {
            let result = await materialOutstockService.audit(this.item.billId);
            this.checkResult(result);
        },
        async unaudit() {
            let result = await materialOutstockService.unaudit(this.item.billId);
            this.checkResult(result);
        },
        async finish() {
            let result = await materialOutstockService.finish(this.item.billId);
            this.checkResult(result)
        },
        checkResult(result) {
            if (result.status === 200) {
                this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                this.getById();
                this.getMaterial();
            }
        },
        ok() {
        	let temp = {}
            if (this.selectedMaterial != '' && this.selectedWarehouse != '' && this.materialCount != '') {
                temp['materialId'] = this.selectedMaterial
                temp['warehouse'] = this.selectedWarehouse
                for (let i = 0; i < this.allMaterials.length; ++i) {
                    if (this.allMaterials[i]['materialId'] == this.selectedMaterial) {
                        temp['materialName'] = this.allMaterials[i]['materialName']
                    }
                }
                for (let i = 0; i < this.allWarehouses.length; ++i) {
                    if (this.allWarehouses[i]['warehouseId'] == this.selectedWarehouse) {
                        temp['warehouseName'] = this.allWarehouses[i]['warehouseName']
                    }
                }
                temp['quantity'] = parseInt(this.materialCount);
                temp['principal'] = this.$store.state.app.loginAdmin.adminId;
                temp['principalName'] = this.$store.state.app.loginAdmin.trueName;
                temp['place'] = this.materialPlace;
                temp['remark'] = this.materialRemark;
            }
            this.materials.push(temp);
            this.selectedMaterial = '';
            this.selectedWarehouse = '';
            this.materialCount = '';
            this.materialPlace = '';
            this.materialRemark = '';
        },
        cancel() {
            this.selectedMaterial = '';
            this.selectedWarehouse = '';
            this.materialCount = '';
            this.materialPlace = '';
            this.materialRemark = '';
        },
        picked(time) {
        	this.item.materialOutstockTime = time;
        },
        addMaterial() {
        	this.modal = true;
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
