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
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_TRANSFER')" prop="toPrincipal"><i-input v-model="item.toPrincipal"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_CHARGE')" prop="warehousePrincipal"><i-input v-model="item.warehousePrincipal"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_SOURCE')" prop="materialSource"><i-input v-model="item.materialSource"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_STATE')" prop="materialOutstockState"><radio-group v-model="item.materialOutstockState"><radio v-for="item in materialOutstockStateList" :key="item.value" :label="item.value">{{ item.descript }}</radio></radio-group></form-item>
                            <form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_RELATED')" prop="relatedBill"><i-input v-model="item.relatedBill"></i-input></form-item>
                        	<form-item :label="$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_SELECT')" prop="materialIdList"><i-input v-model="item.materialIdList"></i-input></form-item>
                        </div>
                   </div>
    			</i-form>
    		</div>
    	</div>
    	<div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(materialOutstockAddPermission&&$route.params.id==='add'&&item.billId===0)||(materialOutstockUpdatePermission&&item.billId!==0)">{{ $t('common.SAVE') }}</i-button>
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
                materialIdList: [
                    { required: true, message: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_MATERIAL_SELECT')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                remark: [
                    { required: true, message: this.$t('field.MATERIAL_OUTSTOCK.MATERIAL_OUTSTOCK_TITLE')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
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
                this.$router.replace('/material-outstock');
            }
        },
        setDefault() {
            this.item = {
            	billId: 0,
                billNo: '111',
                remark: '',
                materialOutstockTime: new Date(),
                toPrincipal: 0,
                warehousePrincipal: 0,
                materialSource: 1,
                materialOutstockState: 1,
                relatedBill: 2,
                materialIdList: '0'
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.billId === 0) return;
            let result = await materialOutstockService.getById(this.item.billId);
            if (result.status === 200) {
                this.item = result.data;
                this.item.materialOutstockTime = new Date().toLocaleDateString(this.item.billTime);
                if (this.item.sysDefault) { // 系统默认用户不可查看
                    this.$router.replace('/material-outstock');
                }
                this.item.materialOutstockState = Number(this.item.billState);
            } else {
                this.$router.replace('/material-outstock');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    if (this.$route.params.id === 'add' && this.item.billId === 0) {
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
        picked(time) {
        	this.item.materialOutstockTime = time;
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
