<template>
    <div :class="[prefixCls]">
        <div id="total-amount-chart" v-if="showTotalAmount"></div>
        <div id="total-cost-chart" v-if="showTotalCost"></div>
        <div class="clearfix"></div>
    </div>
</template>

<script>
import echarts from 'echarts';
import util from '../libs/util';

import stockService from '../service/stock';

export default {
    name: 'materialStockChart',
    data() {
        return {
            prefixCls: 'di-material-stock-chart',
            data: {
                totalAmount: [],
                totalCost: []
            },
            chart: {
                totalAmount: { object: null, option: {} },
                totalCost: { object: null, option: {} }
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
        showTotalCost: {
            type: Boolean,
            default: false
        }
    },
    computed: {
        title() {
            return {
                totalAmount: this.$t('field.MATERIAL_COST.TOTAL_AMOUNT'),
                totalCost: this.$t('field.MATERIAL_COST.TOTAL_COST')
            };
        },
        legend() {
            return {
                totalAmount: '数量',
                totalCost: '金额'
            };
        }
    },
    methods: {
        initData() {
            if (this.showTotalAmount) this.getStockOrderByField('totalAmount');
            if (this.showTotalCost) this.getStockOrderByField('totalCost');
        },
        async getStockOrderByField(field) {
            let result = await stockService.searchMaterialCost(Object.assign({ current: 1, limit: this.itemLimit, sortColumn: field, sort: 'desc' }, this.queryParameters));
            if (result.status === 200) {
                this.data[field] = result.data.list;
                this.renderChart(field, 'materialName');
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
            this.chart[field].option.series[0].name = this.legend[field];
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