<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list">
                    <i-input icon="search"
                             :placeholder="$t('component.PLEASE_INPUT')+$t('field.MATERIAL_STOCK.MATERIAL_NO')+
                                           '/'+$t('field.MATERIAL_STOCK.MATERIAL_NAME')"
                             v-model="vm.queryParameters.searchKey"
                             @on-enter="search()"
                             style="width:300px"></i-input>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="main-content">
                <i-table :height="tableHeight"
                         ref="table"
                         :columns="columnList"
                         :data="vm.items"
                         @on-sort-change="handleSort"></i-table>
            </div>
            <div class="page-panel">
                <page :total="vm.queryParameters.total"
                      :current="vm.queryParameters.current"
                      :page-size="vm.queryParameters.limit"
                      page-size-place="top" show-elevator show-sizer show-total
                      @on-change="vm.queryParameters.current=arguments[0];search()"
                      @on-page-size-change="vm.queryParameters.limit=arguments[0];search()"></page>
            </div>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import stockService from '../../service/stock';

export default {
    mixins: [Permission],
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
        columnList() {
            return [
                {width: 80, align: 'center'},
                {title: this.$t('field.MATERIAL_STOCK.MATERIAL_NO'), key: 'materialNo', sortable: 'custom'},
                {title: this.$t('field.MATERIAL_STOCK.MATERIAL_NAME'), key: 'materialName', sortable: 'custom'},
                {title: this.$t('field.MATERIAL_STOCK.MATERIAL_AMOUNT'), key: 'totalAmount', sortable: 'custom'},
                {title: this.$t('field.MATERIAL_STOCK.MATERIAL_ORDERED'), key: 'orderedAmount', sortable: 'custom'},
                {title: this.$t('field.MATERIAL_STOCK.MATERIAL_LEFT'), key: 'leftAmount', sortable: 'custom'},
            ];
        },
        tableHeight() {
            return document.documentElement.clientHeight - 220;
        },
    },
    methods: {
        initData() {
            this.search();
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
        async search() {
            let result = await stockService.materialSearch(this.vm.queryParameters);
            if (result.status === 200) {
                let items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    if (!item.sysDefault) {
                        item['detailPermission'] = true;
                        if (this.adminRemovePermission)
                            item['removePermission'] = true;
                    }
                });
                this.vm.items = items;
            }
        }
    },
    mounted() {
        this.search();
    }
}
</script>
