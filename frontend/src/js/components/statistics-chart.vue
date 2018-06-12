<template>
    <div :class="[prefixCls]">
        <div :id="uid+'-pre-week-chart'" style="width:60%"></div>
        <div :id="uid+'-history-chart'" style="width:40%"></div>
        <div class="clearfix"></div>
    </div>
</template>

<script>
import echarts from 'echarts';
import util from '../libs/util';

import orderService from '../service/order';
import productionDrawService from '../service/production-draw';
import productionReturnService from '../service/production-return';
import productInstockService from '../service/product-instock';
import productOutstockService from '../service/product-outstock';
import materialInstockService from '../service/material-instock';
import materialOutstockService from '../service/material-outstock';
import developmentDrawService from '../service/development-draw';
import developmentReturnService from '../service/development-return';

export default {
    name: 'statisticsChart',
    data() {
        return {
            prefixCls: 'di-material-stock-chart',
            uid: Math.random().toString(36).substr(2),
            data: [],
            model: '',
            data: {},
            chart: {
                preWeek: { object: null, option: {} },
                history: { object: null, option: {} }
            },
            lineOption: {
                title: {
                    text: 'title'
                },
                color: [ 'rgb(54, 172, 236)' ],
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['记录数']
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        name: '记录数',
                        type: 'line',
                        data: [],
                        markPoint: {
                            data: [
                                {type: 'max', name: '最大值'},
                                {type: 'min', name: '最小值'}
                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: '平均值'}
                            ]
                        },
                        smooth: false
                    }
                ]
            },
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
        type: {
            validator (value) {
                return util.oneOf(value, ['order', 'production-draw', 'production-return', 'product-instock', 'product-outstock', 'material-instock', 'material-outstock', 'development-draw', 'development-return']);
            }
        }
    },
    computed: {
        title() {
            return {
                preWeek: '近一周提交单据数量',
                history: '各类状态单据数量'
            };
        }
    },
    methods: {
        initOption() {
            if (this.type === 'order') {
                this.option = { service: orderService, label: 'field.ORDER_STATE.', ignore: [ 4, 5 ] }
            } else if (this.type === 'production-draw') {
                this.option = { service: productionDrawService, label: 'field.BILL_STATE.STATE', ignore: [ 3 ] }
            } else if (this.type === 'production-return') {
                this.option = { service: productionReturnService, label: 'field.BILL_STATE.STATE', ignore: [ 3 ] }
            } else if (this.type === 'product-instock') {
                this.option = { service: productInstockService, label: 'field.PRODUCT_INSTOCK_STATE.', ignore: [ 3 ] }
            } else if (this.type === 'product-outstock') {
                this.option = { service: productOutstockService, label: 'field.PRODUCT_OUTSTOCK_STATE.', ignore: [ 3 ] }
            } else if (this.type === 'material-instock') {
                this.option = { service: materialInstockService, label: 'field.MATERIAL_INSTOCK_STATE.', ignore: [ 3 ] }
            } else if (this.type === 'material-outstock') {
                this.option = { service: materialOutstockService, label: 'field.MATERIAL_OUTSTOCK_STATE.', ignore: [ 3 ] }
            } else if (this.type === 'development-draw') {
                this.option = { service: developmentDrawService, label: 'field.BILL_STATE.STATE', ignore: [ 3 ] }
            } else if (this.type === 'development-return') {
                this.option = { service: developmentReturnService, label: 'field.BILL_STATE.STATE', ignore: [ 3 ] }
            }
        },
        async getStatistics() {
            let result = await this.option.service.getStatistics();
            if (result.status === 200) {
                this.data = result.data;
                this.data.history.forEach((item) => {
                    item.label = Number(item.label);
                    item.labelCn = this.$t(this.option.label + Number(item.label));
                });
                this.renderLineChart('preWeek', 'label');
                this.renderPieChart('history', 'labelCn');
            }
        },
        renderLineChart(field, nameField) {
            if (this.chart[field].object != null)
                this.chart[field].object.dispose();
            this.chart[field].object = echarts.init(document.getElementById(this.uid + '-' + field.replace(/([A-Z])/g,"-$1").toLowerCase() + '-chart'));
            this.chart[field].option = util.deepCopy(this.lineOption);
            this.chart[field].option.title.text = this.title[field];
            this.chart[field].option.xAxis.data = _.map(this.data[field], nameField);
            this.chart[field].option.series[0].data = _.map(this.data[field], (item) => {
                return { name: item[nameField], value: item['value'] };
            });
            this.chart[field].object.setOption(this.chart[field].option, true);
        },
        renderPieChart(field, nameField) {
            if (this.chart[field].object != null)
                this.chart[field].object.dispose();
            this.chart[field].object = echarts.init(document.getElementById(this.uid + '-' + field.replace(/([A-Z])/g,"-$1").toLowerCase() + '-chart'));
            this.chart[field].option = util.deepCopy(this.pieOption);
            this.chart[field].option.title.text = this.title[field];
            this.chart[field].option.legend.data = _.map(this.data[field], nameField);
            this.chart[field].option.legend.selected = _.fromPairs(_.map(this.data[field], (item) => {
                return [ item[nameField], !util.oneOf(item.label, this.option.ignore) ];
            }));
            this.chart[field].option.series[0].data = _.map(this.data[field], (item) => {
                return { name: item[nameField], value: item['value'] };
            });
            this.chart[field].object.setOption(this.chart[field].option, true);
        }
    },
    created() {
        this.initOption();
    },
    mounted() {
        this.getStatistics();
    }
}
</script>