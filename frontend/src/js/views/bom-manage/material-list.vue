<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                    <!-- 搜索框 -->
                    <i-input :placeholder="$t('component.PLEASE_INPUT')+$t('field.MATERIAL.MATERIAL_NO')+'/'+$t('field.MATERIAL.MATERIAL_NAME')" v-model="vm.queryParameters.searchKey" @on-enter="search" style="width:280px"></i-input>
                    <i-button type="ghost" shape="circle" icon="ios-search" @click="search" style="margin-left:8px"></i-button>
                </div>
                <div class="pull-left operate-list" v-show="selectItems!=''">
                    <a class="cancel-btn" @click="clearChecked"><icon type="close"></icon></a>
                    <span class="split-bar">{{ $t('component.SELECTED') }} <span class="text-primary">{{ selectItems.length }}</span> {{ $t('component.ITEMS') }}</span>
                    <!-- 删除 -->
                    <span class="label-btn" @click="remove(selectItems)" v-if="materialRemovePermission"><icon type="trash-a"></icon>{{ $t('common.REMOVE') }}</span>
                </div>
                <!-- 顶部右侧按钮 -->
                <div class="button-list pull-right">
                    <i-button class="operate-btn" type="primary" shape="circle" @click="$router.push('/material/add')" v-if="materialAddPermission">{{ $t('common.ADD') }}</i-button>
                </div>
                <div class="clearfix"></div>
            </div>
            <!-- 表格 -->
            <div class="main-content" style="display:flex">
                <div style="border: 1px solid #dddee1; padding: 15px; margin-right: 10px; min-width: 200px;"><tree :data="categoryList" @on-select-change="selectChange" ref="tree"></tree></div>
                <i-table border :height="tableHeight" ref="table" :columns="columnList" :data="vm.items" @on-sort-change="handleSort" @on-selection-change="selectItems=arguments[0]"></i-table>
            </div>
            <!-- 翻页控制器 -->
            <div class="page-panel">
                <page :total="vm.queryParameters.total" :current="vm.queryParameters.current" :page-size="vm.queryParameters.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="vm.queryParameters.current=arguments[0];selectItems=[];search()" @on-page-size-change="vm.queryParameters.limit=arguments[0];selectItems=[];search()"></page>
            </div>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import materialService from '../../service/material';
import materialCategoryService from '../../service/material-category';

export default {
    name: 'material-list',
    mixins: [ Permission ],
    data() {
        return {
            vm: {
                queryParameters: {
                    categoryId: -1,
                    searchKey: '',
                    total: 0,
                    limit: 10,
                    current: 1,
                    sortColumn: '',
                    sort: ''
                },
                items: [],
                identity: 'materialId',
            },
            selectItems: [],
            categoryList: []
        }
    },
    computed: {
        tableHeight() {
            return document.documentElement.clientHeight - 220;
        },
        // 控制表格显示哪些列
        columnList() {
            return [
                { type: 'selection', width: 60, align: 'center' },
                { title: this.$t('field.MATERIAL.MATERIAL_NO'), key: 'materialNo', sortable: 'custom' },
                { title: this.$t('field.MATERIAL.MATERIAL_NAME'), key: 'materialName', sortable: 'custom' },
                { title: this.$t('field.MATERIAL.UNIT'), key: 'unit' },
                { title: this.$t('field.MATERIAL.SPEC'), key: 'spec' },
                { title: this.$t('field.MATERIAL.CATEGORY_NAME'), key: 'categoryName', sortable: 'custom' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            this.$router.push('/material/'+row[this.vm.identity])
                        }, 'detailPermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => {
                            this.remove([row]) 
                        }, 'removePermission')]);
                    } 
                }
            ];
        },
    },
    methods: {
        initData() {
            this.search();
        },
        async search() {
            let idList = _.map(this.selectItems, this.vm.identity).join(",");
            let result = await materialService.search(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item['detailPermission'] = true;
                    if (this.materialRemovePermission)
                        item['removePermission'] = true;
                });
                this.vm.items = items;
                this.$nextTick(() => {
                    for (var i = 0; i < items.length; ++i) {
                        if ((','+idList+',').indexOf(','+items[i][this.vm.identity]+',') > -1) {
                            this.$refs.table.toggleSelect(i);
                        }
                    }
                });
            }
        },
        async initCategoryList(){
            let result = await materialCategoryService.getList();
            if (result.status === 200) {
                var items = result.data;
                this.categoryList = [{ categoryId: -1, title: this.$t('component.ALL_CATEGORY'), expand: true, children: this.generateCategoryList(0, items) }];
            }
        },
        generateCategoryList(parent, list){
            let result = [];
            list.forEach((item) => {
                if (item.parentId == parent) {
                    item.title = item.categoryName;
                    item.children = this.generateCategoryList(item.categoryId, list);
                    result.push(item);
                }
            });
            return result;
        },
        handleSort(data) {
            if (data.order === 'normal') {
                this.vm.queryParameters.sortColumn = '';
                this.vm.queryParameters.sort = '';
            } else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            this.selectItems = [];
            this.search();
        },
        remove(selectItems) {
            let idList = _.map(selectItems, this.vm.identity).join(",");
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: async () => {
                    let result = await materialService.remove(idList);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.REMOVE_SUCCESS'));
                        this.selectItems = [];
                        this.search();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        clearChecked() {
            this.$refs.table.selectAll(false);
        },
        selectChange(items) {
            this.vm.queryParameters.categoryId = items[0].categoryId;
            this.search();
        }
    },
    mounted() {
        this.initData();
        this.initCategoryList();
    }
}
</script>
