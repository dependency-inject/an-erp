<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.MATERIAL.MATERIAL_NO')" prop="materialNo"><i-input v-model="item.materialNo"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL.MATERIAL_NAME')" prop="materialName"><i-input v-model="item.materialName"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.MATERIAL_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.MATERIAL.UNIT')" prop="unit"><i-input v-model="item.unit"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL.SPEC')" prop="spec"><i-input v-model="item.spec"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL.REMARK')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.CATEGORY_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.MATERIAL.CATEGORY_ID')" prop="categoryId">
                                <tree-select type="material-category" v-model="item.categoryId"></tree-select>
                            </form-item>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="editable">{{ $t('common.SAVE') }}</i-button>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission'

import treeSelect from '../../components/tree-select';

import materialService from '../../service/material';

export default {
    name: 'material-detail',
    mixins: [Permission],
    data() {
        return {
            item: {},
        }
    },
    computed: {
        rules() {
            return {
                materialName: [
                    { required: true, message: this.$t('field.MATERIAL.MATERIAL_NAME') + this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                materialNo: [
                    { required: true, message: this.$t('field.MATERIAL.MATERIAL_NO') + this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                categoryId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT') + this.$t('field.MATERIAL.CATEGORY_ID'), trigger: 'blur' }
                ]
            }
        },
        editable() {
            return (this.materialAddPermission && this.$route.params.id === 'add' && this.item.materialId === 0) || (this.materialUpdatePermission && this.item.materialId !==0);
        }
    },
    components: { treeSelect },
    methods: {
        initData() {
            // 路由检查
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.materialId = Number(this.$route.params.id);
                this.getById();
            } else if (this.$route.params.id === 'add') {
                this.setDefault();
            } else {
                this.$router.replace('/material');
            }
        },
        setDefault() {
            this.item = {
                materialId: 0,
                materialName: '',
                materialNo: '',
                unit: '',
                spec: '',
                remark: '',
                categoryId: '',
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.materialId === 0) return;
            let result = await materialService.getById(this.item.materialId);
            if (result.status === 200) {
                this.item = result.data;
            } else {
                this.$router.replace('/material');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    if (this.$route.params.id === 'add' && this.item.materialId === 0) {
                        let result = await materialService.add(this.item);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            var item = result.data;
                            this.$router.replace('/material/' + item.materialId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await materialService.update(this.item);
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
