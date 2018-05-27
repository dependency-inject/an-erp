<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                    <!-- 搜索框 -->
                    <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.MATERIAL.MATERIAL_NAME')" v-model="vm.queryParameters.searchKey" @on-enter="selectItems=[];search()" style="width:300px"></i-input>
                </div>
                <!-- 选中表项后的批量处理按钮 -->
                <!-- 顶部右侧按钮 -->
                <div class="clearfix"></div>
            </div>
            <!-- 表格 -->
            <div class="main-content">
                <i-table :height="tableHeight" ref="table" :columns="columnList" :data="vm.items" @on-sort-change="handleSort" @on-selection-change="selectItems=arguments[0]" @detail="$router.push('/admin/'+arguments[0][vm.identity])" @remove="remove([arguments[0]])"></i-table>
            </div>
    	    </modal>
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

import stockService from '../../service/stock';

import adminService from '../../service/admin';

export default {
    mixins: [ Permission ],
    data() {
        return {
            vm: {
                queryParameters: {
                    closed: -1,
                    searchKey: '',
                    total: 0,
                    limit: 10,
                    current: 1,
                    sortColumn: '',
                    sort: ''
                },
                items: [],
                identity: 'adminId',
            },
            selectItems: [],
        }
    },
    computed: {
        rules() {
            return {
                roleName: [
                    { required: true, message: this.$t('field.MATERIAL.MATERIAL_COST')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ]
            }
        },
        tableHeight() {
            return document.documentElement.clientHeight - 220;
        },
        // 控制表格显示哪些列
        columnList() {
            return [
                { type: 'selection', width: 80, align: 'center' },
                { title: this.$t('field.MATERIAL.MATERIAL_ID'), key: 'materialNo', sortable: 'custom' },
                { title: this.$t('field.MATERIAL.MATERIAL_NAME'), key: 'materialName', sortable: 'custom' },
                { title: this.$t('field.MATERIAL.MATERIAL_COST'), key: 'cost', sortable: 'custom' },
                { title: this.$t('field.MATERIAL.QUANTITY'), key: 'realStock', sortable: 'custom' },
                { title: this.$t('field.MATERIAL.TOTAL_COST'), key: 'totalCost', sortable: 'custom' },
            ];
        },
        // 账号状态筛选框的当前值
        closedCn() {
            for (let i = 0; i < this.closedList.length; ++i) {
                if (this.closedList[i].value === this.vm.queryParameters.closed)
                    return this.closedList[i].descript;
            }
        },
        // 账号状态筛选框的列表值
        closedList() {
            return [
                { value: -1, descript: this.$t('field.CLOSED.-1') },
                { value: 0, descript: this.$t('field.CLOSED.0') },
                { value: 1, descript: this.$t('field.CLOSED.1') },
            ]
        }
    },
    methods: {
        edit(params) {
            // console.log(this.item.roleId);
            // console.log(params);
            this.modal.title = this.$t('common.EDIT') + this.$t('field.MATERIAL.MATERIAL_COST');
            this.$refs.formValidate.resetFields();
            this.modal.item.roleId = 0;
            this.modal.item.roleName = params.row.cost;
            this.modal.visible = true;
            
        },
        initData() {
            this.search();
        },
        async search() {
            let idList = _.map(this.selectItems, this.vm.identity).join(",");
            let result = await stockService.search(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item['detailPermission'] = true;
                    item['totalCost'] = parseInt(item['realStock'])*parseInt(item['cost']);
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
            } else if (data.key === 'closedCn') {
                this.vm.queryParameters.sortColumn = 'closed';
                this.vm.queryParameters.sort = data.order;
            } else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            this.selectItems = [];
            this.search();
        },
        updateClosedState(selectItems, state) {
            let idList = _.map(selectItems, this.vm.identity).join(",");
            this.$Modal.confirm({
                content: this.$t('common.OPERATE_CONFIRM'),
                onOk: async () => {
                    let result = await adminService.updateClosedState(idList, state);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.OPERATE_SUCCESS'));
                        this.search();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        remove(selectItems) {
            let idList = _.map(selectItems, this.vm.identity).join(",");
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: async () => {
                    let result = await adminService.remove(idList);
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
        }
    },
    mounted() {
        this.initData();
    }
}
</script>

