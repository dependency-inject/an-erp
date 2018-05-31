<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                    <!-- 搜索框 -->
                    <i-input icon="search" v-model="queryParameters.searchKey" :placeholder="$t('component.PLEASE_INPUT')+$t('field.CLIENT.ID')+'/'+$t('field.CLIENT.NAME')+'/'+$t('field.CLIENT.PHONE')"  style="width:300px" @on-click="selectItems=[];search()"></i-input>
                </div>
                <div class="pull-left operate-list" v-show="selectItems!=''">
                    <a class="cancel-btn" @click="clearChecked"><icon type="close"></icon></a>
                    <span class="split-bar">{{ $t('component.SELECTED') }} <span class="text-primary">{{ selectItems.length }}</span> {{ $t('component.ITEMS') }}</span>
                    <span class="label-btn" @click="allremove(selectItems)" v-if="adminRemovePermission"><icon type="trash-a"></icon>{{ $t('common.REMOVE') }}</span>
                </div>
                <!--新增客户按钮-->
                <div class="button-list pull-right">
                    <i-button class="operate-btn" type="primary" shape="circle" @click="add" v-if="adminAddPermission">{{ $t('common.ADD') }}</i-button>
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
            <Modal v-model="modal" :title="title" @on-ok="ok" @on-cancel="cancel">
                <div v-if="modalState==1">
                    <i-input :placeholder="$t('component.PLEASE_INPUT')+$t('field.CLIENT.NAME')" v-model="edited.clientName"></i-input>
                    <i-input :placeholder="$t('component.PLEASE_INPUT')+$t('field.CLIENT.PHONE')" v-model="edited.contactPhone"></i-input>
                </div>
                <div v-else-if="modalState==2">
                    <i-input :placeholder="$t('component.PLEASE_INPUT')+$t('field.CLIENT.NAME')" v-model="edited.clientName"></i-input>
                    <i-input :placeholder="$t('component.PLEASE_INPUT')+$t('field.CLIENT.PHONE')" v-model="edited.contactPhone"></i-input>
                </div>
                <div v-else>
                    <i-input :placeholder="$t('component.PLEASE_INPUT')+$t('field.CLIENT.NAME')" v-model="edited.clientName" disabled></i-input>
                    <i-input :placeholder="$t('component.PLEASE_INPUT')+$t('field.CLIENT.PHONE')" v-model="edited.contactPhone" disabled></i-input>
                </div>
            </Modal>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import clientService from '../../service/client';
export default {
    mixins: [ Permission ],
    data() {
        return {
            items: [],
            selectItems: [],
            queryParameters: {
                current: 1,
                limit: 10,
                total: 10,
                sortColumn: '',
                sort: '',
                searchKey: '',
            },
            modal: false,
            modalState: 0,
            edited: {},
        }
    },
    computed: {
        tableHeight() {
            return document.documentElement.clientHeight - 220;
        },
        columnList() {
            return [
                { type: 'selection', width: 80, align: 'center' },
                { title: this.$t('field.CLIENT.ID'), key: 'clientId', sortable: 'custom' },
                { title: this.$t('field.CLIENT.NAME'), key: 'clientName', sortable: 'custom'},
                { title: this.$t('field.CLIENT.PHONE'), key: 'contactPhone'},
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.EDIT'), (row) => {
                            this.edit(row)
                        }, 'detailPermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => { 
                            this.remove(row) 
                        }, 'removePermission')]);
                    } 
                }
            ];
        },
        title() {
            if (this.modalState == 1) {
                return '新增客户信息'
            } else if (this.modalState == 2) {
                return '修改客户信息'
            } else {
                return '确定要删除吗'
            }
        }
    },
    methods: {
        initData() {
            this.search()
        },
        add() {
            this.modal = true
            this.modalState = 1
        },
        edit(row) {
            this.modal = true
            this.modalState = 2
            //进行深拷贝
            this.edited = row
        },
        remove(row) {
            this.modal = true
            this.modalState = 3
            this.edited = row
        },
        async allremove(selectItems) {
            for (let i=0; i <selectItems.length; ++i) {
                await clientService.shanchu(selectItems[i])
            }
            this.toast()
            this.selectItems = []
            this.search()
        },
        async ok() {
            let result
            if (this.modalState == 1) {
                result = await clientService.add(this.edited)
            } else if (this.modalState == 2) {
                result = await clientService.update(this.edited)
            } else {
                result = await clientService.shanchu(this.edited) 
            }
            this.search()
            this.edited = {}
            this.toast()
        },
        toast() {
            this.$Message.info({
                content: this.$t('common.OPERATE_SUCCESS'),
                duration: 2,
            })
        },
        cancel() {
            this.edited = {}
        },
        async search() {
            let result = await clientService.search(this.queryParameters)
            if (result.status === 200) {
                this.items = result.data.list
                this.queryParameters.total = result.data.total
                this.items.forEach((item) => {
                    item['detailPermission'] = true
                    item['removePermission'] = true
            })
            }
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
        clearChecked() {
            this.$refs.table.selectAll(false)
        }
    },
    mounted() {
        this.initData()
    }
}
</script>
