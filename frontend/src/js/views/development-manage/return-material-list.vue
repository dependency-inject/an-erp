<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                    <!-- 状态筛选框 -->
                    <dropdown trigger="click" placement="bottom-start" @on-click="vm.queryParameters.state=arguments[0];selectItems=[];search()" style="margin: 0 10px">
                        <a href="javascript:void(0)"><span class="main-user-name">{{ stateCn }} </span><icon type="arrow-down-b"></icon></a>
                        <dropdown-menu slot="list">
                            <dropdown-item v-for="item in stateList" :key="item.value" :name="item.value">{{ item.descript }}</dropdown-item>
                        </dropdown-menu>
                    </dropdown>
                    <!-- 搜索框 -->
                    <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.RETURN_MATERIAL.BILL_NO')+'/'+$t('field.RETURN_MATERIAL.FROM_PRINCIPAL')" v-model="vm.queryParameters.searchKey" @on-enter="selectItems=[];search()" style="width:300px"></i-input>
                    <!-- 日期选择框 -->
                    <date-picker v-model="vm.queryParameters.beginTime" type="datetime" :placeholder="$t('component.BEGIN_TIME')" @on-change="search"></date-picker> - <date-picker v-model="vm.queryParameters.endTime" type="datetime" :placeholder="$t('component.END_TIME')" @on-change="search"></date-picker>
                </div>
                <div class="pull-left operate-list" v-show="selectItems!=''">
                    <a class="cancel-btn" @click="clearChecked"><icon type="close"></icon></a>
                    <span class="split-bar">{{ $t('component.SELECTED') }} <span class="text-primary">{{ selectItems.length }}</span> {{ $t('component.ITEMS') }}</span>
                    <!-- 审核 -->
                    <span class="label-btn" @click="audit(selectItems)" ><icon type="checkmark"></icon>{{ $t('common.AUDIT') }}</span>
                    <!-- 反审核 -->
                    <span class="label-btn" @click="unaudit(selectItems)" ><icon type="reply"></icon>{{ $t('common.UNAUDIT') }}</span>
                    <!-- 删除 -->
                    <span class="label-btn" @click="remove(selectItems)" ><icon type="trash-a"></icon>{{ $t('common.REMOVE') }}</span>
                </div>
                <div class="button-list pull-right">
                    <Button class="operate-btn" type="primary" shape="circle" @click="$router.push('/development-return/add')">{{ $t('common.ADD') }}</Button>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="main-content">
                <Table border ref="table" :columns="columnsList" :data="vm.items" :height="tableHeight" @on-sort-change="handleSort" @on-selection-change="selectItems=arguments[0]"></Table>
            </div>
            <div class="page-panel">
                <page :total="vm.queryParameters.total" :current="vm.queryParameters.current" :page-size="vm.queryParameters.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="vm.queryParameters.current=arguments[0];selectItems=[];search()" @on-page-size-change="vm.queryParameters.limit=arguments[0];selectItems=[];search()"></page>
            </div>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';
import util from '../../libs/util.js';
import commonSelect from '../../components/common-select';
import developmentReturnService from '../../service/development-return';
export default {
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
                    sort: '',
                    state: -1,
                    beginTime: '',
                    endTime: ''
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
        columnsList() {
            return [
                { type: 'selection', width: 60, align: 'center' },
                { title: this.$t('field.RETURN_MATERIAL.BILL_NO'), key: 'billNo', sortable: 'custom' },
                { title: this.$t('field.RETURN_MATERIAL.FROM_PRINCIPAL'), key: 'fromPrincipalName', sortable:'custom' },
                { title: this.$t('field.RETURN_MATERIAL.WAREHOUSE_PRINCIPAL'), key: 'warehousePrincipalName', sortable: 'custom' },
                { title: this.$t('field.RETURN_MATERIAL.BILL_TIME'), key: 'billTimeLocal', sortable: 'custom' },
                { title: this.$t('field.RETURN_MATERIAL.BILL_STATE'), key: 'billStateCn', sortable: 'custom' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            this.$router.push('/development-return/'+row[this.vm.identity])
                        }, 'detailPermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => {
                            this.remove([row])
                        }, 'removePermission')]);
                    }
                }
            ]
        },
        stateCn() {
            for (let i = 0; i < this.stateList.length; ++i) {
                if (this.stateList[i].value === this.vm.queryParameters.state)
                    return this.stateList[i].descript;
            }
        },
        stateList() {
            return [
                { value: -1, descript: this.$t('field.BILL_STATE.ALL_STATE') },
                { value: 1, descript: this.$t('field.BILL_STATE.STATE1') },
                { value: 2,  descript: this.$t('field.BILL_STATE.STATE2') },
                { value: 3, descript: this.$t('field.BILL_STATE.STATE3') },
            ]
        }
    },
    methods: {
        initData() {
            this.search()
        },
        async search() {
            let idList = _.map(this.selectItems, this.vm.identity).join(",");
            let beginTime = -1;
            if (this.vm.queryParameters.beginTime != '')
                beginTime = this.vm.queryParameters.beginTime.getTime();
            let endTime = -1;
            if (this.vm.queryParameters.endTime != '')
                endTime = this.vm.queryParameters.endTime.getTime();
            let result = await developmentReturnService.searchBill(Object.assign({}, this.vm.queryParameters, { beginTime: beginTime, endTime: endTime }));
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item.billStateCn = this.$t('field.BILL_STATE.STATE' + Number(item.billState));
                    item.billTimeLocal = util.formatTimestamp(item.billTime, "yyyy-MM-dd hh:mm:ss");
                    item['detailPermission'] = true;
                    if (item.billState === 1 && this.developmentReturnRemovePermission)
                        item['removePermission'] = true;
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
            } else if (data.key === 'fromPrincipalName') {
                this.vm.queryParameters.sortColumn = 'fromPrincipal';
                this.vm.queryParameters.sort = data.order;
            } else if (data.key === 'warehousePrincipalName') {
                this.vm.queryParameters.sortColumn = 'warehousePrincipal';
                this.vm.queryParameters.sort = data.order;
            } else if (data.key === 'billTimeLocal') {
                this.vm.queryParameters.sortColumn = 'billTime';
                this.vm.queryParameters.sort = data.order;
            } else if (data.key === 'billStateCn') {
                this.vm.queryParameters.sortColumn = 'billState';
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
                    let result = await developmentReturnService.auditBill(idList)
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
                    let result = await developmentReturnService.unauditBill(idList)
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
            let idList = _.map(selectItems, this.vm.identity).join(",")
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: async () => {
                    let result = await developmentReturnService.deleteBill(idList)
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.REMOVE_SUCCESS'))
                        this.selectItems = []
                        this.search()
                    } else {
                        this.$Message.error(result.data)
                    }
                }
            });
        },
        clearChecked() {
            this.$refs.table.selectAll(false)
        }
    },
    mounted() {
        this.initData()
    }
}
</script>