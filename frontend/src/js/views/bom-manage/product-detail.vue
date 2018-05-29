<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.DETAIL_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.PRODUCT.NO')" prop="productNo"><i-input v-model="item.productNo"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.NAME')" prop="productName"><i-input v-model="item.productName"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.UNIT')" prop="unit"><i-input v-model="item.unit"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.CATEGORY_ID')" prop="categoryId"><i-input v-model="item.categoryId"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.SPEC')" prop="spec"><i-input v-model="item.spec"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.PRICE')" prop="price"><i-input v-model="item.price"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.STATE')" prop="closed"><radio-group v-model="item.closed"><radio v-for="item in closedList" :key="item.value" :label="item.value">{{ item.descript }}</radio></radio-group></form-item>
                            <form-item :label="$t('field.PRODUCT.REMARK')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.CREATE_AT')" prop="createAt"><i-input v-model="item.createAt" :disabled="true"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.CREATE_BY')" prop="createBy"><i-input v-model="item.createBy" :disabled="true"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.UPDATE_AT')" prop="updateAt"><i-input v-model="item.updateAt" :disabled="true"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.UPDATE_BY')" prop="updateBy"><i-input v-model="item.updateBy" :disabled="true"></i-input></form-item>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(productAddPermission&&$route.params.id==='add'&&item.productId===0)||(productUpdatePermission&&item.productId!==0)">{{ $t('common.SAVE') }}</i-button>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import permissionTable from '../../components/permission-table';

import productService from '../../service/product';

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
                productNo: [
                    { required: true, message: this.$t('field.PRODUCT.NO')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                productName: [
                    { required: true, message: this.$t('field.PRODUCT.NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                unit: [
                    { required: true, message: this.$t('field.PRODUCT.UNIT')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                categoryId: [
                    { required: true, message: this.$t('field.PRODUCT.CATEGORT_ID')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                spec: [
                    { required: true, message: this.$t('field.PRODUCT.SPEC')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                closed: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.PRODUCT.STATE'), trigger: 'change' }
                ]
            }
        },
        closedList() {
            return [
                { value: 0, descript: this.$t('field.CLOSED.0') },
                { value: 1, descript: this.$t('field.CLOSED.1') },
            ]
        }
    },
    methods: {
        initData() {
            // 路由检查
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.productId = Number(this.$route.params.id);
                this.getById();
            } else if (this.$route.params.id === 'add') {
                this.setDefault();
            } else {
                this.$router.replace('/product');
            }
        },
        setDefault() {
            this.item = {
                productId: 0,
                productName: '',
                unit: '',
                categoryId: 0,
                spec: '',
                price: 0,
                closed: 0
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.productId === 0) return;
            let result = await productService.getById(this.item.productId);
            if (result.status === 200) {
                this.item = result.data;
                if (this.item.sysDefault) { // 系统默认用户不可查看
                    this.$router.replace('/product');
                }
                this.item.closed = Number(this.item.closed);
            } else {
                this.$router.replace('/product');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    if (this.$route.params.id === 'add' && this.item.productId === 0) {
                        let result = await productService.add(this.item);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            var item = result.data;
                            this.$router.replace('/product/' + item.productId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await productService.update(this.item);
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
        handleRoleChange(roleList) {
            this.permissionIdList = _.union(_.map(roleList, function(item) {
                return item.permissionIdList.split(',');
            })).join(',');
        },
        addToCart(){},
        removeFromCart(){},
        generateMaterialList(){}
        // TODO: 购物车功能
        // 上方提供模板，第一个为select框，选择materialId，第二三四为输入框，数量quantity.属性property.备注remark 其中第一第二不能为空
        // 点击加号添加至列表中，点击保存将数据保存
        // 数据库price默认为0
        // 备注remark
        //  上方： form
        // 下方： table
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
