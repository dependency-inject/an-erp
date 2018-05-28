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
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.BACK')" @on-ok="back">
        	<i-table :height="tableHeight" ref="table2" :columns="columnList2" :data="modal.items" @on-sort-change="handleSortPrice"></i-table>
            <div class="page-panel">
                <page :total="modal.total" :current="modal.current" :page-size="modal.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="modal.current=arguments[0];selectItems=[];searchMaterialPrice()" @on-page-size-change="modal.limit=arguments[0];selectItems=[];searchMaterialPrice()"></page>
            </div>
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
                identity: 'materialNo',
            },
            modal: {
                title: 'title',
                items: [],
                visible: false,
                sortColumn: '',
                sort: '',
                total: 0,
                limit: 10,
                current: 1,
                identity: 'supplierId'
            },
            selectItems: [],
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
                { title: this.$t('field.SUPPLIER.MATERIAL_NO'), key: 'materialNo', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NAME'), key: 'materialName', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.UNIT'), key: 'unit', },
                { title: this.$t('field.SUPPLIER.INVENTORY'), key: 'inventory' },
                { title: this.$t('field.SUPPLIER.DRAW_MATERIAL_QUANTITY'), key: 'drawMaterialQuantity' },
                { title: this.$t('field.SUPPLIER.PRODUCT_OUTSTOCK_QUANTITY'), key: 'productOutstockQuantity' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('field.SUPPLIER.SEARCH_MATERIAL_PRICE'), (row) => {
                            this.searchMaterialPrice(row); 
                        }, 'detailPermission')]);
                    } 
                }
            ];
        },
        columnList2() {
            return [
                { type: 'index', width: 80, align: 'center' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NO'), key: 'materialNo', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NAME'), key: 'materialName', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.SUPPLIPER_NAME'), key: 'supplierName', sortable: 'custom'},
                { title: this.$t('field.SUPPLIER.PRICE'), key: 'price', sortable: 'custom' }
            ];
        }
    },
    methods: {
        initData() {
            this.search();
        },
        async search() {
            let idList = _.map(this.selectItems, this.vm.identity).join(",");
            let result = await supplierService.searchLackMaterial(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    if (this.lackMaterialPermission)
                        item['detailPermission'] = true;
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
        handleSort(data) {
            if (data.order === 'normal') {
                this.vm.queryParameters.sortColumn = '';
                this.vm.queryParameters.sort = '';
            }
            else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            this.selectItems = [];
            this.search();
        },
        handleSortPrice(data) {
            if (data.order === 'normal') {
                this.modal.sortColumn = '';
                this.vm.queryParameters.sort = '';
            }
            else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            this.selectItems = [];
            this.search();
        },
        async searchMaterialPrice(selectItem) {
            let idList = _.map(this.selectItems, this.modal.identity).join(",");
            this.modal.title = this.$t('field.SUPPLIER.SEARCH_MATERIAL_PRICE');
            this.modal.visible = true;
            let result = await supplierService.searchPriceReverse(selectItem);
            if (result.status === 200) {
                var items = result.data.list;
                this.modal.total = result.data.total;
                this.modal.items = items;
                this.$nextTick(() => {
                    for (var i = 0; i < items.length; ++i) {
                        if ((','+idList+',').indexOf(','+items[i][this.modal.identity]+',') > -1) {
                            this.$refs.table.toggleSelect(i);
                        }
                    }
                });
            }
        },
        back() {
            this.modal = {
                title: 'title',
                items: [],
                visible: false,
                sortColumn: '',
                sort: '',
                total: 0,
                limit: 10,
                current: 1
            }
        },
    },
    mounted() {
        this.initData();
    }
}
</script>
