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
                <page :total="vm.queryParameters.total" :current="vm.queryParameters.current" :page-size="vm.queryParameters.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="vm.queryParameters.current=arguments[0];search()" @on-page-size-change="vm.queryParameters.limit=arguments[0];search()"></page>
            </div>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :footer-hide="true" :width="700">
        	<i-table :height="450" ref="table2" :columns="columnList2" :data="modal.items" @on-sort-change="handleSortPrice"></i-table>
            <div style="padding-top:15px;text-align:center;">
                <page :total="modal.queryParameters.total" :current="modal.queryParameters.current" :page-size="modal.queryParameters.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="modal.queryParameters.current=arguments[0];searchMaterialPrice()" @on-page-size-change="modal.queryParameters.limit=arguments[0];searchMaterialPrice()"></page>
            </div>
    	</modal>
	</div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import stockService from '../../service/stock';

export default {
    name: 'lack-material',
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
            },
            modal: {
                title: 'title',
                items: [],
                visible: false,
                queryParameters: {
                    materialId: 0,
                    total: 0,
                    limit: 10,
                    current: 1,
                    sortColumn: '',
                    sort: ''
                }
            },
            selectMatertal: []
        }
    },
    computed: {
        tableHeight() {
            return document.documentElement.clientHeight - 220;
        },
        // 控制表格显示哪些列
        columnList() {
            return [
                { width: 50, align: 'center' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NO'), key: 'materialNo', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NAME'), key: 'materialName', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.UNIT'), key: 'unit', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.INVENTORY'), key: 'totalStock', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.PRODUCT_OUTSTOCK_QUANTITY'), key: 'useOutstock', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.DRAW_MATERIAL_QUANTITY'), key: 'useDraw', sortable: 'custom' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('field.SUPPLIER.SEARCH_MATERIAL_PRICE'), (row) => {
                            this.showMaterialPrice(row); 
                        }, 'detailPermission')]);
                    } 
                }
            ];
        },
        columnList2() {
            return [
                { title: this.$t('field.SUPPLIER.MATERIAL_NO'), key: 'materialNo' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NAME'), key: 'materialName' },
                { title: this.$t('field.SUPPLIER.SUPPLIER_NAME'), key: 'supplierName' },
                { title: this.$t('field.SUPPLIER.PRICE'), key: 'price', sortable: 'custom' }
            ];
        }
    },
    methods: {
        initData() {
            this.search();
        },
        async search() {
            let result = await stockService.searchLackMaterial(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item['detailPermission'] = true;
                });
                this.vm.items = items;
            }
        },
        handleSort(data) {
            if (data.order === 'normal') {
                this.vm.queryParameters.sortColumn = '';
                this.vm.queryParameters.sort = '';
            } else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            this.search();
        },
        showMaterialPrice(item) {
            this.modal.title = this.$t('field.SUPPLIER.SEARCH_MATERIAL_PRICE');
            this.modal.queryParameters.materialId = item.materialId;
            this.modal.visible = true;
            this.modal.items = [];
            this.searchMaterialPrice();
        },
        async searchMaterialPrice() {
            let result = await stockService.searchPriceReverse(this.modal.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.modal.queryParameters.total = result.data.total;
                this.modal.items = items;
            }
        },
        handleSortPrice(data) {
            if (data.order === 'normal') {
                this.modal.queryParameters.sortColumn = '';
                this.modal.queryParameters.sort = '';
            } else {
                this.modal.queryParameters.sortColumn = data.key;
                this.modal.queryParameters.sort = data.order;
            }
            this.searchMaterialPrice();
        }
    },
    mounted() {
        this.initData();
    }
}
</script>
