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
            <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="save" :loading="true">
        	<i-form ref="formValidate" :model="modal.item" :rules="rules" :label-width="90">
            	<form-item :label="$t('field.MATERIAL.MATERIAL_COST')" prop="cost"><i-input v-model="modal.item.cost"></i-input></form-item>
        	</i-form>
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

import materialService from '../../service/material';

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
            modal: {
                title: 'title',
                item: {},
                visible: false
            },
            validateuser: function(rule, value, callback){
                    if(!/^[1-9]\d*$/.test(value)){
                        return callback(new Error("ERROR"));
                    }else{
                        callback();
                    }
            }
        }
    },
    computed: {
        rules() {
            return {
                cost: [
                    { required: true, message: this.$t('field.MATERIAL.MATERIAL_COST')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' },
                    { validator: this.validateuser, message: this.$t('field.MATERIAL.MATERIAL_COST')+this.$t('field.NOT_VALID'), trigger: 'blur'}
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
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.EDIT'), (row) => {
                            this.edit(params)
                        }, 'detailPermission')]);
                    } 
                }
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
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid){
                    let result = {}
                    result = await materialService.update(this.modal.item);
                    if(result.status === 200) {
                        this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                        this.modal.visible = false;
                        await this.search();
                    } else {
                        this.$Message.error(result.data);
                        this.$refs.modal.abortLoading();
                    }
                } else{
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.buttonLoading = false;
                }
            })
        },
        edit(params) {
            // console.log(this.item.roleId);
            console.log(params);
            this.modal.title = this.$t('common.EDIT') + this.$t('field.MATERIAL.MATERIAL_COST');
            this.$refs.formValidate.resetFields();
            this.modal.item.cost = params.row.cost;
            this.modal.item.materialId = params.row.materialId;
            this.modal.visible = true;
            
        },
        initData() {
            this.search();
        },
        async search() {
            let idList = _.map(this.selectItems, this.vm.identity).join(",");
            let result = await materialService.search(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item['detailPermission'] = true;
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

