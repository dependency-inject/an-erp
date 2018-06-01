<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.SUPPLIER.SUPPLIER_NAME')" prop="supplierName"><i-input v-model="item.supplierName"></i-input></form-item>
                            <form-item :label="$t('field.SUPPLIER.CONTACT')" prop="contact"><i-input v-model="item.contact"></i-input></form-item>
                            <form-item :label="$t('field.SUPPLIER.CONTACT_PHONE')" prop="contactPhone"><i-input v-model="item.contactPhone"></i-input></form-item>
                            <form-item :label="$t('field.SUPPLIER.REGION')" prop="region"><i-input v-model="item.region"></i-input></form-item>
                            <form-item :label="$t('field.SUPPLIER.ADDRESS')" prop="address"><i-input v-model="item.address"></i-input></form-item>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(supplierAddPermission&&$route.params.id==='add'&&item.supplierId===0)||(supplierUpdatePermission&&item.supplierId!==0)">{{ $t('common.SAVE') }}</i-button>
            <i-button class="operate-btn" type="success" shape="circle" @click="$router.push('/supplier-material/'+item.supplierId)" v-if="item.supplierId!==0">{{ $t('field.SUPPLIER.MATERIAL_INFO') }}</i-button>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import supplierService from '../../service/supplier';

export default {
    mixins: [ Permission ],
    data() {
        return {
            item: {}
        }
    },
    computed: {
        rules() {
            return {
                supplierName: [
                    { required: true, message: this.$t('field.SUPPLIER.SUPPLIER_NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                contact: [
                    { required: true, message: this.$t('field.SUPPLIER.CONTACT')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ]
            }
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
