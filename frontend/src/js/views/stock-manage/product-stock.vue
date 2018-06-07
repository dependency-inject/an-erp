<template>
    <div class="main-panel">
        <div class="main-panel-content" style="padding-bottom:65px">
            <div class="operate-panel">
                <div class="pull-left operate-list">
                    <i-input icon="search"
                             :placeholder="$t('component.PLEASE_INPUT')+$t('field.PRODUCT_STOCK.PRODUCT_NO')+
                                           '/'+$t('field.PRODUCT_STOCK.PRODUCT_NAME')"
                             v-model="vm.queryParameters.searchKey"
                             @on-enter="search();chartQueryParameters.searchKey=vm.queryParameters.searchKey"
                             style="width:300px"></i-input>
                </div>
                <div class="button-list pull-right">
                    <!-- <i-button class="operate-btn" type="ghost" shape="circle" @click="">{{ $t('common.EXPORT') }}</i-button> -->
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="statistics-panel">
                <div><div style="background-color:#fb7884"><h4>{{ vm.queryParameters.total || 0 }}</h4>{{ $t('field.PRODUCT_STOCK.PRODUCT_COUNT') }}</div></div>
                <div><div style="background-color:#03a9f3"><h4>{{ vm.statistics.totalAmount || 0 }}</h4>{{ $t('field.PRODUCT_STOCK.PRODUCT_AMOUNT') }}</div></div>
                <div><div style="background-color:#9675ce"><h4>{{ vm.statistics.leftAmount || 0 }}</h4>{{ $t('field.PRODUCT_STOCK.PRODUCT_LEFT') }}</div></div>
            </div>
            <div class="main-content">
                <i-table ref="table"
                         :columns="columnList"
                         :data="vm.items"
                         @on-sort-change="handleSort"></i-table>
            </div>
            <div class="page-panel">
                <page :total="vm.queryParameters.total"
                      :current="vm.queryParameters.current"
                      :page-size="vm.queryParameters.limit"
                      page-size-place="top" show-elevator show-sizer show-total
                      @on-change="vm.queryParameters.current=arguments[0];search()"
                      @on-page-size-change="vm.queryParameters.limit=arguments[0];search()"></page>
            </div>
        </div>
        <product-stock-chart :query-parameters="chartQueryParameters" show-total-amount show-ordered-amount show-left-amount></product-stock-chart>
    </div>
</template>

<script>
import util from '../../libs/util.js';

import productStockChart from '../../components/product-stock-chart';

import stockService from '../../service/stock';

export default {
    name: 'product-stock',
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
                identity: 'productId',
            },
            modal: {
                title: 'title',
                item: {},
                visible: false
            },
            chartQueryParameters: {
                searchKey: ''
            }
        }
    },
    components: { productStockChart },
    computed: {
        columnList() {
            return [
                {width: 50, align: 'center'},
                {title: this.$t('field.PRODUCT_STOCK.PRODUCT_NO'), key: 'productNo', sortable: 'custom'},
                {title: this.$t('field.PRODUCT_STOCK.PRODUCT_NAME'), key: 'productName', sortable: 'custom'},
                {title: this.$t('field.PRODUCT_STOCK.PRODUCT_AMOUNT'), key: 'totalAmount', sortable: 'custom'},
                {title: this.$t('field.PRODUCT_STOCK.PRODUCT_ORDERED'), key: 'orderedAmount', sortable: 'custom'},
                {title: this.$t('field.PRODUCT_STOCK.PRODUCT_LEFT'), key: 'leftAmount', sortable: 'custom'},
                // TODO: 流水明细
            ];
        }
    },
    methods: {
        initData() {
            this.search();
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
        async search() {
            let result = await stockService.productSearch(this.vm.queryParameters);
            if (result.status === 200) {
                let items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                this.vm.items = items;
                this.vm.statistics = result.data.statistics;
            }
        }
    },
    mounted() {
        this.initData();
    }
}
</script>
