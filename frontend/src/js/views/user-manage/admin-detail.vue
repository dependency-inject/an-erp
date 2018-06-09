<template>
    <div class="main-panel">
        <div class="main-panel-content2">
            <div class="panel-container">
                <i-form ref="formValidate" :model="item" :rules="rules" :label-width="90" inline>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.BASE_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.ADMIN.TRUE_NAME')" prop="trueName"><i-input v-model="item.trueName"></i-input></form-item>
                            <form-item :label="$t('field.ADMIN.MOBILE')" prop="mobile"><i-input v-model="item.mobile"></i-input></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.ACCOUNT_INFO') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.ADMIN.LOGIN_NAME')" prop="loginName"><i-input v-model="item.loginName" :disabled="item.adminId!==0"></i-input></form-item>
                            <form-item :label="$t('field.ADMIN.STATE')" prop="closed"><radio-group v-model="item.closed"><radio v-for="item in closedList" :key="item.value" :label="item.value">{{ item.descript }}</radio></radio-group></form-item>
                        </div>
                    </div>
                    <div class="chief-panel">
                        <div class="panel-header">{{ $t('field.ROLE_PERMISSION2') }}</div>
                        <div class="panel-body">
                            <form-item :label="$t('field.ADMIN.ROLE')" prop="roleId"><common-select ref="role" type="role" v-model="item.roleIdList" multiple @on-change="handleRoleChange"></common-select></form-item>
                            <permission-table v-model="permissionIdList" :disabled="true" style="margin-top:20px"></permission-table>
                        </div>
                    </div>
                </i-form>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="save" v-if="editable">{{ $t('common.SAVE') }}</i-button>
        </div>
    </div>
</template>

<script>
import Permission from '../../mixins/permission';

import permissionTable from '../../components/permission-table';
import commonSelect from '../../components/common-select';

import adminService from '../../service/admin';

export default {
    name: 'admin-detail',
    mixins: [ Permission ],
    data() {
        return {
            item: {},
            permissionIdList: ''
        }
    },
    components: { permissionTable, commonSelect },
    computed: {
        rules() {
            return {
                trueName: [
                    { required: true, message: this.$t('field.ADMIN.TRUE_NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                mobile: [
                    { required: true, message: this.$t('field.ADMIN.MOBILE')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                loginName: [
                    { required: true, message: this.$t('field.ADMIN.LOGIN_NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                closed: [
                    { type: 'number', required: true, message: this.$t('field.PLEASE_SELECT')+this.$t('field.ADMIN.STATE'), trigger: 'change' }
                ]
            }
        },
        closedList() {
            return [
                { value: 0, descript: this.$t('field.CLOSED.0') },
                { value: 1, descript: this.$t('field.CLOSED.1') },
            ]
        },
        editable() {
            return (this.adminAddPermission && this.$route.params.id === 'add' && this.item.adminId === 0) || (this.adminUpdatePermission && this.item.adminId !==0);
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
                this.$router.replace('/admin');
            }
        },
        setDefault() {
            this.item = {
                adminId: 0,
                trueName: '',
                mobile: '',
                loginName: '',
                closed: 0,
                roleIdList: ''
            }
        },
        async getById() {
            if (this.$route.params.id === 'add' && this.item.adminId === 0) return;
            let result = await adminService.getById(this.item.adminId);
            if (result.status === 200) {
                this.item = result.data;
                if (this.item.sysDefault) { // 系统默认用户不可查看
                    this.$router.replace('/admin');
                }
                this.item.closed = Number(this.item.closed);
            } else {
                this.$router.replace('/admin');
            }
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    if (this.$route.params.id === 'add' && this.item.adminId === 0) {
                        let result = await adminService.add(this.item);
                        if (result.status === 200) {
                            this.$Message.success(this.$t('common.SAVE_SUCCESS')+this.$t('common.DOT')+this.$t('common.INITIAL_PASSWORD'));
                            var item = result.data;
                            this.$router.replace('/admin/' + item.adminId);
                        } else {
                            this.$Message.error(result.data);
                        }
                    } else {
                        let result = await adminService.update(this.item);
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
        handleRoleChange(roleList) {
            this.permissionIdList = _.union(_.map(roleList, function(item) {
                return item.permissionIdList.split(',');
            })).join(',');
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
