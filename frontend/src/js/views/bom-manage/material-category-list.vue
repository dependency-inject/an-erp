<template>
	<div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <!-- 顶部右侧按钮 -->
                <div class="button-list pull-right">
                    <i-button class="operate-btn" type="primary" shape="circle" @click="add(0)">{{ $t('common.ADD') }}</i-button>
                </div>
                <div class="clearfix"></div>
            </div>
            <!-- 表格 -->
            <div class="main-content">
                <tree-table :height="tableHeight" ref="table" :columns="columnList" :data="vm.items" :operates="tableOperates" :identity="vm.identity" @add="add(arguments[0][vm.identity])" @edit="edit" @remove="remove([arguments[0]])"></tree-table>
            </div>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="save" :loading="true">
	       	<i-form ref="formValidate" :model="modal.item" :rules="rules" :label-width="90">
	            <form-item :label="$t('field.MATERIAL_CATEGORY.CATEGORY_NAME')" prop="categoryName"><i-input v-model="modal.item.categoryName"></i-input></form-item>
				<form-item :label="$t('field.MATERIAL_CATEGORY.PARENT_ID')" prop="parentId"><material-category-select ref="select" v-model="modal.item.parentId" root-option disabled></material-category-select></form-item>
	        </i-form>
	    </modal>
    </div>
</template>

<script>
import treeTable from '../../components/tree-table';
import materialCategorySelect from '../../components/material-category-select';

import materialCategoryService from '../../service/material-category';

export default {
    data() {
        return {
        	vm: {
                items: [],
                identity: 'categoryId',
                parentIdentity: 'parentId'
            },
            modal:{
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
                    { required: true, message: this.$t('field.MATERIAL_CATEGORY.CATEGORY_NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                parentId: [
                	{ type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.MATERIAL_CATEGORY.PARENT_ID'),
                		trigger: 'change' }
                ]
            }
        },
        tableHeight() {
            return document.documentElement.clientHeight - 180;
        },
        // 控制表格显示哪些列
        columnList() {
            return [
                { title: this.$t('field.MATERIAL_CATEGORY.CATEGORY_NAME'), key: 'categoryName', width: 400 }
            ];
        },
        // 控制表格有哪些操作
        tableOperates() {
        	return [
	            { event: "add", icon: "plus", text: this.$t('common.ADD_CHILD') },
	            { event: "edit", icon: "edit", text: this.$t('common.EDIT') },
	            { event: "remove", icon: "trash-a", text: this.$t('common.REMOVE'), checkChild: true }
        	]
    	},
    },
    components: { treeTable, materialCategorySelect },
    methods: {
        initData() {
            this.search();
        },
        async search() {
            let result = await materialCategoryService.getAll();
            if (result.status === 200) {
                var items = result.data;
                items.forEach((item) => {
                    if (item[this.vm.parentIdentity] === 0) {
                        item.parent = -1;
                    } else {
                        items.forEach((obj, index) => {
                            if (item[this.vm.parentIdentity] === obj[this.vm.identity]) {
                                item.parent = index;
                            }
                        });
                    }
                });
                this.vm.items = items;
            }
        },
        remove(selectItems) {
        	let idList = _.map(selectItems, this.vm.identity).join(",");
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: async () => {
                    let result = await materialCategoryService.remove(idList);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.REMOVE_SUCCESS'));
                        this.$refs.select.search();
                        this.search();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        add(parent) {
        	this.modal.title = this.$t('common.ADD') + this.$t('field.MATERIAL_CATEGORY.CATEGORY_NAME');
            this.$refs.formValidate.resetFields();
            this.modal.item.categoryId = 0;
            this.modal.item.categoryName = '';
            this.modal.item.parentId = parent;
            this.modal.visible = true;
        },
        edit(item) {
        	this.modal.title = this.$t('common.EDIT') + this.$t('field.MATERIAL_CATEGORY.CATEGORY_NAME');
            this.$refs.formValidate.resetFields();
            this.modal.item.categoryId = item.categoryId;
            this.modal.item.categoryName = item.categoryName;
            this.modal.item.parentId = item.parentId;
            this.modal.visible = true;        	
        },
        //
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let result = {}
                    if (this.modal.item.categoryId === 0)
                        result = await materialCategoryService.add(this.modal.item);
                    else
                        result = await materialCategoryService.update(this.modal.item);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                        this.modal.visible = false;
                        this.$refs.select.search();
                        await this.search();
                    } else {
                        this.$Message.error(result.data);
                        this.$refs.modal.abortLoading();
                    }
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.abortLoading();
                }
            });
        },
    },
    mounted() {
        this.initData();
    }
}
</script>
