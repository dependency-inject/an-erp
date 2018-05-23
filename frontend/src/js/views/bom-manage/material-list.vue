<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                    <!-- 搜索框 -->
                    <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.MATERIAL.MATERIAL_NO')+'/'+$t('field.MATERIAL.MATERIAL_NAME')" v-model="vm.queryParameters.searchKey" @on-enter="selectItems=[];search()" style="width:300px"></i-input>
                </div>
                <!-- 选中表项后的批量处理按钮 -->
                <div class="pull-left operate-list" v-show="selectItems!=''">
                    <a class="cancel-btn" @click="clearChecked"><icon type="close"></icon></a>
                    <span class="split-bar">{{ $t('component.SELECTED') }} <span class="text-primary">{{ selectItems.length }}</span> {{ $t('component.ITEMS') }}</span>
                    <!-- 启用 -->
                    <!--<span class="label-btn" @click="updateClosedState(selectItems, 0)" v-if="adminUpdatePermission"><icon type="android-open"></icon>{{ $t('common.ENABLE') }}</span>-->
                    <!-- 停用 -->
                    <!--<span class="label-btn" @click="updateClosedState(selectItems, 1)" v-if="adminUpdatePermission"><icon type="android-exit"></icon>{{ $t('common.DISABLE') }}</span>-->
                    <!-- 删除 -->
                    <span class="label-btn" @click="remove(selectItems)" v-if="adminRemovePermission"><icon type="trash-a"></icon>{{ $t('common.REMOVE') }}</span>
                </div>
                <!-- 顶部右侧按钮 -->
                <div class="button-list pull-right">
                    <i-button class="operate-btn" type="primary" shape="circle" @click="$router.push('/material/add')" v-if="materialAddPermission">{{ $t('common.ADD') }}</i-button>
                </div>
                <div class="clearfix"></div>
            </div>
            <!-- 表格 -->
            <div class="main-content">
                <i-table :height="tableHeight" ref="table" :columns="columnList" :data="vm.items" @on-sort-change="handleSort" @on-selection-change="selectItems=arguments[0]" @detail="$router.push('/admin/'+arguments[0][vm.identity])" @remove="remove([arguments[0]])"></i-table>
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
                identity: 'materialId',
            },
            selectItems: [],
        }
    },
    computed: {
        tableHeight() {
            return document.documentElement.clientHeight - 220;
        },
        // 控制表格显示哪些列
        columnList() {
            return [
                { type: 'selection', width: 80, align: 'center' },
                { title: this.$t('field.MATERIAL.MATERIAL_NO'), key: 'materialNo', sortable: 'custom' },
                { title: this.$t('field.MATERIAL.MATERIAL_NAME'), key: 'materialName', sortable: 'custom' },
                { title: this.$t('field.MATERIAL.UNIT'), key: 'unit' },
                { title: this.$t('field.MATERIAL.SPEC'), key: 'spec' },
                { title: this.$t('field.MATERIAL.CATEGORY'), key: 'categoryName', sortable: 'custom' },
                { title: this.$t('field.MATERIAL.COST'), key: 'cost', sortable: 'custom' },
                { title: this.$t('field.MATERIAL.REMARK'), key: 'remark' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            this.$router.push('/admin/'+row[this.vm.identity]) 
                        }, 'detailPermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => { 
                            this.remove([row]) 
                        }, 'removePermission')]);
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
    }
}
</script>
