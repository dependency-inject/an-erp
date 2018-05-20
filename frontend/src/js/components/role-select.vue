<template>
    <i-select v-model="model" :disabled="disabled" :multiple="multiple">
        <i-option v-for="item in data" :key="item.hashCode" :value="item.roleId">{{ item.roleName }}</i-option>
    </i-select>
</template>

<script>
import Emitter from '../mixins/emitter';

import util from '../libs/util.js';

import roleService from '../service/role'

export default {
    mixins: [ Emitter ],
    data() {
        return {
            data: [],
            model: this.multiple ? this.rebuildValue(this.value) : this.value
        }
    },
    props: {
        value: {
            type: [String, Number],
            required: true
        },
        disabled: {
            type: Boolean,
            default: false
        },
        multiple: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        async search() {
            let result = await roleService.getList();
            if (result.status === 200) {
                this.data = result.data;
                this.data.forEach((item) => {
                    item.hashCode = util.hashCode(JSON.stringify(item));
                });
                this.emitChange();
            }
        },
        updateValue() {
            this.$emit('input', this.model.join(','));
            this.dispatch('FormItem', 'on-form-change');
        },
        emitChange() {
            if (this.multiple) {
                let items = this.data.filter((item) => { return util.oneOf(item.roleId, this.model) });
                this.$emit('on-change', items);
            } else {
                let items = this.data.filter((item) => { return item.roleId == this.model });
                this.$emit('on-change', items.length == 0? { roleId: '', roleName: '', permissionIds: '' }: items[0]);
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
            this.model = this.multiple ? this.rebuildValue(val) : val;
        },
        model() {
            this.updateValue();
            this.emitChange();
        }
    }
}
</script>