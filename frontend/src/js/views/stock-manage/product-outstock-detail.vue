<template>
    <div class="main-panel">
       <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.PROINSTOCK.PROINSTOCK_ID')" prop="proOutstockNo"><i-input v-model="item.proOutstockNo"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.PROINSTOCK_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.PROINSTOCK.PROINSTOCK_TIME')" prop="prooutstockTime"><i-input v-model="item.prooutstockTime" :disabled=true></i-input></form-item>
                            <form-item :label="$t('field.PROINSTOCK.PROINSTOCK_PERSON')" prop="proOutstockPerson"><i-input v-model="item.proOutstockPerson"></i-input></form-item>
                            <form-item :label="$t('field.PROINSTOCK.STOCK_PERSON')" prop="stockPerson"><i-input v-model="item.stockPerson"></i-input></form-item>
                            <form-item :label="$t('field.PROINSTOCK.PRO_SOURCE')" prop="productSource"><i-input v-model="item.productSource"></i-input></form-item>    
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.PRO_STATUS_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.PROINSTOCK.STATUS')" prop="status"><radio-group v-model="item.status"><radio v-for="item in statusList" :key="item.value" :label="item.value">{{ item.descript }}</radio></radio-group></form-item>
                        </div>
                    </div>
                </i-form>
            </div>
       </div>
       <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="(productOutstockAddPermission&&$route.params.id==='add')||(productOutstockUpdatePermission)">{{ $t('common.SAVE') }}</i-button>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission'
import productoutstockService from '../../service/product-outstock';

export default {
    mixins: [Permission],
    data() {
        return {
            item: {},
        }
    },
    computed: {
        rules() {
            return {
                proOutstockNo: [
                    { required: true, trigger: 'blur' }
                ],
            }
        },
        statusList() {
            return [
                { value: 1, descript: this.$t('field.PROIN_STATUS.1') },
                { value: 2, descript: this.$t('field.PROIN_STATUS.2') },
                { value: 3, descript: this.$t('field.PROIN_STATUS.3') },
            ]
        }
    },
    methods: {
        initData() {
            // 路由检查
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.billId = Number(this.$route.params.id);
                this.getById();
            } else if (this.$route.params.id === 'add') {
                this.setDefault();
            } else {
                this.$router.replace('/product-outstock');
            }
        },
        getData() {
            let date = new Date();
            let sep = "-";
            let month = date.getMonth() + 1;
            let day = date.getDate();
            let currentDate = date.getFullYear() + sep + month + sep + day;
            return currentDate;
        },
        setDefault() {
            this.item = {
                billId: 0,
                proOutstockNo: '123', 
                proOutstockPerson: 0,
                stockPerson: 0,
                productSource: 1,
                relatedBill: 0,
                status: 1,
                remark:'',
                productIdList:'0',
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    if (this.$route.params.id === 'add') {
                        let result = await productoutstockService.add(this.item);
                        console.log(result.status);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                            var item = result.data;
                            this.$router.replace('/product-outstock/' + item.billId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await productoutstockService.update(this.item);
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
        async getById() {
            if (this.$route.params.id === 'add' && this.item.billId === 0) return;
            let result = await productoutstockService.getById(this.item.billId);
            if (result.status === 200) {
                this.item = result.data;
                console.log(this.item);
                //this.item.prooutstockTime = new Date().toLocaleDateString(this.item.billTime);
                if (this.item.sysDefault) { // 系统默认用户不可查看
                    this.$router.replace('/product-outstock');
                }
                this.item.status = Number(this.item.billState);
            } else {
                this.$router.replace('/product-outstock');
            }
        },
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
