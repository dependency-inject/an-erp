<template>
    <i-select v-model="model" :disabled="disabled" :multiple="multiple" transfer>
        <i-option v-for="item in data" :key="item.hashCode" :value="item[option.value]">{{ item.labelValue }}</i-option>
    </i-select>
</template>

<script>
import Emitter from '../mixins/emitter';

import util from '../libs/util.js';

import productService from '../service/product';
import materialService from '../service/material';
import clientService from '../service/client';
import adminService from '../service/admin';
import roleService from '../service/role';

export default {
    mixins: [ Emitter ],
    data() {
        return {
            data: [],
            model: this.rebuildValue(this.value),
            option: {}
        }
    },
    props: {
        type: {
            validator (value) {
                return util.oneOf(value, ['product', 'material', 'client', 'admin', 'role']);
            }
        },
        value: {
            type: [String, Number],
            required: true
        },
        queryParameters: {
            type: Object
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
        initOption() {
            if (this.type === 'product') {
                this.option = { service: productService, value: 'productId', label: ['productNo', 'productName'] }
            } else if (this.type === 'material') {
                this.option = { service: materialService, value: 'materialId', label: ['materialNo', 'materialName'] }
            } else if (this.type === 'client') {
                this.option = { service: clientService, value: 'clientId', label: ['clientName'] }
            } else if (this.type === 'admin') {
                this.option = { service: adminService, value: 'adminId', label: ['trueName'] }
            } else if (this.type === 'role') {
                this.option = { service: roleService, value: 'roleId', label: ['roleName'] }
            }
        },
        async search() {
            // TODO: 角色选择框修改名称后，显示信息不更新
            let result = await this.option.service.getList(this.queryParameters);
            if (result.status === 200) {
                this.data = result.data;
                this.data.forEach((item) => {
                    item.labelValue = item[this.option.label[0]];
                    for (let i = 1; i < this.option.label.length; ++i)
                        item.labelValue += (' - ' + item[this.option.label[i]]);
                    item.hashCode = util.hashCode(JSON.stringify(item));
                });
                this.emitChange();
                this.model = this.rebuildValue(this.value);
            }
        },
        updateValue() {
            if (this.multiple) {
                this.$emit('input', this.model.join(','));
            } else {
                this.$emit('input', this.model);
            }
            this.dispatch('FormItem', 'on-form-change');
        },
        emitChange() {
            if (this.multiple) {
                let items = this.data.filter((item) => { return util.oneOf(item[this.option.value], this.model) });
                this.$emit('on-change', items);
            } else {
                let items = this.data.filter((item) => { return item[this.option.value] == this.model });
                this.$emit('on-change', items.length == 0? null: items[0]);
            }
        },
        rebuildValue(value) {
            if (!this.multiple) return value;
            let arr = value.split(',').filter((item) => { return item != '' });
            for (var i = 0; i < arr.length; ++i) {
                arr[i] = Number(arr[i]);
            }
            return arr;
        }
    },
    created() {
        this.initOption();
        this.search();
    },
    watch: {
        value(val) {
            if (this.data.length > 0) {
                this.model = this.rebuildValue(val);
            }
        },
        model() {
            this.updateValue();
            this.emitChange();
        }
    }
}
</script>