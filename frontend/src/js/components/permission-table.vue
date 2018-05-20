<template>
    <checkbox-group ref="group" v-model="model">
        <table :class="[prefixCls]">
            <tbody>
                <tr v-for="item in rebuildData">
                    <th>{{ $t('permission.' + item.moduleName + '.INDEX') }}</th>
                    <td><checkbox :class="[prefixCls + '-checkbox']" v-for="child in item.childrens" :key="child.permissionId" :label="child.permissionId" :disabled="disabled">{{ $t('permission.' + item.moduleName + '.' + child.permissionName) }}</checkbox></td>
                </tr>
            </tbody>
        </table>
    </checkbox-group>
</template>

<script>
import roleService from '../service/role'

export default {
    name: 'permissionTable',
    data() {
        return {
            prefixCls: 'di-permission-table',
            data: [],
            rebuildData: [],
            model: this.rebuildValue(this.value)
        }
    },
    props: {
        value: {
            type: String,
            required: true
        },
        disabled: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        makeRebuildData() {
            this.data.forEach((item) => {
                let find = false;
                this.rebuildData.forEach((obj) => {
                    if (item.moduleId === obj.moduleId) {
                        obj.childrens.push(item);
                        find = true;
                    }
                });

                if (!find) {
                    let obj = { moduleId: item.moduleId, moduleName: item.moduleName, description: item.moduleDescription }
                    obj.childrens = [ item ]
                    this.rebuildData.push(obj);
                }
            });
        },
        async search() {
            let result = await roleService.getPermissionList();
            if (result.status === 200) {
                this.data = result.data;
                this.makeRebuildData();
                this.$nextTick(() => {
                    if (this.$refs.group) {
                        this.$refs.group.updateModel(true);
                    }
                });
            }
        },
        rebuildValue(value) {
            let arr = value.split(',').filter((item) => { return item != '' });
            for (var i = 0; i < arr.length; ++i) {
                arr[i] = Number(arr[i]);
            }
            return arr;
        }
    },
    created() {
        this.search();
    },
    watch: {
        value(val) {
            this.model = this.rebuildValue(val);
        },
        model() {
            this.$emit('input', this.model.join(','));
        }
    }
}
</script>