<template>
    <div class="main-panel no-scroll">
        <div class="main-panel-content">
            <div class="operate-panel">
                <div class="pull-left operate-list" v-show="selectItems==''">
                    <div><Input clearable type="text" v-model="searchKey.billNo" :placeholder="$t('field.DRAW_MATERIAL.BILL_NO')" @on-enter="selectItems=[];search()"></Input></div>
                    <div><Input clearable type="text" v-model="searchKey.toPrincipal" :placeholder="$t('field.DRAW_MATERIAL.TO_PRINCIPAL')" @on-enter="selectItems=[];search()"></Input></div>
                    <div><DatePicker type="daterange" v-model="searchKey.billTime" :placeholder="$t('component.START_TIME')+'-'+$t('component.END_TIME')"></DatePicker></div>
                    <div><Select v-model="searchKey.billState" :placeholder="$t('component.ALL_STATE')" style="width:100px" clearable>
                        <Option v-for="item in status" :value="item.value" :key="item.value">{{ item.label }}</Option>
                    </Select> </div>
                    <Button type="ghost" shape="circle" icon="ios-search" @click="search()"></Button>
                </div>
                <div class="pull-left operate-list" v-show="selectItems!=''">
                    <a class="cancel-btn" @click="clearChecked"><icon type="close"></icon></a>
                    <span class="label-btn" @click="remove(selectItems)" ><icon type="trash-a"></icon>{{ $t('common.REMOVE') }}</span>
                </div>
                <div class="button-list pull-right">
                    <Button  type="primary" @click="$router.push('/development-draw/add')">{{ $t('common.ADD') }}</Button>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="main-content">
                <Table border ref="table" :columns="columnsList" :data="vm.items" :height="tableHeight" @on-selection-change="selectItems=arguments[0]"></Table>
            </div>
        </div>
    </div>
</template>

<script>
    import developmentDrawService from '../../service/development-draw';


    export default {
        data() {
            return {
                vm: {
                    items: [],
                    identity: 'billId',
                },
                selectItems: [],
                searchKey: {
                    toPrincipal: '',
                    billTime: '',
                    billNo: '',
                    billState: ''
                }
            }
        },
        computed: {
            status() {
                return [
                    { value: 1, label: this.$t('field.BILL_STATE.STATE1') },
                    { value: 2,  label: this.$t('field.BILL_STATE.STATE2') },
                    { value: 3, label: this.$t('field.BILL_STATE.STATE3') },
                ]
            },
            tableHeight() {
                return document.documentElement.clientHeight - 220;
            },
            columnsList() {
                return [
                    { type: 'selection', width: 60, align: 'center' },
                    { title: this.$t('field.DRAW_MATERIAL.BILL_NO'), key: 'billNo' },
                    { title: this.$t('field.DRAW_MATERIAL.TO_PRINCIPAL'), key: 'toPrincipal' },
                    { title: this.$t('field.DRAW_MATERIAL.DRAW_REASON'), key:'drawReason' },
                    { title: this.$t('field.DRAW_MATERIAL.BILL_STATE'), key: 'billState' },
                    { title: this.$t('field.DRAW_MATERIAL.BILL_TIME'), key: 'billTime' },
                    { title: this.$t('field.DRAW_MATERIAL.REMARK'), key: 'remark' },
                    { title: this.$t('field.OPERATE'), key: 'action', render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: { type: 'primary', size: 'small' },
                                    style: { marginRight: '5px' },
                                    on: { click: () => {
                                            this.$router.push('/development-draw/' + params.row[this.vm.identity])
                                        }}
                                }, this.$t('common.DETAIL')),
                                h('Button', {
                                    props: { type: 'error', size: 'small' },
                                    on: { click: () => {
                                            this.remove([params.row])
                                        }}
                                }, this.$t('common.REMOVE'))
                            ]);
                        }
                    }
                ]
            }
        },
        methods: {
            initData() {
                this.search()
            },
            async search() {
                this.searchKey.billTime[0] = new Date(this.searchKey.billTime[0]).getTime()
                this.searchKey.billTime[1] = new Date(this.searchKey.billTime[1]).getTime()
                console.log(JSON.stringify(this.searchKey))
                let result = await developmentDrawService.searchBill(JSON.stringify(this.searchKey))
                console.log(result)
                if (result.status === 200) {
                    var items = result.data
                    items.forEach((item) => {
                        if (item.billState === 1) {
                            item.billState = this.$t('field.BILL_STATE.STATE1')
                        } else if (item.billState === 2) {
                            item.billState = this.$t('field.BILL_STATE.STATE2')
                        } else if (item.billState === 3) {
                            item.billState = this.$t('field.BILL_STATE.STATE3')
                        }
                        if (item.drawReason === 2) {
                            item.drawReason = this.$t('field.DRAW_REASON.REASON2')
                        }
                        let date = new Date(item.billTime)
                        item.billTime = date.getFullYear() + '-' + (date.getMonth() < 9 ? '0' + (date.getMonth() + 1): date.getMonth() + 1) + '-' + date.getDate()
                    })
                    this.vm.items = items;
                }
            },
            remove(selectItems) {
                let idList = _.map(selectItems, this.vm.identity).join(",")
                this.$Modal.confirm({
                    content: this.$t('common.REMOVE_CONFIRM'),
                    onOk: async () => {
                        let result = await developmentDrawService.deletebills(idList)
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.REMOVE_SUCCESS'))
                            this.selectItems = []
                            this.search()
                        } else {
                            this.$Message.error(result.data)
                        }
                    }
                });
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
