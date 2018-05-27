<template>
	<div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list">
                    <!-- 搜索框 -->
                    <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.SUPPLIER.MATERIAL_NO')+'/'+$t('field.SUPPLIER.MATERIAL_NAME')" v-model="vm.queryParameters.searchKey" @on-enter="search()" style="width:300px"></i-input>
                </div>
                <div class="clearfix"></div>
            </div>
            <!-- 表格 -->
            <div class="main-content">
                <i-table :height="tableHeight" ref="table" :columns="columnList" :data="vm.items" @on-sort-change="handleSort"></i-table>
            </div>
            <!-- 翻页控制器 -->
            <div class="page-panel">
                <page :total="vm.queryParameters.total" :current="vm.queryParameters.current" :page-size="vm.queryParameters.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="vm.queryParameters.current=arguments[0];selectItems=[];search()" @on-page-size-change="vm.queryParameters.limit=arguments[0];selectItems=[];search()"></page>
            </div>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="save" :loading="true">
        	<i-table :height="tableHeight" ref="table" :columns="columnList" :data="modal.items" @on-sort-change="handleSortPrice"></i-table>
    	</modal>
	</div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import supplierService from '../../service/supplier';

export default {
    mixins: [ Permission ],
    data() {
        return {
            vm: {
                queryParameters: {
                    searchKey: '',
                    total: 0,
                    limit: 10,
                    current: 1,
                    sortColumn: '',
                    sort: ''
                },
                items: [],
                identity: 'supplierId',
            },
            modal: {
                title: 'title',
                items: [],
                visible: false
            }
            // selectItems: [],
        }
    },
    computed: {
        tableHeight() {
            return document.documentElement.clientHeight - 220;
        },
        // 控制表格显示哪些列
        columnList() {
            return [
                { type: 'index', width: 80, align: 'center' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NO'), key: 'supplierName', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NAME'), key: 'contact', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.UNIT'), key: 'contactPhone', },
                { title: this.$t('field.SUPPLIER.INVENTORY'), key: 'region' },
                { title: this.$t('field.SUPPLIER.DRAW_MATERIAL_QUANTITY'), key: 'address' },
                { title: this.$t('field.SUPPLIER.PRODUCT_OUTSTOCK_QUANTITY'), key: 'address' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('field.SUPPLIER.SEARCH_MATERIAL_PRICE'), (row) => {
                            this.searchMaterialPrice([row]); 
                        }, 'detailPermission')]);
                    } 
                }
            ];
        }
    },
    methods: {
        initData() {
            this.search();
        },
        async search() {
            // let idList = _.map(this.selectItems, this.vm.identity).join(",");
            let result = await supplierService.search(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    if (this.lackMaterialPermission)
                        item['Permission'] = true;
                });
                this.vm.items = items;
                // this.$nextTick(() => {
                //     for (var i = 0; i < items.length; ++i) {
                //         if ((','+idList+',').indexOf(','+items[i][this.vm.identity]+',') > -1) {
                //             this.$refs.table.toggleSelect(i);
                //         }
                //     }
                // });
            }
        },
        handleSort(data) {
            if (data.order === 'normal') {
                this.vm.queryParameters.sortColumn = '';
                this.vm.queryParameters.sort = '';
            }
            else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            // this.selectItems = [];
            this.search();
        },
        handleSortPrice(data) {
            if (data.order === 'normal') {
                this.vm.queryParameters.sortColumn = '';
                this.vm.queryParameters.sort = '';
            }
            else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            // this.selectItems = [];
            this.search();
        },
        searchMaterialPrice(selectItems) {
            // let idList = _.map(selectItems, this.vm.identity).join(",");
            // this.$Modal.confirm({
            //     content: this.$t('common.REMOVE_CONFIRM'),
            //     onOk: async () => {
            //         let result = await supplierService.remove(idList);
            //         if (result.status === 200) {
            //             this.$Message.success(this.$t('common.REMOVE_SUCCESS'));
            //             this.selectItems = [];
            //             this.search();
            //         } else {
            //             this.$Message.error(result.data);
            //         }
            //     }
            // });
            this.modal.title = this.$t('field.SUPPLIER.SEARCH_MATERIAL_PRICE');
            // this.modal.item.roleId = 0;
            // this.modal.item.roleName = '';
            this.modal.visible = true;
        }
    },
    mounted() {
        this.initData();
    }
}
</script>
