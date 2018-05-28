<template>
	<div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                    <dropdown trigger="click" placement="bottom-start" @on-click="vm.queryParameters.productInstockState=arguments[0];selectItems=[];search()" style="margin:0 10px">
            			<a href="javascript:void(0)"><span class="main-document-name">{{ productInstockStateCn }}</span><icon type="arrow-down-b"></icon></a>
            			<dropdown-menu slot="list">
            				<dropdown-item v-for="item in productInstockStateList" :key="item.value" :name="item.value">{{ item.descript }}</dropdown-item>
            			</dropdown-menu>
            		</dropdown>
                    <!-- 搜索框 -->
                    <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.PROINSTOCK.PROINSTOCK_ID')" v-model="vm.queryParameters.searchKey" @on-enter="selectItems=[];search()" style="width:300px"></i-input>
                </div>
                <div class="pull-left operate-list" v-show="selectItems!=''">
                    <a class="cancel-btn" @click="clearChecked"><icon type="close"></icon></a>
                    <span class="split-bar">{{ $t('component.SELECTED') }} <span class="text-primary">{{ selectItems.length }}</span> {{ $t('component.ITEMS') }}</span>
                    <!-- 待审核 -->
                    <span class="label-btn" @click="" v-if="productInstockUpdatePermission"><icon type="android-open"></icon>{{ $t('field.PRODUCT_STOCK_STATE.2') }}</span>
                    <!-- 已审核 -->
                    <span class="label-btn" @click="" v-if="productInstockUpdatePermission"><icon type="android-exit"></icon>{{ $t('field.PRODUCT_STOCK_STATE.3') }}</span>
                    <!-- 删除 -->
                    <span class="label-btn" @click="remove(selectItems)" v-if="productInstockRemovePermission"><icon type="trash-a"></icon>{{ $t('common.REMOVE') }}</span>
                </div>
                <!-- 顶部右侧按钮 -->
                <div class="button-list pull-right">
                    <i-button class="operate-btn" type="primary" shape="circle" @click="$router.push('/product-instock/add')" v-if="productInstockAddPermission">{{ $t('common.ADD') }}</i-button>
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
import productinstockService from '../../service/product-instock';

export default {
    mixins: [ Permission ],
    data() {
        return {
            vm: {
                queryParameters: {
                    productInstockState: -1,
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
                { title: this.$t('field.PROINSTOCK.PROINSTOCK_ID'), key: 'billNo', sortable: 'custom' },
                { title: this.$t('field.PROINSTOCK.PROINSTOCK_TIME'), key: 'productInstockTime', sortable: 'custom' },
                { title: this.$t('field.PROINSTOCK.PROINSTOCK_PERSON'), key: 'fromPrincipal' },
                { title: this.$t('field.PROINSTOCK.STOCK_PERSON'), key: 'warehousePrincipal', sortable: 'custom' },
                { title: this.$t('field.PROINSTOCK.PRO_SOURCE'), key: 'productSource', sortable: 'custom' },
                { title: this.$t('field.PROINSTOCK.STATUS'), key: 'productInstockStateCn', sortable: 'custom' },

                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            this.$router.push('/product-instock/'+row[this.vm.identity]) 
                        }, 'detailPermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => { 
                            this.remove([row]) 
                        }, 'removePermission')]);
                    } 
                }
            ];
        },
        productInstockStateCn() {
            for (let i = 0; i < this.productInstockStateList.length; ++i) {
                if (this.productInstockStateList[i].value === this.vm.queryParameters.productInstockState)
                    return this.productInstockStateList[i].descript;
            }
        },
        productInstockStateList() {
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
            let result = await productinstockService.search(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item.productInstockTime = new Date().toLocaleDateString(item.billTime);
                    item.productInstockStateCn = this.$t('field.PRODUCT_STOCK_STATE.' + Number(item.billState));
                    if (!item.sysDefault) {
                        item['detailPermission'] = true;
                        if (this.productInstockRemovePermission)
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
            } else if (data.key === 'productInstockStateCn') {
                this.vm.queryParameters.sortColumn = 'productInstockState';
                this.vm.queryParameters.sort = data.order;
            } else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            this.selectItems = [];
            this.search();
        },
        /*updateAuditState(selectItems, state) {
            let idList = _.map(selectItems, this.vm.identity).join(",");
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await productInstockService.updateAuditState(idList, state);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.search();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },*/
        remove(selectItems) {
            let idList = _.map(selectItems, this.vm.identity).join(",");
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: async () => {
                    let result = await productinstockService.remove(idList);
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
