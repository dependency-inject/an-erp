<template>
    <div class="main-panel">
        <div class="main-panel-content" style="padding-bottom:65px">
            <div class="operate-panel">
                <div class="pull-left operate-list">
                    <!-- 搜索框 -->
                    <i-input :placeholder="$t('component.PLEASE_INPUT')+$t('field.MATERIAL_COST.MATERIAL_NO')+'/'+$t('field.MATERIAL_COST.MATERIAL_NAME')" v-model="vm.queryParameters.searchKey" @on-enter="search();chartQueryParameters.searchKey=vm.queryParameters.searchKey" style="width:280px"></i-input>
                    <i-button type="ghost" shape="circle" icon="ios-search" @click="search();chartQueryParameters.searchKey=vm.queryParameters.searchKey" style="margin-left:8px"></i-button>
                </div>
                <div class="button-list pull-right">
                    <!-- <i-button class="operate-btn" type="ghost" shape="circle" @click="">{{ $t('common.EXPORT') }}</i-button> -->
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="statistics-panel">
                <div><div style="background-color:#fb7884"><h4>{{ vm.queryParameters.total || 0 }}</h4>{{ $t('field.MATERIAL_COST.MATERIAL_COUNT') }}</div></div>
                <div><div style="background-color:#03a9f3"><h4>{{ vm.statistics.totalAmount || 0 }}</h4>{{ $t('field.MATERIAL_COST.TOTAL_AMOUNT') }}</div></div>
                <div><div style="background-color:#9675ce"><h4>{{ vm.statistics.totalCost || 0 }}</h4>{{ $t('field.MATERIAL_COST.TOTAL_COST') }}</div></div>
            </div>
            <!-- 表格 -->
            <div class="main-content">
                <i-table border ref="table" :columns="columnList" :data="vm.items" @on-sort-change="handleSort"></i-table>
            </div>
            <!-- 翻页控制器 -->
            <div class="page-panel">
                <page :total="vm.queryParameters.total" :current="vm.queryParameters.current" :page-size="vm.queryParameters.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="vm.queryParameters.current=arguments[0];search()" @on-page-size-change="vm.queryParameters.limit=arguments[0];search()"></page>
            </div>
        </div>
        <material-stock-cost-chart :query-parameters="chartQueryParameters" show-total-amount show-total-cost></material-stock-cost-chart>
    </div>
</template>

<script>
import util from '../../libs/util.js';

import materialStockCostChart from '../../components/material-stock-cost-chart';

import stockService from '../../service/stock';

export default {
    name: 'stock-cost',
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
                statistics: {},
                identity: 'materialId',
            },
            chartQueryParameters: {
                searchKey: ''
            }
        }
    },
    components: { materialStockCostChart },
    computed: {
        columnList() {
            return [
                { title: this.$t('field.MATERIAL_COST.MATERIAL_NO'), key: 'materialNo', sortable: 'custom' },
                { title: this.$t('field.MATERIAL_COST.MATERIAL_NAME'), key: 'materialName', sortable: 'custom' },
                { title: this.$t('field.MATERIAL_COST.MATERIAL_COST'), key: 'cost', sortable: 'custom' },
                { title: this.$t('field.MATERIAL_COST.TOTAL_AMOUNT'), key: 'totalAmount', sortable: 'custom' },
                { title: this.$t('field.MATERIAL_COST.TOTAL_COST'), key: 'totalCost', sortable: 'custom' },
                // TODO: 流水明细
            ];
        }
    },
    methods: {
        initData() {
            this.search();
        },
        async search() {
            let result = await stockService.searchMaterialCost(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                this.vm.items = items;
                this.vm.statistics = result.data.statistics;
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
        }
    },
    mounted() {
        this.initData();
    }
}
</script>

