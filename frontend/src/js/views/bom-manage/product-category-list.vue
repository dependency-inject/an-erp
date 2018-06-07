<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <!-- 顶部右侧按钮 -->
                <div class="button-list pull-right">
                    <i-button class="operate-btn" type="primary" shape="circle" @click="add(0)">
                        {{ $t('common.ADD') }}
                    </i-button>
                </div>
                <div class="clearfix"></div>
            </div>
            <!-- 表格 -->
            <div class="main-content">
                <tree-table 
                    :height="tableHeight" 
                    ref="table" 
                    :columns="columnList"
                    :data="vm.receiveData" 
                    :operates="tableOperates"
                    :identity="vm.identity"
                    @add="add(arguments[0][vm.identity])"
                    @edit="edit"
                    @remove="remove([arguments[0]])"
                >
                </tree-table>
            </div>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="save" :loading="true">
            <i-form ref="formValidate" :model="modal.item" :rules="rules" :label-width="90">
                <form-item :label="$t('field.PRODUCT_CATEGORY.CATEGORY_NAME')" prop="categoryName">
                    <i-input v-model="modal.item.categoryName"></i-input>
                </form-item>
                <form-item :label="$t('field.PRODUCT_CATEGORY.PARENT_ID')" prop="parentId">
                    <tree-select ref="select" type="product-category" v-model="modal.item.parentId" root-option disabled></tree-select>
                </form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import treeTable from '../../components/tree-table';
import treeSelect from '../../components/tree-select';

import productCategoryService from '../../service/product-category';

export default {
    name: 'product-category-list',
    data() {
        return {
            vm: {
                receiveData: [],
                mapIdIndex: [],

                identity: 'categoryId',
                parentIdentity: 'parentId'
            },

            // 模态框数据
            modal: {
                title: 'title',
                item: {
                    parentId: ''
                },
                visible: false
            }
        }
    },

    computed: {
        rules() {
            return {
                categoryName: [
                    { required: true, message: this.$t('field.PRODUCT_CATEGORY.CATEGORY_NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                parentId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.PRODUCT_CATEGORY.PARENT_ID'),
                    trigger: 'change' }
                ]
            }
        },

        tableHeight() {
            return document.documentElement.clientHeight - 180;
        },
        columnList() {
            return [{
                title: this.$t('field.PRODUCT_CATEGORY.CATEGORY_NAME'),
                key: 'categoryName',
                width: 400
            }];
        },
        tableOperates() {
            return [{
                    event: "add",
                    icon: "plus",
                    text: this.$t('common.ADD')
                },
                {
                    event: "edit",
                    icon: "edit",
                    text: this.$t('common.EDIT')
                },
                {
                    event: "remove",
                    icon: "trash-a",
                    text: this.$t('common.REMOVE'),
                    checkChild: true
                }
            ]
        },
    },

    components: {
        treeTable, treeSelect
    },

    methods: {
        initData() {
            this.search();
        },

        async search() {
            let result = await productCategoryService.getList();

            if (result.status === 200) {
                this.vm.receiveData = result.data;
                this.buildMapIdIndex();

                var items = this.vm.receiveData;
                var map = this.vm.mapIdIndex;

                // 为 parent 赋值
                items.forEach((obj) => {
                    if (obj[this.vm.parentIdentity] === 0) {
                        obj.parent = -1;
                    } 
                    else {
                        obj.parent = map.get(obj[this.vm.parentIdentity]);
                    }
                });
            }
        },

        // 删除记录
        remove(selectItems) {
            let idList = _.map(selectItems, this.vm.identity).join(",");
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: async () => {
                    let result = await productCategoryService.remove(idList);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.REMOVE_SUCCESS'));
                        this.$refs.select.search();
                        this.search();
                    }
                    else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },

        // 添加记录
        add(parent) {
            this.modal.title = this.$t('common.ADD') + this.$t('field.PRODUCT_CATEGORY.CATEGORY_NAME');
            this.$refs.formValidate.resetFields();
            this.modal.item.categoryId = 0;
            this.modal.item.categoryName = '';
            this.modal.item.parentId = parent;
            this.modal.visible = true;
        },

        // 修改记录
        edit(item) {
            this.modal.title = this.$t('common.EDIT') + this.$t('field.PRODUCT_CATEGORY.CATEGORY_NAME');
            this.$refs.formValidate.resetFields();
            this.modal.item.categoryId = item.categoryId;
            this.modal.item.categoryName = item.categoryName;
            this.modal.item.parentId = item.parentId;
            this.modal.visible = true;
        },

        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let result = {}
                    if (this.modal.item.categoryId === 0)
                        result = await productCategoryService.add(this.modal.item);
                    else
                        result = await productCategoryService.update(this.modal.item);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                        this.modal.visible = false;
                        this.$refs.select.search();
                        await this.search();
                    } else {
                        this.$Message.error(result.data);
                        this.$refs.modal.buttonLoading = false;
                    }
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.buttonLoading = false;
                }
            });
        },
        
        // 构造 categoryId-Index 的 map
        buildMapIdIndex() {
            this.vm.mapIdIndex = new Map();
            var map = this.vm.mapIdIndex;
            var receiveData = this.vm.receiveData;

            receiveData.forEach((obj, index) => {
                map.set(obj[this.vm.identity], index)
            });
        },
    },

    mounted() {
        this.initData();
    }
}
</script>