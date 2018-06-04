<template>
    <div :class="[prefixCls]">
        <div id="total-amount-chart" v-if="showTotalAmount"></div>
        <div id="ordered-amount-chart" v-if="showOrderedAmount"></div>
        <div id="left-amount-chart" v-if="showLeftAmount"></div>
        <div class="clearfix"></div>
    </div>
</template>

<script>
import echarts from 'echarts';
import util from '../libs/util';

import stockService from '../service/stock';

export default {
    name: 'productStockChart',
    data() {
        return {
            prefixCls: 'di-material-stock-chart',
            data: {
                totalAmount: [],
                orderedAmount: [],
                leftAmount: []
            },
            chart: {
                totalAmount: { object: null, option: {} },
                orderedAmount: { object: null, option: {} },
                leftAmount: { object: null, option: {} }
            },
            initCount: 10,
            pieOption: {
                title : {
                    text: 'title',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    type: 'scroll',
                    orient: 'vertical',
                    right: 0,
                    top: 10,
                    bottom: 10
                },
                series : [
                    {
                        name: '数量',
                        type: 'pie',
                        radius : '55%'
                    }
                ]
            }
        }
    },
    props: {
        itemLimit: {
            type: Number,
            default: 50
        },
        queryParameters: {
            type: Object,
            default: {}
        },
        showTotalAmount: {
            type: Boolean,
            default: false
        },
        showOrderedAmount: {
            type: Boolean,
            default: false
        },
        showLeftAmount: {
            type: Boolean,
            default: false
        }
    },
    computed: {
        title() {
            return {
                totalAmount: this.$t('field.PRODUCT_STOCK.PRODUCT_AMOUNT'),
                orderedAmount: this.$t('field.PRODUCT_STOCK.PRODUCT_ORDERED'),
                leftAmount: this.$t('field.PRODUCT_STOCK.PRODUCT_LEFT')
            };
        }
    },
    methods: {
        initData() {
            if (this.showTotalAmount) this.getStockOrderByField('totalAmount');
            if (this.showOrderedAmount) this.getStockOrderByField('orderedAmount');
            if (this.showLeftAmount) this.getStockOrderByField('leftAmount');
        },
        async getStockOrderByField(field) {
            let result = await stockService.productSearch(Object.assign({ current: 1, limit: this.itemLimit, sortColumn: field, sort: 'desc' }, this.queryParameters));
            if (result.status === 200) {
                this.data[field] = result.data.list;
                this.renderChart(field, 'productName');
            }
        },
        renderChart(field, nameField) {
            if (this.chart[field].object != null)
                this.chart[field].object.dispose();
            this.chart[field].object = echarts.init(document.getElementById(field.replace(/([A-Z])/g,"-$1").toLowerCase() + '-chart'));
            this.chart[field].option = util.deepCopy(this.pieOption);
            this.chart[field].option.title.text = this.title[field];
            this.chart[field].option.legend.data = _.map(this.data[field], nameField);
            this.chart[field].option.legend.selected = _.fromPairs(_.map(this.data[field], (item, index) => {
                return [ item[nameField], index < this.initCount && item[field] > 0 ];
            }));
            this.chart[field].option.series[0].data = _.map(this.data[field], (item) => {
                return { name: item[nameField], value: item[field] };
            });
            this.chart[field].object.setOption(this.chart[field].option, true);
        }
    },
    mounted() {
        this.initData();
    },
    watch: {
        queryParameters: {
            handler () {
                this.initData();
            },
            deep: true
        }
    }
}
</script>