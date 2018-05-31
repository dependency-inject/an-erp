<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.ORDER.ID')" prop="id"><i-input v-model="item.billNo" disabled></i-input></form-item>
                            <form-item :label="$t('field.ORDER.SALES_MAN')" prop="salesMan"><i-input v-model="item.salesName" disabled></i-input></form-item>
                            <form-item :label="$t('field.ORDER.CLIENT')" prop="client"><i-input v-model="item.contact" :disabled="disabled"></i-input></form-item>
                            <form-item :label="$t('field.CLIENT.PHONE')" prop="phone"><i-input v-model="item.contactPhone" :disabled="disabled"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.ORDER.INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.ORDER.TIME')" prop="time"><i-input v-model="item.billTime" disabled></i-input></form-item>
                            <form-item :label="$t('field.ORDER.AMOUNT')" prop="amount"><i-input v-model="item.billAmount" :disabled="disabled"></i-input></form-item>
                            <form-item :label="$t('field.ORDER.STATE')" prop="state"><i-input v-model="getState" disabled></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel" v-if="checkaudit">
                        <div class="panel-header">{{ $t('field.ORDER.AUDIT_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.ORDER.AUDIT_TIME')" prop="audittime"><i-input v-model="item.auditAt" disabled></i-input></form-item>
                            <form-item :label="$t('field.ORDER.AUDIT_MAN')" prop="auditman"><i-input v-model="item.auditName" disabled></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel" v-if="checkproduce">
                        <div class="panel-header">{{ $t('field.ORDER.PRODUCE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.ORDER.PRODUCE_TIME')" prop="producetime"><i-input v-model="item.produceAt" disabled></i-input></form-item>
                            <form-item :label="$t('field.ORDER.PRODUCE_MAN')" prop="producetman"><i-input v-model="item.produceName" disabled></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel" v-if="checkdeliver">
                        <div class="panel-header">{{ $t('field.ORDER.DELIVER_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.ORDER.DELIVER_TIME')" prop="delivertime"><i-input v-model="item.deliveryAt" disabled></i-input></form-item>
                            <form-item :label="$t('field.ORDER.DELIVER_MAN')" prop="delivertman"><i-input v-model="item.deliverName" disabled></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.ORDER.PRODUCT_INFO') }}
                            <i-button class="operate-btn" type="primary" shape="circle" @click="addProduct" v-if="$route.params.id === 'add'">{{ $t('common.ADD') }}</i-button>
                        </div>
                        <div class="panel-body">
                            <i-table :height="tableHeight" ref="table" :columns="columnList" :data="products"></i-table>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.ORDER.REMARK') }}</div>
                        <div class="panel-body">
                            <i-input v-model="item.remark" :disabled="disabled"></i-input>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="$route.params.id === 'add'">{{ $t('common.SAVE') }}</i-button>
            <i-button class="operate-btn" type="primary" shape="circle" @click="shenhe" v-if="item.billState==1 && orderAuditPermission">{{ $t('common.AUDIT') }}</i-button>
            <i-button class="operate-btn" type="primary" shape="circle" @click="fanshenhe" v-if="item.billState==2 && orderAuditPermission">{{ $t('common.UNAUDIT') }}</i-button>
            <i-button class="operate-btn" type="primary" shape="circle" @click="produce" v-if="item.billState==2 && orderProducePermission">{{ $t('field.ORDER.PRODUCE') }}</i-button>
            <i-button class="operate-btn" type="primary" shape="circle" @click="deliver" v-if="item.billState==3 && orderDeliveryPermission">{{ $t('field.ORDER.DELIVER') }}</i-button>
            <i-button class="operate-btn" type="error" shape="circle" @click="cancel" v-if="item.billState==3 && orderCancelPermission">{{ $t('field.ORDER.CANCEL') }}</i-button>
        </div>
        <Modal v-model="modal" :title="title" @on-ok="ok" @on-cancel="cancel">
            <!--selected等于option里的value-->
            <Select v-model="selected" style="width:200px">
                <Option v-for="item in allProducts" :value="item.productId" :key="item.productId">{{ item.productName }}</Option>
            </Select>
            <i-input v-model="itemCount" :placeholder="$t('field.ORDER.PRODUCT_COUNT')"></i-input>
            <i-input v-model="itemPrice" :placeholder="$t('field.ORDER.PRODUCT_PRICE')"></i-input>
        </Modal>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import util from '../../libs/util.js';

import orderService from '../../service/order';
export default {
    mixins: [ Permission ],
    data() {
        return {
            item: {},
            disabled: true,
            products: [],
            allProducts: [],
            modal: false,
            title: '添加商品',
            selected: '',
            itemCount: '',
            itemPrice: '',
        }
    },
    computed: {
        stateList() {
            return [
                { value: 1, descript: this.$t('field.ORDERSTATE.1') },
                { value: 2, descript: this.$t('field.ORDERSTATE.2') },
                { value: 3, descript: this.$t('field.ORDERSTATE.3') },
                { value: 4, descript: this.$t('field.ORDERSTATE.4') },
                { value: 5, descript: this.$t('field.ORDERSTATE.5') },
            ]
        },
        columnList() {
            return [
                { title: this.$t('field.ORDER.PRODUCT_NAME'), key: 'productName'},
                { title: this.$t('field.ORDER.PRODUCT_COUNT'), key: 'quantity'},
                { title: this.$t('field.ORDER.PRODUCT_PRICE'), key: 'price'},
            ]
        },
        checkaudit() {
            return this.item['billState'] > 1 && this.item['billState'] < 5
        },
        checkproduce() {
            return this.item['billState'] > 2 && this.item['billState'] < 5
        },
        checkdeliver() {
            return this.item['billState'] > 3 && this.item['billState'] < 5
        },
        getState() {
            if(this.item['billState'] !== undefined) {
                return this.stateList[this.item['billState']-1].descript
            }
            else {
                return null
            }
        },
        tableHeight() {
            return document.documentElement.clientHeight - 400
        },
    },
    methods: {
        initData() {
            if (!isNaN(Number(this.$route.params.id))) {
                this.item.billId = Number(this.$route.params.id)
                this.getById()
                this.getProduct()
            } else if (this.$route.params.id === 'add') {
                this.disabled = false
                this.item['salesName'] = this.$store.state.app.loginAdmin.trueName
                this.getProducts()
            }
        },
        async getById() {
            let result = await orderService.getById(this.item.billId)
            if (result.status === 200) {
                this.item = result.data
                this.item['billTime'] = util.formatTimestamp(this.item['billTime'], 'yyyy-MM-dd')
                this.item['auditAt'] = util.formatTimestamp(this.item['auditAt'], 'yyyy-MM-dd')
                this.item['produceAt'] = util.formatTimestamp(this.item['produceAt'], 'yyyy-MM-dd')
                this.item['deliveryAt'] = util.formatTimestamp(this.item['deliveryAt'], 'yyyy-MM-dd')
            }
        },
        async getProduct() {
            //对于一张订单获取这张订单的所有商品
            let result = await orderService.getProduct(this.item.billId)
            if (result.status === 200) {
                this.products = result.data
            }
        },
        async getProducts() {
            //获得所有的商品供选择
            let result = await orderService.getProducts()
            if (result.status === 200) {
                this.allProducts = result.data
            }
        },
        toast(content) {
            this.$Message.info({
                content: content,
                duration: 2,
            })
        },
        async save() {
            if (this.item.contact == undefined || this.item.contactPhone == undefined || this.item.billAmount == undefined || this.products.length == 0) {
                this.toast('信息尚未填写完毕')
            } else {
                let order = {}
                order.adminId = this.$store.state.app.loginAdmin.adminId
                order.contact = this.item.contact
                order.contactPhone = this.item.contactPhone
                order.billAmount = this.item.billAmount
                order.remark = this.item.remark
                order.products = JSON.stringify(this.products)
                let result = await orderService.add(order)
                if (result.status === 200) {
                    this.$router.push('/order/'+result.data)
                }
            }
        },
        async shenhe() {
            let result = await orderService.shenhe(this.$store.state.app.loginAdmin.adminId, this.item.billId)
            this.checkResult(result)
        },
        async fanshenhe() {
            let result = await orderService.fanshenhe(this.$store.state.app.loginAdmin.adminId, this.item.billId)
            this.checkResult(result)
        },
        async produce() {
            let result = await orderService.produce(this.$store.state.app.loginAdmin.adminId, this.item.billId)
            this.checkResult(result)
        },
        async deliver() {
            let result = await orderService.deliver(this.$store.state.app.loginAdmin.adminId, this.item.billId)
            this.checkResult(result)
        },
        async cancel() {
            let result = await orderService.cancel(this.$store.state.app.loginAdmin.adminId, this.item.billId)
            this.checkResult(result)
        },
        checkResult(result) {
            if (result.status === 200) {
                this.toast(this.$t('common.OPERATE_SUCCESS'))
                this.getById()
                this.getProduct()
            }
        },
        ok() {
            let temp = {}
            if (this.selected != '' && this.itemCount != '' && this.itemPrice != '') {
                temp['productId'] = this.selected
                for (let i = 0; i < this.allProducts.length; ++i) {
                    if (this.allProducts[i]['productId'] == this.selected) {
                        temp['productName'] = this.allProducts[i]['productName']
                    }
                }
                temp['quantity'] = parseInt(this.itemCount)
                temp['price'] = parseInt(this.itemPrice)
            }
            console.log(temp)
            this.products.push(temp)
            this.selected = ''
            this.itemCount = ''
            this.itemPrice = ''
        },
        cancel() {
            this.selected = ''
            this.itemCount = ''
            this.itemPrice = ''
        },
        addProduct() {
            this.modal = true
        }
    },
    mounted() {
        this.initData()
    }
}
</script>
