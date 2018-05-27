<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.SUPPLIER.SUPPLIER_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.SUPPLIER.SUPPLIER_NAME')" prop="supplierName"><i-input v-model="item.supplierName"></i-input></form-item>
                            <form-item :label="$t('field.SUPPLIER.CONTACT')" prop="contact"><i-input v-model="item.contact"></i-input></form-item>
                            <form-item :label="$t('field.SUPPLIER.CONTACT_PHONE')" prop="contactName"><i-input v-model="item.contactName"></i-input></form-item>
                            <form-item :label="$t('field.SUPPLIER.REGION')" prop="region"><i-input v-model="item.region"></i-input></form-item>
                            <form-item :label="$t('field.SUPPLIER.ADDRESS')" prop="address"><i-input v-model="item.address"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.PRICE_INFO') }}</div>
                        <div class="pull-left operate-list" v-show="selectItems==''">
                            <!-- 搜索框 -->
                            <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.SUPPLIER.MATERIAL_NO')+$t('field.SUPPLIER.MATERIAL_NAME')" v-model="vm.queryParameters.searchKey" @on-enter="selectItems=[];search()" style="width:300px"></i-input>
                        </div>
                        <div class="button-list pull-right">
                            <i-button class="operate-btn" type="primary" shape="circle" @click="$router.push('/supplier/add')" v-if="supplierAddPermission">{{ $t('common.ADD') }}</i-button>
                        </div>
                        <div class="panel-body">
                            <i-table :height="tableHeight" ref="table" :columns="columnList" :data="priceItems" @on-sort-change="handleSortPrice"></i-table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(adminAddPermission&&$route.params.id==='add'&&item.adminId===0)||(adminUpdatePermission&&item.adminId!==0)">{{ $t('common.SAVE') }}</i-button>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="save" :loading="true">
        	<i-table :height="tableHeight" ref="table" :columns="columnList" :data="modal.items" @on-sort-change="handleSortPrice"></i-table>
    	</modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import supplierService from '../../service/supplier';

export default {
    mixins: [ Permission ],
    data() {
        return {
            item: {},
            priceItems: []
        }
    },
    computed: {
        rules() {
            return {
                supplierName: [
                    { required: true, message: this.$t('field.ADMIN.TRUE_NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
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
                { title: this.$t('field.SUPPLIER.MATERIAL_NO'), key: 'supplierName', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NAME'), key: 'contact', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.PRICE'), key: 'contactPhone', sortable: 'custom' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            this.$router.push('/supplier/'+row[this.vm.identity]) 
                        }, 'detailPermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => { 
                            this.remove([row]) 
                        }, 'removePermission')]);
                    } 
                }
            ];
        }
    },
    methods: {
        initData() {
            // 路由检查
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.adminId = Number(this.$route.params.id);
                this.getById();
            } else if (this.$route.params.id === 'add') {
                this.setDefault();
            } else {
                this.$router.replace('/supplier');
            }
        },
        setDefault() {
            this.item = {
                supplierId: 0,
                supplierName: '',
                contact: '',
                contactPhone: '',
                region: '',
                address: ''
            }
            this.priceItems = []
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.adminId === 0) return;
            let result = await supplierService.getById(this.item.supplierId);
            if (result.status === 200) {
                this.item = result.data;
                this.item.closed = Number(this.item.closed);
            } else {
                this.$router.replace('/supplier');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    if (this.$route.params.id === 'add' && this.item.supplierId === 0) {
                        let result = await supplierService.add(this.item);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS')+this.$t('common.DOT')+this.$t('common.INITIAL_PASSWORD'));
                            var item = result.data;
                            this.$router.replace('/supplier/' + item.supplierId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await supplierService.update(this.item);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            this.initData();
                        } else {
                            this.$Message.error(result.data);
                        }
                    }
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                }
            });
        },
        async search() {
            let idList = _.map(this.selectItems, this.vm.identity).join(",");
            let result = await supplierService.search(this.vm.queryParameters);
            if (result.status === 200) {
                var items = result.data.list;
                this.vm.queryParameters.total = result.data.total;
                items.forEach((item) => {
                    item['detailPermission'] = true;
                    if (this.supplierRemovePermission)
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
            }
            else {
                this.vm.queryParameters.sortColumn = data.key;
                this.vm.queryParameters.sort = data.order;
            }
            // this.selectItems = [];
            this.search();
        }
    },
    created() {
        this.setDefault();
        this.initData();
    },
    watch: {
        '$route'(to, from) {
            this.initData();
        }
    }
}
</script>
