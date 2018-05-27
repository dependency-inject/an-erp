<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="bill" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-body">
                            <form-item :label="$t('field.RETURN_MATERIAL.BILL_NO')"><i-input v-model="bill.billNo" :disabled="status == 0"></i-input></form-item>
                            <form-item :label="$t('field.RETURN_MATERIAL.FROM_PRINCIPAL')"><i-input v-model="bill.fromPrincipal" disabled></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-body">
                            <form-item :label="$t('field.RETURN_MATERIAL.CREATE_BY')"><i-input v-model="bill.createBy" disabled></i-input></form-item>
                            <form-item :label="$t('field.RETURN_MATERIAL.BILL_TIME')"><i-input type="date" v-model="bill.billTime" :disabled="status == 0"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-body">
                            <form-item :label="$t('field.RETURN_MATERIAL.RETURN_REASON')"><i-input v-model="bill.returnReason" disabled></i-input></form-item>
                            <form-item :label="$t('field.MATERIAL.REMARK')"><i-input v-model="bill.remark" :disabled="status == 0"></i-input></form-item>
                        </div>
                    </div>
                    <div>
                        <i-button class="operate-btn" type="success" @click="modal2 = true" :disabled="status == 0">{{ $t('common.ADD') }}</i-button>
                        <i-button class="operate-btn" type="error" @click="remove(selectItems)" :disabled="status == 0">{{ $t('common.REMOVE') }}</i-button>
                        <br>
                        <div class="main-content">
                            <Table border ref="selection" :columns="columns8" :data="all_mate_data" @on-selection-change="selectItems=arguments[0]"></Table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="savebill" :disabled="status == 0">{{$t('common.SAVE')}}</i-button>
            <i-button class="operate-btn" type="primary" shape="circle" @click="statusto0" v-show="status===1">{{$t('common.AUDIT')}}</i-button>
            <i-button class="operate-btn" type="primary" shape="circle" @click="statusto1" v-show="status===0">{{$t('common.UNAUDIT')}}</i-button>
        </div>
        <Modal v-model="modal2" title="添加物品" @on-ok="savemeta" @on-cancel="cancel" width="400px">
            <div class="add_goods">
                <div width="60px" class="add_goods_title"><div style="color:red;width:5px"></div>物品编号</div>
                <div><Select style="width:200px" v-model="meta.id">
                    <Option v-for="(item,index) in allmetainfo" :value="index" :key="item.materialId">{{ item.materialNo }}</Option>
                </Select></div>
            </div>
            <div class="add_goods">
                <div width="60px" class="add_goods_title"><div style="color:red;width:5px">*</div>数量</div>
                <Input type="text" style="width:200px" v-model="meta.count"></Input>
            </div>
            <div class="add_goods">
                <div width="60px" class="add_goods_title"><div style="color:red;width:5px">*</div>计划供料日期</div>
                <DatePicker type="date" placeholder="年/月/日" style="width:200px"></DatePicker>
            </div>
            <div class="add_goods">
                <div width="60px" class="add_goods_title"><div style="color:red;width:5px"></div>备注</div>
                <Input type="text" style="width:200px" v-model="meta.remark"></Input>
            </div>
        </Modal>
    </div>
</template>

<script>
    import Permission from '../../mixins/permission';

    import permissionTable from '../../components/permission-table';
    import developmentReturnService from '../../service/development-return';
    import materialService from '../../service/material';

    export default {
        data() {
            return {
                bill: {},
                meta:{},
                status:1,
                allmetainfo: [],
                goodssize: [
                    {
                        value: '默认',
                        label: '默认'
                    },
                    {
                        value: '较大',
                        label: '较大'
                    },{
                        value: '中等',
                        label: '中等'
                    }
                ],
                modal2: false,
                columns8: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '材料编号',
                        key: 'materialId'
                    },
                    {
                        title: '材料名称',
                        key:'name'
                    },
                    {
                        title: '数量',
                        key: 'quantity'
                    },
                    {
                        title: '备注',
                        key: 'remark'
                    }
                ],
                all_mate_data:[]
            }
        },
        computed: {
            rules() {
                return {

                }
            },
            loginAdmin(){
                return this.$store.state.app.loginAdmin;
            }
        },
        methods: {
            initData() {
                // 路由检查
                console.log("init")
                if (!isNaN(Number(this.$route.params.id))) {
                    this.bill.billId = Number(this.$route.params.id);
                    this.searchAllMaterials();
                    console.log(this.bill.billId);
                    this.getById();
                    console.log(this.bill.billId);
                    this.searchAllBillMaterials();
                } else if (this.$route.params.id === 'add') {
                    this.setDefault();
                    this.searchAllMaterials();
                } else {
                    this.$router.replace('/development-return');
                }
            },
            async getById() {
                let result = await developmentReturnService.getBill(this.bill.billId);
                console.log(result)
                if (result.status === 200) {
                    let data = result.data;
                    if (data.billState === 1) {
                        data.billState = this.$t('field.BILL_STATE.STATE1')
                    } else if (data.billState === 2) {
                        data.billState = this.$t('field.BILL_STATE.STATE2')
                    } else if (data.billState === 3) {
                        data.billState = this.$t('field.BILL_STATE.STATE3')
                    }
                    let date = new Date(data.billTime)
                    data.billTime = date.getFullYear() + '-' + (date.getMonth() < 9 ? '0' + (date.getMonth() + 1): date.getMonth() + 1) + '-' + date.getDate()
                    this.bill = data
                    if(result.data.billState=="待审核"){
                        this.status=1;
                    }else{
                        this.status=0;
                    }
                }
            },
            setDefault() {
                this.bill = {
                    fromPrincipal:'admin',
                    createBy:'admin',
                    billTime:'',
                    returnReason:'研发退料',
                    remark:''
                },
                    this.meta = {
                        id:'',
                        remark:''
                    }
                console.log(this.bill.createBy)
            },
            async remove(selectItems) {
                console.log(selectItems);
                let idList = _.map(selectItems, 'materialId');
                console.log(idList);
                this.$Modal.confirm({
                    content: this.$t('common.REMOVE_CONFIRM'),
                    onOk: async () => {
                        //console.log(idList);
                        if (!isNaN(Number(this.$route.params.id))) {
                            var tmpidList=idList.join(",")
                            let result = await developmentReturnService.deletematerials(this.bill.billId,tmpidList)
                            if (result.status != 200) {
                                this.$Message.error("error")
                            }
                        }
                        var i;
                        for( i in this.all_mate_data){
                            console.log("i:"+i);
                            console.log("materialId"+this.all_mate_data[i].materialId);
                            var j;
                            for(j in idList){
                                if(this.all_mate_data[i].materialId==idList[j]){
                                    if (this.$route.params.id === 'add') {
                                        var tmpj=idList[j];
                                        console.log(tmpj);
                                        let result = await developmentReturnService.deleteitem(tmpj);
                                        if (result.status != 200) {
                                            this.$Message.error("error")
                                        }
                                    }
                                    this.all_mate_data.splice(i,1);
                                }
                            }
                        }
                    }
                });
            },
            ok () {
                this.$Message.info('Clicked ok');
            },
            cancel () {
                this.$Message.info('Clicked cancel');
            },
            async savemeta() {
                if (!isNaN(Number(this.$route.params.id))) {
                    //addmaterial = (material_id,quantity,remark)
                    console.log('savemeta');
                    console.log(this.bill.billId,this.allmetainfo[this.meta.id].materialId,this.meta.count,this.meta.remark);
                    console.log("************")
                    var count=parseInt(this.meta.count)
                    console.log(typeof count)
                    console.log("************")
                    let result = await developmentReturnService.changematerials(this.bill.billId,this.allmetainfo[this.meta.id].materialId,count,this.meta.remark);
                    console.log(result);
                    if(result.data=="success"){
                        var new_data=[{}];
                        new_data[0].materialId=this.allmetainfo[this.meta.id].materialId;
                        new_data[0].quantity=this.meta.count;
                        new_data[0].remark=this.meta.remark;
                        new_data[0].name=this.allmetainfo[this.meta.id].materialName;
                        var new_all_mate_data = this.all_mate_data.concat(new_data);
                        this.all_mate_data=new_all_mate_data;
                    }else {
                        this.$Message.error("error")
                    }
                } else if (this.$route.params.id === 'add') {
                    console.log(this.allmetainfo[this.meta.id].materialId,this.meta.count,this.meta.remark);
                    let result = await developmentReturnService.addmaterial(this.allmetainfo[this.meta.id].materialId,this.meta.count,this.meta.remark);
                    if(result.data=="success"){
                        console.log(result);
                        var new_data=[{}];
                        new_data[0].materialId=this.allmetainfo[this.meta.id].materialId;
                        new_data[0].quantity=this.meta.count;
                        new_data[0].remark=this.meta.remark;
                        new_data[0].name=this.allmetainfo[this.meta.id].materialName;
                        var new_all_mate_data = this.all_mate_data.concat(new_data);
                        this.all_mate_data=new_all_mate_data;
                    }else {
                        this.$Message.error("error")
                    }
                }
            },
            async savebill(){
                if (this.$route.params.id === 'add') {
                    console.log(this.bill.billNo,this.bill.toPrincipal,this.bill.createBy,2);
                    let result = await developmentReturnService.addBill(this.bill.billNo,this.bill.remark,2);
                    if (result.status != 200) {
                        this.$Message.error("error")
                    }
                }
            },
            async searchAllBillMaterials() {
                console.log('searchAllBillMaterials');
                let result = await developmentReturnService.getAllBillMaterials(this.bill.billId);
                console.log('searchAllBillMaterials');
                console.log(result.data);
                this.all_mate_data = result.data;
            },
            async searchAllMaterials() {
                let result = await materialService.getAllMaterials();
                console.log('searchAllMaterials');
                if (result.status === 200) {
                    this.allmetainfo=result.data;
                }
                console.log(result.data);
            },
            statusto0(){
                this.status=0;
                let result = developmentReturnService.changestatus(this.bill.billId,0);
                console.log(result);
            },
            statusto1(){
                this.status=1;
                let result = developmentReturnService.changestatus(this.bill.billId,1);
                console.log(result);
            }
        },
        created() {
            this.searchAllMaterials();
            this.setDefault();
            this.initData();
        }
    }
</script>
<style>
    .add_goods{
        display: flex;
        flex-flow: wrap;
        justify-content: space-between;
        height: 45px;
    }
    .add_goods_title{
        display: flex;
        flex-flow: wrap;
        justify-content: space-between;
    }
</style>