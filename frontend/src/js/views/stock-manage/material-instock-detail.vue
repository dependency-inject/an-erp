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
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_TRANSFER')" prop="fromPrincipal"><i-input v-model="item.fromPrincipal"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_CHARGE')" prop="warehousePrincipal"><i-input v-model="item.warehousePrincipal"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_SOURCE')" prop="materialSource"><i-input v-model="item.materialSource"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_STATE')" prop="materialInstockState"><radio-group v-model="item.materialInstockState"><radio v-for="item in materialInstockStateList" :key="item.value" :label="item.value">{{ item.descript }}</radio></radio-group></form-item>
                            <form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_RELATED')" prop="relatedBill"><i-input v-model="item.relatedBill"></i-input></form-item>
                        	<form-item :label="$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_SELECT')" prop="materialIdList"><i-input v-model="item.materialIdList"></i-input></form-item>
                        </div>
                   </div>
    			</i-form>
    		</div>
    	</div>
    	<div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(materialInstockAddPermission&&$route.params.id==='add'&&item.billId===0)||(materialInstockUpdatePermission&&item.billId!==0)">{{ $t('common.SAVE') }}</i-button>
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
                remark: [
                    { required: true, message: this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_TITLE')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                materialInstockState: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_STATE'), trigger: 'change' }
                ],
                materialIdList: [
                    { required: true, message: this.$t('field.MATERIAL_INSTOCK.MATERIAL_INSTOCK_MATERIAL_SELECT')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
            }
        },
        materialInstockStateList() {
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
                this.$router.replace('/material-instock');
            }
        },
        setDefault() {
            this.item = {
            	billId: 0,
                billNo: '111',
                remark: '',
                materialInstockTime: new Date(),
                fromPrincipal: 0,
                warehousePrincipal: 0,
                materialSource: 1,
                materialInstockState: 1,
                relatedBill: 2,
                materialIdList: '0'
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.billId === 0) return;
            let result = await materialInstockService.getById(this.item.billId);
            if (result.status === 200) {
                this.item = result.data;
                this.item.materialInstockTime = new Date().toLocaleDateString(this.item.billTime);
                if (this.item.sysDefault) { // 系统默认用户不可查看
                    this.$router.replace('/material-instock');
                }
                this.item.materialInstockState = Number(this.item.billState);
            } else {
                this.$router.replace('/material-instock');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    if (this.$route.params.id === 'add' && this.item.billId === 0) {
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
        picked(time) {
        	this.item.materialInstockTime = time;
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
