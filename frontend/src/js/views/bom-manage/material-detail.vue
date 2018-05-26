<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.MATERIAL.MATERIAL_NAME')" prop="materialName"><i-input v-model="item.materialName"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL.MATERIAL_NO')" prop="materialNo"><i-input v-model="item.materialNo"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.MATERIAL_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.MATERIAL.UNIT')" prop="unit"><i-input v-model="item.unit"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL.SPEC')" prop="spec"><i-input v-model="item.spec"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL.COST')" prop="cost"><i-input v-model="item.cost"></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL.REMARK')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.MATERIAL_CATEGORY_INFO2') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.MATERIAL.CATEGORY_NAME')" prop="categoryName">
                                <!-- TODO 等待叶大佬的控件ing -->
                            </form-item>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(materialAddPermission&&$route.params.id==='add'&&item.materialId===0)||(materialUpdatePermission&&item.materialId!==0)">{{ $t('common.SAVE') }}</i-button>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission'
import materialService from '../../service/material';

export default {
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
                    {
                        required: true,
                        message: this.$t('field.MATERIAL.MATERIAL_NAME') + this.$t('field.NOT_BE_NULL'),
                        trigger: 'blur'
                    }
                ],
                materialNo: [
                    {
                        required: true,
                        message: this.$t('field.MATERIAL.MATERIAL_NO') + this.$t('field.NOT_BE_NULL'),
                        trigger: 'blur'
                    }
                ]
            }
        },
    },
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
                cost: 0,
                spec: '',
                remark: 0,
                categoryId: 0,
                categoryName: ''
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
