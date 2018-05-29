<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list">
                    <!-- 搜索框 -->
                    <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.MATERIAL_COST.MATERIAL_NO')+'/'+$t('field.MATERIAL_COST.MATERIAL_NAME')" v-model="vm.queryParameters.searchKey" @on-enter="search()" style="width:300px"></i-input>
                </div>
                <div class="clearfix"></div>
            </div>
            <!-- 表格 -->
            <div class="main-content">
                <i-table :height="tableHeight" ref="table" :columns="columnList" :data="vm.items" @on-sort-change="handleSort"></i-table>
            </div>
            <!-- 翻页控制器 -->
            <div class="page-panel">
                <page :total="vm.queryParameters.total" :current="vm.queryParameters.current" :page-size="vm.queryParameters.limit" page-size-place="top" show-elevator show-sizer show-total @on-change="vm.queryParameters.current=arguments[0];search()" @on-page-size-change="vm.queryParameters.limit=arguments[0];search()"></page>
            </div>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="save" :loading="true">
            <i-form ref="formValidate" :model="modal.item" :rules="rules" :label-width="90">
                <form-item :label="$t('field.MATERIAL_COST.MATERIAL_COST')" prop="cost"><input-number v-model="modal.item.cost" :min="0" style="width:100%"></input-number></form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import util from '../../libs/util.js';

import materialService from '../../service/material';

export default {
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
                identity: 'materialId',
            },
            modal: {
                title: 'title',
                item: {},
                visible: false
            }
        }
    },
    computed: {
        rules() {
            return {
                cost: [
                    { type: 'number', required: true, message: this.$t('field.MATERIAL_COST.MATERIAL_COST')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ]
            }
        },
        tableHeight() {
            return document.documentElement.clientHeight - 220;
        },
        // 控制表格显示哪些列
        columnList() {
            return [
                { width: 50, align: 'center' },
                { title: this.$t('field.MATERIAL_COST.MATERIAL_NO'), key: 'materialNo', sortable: 'custom' },
                { title: this.$t('field.MATERIAL_COST.MATERIAL_NAME'), key: 'materialName', sortable: 'custom' },
                { title: this.$t('field.MATERIAL_COST.MATERIAL_COST'), key: 'cost', sortable: 'custom' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.EDIT'), (row) => {
                            this.edit(row)
                        }, 'detailPermission')]);
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
            let result = await materialService.search(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item['detailPermission'] = true;
                });
                this.vm.items = items;
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
            this.search();
        },
        edit(item) {
            this.modal.title = this.$t('common.EDIT') + this.$t('field.MATERIAL_COST.MATERIAL_COST');
            this.$refs.formValidate.resetFields();
            this.modal.item.cost = item.cost;
            this.modal.item.materialId = item.materialId;
            this.modal.visible = true;
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let result = {}
                    result = await materialService.updateCost(this.modal.item);
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
        }
    },
    mounted() {
        this.initData();
    }
}
</script>

