<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                    <!-- 搜索框 -->
                    <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.WAREHOUSE.WAREHOUSE_NO')+'/'+$t('field.WAREHOUSE.WAREHOUSE_NAME')" v-model="vm.queryParameters.searchKey" @on-enter="selectItems=[];search()" style="width:300px"></i-input>
                </div>
                <!-- 选中表项后的批量处理按钮 -->
                <div class="pull-left operate-list" v-show="selectItems!=''">
                    <a class="cancel-btn" @click="clearChecked"><icon type="close"></icon></a>
                    <span class="split-bar">{{ $t('component.SELECTED') }} <span class="text-primary">{{ selectItems.length }}</span> {{ $t('component.ITEMS') }}</span>
                    <!-- 删除 -->
                    <span class="label-btn" @click="remove(selectItems)" v-if="warehouseRemovePermission"><icon type="trash-a"></icon>{{ $t('common.REMOVE') }}</span>
                </div>
                <!-- 顶部右侧按钮 -->
                <div class="button-list pull-right">
                    <i-button class="operate-btn" type="primary" shape="circle" @click="add" v-if="warehouseAddPermission">{{ $t('common.ADD') }}</i-button>
                </div>
                <div class="clearfix"></div>
            </div>
            <!-- 表格 -->
            <div class="main-content">
                <i-table :height="tableHeight" ref="table" :columns="columnList" :data="vm.items" @on-sort-change="handleSort" @on-selection-change="selectItems=arguments[0]"></i-table>
            </div>
            <!-- 翻页控制器 -->
            <div class="page-panel">
                <page :total="vm.queryParameters.total" :current="vm.queryParameters.current" :page-size="vm.queryParameters.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="vm.queryParameters.current=arguments[0];selectItems=[];search()" @on-page-size-change="vm.queryParameters.limit=arguments[0];selectItems=[];search()"></page>
            </div>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="save" :loading="true">
            <i-form ref="formValidate" :model="modal.item" :rules="rules" :label-width="90">
                <form-item :label="$t('field.WAREHOUSE.WAREHOUSE_NO')" prop="warehouseNo"><i-input v-model="modal.item.warehouseNo"></i-input></form-item>
                <form-item :label="$t('field.WAREHOUSE.WAREHOUSE_NAME')" prop="warehouseName"><i-input v-model="modal.item.warehouseName"></i-input></form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import warehouseService from '../../service/warehouse';

export default {
    name: 'warehouse-list',
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
                    sort: ''
                },
                items: [],
                identity: 'warehouseId',
            },
            modal: {
                title: 'title',
                item: {},
                visible: false
            },
            selectItems: [],
        }
    },
    computed: {
        rules(){
            return {
                warehouseName:[
                    { required: true, message: this.$t('field.WAREHOUSE.WAREHOUSE_NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur'}
                ],
                warehouseNo:[
                    { required: true, message: this.$t('field.WAREHOUSE.WAREHOUSE_NO')+this.$t('field.NOT_BE_NULL'), trigger: 'blur'}
                ],
            }
        },
        tableHeight() {
            return document.documentElement.clientHeight - 220;
        },
        // 控制表格显示哪些列
        columnList() {
            return [
                { type: 'selection', width: 80, align: 'center' },
                { title: this.$t('field.WAREHOUSE.WAREHOUSE_NO'), key: 'warehouseNo', sortable: 'custom' },
                { title: this.$t('field.WAREHOUSE.WAREHOUSE_NAME'), key: 'warehouseName', sortable: 'custom' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.EDIT'), (row) => {
                            this.update(row)
                        }, 'updatePermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => {
                            this.remove([row])
                        }, 'removePermission')]);
                    }
                }
            ];
        }
    },
    methods: {
        initData() {
            this.search();
        },
        async search() {
            let idList = _.map(this.selectItems, this.vm.identity).join(",");
            let result = await warehouseService.search(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item['updatePermission'] = true;
                    if (this.warehouseRemovePermission)
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
            } else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            this.selectItems = [];
            this.search();
        },
        remove(selectItems) {
            let idList = _.map(selectItems, this.vm.identity).join(",");
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: async () => {
                    let result = await warehouseService.remove(idList);
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
        add() {
            this.modal.title = this.$t('common.ADD') + this.$t('navigate.WAREHOUSE');
            this.$refs.formValidate.resetFields();
            this.modal.item.warehouseId = 0;
            this.modal.item.warehouseNo = '';
            this.modal.item.warehouseName = '';
            this.modal.visible = true;
        },
        update(item){
            this.modal.title = this.$t('common.EDIT') + this.$t('navigate.WAREHOUSE');
            this.$refs.formValidate.resetFields();
            this.modal.item.warehouseId = item.warehouseId;
            this.modal.item.warehouseNo = item.warehouseNo;
            this.modal.item.warehouseName = item.warehouseName;
            this.modal.visible = true;
        },
        clearChecked() {
            this.$refs.table.selectAll(false);
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let result = {}
                    if (this.modal.item.warehouseId === 0)
                        result = await warehouseService.add(this.modal.item);
                    else
                        result = await warehouseService.update(this.modal.item);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                        this.modal.visible = false;
                        this.search();
                    } else {
                        this.$Message.error(result.data);
                        this.$refs.modal.buttonLoading = false;
                    }
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.buttonLoading = false;
                }
            });
        },
    },
    mounted() {
        this.initData();
    }
}
</script>
