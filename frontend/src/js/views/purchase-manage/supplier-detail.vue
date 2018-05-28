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
                            <form-item :label="$t('field.SUPPLIER.CONTACT_PHONE')" prop="contactPhone"><i-input v-model="item.contactPhone"></i-input></form-item>
                            <form-item :label="$t('field.SUPPLIER.REGION')" prop="region"><i-input v-model="item.region"></i-input></form-item>
                            <form-item :label="$t('field.SUPPLIER.ADDRESS')" prop="address"><i-input v-model="item.address"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.SUPPLIER.PRICE_INFO') }}</div>
                        <div class="pull-left operate-list" v-show="selectItems==''">
                            <!-- 搜索框 -->
                            <i-input icon="search" :placeholder="$t('component.PLEASE_INPUT')+$t('field.SUPPLIER.MATERIAL_NO')+$t('field.SUPPLIER.MATERIAL_NAME')" @on-enter="selectItems=[];search()" style="width:300px"></i-input>
                        </div>
                        <!-- 选中表项后的批量处理按钮 -->
                        <div class="pull-left operate-list" v-show="selectItems!=''">
                            <a class="cancel-btn" @click="clearChecked"><icon type="close"></icon></a>
                            <span class="split-bar">{{ $t('component.SELECTED') }} <span class="text-primary">{{ selectItems.length }}</span> {{ $t('component.ITEMS') }}</span>
                            <!-- 删除 -->
                            <span class="label-btn" @click="remove(selectItems)" v-if="supplierRemovePermission"><icon type="trash-a"></icon>{{ $t('common.REMOVE') }}</span>
                        </div>
                        <div class="button-list pull-right">
                            <i-button class="operate-btn" type="primary" shape="circle" @click="$router.push('/supplier/add')" v-if="supplierAddPermission">{{ $t('common.ADD') }}</i-button>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-body">
                            <i-table :height="tableHeight" ref="table" :columns="columnList" :data="priceItems" ></i-table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(supplierAddPermission&&$route.params.id==='add'&&item.supplierId===0)||(supplierUpdatePermission&&item.supplierId!==0)">{{ $t('common.SAVE') }}</i-button>
        </div>
        <!-- <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="save" :loading="true">
        	<i-table :height="tableHeight" ref="table" :columns="columnList" :data="modal.items" ></i-table>
    	</modal> -->
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
            priceItems: [],
            selectItems: [],
            identity: 'id'        
        }
    },
    computed: {
        rules() {
            return {
                supplierName: [
                    { required: true, message: this.$t('field.SUPPLIER.SUPPLIER_NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
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
                { title: this.$t('field.SUPPLIER.MATERIAL_NO'), key: 'materialNo', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.MATERIAL_NAME'), key: 'materialName', sortable: 'custom' },
                { title: this.$t('field.SUPPLIER.PRICE'), key: 'price', sortable: 'custom' },
                { title: this.$t('field.OPERATE'), key: 'action', width: 200, render: (h, params) => {
                        return h('div', [ util.tableButton(h, params, 'primary', this.$t('common.DETAIL'), (row) => {
                            // this.$router.push('/supplier/'+row[this.vm.identity]) 
                        }, 'detailPermission'), util.tableButton(h, params, 'error', this.$t('common.REMOVE'), (row) => { 
                            // this.remove([row]) 
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
                this.item.supplierId = Number(this.$route.params.id);
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
            if (this.$route.params.id === 'add' && this.item.supplierId === 0) return;
            let result = await supplierService.getById(this.item.supplierId);
            if (result.status === 200) {
                this.item = result.data;
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
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
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
        // async search() {
        //     let idList = _.map(this.selectItems, this.identity).join(",");
        //     let result = await supplierService.search(this.vm.queryParameters);
        //     if (result.status === 200) {
        //         var items = result.data.list;
        //         this.vm.queryParameters.total = result.data.total;
        //         items.forEach((item) => {
        //             item['detailPermission'] = true;
        //             if (this.supplierRemovePermission)
        //                 item['removePermission'] = true;
        //         });
        //         this.vm.items = items;
        //         this.$nextTick(() => {
        //             for (var i = 0; i < items.length; ++i) {
        //                 if ((','+idList+',').indexOf(','+items[i][this.vm.identity]+',') > -1) {
        //                     this.$refs.table.toggleSelect(i);
        //                 }
        //             }
        //         });
        //     }
        // },
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
        },
        clearChecked() {
            this.$refs.table.selectAll(false);
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
