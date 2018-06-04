<template>
	<div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                    <dropdown trigger="click" placement="bottom-start" @on-click="vm.queryParameters.productOutstockState=arguments[0];selectItems=[];search()" style="margin:0 10px">
            			<a href="javascript:void(0)"><span class="main-document-name">{{ productOutstockStateCn }}</span><icon type="arrow-down-b"></icon></a>
            			<dropdown-menu slot="list">
            				<dropdown-item v-for="item in productOutstockStateList" :key="item.value" :name="item.value">{{ item.descript }}</dropdown-item>
            			</dropdown-menu>
            		</dropdown>
                    <!-- 搜索框 -->
                    <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.PROOUTSTOCK.PROOUTSTOCK_ID')" v-model="vm.queryParameters.searchKey" @on-enter="selectItems=[];search()" style="width:300px"></i-input>
                </div>
                <div class="pull-left operate-list" v-show="selectItems!=''">
                    <a class="cancel-btn" @click="clearChecked"><icon type="close"></icon></a>
                    <span class="split-bar">{{ $t('component.SELECTED') }} <span class="text-primary">{{ selectItems.length }}</span> {{ $t('component.ITEMS') }}</span>
                    <!-- 审核 -->
                    <span class="label-btn" @click="audit(selectItems)" ><icon type="checkmark"></icon>{{ $t('common.AUDIT') }}</span>
                    <!-- 反审核 -->
                    <span class="label-btn" @click="unaudit(selectItems)" ><icon type="reply"></icon>{{ $t('common.UNAUDIT') }}</span>
                    <!-- 删除 -->
                    <span class="label-btn" @click="remove(selectItems)" v-if="productOutstockRemovePermission"><icon type="trash-a"></icon>{{ $t('common.REMOVE') }}</span>
                </div>
                <!-- 顶部右侧按钮 -->
                <div class="button-list pull-right">
                    <i-button class="operate-btn" type="primary" shape="circle" @click="$router.push('/product-outstock/add')" v-if="productOutstockAddPermission">{{ $t('common.ADD') }}</i-button>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="main-content">
                <i-table :height="tableHeight" ref="table" :columns="columnList" :data="vm.items" @on-selection-change="selectItems=arguments[0]" @remove="remove([arguments[0]])"></i-table>
            </div>
            <!-- 翻页控制器 -->
            <div class="page-panel">
                	<page :total="vm.queryParameters.total" :current="vm.queryParameters.current" :page-size="vm.queryParameters.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="vm.queryParameters.current=arguments[0];selectItems=[];search()" @on-page-size-change="vm.queryParameters.limit=arguments[0];selectItems=[];search()"></page>
            </div>
        </div>
	</div>
</template>

<script>

import Permission from '../../mixins/permission';
import util from '../../libs/util.js';
import productoutstockService from '../../service/product-outstock';

export default {
    mixins: [ Permission ],
    data() {
        return {
            vm: {
                queryParameters: {
                    productOutstockState: -1,
                    searchKey: '',
                    total: 0,
                    limit: 10,
                    current: 1,
                    sortColumn: '',
                    sort: ''
                },
                items: [],                
                identity: 'billId',
            },
            selectItems: [],
        }
    },
    computed: {
        tableHeight() {
            return document.documentElement.clientHeight - 220;
        },
        columnList() {
            return [
                { type: 'selection', width: 80, align: 'center' },
                { title: this.$t('field.PROOUTSTOCK.PROOUTSTOCK_ID'), key: 'billNo', sortable: 'custom' },
                { title: this.$t('field.PROOUTSTOCK.PROOUTSTOCK_TIME'), key: 'productOutstockTime', sortable: 'custom' },
                { title: this.$t('field.PROOUTSTOCK.PROOUTSTOCK_PERSON'), key: 'toPrincipalName' },
                { title: this.$t('field.PROOUTSTOCK.STOCK_PERSON'), key: 'warehousePrincipalName', sortable: 'custom' },
                { title: this.$t('field.PROOUTSTOCK.PRO_SOURCE'), key: 'productWhereaboutsName', sortable: 'custom' },
                { title: this.$t('field.PROOUTSTOCK.STATUS'), key: 'productOutstockStateCn', sortable: 'custom' },

                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            this.$router.push('/product-outstock/'+row[this.vm.identity]) 
                        }, 'detailPermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => { 
                            this.remove([row]) 
                        }, 'removePermission')]);
                    } 
                }
            ];
        },
        productOutstockStateCn() {
            for (let i = 0; i < this.productOutstockStateList.length; ++i) {
                if (this.productOutstockStateList[i].value === this.vm.queryParameters.productOutstockState)
                    return this.productOutstockStateList[i].descript;
            }
        },
        productOutstockStateList() {
            return [
                { value: -1, descript: this.$t('field.PRODUCT_STOCK_STATE.-1') },
                { value: 1, descript: this.$t('field.PRODUCT_STOCK_STATE.1') },
                { value: 2, descript: this.$t('field.PRODUCT_STOCK_STATE.2') },
                { value: 3, descript: this.$t('field.PRODUCT_STOCK_STATE.3') },
            ]
        }
    },
    methods: {
        initData() {
            this.search();
        },
        async search() {
            let idList = _.map(this.selectItems, this.vm.identity).join(",");
            let result = await productoutstockService.search(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item.productOutstockTime = new Date().toLocaleDateString(item.createAt);
                    item.productOutstockStateCn = this.$t('field.PRODUCT_STOCK_STATE.' + Number(item.billState));
                    item.productWhereaboutsName = this.$t('field.PRODUCT_WARE_NAME.' + Number(item.productWhereabouts));
                    if (!item.sysDefault) {
                        item['detailPermission'] = true;
                        if (this.productOutstockRemovePermission)
                            item['removePermission'] = true;
                    }
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
            } else if (data.key === 'productOutstockStateCn') {
                this.vm.queryParameters.sortColumn = 'productOutstockState';
                this.vm.queryParameters.sort = data.order;
            } else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            this.selectItems = [];
            this.search();
        },
        audit(selectItems) {
            let idList = _.map(selectItems, this.vm.identity).join(",")
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await productoutstockService.audit(idList)
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'))
                        this.search()
                    } else {
                        this.$Message.error(result.data)
                    }
                }
            });
        },
        unaudit(selectItems) {
            let idList = _.map(selectItems, this.vm.identity).join(",")
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await productoutstockService.unaudit(idList)
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'))
                        this.search()
                    } else {
                        this.$Message.error(result.data)
                    }
                }
            });
        },
        remove(selectItems) {
            let idList = _.map(selectItems, this.vm.identity).join(",");
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: async () => {
                    let result = await productoutstockService.remove(idList);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.REMOVE_SUCCESS'));
                        this.selectItems = [];
                        this.search();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        clearChecked() {
            this.$refs.table.selectAll(false);
        },
    },
    mounted() {
        this.initData();
    }
}
</script>
