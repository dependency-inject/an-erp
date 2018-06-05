<template>
    <div class="main-panel">
    	<div class="main-panel-content2">
    		<div class="panel-container">
    			<i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
    				<div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_NO')" prop="billNo"><i-input v-model="item.billNo"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_TITLE')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_TIME')" prop="materialInstockTime"><date-picker v-model="item.materialInstockTime" v-on:change="picked"></date-picker></form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_TRANSFER')">
                            	<Select v-model="selectedPrincipal" style="width:200px">
									<Option v-for="item in allAdmins" :value="item.adminId" :key="item.adminId">{{ item.trueName }}</Option>
								</Select>
							</form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_CHARGE')"><i-input :value="this.$store.state.app.loginAdmin.trueName" :disabled=true></i-input></form-item>
                            
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_SOURCE')">
                            	<Select v-model="selectedSource" style="width:200px">
									<Option v-for="item in allSources" :value="item.label" :key="item.str">{{ item.str }}</Option>
								</Select>
							</form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_STATE')" prop="materialInstockState">
                            	{{materialInstockStateList[item.materialInstockState - 1].descript}}
                            </form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_RELATED')" prop="relatedBill"><i-input v-model="item.relatedBill"></i-input></form-item>
                        </div>
						<div class="chief-panel">
							<div class="panel-header">{{ $t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_SELECT') }}
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
				<form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_NAME')">
					<Select v-model="selectedMaterial" style="width:200px">
						<Option v-for="item in allMaterials" :value="item.materialId" :key="item.materialId">{{ item.materialName }}</Option>
					</Select>
				</form-item>
				<form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_WAREHOUSE')">
					<Select v-model="selectedWarehouse" style="width:200px">
						<Option v-for="item in allWarehouses" :value="item.warehouseId" :key="item.warehouseId">{{ item.warehouseName }}</Option>
					</Select>
				</form-item>
				<form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_COUNT')">
					<i-input v-model="materialCount" :placeholder="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_COUNT')"></i-input>
				</form-item>
				<form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_PRINCIPAL')">
					<i-input v-model="materialPrincipal" :placeholder="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_PRINCIPAL')" :disabled=true></i-input>
				</form-item>
				<form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_PLACE')">
					<i-input v-model="materialPlace" :placeholder="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_PLACE')"></i-input>
				</form-item>
				<form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_REMARK')">
					<i-input v-model="materialRemark" :placeholder="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_REMARK')"></i-input>
				</form-item>
			</i-form>
		</Modal>
    	<div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(materialInstockAddPermission&&$route.params.id==='add'&&item.billId===0)||(materialInstockUpdatePermission&&item.billId!==0)">{{ $t('common.SAVE') }}</i-button>
        	<i-button class="operate-btn" type="primary" shape="circle" @click="audit" v-if="item.materialInstockState==1 && materialInstockAuditPermission">{{ $t('common.AUDIT') }}</i-button>
            <i-button class="operate-btn" type="primary" shape="circle" @click="unaudit" v-if="item.materialInstockState==2 && materialInstockAuditPermission">{{ $t('common.UNAUDIT') }}</i-button>
            <i-button class="operate-btn" type="primary" shape="circle" @click="finish" v-if="item.materialInstockState==2 && materialInstockFinishPermission">{{ $t('完成') }}</i-button>
    	</div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import permissionTable from '../../components/permission-table';

import materialInstockService from '../../service/material-instock';

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
            allSources: [{str: '退料入库', label: 1}, {str: '采购入库', label: 2}],
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
                    { required: true, message: this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_NO')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                materialInstockState: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_STATE'), trigger: 'change' }
                ],
            }
        },
        materialInstockStateList() {
            return [
                { value: 1, descript: this.$t('field.MATERIAL_STOCK_STATE.1') },
                { value: 2, descript: this.$t('field.MATERIAL_STOCK_STATE.2') },
                { value: 3, descript: this.$t('field.MATERIAL_STOCK_STATE.3') },
            ]
        },
        columnList() {
            return [
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_NAME'), key: 'materialName'},
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_COUNT'), key: 'quantity'},
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_WAREHOUSE'), key: 'warehouseName'},
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_PRINCIPAL'), key: 'principalName'},
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_PLACE'), key: 'place'},
                { title: this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_REMARK'), key: 'remark'},
            
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
                this.$router.replace('/material-instock');
            }
        },
        setDefault() {
            this.item = {
            	billId: 0,
                billNo: '',
                remark: '',
                materialInstockTime: new Date(),
                warehousePrincipal: this.$store.state.app.loginAdmin.adminId,
                materialSource: 1,
                materialInstockState: 1,
                relatedBill: '',
                materials: [],
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.billId === 0) return;
            let result = await materialInstockService.getById(this.item.billId);
            if (result.status === 200) {
            	this.getAdmins();
                this.item = result.data;
                this.item.materialInstockTime = new Date().toLocaleDateString(this.item.billTime);
                this.selectedPrincipal = this.item.fromPrincipal;
                this.selectedSource = this.item.materialSource;
                if (this.item.sysDefault) { // 系统默认用户不可查看
                    this.$router.replace('/material-instock');
                }
                this.item.materialInstockState = Number(this.item.billState);
            } else {
                this.$router.replace('/material-instock');
            }
        },
        async getMaterial() {
            let result = await materialInstockService.getMaterial(this.item.billId)
            if (result.status === 200) {
                this.materials = result.data;
            }
        },
        async getMaterials() {
            let result = await materialInstockService.getMaterials()
            if (result.status === 200) {
                this.allMaterials = result.data;
            }
        },
        async getAdmins() {
            let result = await materialInstockService.getAdmins()
            if (result.status === 200) {
                this.allAdmins = result.data;
            }
        },
        async getWarehouses() {
            let result = await materialInstockService.getWarehouses()
            if (result.status === 200) {
                this.allWarehouses = result.data;
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                	this.item.selectedPrincipal = this.selectedPrincipal;
                    this.item.selectedSource = this.selectedSource;
                    if (this.$route.params.id === 'add' && this.item.billId === 0)
                    {
                    	this.item.materials = JSON.stringify(this.materials);
                        let result = await materialInstockService.add(this.item);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS')+this.$t('common.DOT'));
                            var item = result.data;
                            this.$router.replace('/material-instock/' + item.billId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await materialInstockService.update(this.item);
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
            let result = await materialInstockService.audit(this.item.billId);
            this.checkResult(result);
        },
        async unaudit() {
            let result = await materialInstockService.unaudit(this.item.billId);
            this.checkResult(result);
        },
        async finish() {
            let result = await materialInstockService.finish(this.item.billId);
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
        	this.item.materialInstockTime = time;
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
