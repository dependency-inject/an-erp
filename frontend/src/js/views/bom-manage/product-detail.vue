<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.DETAIL_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.PRODUCT.PRODUCT_NO')" prop="productNo"><i-input v-model="item.productNo"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.PRODUCT_NAME')" prop="productName"><i-input v-model="item.productName"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.UNIT')" prop="unit"><i-input v-model="item.unit"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.CATEGORY_ID')" prop="categoryId"><tree-select type="product-category" v-model="item.categoryId"></tree-select></form-item>
                            <form-item :label="$t('field.PRODUCT.SPEC')" prop="spec"><i-input v-model="item.spec"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.PRICE')" prop="price"><i-input v-model="item.price"></i-input></form-item>
                            <form-item :label="$t('field.PRODUCT.STATE')" prop="closed"><radio-group v-model="item.closed"><radio v-for="item in closedList" :key="item.value" :label="item.value">{{ item.descript }}</radio></radio-group></form-item>
                            <form-item :label="$t('field.PRODUCT.REMARK')" prop="remark"><i-input v-model="item.remark"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.MATERIAL_INFO') }}&nbsp;&nbsp;<span v-if="editable">（<a class="remark" @click="addMaterial"><icon type="plus"></icon> {{ $t('common.ADD')+$t('field.MATERIAL_INFO') }}</a>）</span></div>
                        <div class="panel-body">
                            <i-table border :columns="columnList" :data="this.item.productMaterialList"></i-table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(productAddPermission&&$route.params.id==='add'&&item.productId===0)||(productUpdatePermission&&item.productId!==0)">{{ $t('common.SAVE') }}</i-button>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" @on-ok="saveMaterial" :loading="true">
            <i-form ref="formValidate2" :model="modal.item" :rules="rules2" :label-width="90">
                <form-item :label="$t('field.PRODUCT.MATERIAL')" prop="materialId">
                    <common-select type="material" v-model="modal.item.materialId" @on-change="materialSelectChange"></common-select>
                </form-item>
                <form-item :label="$t('field.PRODUCT.MATERIAL_QUANTITY')" prop="quantity"><input-number v-model="modal.item.quantity" :min="1" style="width:100%"></input-number></form-item>
                <form-item :label="$t('field.PRODUCT.MATERIAL_PROPERTY')" prop="materialProperty"><i-input v-model="modal.item.materialProperty" type="textarea"></i-input></form-item>
                <form-item :label="$t('field.PRODUCT.MATERIAL_REMARK')" prop="remark"><i-input v-model="modal.item.remark" type="textarea"></i-input></form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import commonSelect from '../../components/common-select';
import treeSelect from '../../components/tree-select';

import productService from '../../service/product';

export default {
    mixins: [ Permission ],
    data() {
        return {
            item: {},
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
                productNo: [
                    { required: true, message: this.$t('field.PRODUCT.PRODUCT_NO')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                productName: [
                    { required: true, message: this.$t('field.PRODUCT.PRODUCT_NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                categoryId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.PRODUCT.CATEGORY_ID'), trigger: 'change' }
                ],
                closed: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.PRODUCT.STATE'), trigger: 'change' }
                ]
            }
        },
        rules2() {
            return {
                materialId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.PRODUCT.MATERIAL'), trigger: 'change' }
                ],
                quantity: [
                    { type: 'number', required: true, message: this.$t('field.PRODUCT.MATERIAL_QUANTITY')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ]
            }
        },
        closedList() {
            return [
                { value: 0, descript: this.$t('field.CLOSED.0') },
                { value: 1, descript: this.$t('field.CLOSED.1') },
            ]
        },
        editable() {
            return (this.productAddPermission && this.$route.params.id === 'add' && this.item.productId === 0) || (this.productUpdatePermission && this.item.productId !==0);
        },
        columnList() {
            let result = [
                { title: this.$t('field.PRODUCT.MATERIAL_NO'), key: 'materialNo' },
                { title: this.$t('field.PRODUCT.MATERIAL_NAME'), key: 'materialName' },
                { title: this.$t('field.PRODUCT.MATERIAL_QUANTITY'), key: 'quantity' },
                { title: this.$t('field.PRODUCT.MATERIAL_PROPERTY'), key: 'materialProperty' },
                { title: this.$t('field.PRODUCT.MATERIAL_REMARK'), key: 'remark' }
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
    components: { commonSelect, treeSelect },
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
                closed: 0,
                remark: '',
                productMaterialList: []
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.productId === 0) return;
            let result = await productService.getById(this.item.productId);
            if (result.status === 200) {
                this.item = result.data;
                this.item.closed = Number(this.item.closed);
            } else {
                this.$router.replace('/product');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let obj = Object.assign({}, this.item, { productMaterialList: JSON.stringify(this.item.productMaterialList) });
                    if (this.$route.params.id === 'add' && this.item.productId === 0) {
                        let result = await productService.add(obj);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            var item = result.data;
                            this.$router.replace('/product/' + item.productId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await productService.update(obj);
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
        addMaterial() {
            this.modal.title = this.$t('common.ADD') + this.$t('field.MATERIAL_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = -1;
            this.modal.item.materialId = '';
            this.modal.item.quantity = 1;
            this.modal.item.materialProperty = '';
            this.modal.item.remark = '';
            this.modal.visible = true;
        },
        editMaterial(item) {
            this.modal.title = this.$t('common.EDIT') + this.$t('field.MATERIAL_INFO');
            this.$refs.formValidate2.resetFields();
            this.modal.item._index = item._index;
            this.modal.item.materialId = item.materialId;
            this.modal.item.quantity = item.quantity;
            this.modal.item.materialProperty = item.materialProperty;
            this.modal.item.remark = item.remark;
            this.modal.visible = true;
        },
        saveMaterial() {
            this.$refs.formValidate2.validate(async (valid) => {
                if (valid) {
                    if (this.modal.item._index === -1) {
                        this.item.productMaterialList.push({
                            materialId: this.modal.item.materialId,
                            materialNo: this.modal.item.materialNo,
                            materialName: this.modal.item.materialName,
                            quantity: this.modal.item.quantity,
                            materialProperty: this.modal.item.materialProperty,
                            remark: this.modal.item.remark
                        });
                    } else {
                        this.item.productMaterialList[this.modal.item._index].materialId = this.modal.item.materialId;
                        this.item.productMaterialList[this.modal.item._index].materialNo = this.modal.item.materialNo;
                        this.item.productMaterialList[this.modal.item._index].materialName = this.modal.item.materialName;
                        this.item.productMaterialList[this.modal.item._index].quantity = this.modal.item.quantity;
                        this.item.productMaterialList[this.modal.item._index].materialProperty = this.modal.item.materialProperty;
                        this.item.productMaterialList[this.modal.item._index].remark = this.modal.item.remark;
                    }
                    this.modal.visible = false;
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.buttonLoading = false;
                }
            });
        },
        removeMaterial(index) {
            if (!this.editable) return;
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: () => {
                    this.item.productMaterialList.splice(index, 1);
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
