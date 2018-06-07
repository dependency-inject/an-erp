<template>
	<div class="main-panel">
		<div class="main-panel-content2">
            <div class="panel-container">
                <div class="chief-panel">
                    <div class="panel-header">{{ $t('field.ROLE_PERMISSION') }}&nbsp;&nbsp;（<a class="remark" @click="add"><icon type="plus"></icon> {{ $t('common.ADD')+$t('field.ROLE.ROLE_NAME') }}</a>）</div>
                    <div class="panel-body">
                        <div class="data-item">
                            <div class="item-start">{{ $t('field.ROLE.SELECT_ROLE') }}</div>
                            <common-select ref="role" class="item-middle" type="role" v-model="item.roleId" @on-change="roleSelectChange"></common-select>
                            <div class="item-end" v-if="item.roleId&&!item.sysDefault"><a @click="edit(item)"><icon type="edit"></icon> {{ $t('common.EDIT')+$t('field.ROLE.ROLE_NAME') }}</a><a @click="remove(item)"><icon type="close"></icon> {{ $t('common.REMOVE')+$t('field.ROLE.ROLE_NAME') }}</a></div>
                        </div>
                    </div>
                </div>
                <div class="chief-panel">
                    <div class="panel-header">{{ $t('field.PERMISSION_DETAIL') }}</div>
                    <div class="panel-body">
                        <permission-table v-model="item.permissionIdList" :disabled="!item.roleId||item.sysDefault"></permission-table>
                    </div>
                </div>
            </div>
        </div>
        <div class="panel-bottom">
            <i-button class="operate-btn" type="primary" shape="circle" @click="savePermissions">{{ $t('common.SAVE') }}</i-button>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" :ok-text="$t('common.SAVE')" @on-ok="save" :loading="true">
        	<i-form ref="formValidate" :model="modal.item" :rules="rules" :label-width="90">
            	<form-item :label="$t('field.ROLE.ROLE_NAME')" prop="roleName"><i-input v-model="modal.item.roleName"></i-input></form-item>
        	</i-form>
    	</modal>
	</div>
</template>

<script>
import permissionTable from '../../components/permission-table';
import commonSelect from '../../components/common-select';

import roleService from '../../service/role';

export default {
    name: 'role-list',
    data() {
        return {
        	item: {
        	},
            modal: {
                title: 'title',
                item: {},
                visible: false
            }
        }
    },
    components: { permissionTable, commonSelect },
    computed: {
        rules() {
            return {
                roleName: [
                    { required: true, message: this.$t('field.ROLE.ROLE_NAME')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        initData() {
        },
        setDefault() {
            this.item = {
                roleId: 0,
                permissionIdList: ''
            }
        },
        add() {
            this.modal.title = this.$t('common.ADD') + this.$t('field.ROLE.ROLE_NAME');
            this.$refs.formValidate.resetFields();
            this.modal.item.roleId = 0;
            this.modal.item.roleName = '';
            this.modal.visible = true;
        },
        edit(item) {
            this.modal.title = this.$t('common.EDIT') + this.$t('field.ROLE.ROLE_NAME');
            this.$refs.formValidate.resetFields();
            this.modal.item.roleId = item.roleId;
            this.modal.item.roleName = item.roleName;
            this.modal.visible = true;
        },
        remove(item) {
            this.$Modal.confirm({
                content: this.$t('common.REMOVE_CONFIRM'),
                onOk: async () => {
                    let result = await roleService.remove(item.roleId);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.REMOVE_SUCCESS'));
                        this.selectItems = [];
                        this.$refs.role.search();
                    } else {
                        this.$Message.error(result.data);
                    }
                }
            });
        },
        save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let result = {}
                    if (this.modal.item.roleId === 0)
                        result = await roleService.add(this.modal.item);
                    else
                        result = await roleService.update(this.modal.item);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                        this.modal.visible = false;
                        await this.$refs.role.search();
                    } else {
                        this.$Message.error(result.data);
                        this.$refs.modal.buttonLoading = false;
                    }
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.buttonLoading = false;
                }
            });
        },
        async savePermissions() {
            if (!this.item.roleId || this.item.sysDefault) {
                this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                return;
            }
            let result = await roleService.updatePermissions(this.item.roleId, this.item.permissionIdList);
            if (result.status === 200) {
                this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                this.$refs.role.search();
            } else {
                this.$Message.error(result.data);
            }
        },
        roleSelectChange(item) {
            if (item === null) {
                this.item.roleName = '';
                this.item.sysDefault = undefined;
                this.item.permissionIdList = '';
            } else {
                this.item.roleName = item.roleName;
                this.item.sysDefault = item.sysDefault;
                this.item.permissionIdList = item.permissionIdList;
            }
        }
    },
    created() {
    	this.setDefault();
        this.initData();
    }
}
</script>
