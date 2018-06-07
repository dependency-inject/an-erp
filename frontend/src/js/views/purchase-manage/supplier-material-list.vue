<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div style="padding:12px;border-bottom:1px solid #ddd;font-size:16px">
                <span class="text-primary">{{ $t('field.SUPPLIER.SUPPLIER_NAME')+'：' }}</span>
                <span>{{ item.supplierName }}</span>
                <span class="text-primary" style="margin-left:30px">{{ $t('field.SUPPLIER.CONTACT')+'：' }}</span>
                <span>{{ item.contact }}</span>
            </div>
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                    <!-- 搜索框 -->
                    <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.SUPPLIER.MATERIAL_NO')+'/'+$t('field.SUPPLIER.MATERIAL_NAME')" v-model="vm.queryParameters.searchKey" @on-enter="selectItems=[];search()" style="width:300px"></i-input>
                </div>
                <!-- 选中表项后的批量处理按钮 -->
                <div class="pull-left operate-list" v-show="selectItems!=''">
                    <a class="cancel-btn" @click="clearChecked"><icon type="close"></icon></a>
                    <span class="split-bar">{{ $t('component.SELECTED') }} <span class="text-primary">{{ selectItems.length }}</span> {{ $t('component.ITEMS') }}</span>
                    <!-- 删除 -->
                    <span class="label-btn" @click="remove(selectItems)" v-if="supplierUpdatePermission"><icon type="trash-a"></icon>{{ $t('common.REMOVE') }}</span>
                </div>
                <!-- 顶部右侧按钮 -->
                <div class="button-list pull-right">
                    <i-button class="operate-btn" type="primary" shape="circle" @click="addPrice" v-if="supplierUpdatePermission">{{ $t('common.ADD') }}</i-button>
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
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="savePrice" :loading="true">
            <i-form ref="formValidate" :model="modal.item" :rules="rules" :label-width="90">
                <form-item :label="$t('field.SUPPLIER.MATERIAL')" prop="materialId">
                    <common-select type="material" v-model="modal.item.materialId"></common-select>
                </form-item>
                <form-item :label="$t('field.SUPPLIER.PRICE')" prop="price"><input-number v-model="modal.item.price" :min="0" style="width:100%"></input-number></form-item>
                <form-item :label="$t('field.SUPPLIER.REMARK')" prop="remark"><i-input v-model="modal.item.remark" type="textarea"></i-input></form-item>
            </i-form>
        </modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import commonSelect from '../../components/common-select';

import supplierService from '../../service/supplier';

export default {
    mixins: [ Permission ],
    data() {
        return {
            item: {
                supplierId: 0
            },
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
                identity: 'supplierMaterialId'
            },
            selectItems: [],
            modal: {
                title: 'title',
                item: {
                    materialId: ''
                },
                visible: false
            }
        }
    },
    computed: {
        rules() {
            return {
                materialId: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.SUPPLIER.MATERIAL'), trigger: 'change' }
                ],
                price: [
                    { type: 'number', required: true, message: this.$t('field.SUPPLIER.PRICE')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ]
            }
        },
        tableHeight() {
            return document.documentElement.clientHeight - 270;
        },
        // 控制表格显示哪些列
        columnList() {
            return [
                { type: 'selection', width: 80, align: 'center' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NO'), key: 'materialNo' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NAME'), key: 'materialName' },
                { title: this.$t('field.SUPPLIER.PRICE'), key: 'price', sortable: 'custom' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            this.updatePrice(row);
                        }, 'detailPermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => { 
                            this.remove([row]) 
                        }, 'removePermission')]);
                    } 
                }
            ];
        }
    },
    components: { commonSelect },
    methods: {
        initData() {
            // 路由检查
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.supplierId = Number(this.$route.params.id);
                this.getById();
                this.search();
            } else {
                this.$router.replace('/supplier');
            }
        },
        async getById() {
            if (this.item.supplierId === 0) return;
            let result = await supplierService.getById(this.item.supplierId);
            if (result.status === 200) {
                this.item = result.data;
            } else {
                this.$router.replace('/supplier');
            }
        },
        async search() {
            let idList = _.map(this.selectItems, this.vm.identity).join(",");
            let result = await supplierService.searchMaterial(Object.assign({ supplierId: this.item.supplierId }, this.vm.queryParameters));
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item['detailPermission'] = true;
                    if (this.supplierUpdatePermission)
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
                    let result = await supplierService.removeMaterial(idList);
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
        addPrice() {
            this.modal.title = this.$t('common.ADD') + this.$t('field.SUPPLIER.MATERIAL_INFO');
            this.$refs.formValidate.resetFields();
            this.modal.item.supplierMaterialId = 0;
            this.modal.item.supplierId = this.item.supplierId;
            this.modal.item.materialId = '';
            this.modal.item.price = 0;
            this.modal.item.remark = '';
            this.modal.visible = true;
        },
        updatePrice(item) {
            this.modal.title = this.$t('common.EDIT') + this.$t('field.SUPPLIER.MATERIAL_INFO');
            this.$refs.formValidate.resetFields();
            this.modal.item.supplierMaterialId = item.supplierMaterialId;
            this.modal.item.supplierId = item.supplierId;
            this.modal.item.materialId  = item.materialId;
            this.modal.item.price = item.price;
            this.modal.item.remark = item.remark;
            this.modal.visible = true;
        },
        savePrice() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let result = {}
                    if (this.modal.item.supplierMaterialId === 0)
                        result = await supplierService.addMaterial(this.modal.item);
                    else
                        result = await supplierService.updateMaterial(this.modal.item);
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
        }
    },
    created() {
        this.initData();
    },
    watch: {
        '$route'(to, from) {
            this.initData();
        }
    }
}
</script>
