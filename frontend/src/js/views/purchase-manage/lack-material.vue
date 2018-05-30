<template>
	<div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list">
                    <!-- 搜索框 -->
                    <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.SUPPLIER.MATERIAL_NO')" v-model="vm.queryParameters.searchKey" @on-enter="search()" style="width:300px"></i-input>
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
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.BACK')" @on-ok="back">
        	<i-table :height="tableHeight" ref="table2" :columns="columnList2" :data="modal.items" @on-sort-change="handleSortPrice"></i-table>
            <div class="page-panel">
                <page :total="modal.total" :current="modal.current" :page-size="modal.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="modal.current=arguments[0];searchMaterialPrice()" @on-page-size-change="modal.limit=arguments[0];searchMaterialPrice()"></page>
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
                    limit: 5,
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
                sortColumn: '',
                sort: '',
                total: 0,
                limit: 10,
                current: 1,
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
                { type: 'index', width: 80, align: 'center' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NO'), key: 'materialNo', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NAME'), key: 'materialName', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.UNIT'), key: 'unit', },
                { title: this.$t('field.SUPPLIER.INVENTORY'), key: 'totalstock' },
                { title: this.$t('field.SUPPLIER.DRAW_MATERIAL_QUANTITY'), key: 'usedraw' },
                { title: this.$t('field.SUPPLIER.PRODUCT_OUTSTOCK_QUANTITY'), key: 'useoutstock' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('field.SUPPLIER.SEARCH_MATERIAL_PRICE'), (row) => {
                            this.selectMatertal = row;
                            this.searchMaterialPrice(); 
                        }, 'detailPermission')]);
                    } 
                }
            ];
        },
        columnList2() {
            return [
                { type: 'index', width: 80, align: 'center' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NO'), key: 'materialNo' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NAME'), key: 'materialName' },
                { title: this.$t('field.SUPPLIER.SUPPLIER_NAME'), key: 'supplierName', sortable: 'custom'},
                { title: this.$t('field.SUPPLIER.PRICE'), key: 'price', sortable: 'custom' }
            ];
        }
    },
    methods: {
        initData() {
            this.search();
        },
        async search() {
            let result = await supplierService.searchLackMaterial(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    if (this.lackMaterialPermission)
                        item['detailPermission'] = true;
                });
                this.vm.items = items;
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
            this.search();
        },
        handleSortPrice(data) {
            if (data.order === 'normal') {
                this.modal.sortColumn = '';
                this.modal.sort = '';
            }
            else {
                this.modal.sortColumn = data.key;
                this.modal.sort = data.order;
            }
            this.searchMaterialPrice();
        },
        async searchMaterialPrice() {
            this.modal.title = this.$t('field.SUPPLIER.SEARCH_MATERIAL_PRICE');
            this.modal.visible = true;
            this.modal.materialId = this.selectMatertal.materialId;
            let result = await supplierService.searchPriceReverse(this.modal);
            if (result.status === 200) {
                var items = result.data.list;
                this.modal.total = result.data.total;
                this.modal.items = items;
            }
        },
        back() {
            this.selectMatertal = [];
            this.modal = {
                title: 'title',
                items: [],
                visible: false
            }
        },
    },
    mounted() {
        this.initData();
    }
}
</script>
