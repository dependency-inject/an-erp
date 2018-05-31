<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                     <!-- 账号状态筛选框 -->
                    <dropdown trigger="click" placement="bottom-start" style="margin:0 10px" @on-click="queryParameters.state=arguments[0];selectItems=[];search()">
                        <a href="javascript:void(0)"><span class="main-user-name">{{ allState }}</span><icon type="arrow-down-b"></icon></a>
                        <dropdown-menu slot="list">
                            <dropdown-item v-for="item in stateList" :key="item.value" :name="item.value">{{ item.descript }}</dropdown-item>
                        </dropdown-menu>
                    </dropdown>
                    <!-- 搜索框 -->
                    <i-input icon="search" v-model="queryParameters.searchKey" :placeholder="$t('component.PLEASE_INPUT')+$t('field.ORDER.ID')+'/'+$t('field.ORDER.SALES_MAN')+'/'+$t('field.ORDER.CLIENT')"  style="width:300px" @on-click="selectItems=[];search()"></i-input>
                </div>
                <!--新增订单按钮-->
                <div class="button-list pull-right">
                    <i-button class="operate-btn" type="primary" shape="circle" @click="$router.push('/order/add')" v-if="orderAddPermission">{{ $t('common.ADD') }}</i-button>
                </div>
                <div class="clearfix"></div>
            </div>
            <!--显示订单表格-->
            <div class="main-content">
                <i-table :height="tableHeight" ref="table" :columns="columnList" :data="items" @on-sort-change="handleSort" @on-selection-change="selectItems=arguments[0]"></i-table>
            </div>
            <!-- 翻页控制器 -->
            <div class="page-panel">
                <page :total="queryParameters.total" :current="queryParameters.current" :page-size="queryParameters.limit" @on-change="queryParameters.current=arguments[0];search();" page-size-place="top" show-elevator show-sizer show-total @on-page-size-change="queryParameters.limit=arguments[0];search();"></page>
            </div>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import orderService from '../../service/order';
export default {
    mixins: [ Permission ],
    data() {
        return {
            selectItems: [],
            items: [],
            queryParameters: {
                current: 1,
                limit: 10,
                total: 10,
                sortColumn: '',
                sort: '',
                searchKey: '',
                state: -1,
            }
        }
    },
    computed: {
        tableHeight() {
            return document.documentElement.clientHeight - 220
        },
        columnList() {
            return [
                { type: 'selection', width: 80, align: 'center' },
                { title: this.$t('field.ORDER.ID'), key: 'billNo', sortable: 'custom' },
                { title: this.$t('field.ORDER.SALES_MAN'), key: 'salesName'},
                { title: this.$t('field.ORDER.CLIENT'), key: 'contact'},
                { title: this.$t('field.ORDER.AMOUNT'), key: 'billAmount', sortable: 'custom'},
                { title: this.$t('field.ORDER.TIME'), key: 'billTime', sortable: 'custom'},
                { title: this.$t('field.ORDER.STATE'), key: 'billState'},
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            this.$router.push('/order/'+row['billId']) 
                        }, 'detailPermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => { 
                            this.remove(row)
                        }, 'removePermission')]);
                    } 
                }
            ];
        },
        allState() {
            for(let i = 0; i < this.stateList.length; ++i){
                if(this.stateList[i].value == this.queryParameters.state){
                    return this.stateList[i].descript
                }
            }
        },
        stateList() {
            return [
                { value: -1, descript: this.$t('field.ORDERSTATE.-1') },
                { value: 1, descript: this.$t('field.ORDERSTATE.1') },
                { value: 2, descript: this.$t('field.ORDERSTATE.2') },
                { value: 3, descript: this.$t('field.ORDERSTATE.3') },
                { value: 4, descript: this.$t('field.ORDERSTATE.4') },
                { value: 5, descript: this.$t('field.ORDERSTATE.5') },
            ]
        }
    },
    methods: {
        initData() {
            this.search()
        },
        handleSort(data) {
            if (data.order === 'normal') {
                this.queryParameters.sortColumn = ''
                this.queryParameters.sort = ''
            } else {
                this.queryParameters.sortColumn = data.key
                this.queryParameters.sort = data.order
            }
            this.search()
        },
        toast(content) {
            this.$Message.info({
                content: content,
                duration: 2,
            })
        },
        async remove(row) {
            let result = await orderService.remove(row)
            if (result.status === 200) {
                this.toast(this.$t('common.OPERATE_SUCCESS'))
                this.search()
            }
        },
        async search() {
            let result = await orderService.search(this.queryParameters)
            if (result.status === 200) {
                this.items = result.data.list
                this.queryParameters.total = result.data.total
                this.items.forEach((item) => {
                    item['detailPermission'] = true
                    if (item['billState'] == 1) {
                        if(this.orderRemovePermission) {
                            item['removePermission'] = true
                        }
                    }
                    item['billTime'] = util.formatTimestamp(item['billTime'], 'yyyy-MM-dd')
                    item['billState'] = this.stateList[item['billState']].descript
            })
            }
        }
    },
    mounted() {
        this.initData()
    }
}
</script>
