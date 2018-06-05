<template>
    <i-select v-model="model" :disabled="disabled" transfer>
        <i-option v-for="item in treeData" :key="item.hashCode" :value="item[option.value]" :style="optionStyles(item)">{{ item.labelValue }}</i-option>
    </i-select>
</template>

<script>
import Emitter from '../mixins/emitter';

import util from '../libs/util.js';

import productCategoryService from '../service/product-category';
import materialCategoryService from '../service/material-category';

export default {
    mixins: [ Emitter ],
    data() {
        return {
            data: [],
            treeData: [],
            model: '',
            option: {}
        }
    },
    props: {
        type: {
            validator (value) {
                return util.oneOf(value, ['product-category', 'material-category']);
            }
        },
        value: {
            type: [String, Number],
            required: true
        },
        queryParameters: {
            type: Object
        },
        rootOption: {
            type: Boolean,
            default: false
        },
        disabled: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        initOption() {
            if (this.type === 'product-category') {
                this.option = { service: productCategoryService, value: 'categoryId', parent: 'parentId', label: ['categoryName'] }
            } else if (this.type === 'material-category') {
                this.option = { service: materialCategoryService, value: 'categoryId', parent: 'parentId', label: ['categoryName'] }
            }
        },
        makeTreeData(parent) {
            this.data.forEach((row) => {
                if (row[this.option.parent] === parent) {
                    let obj = Object.assign({}, row);
                    if (parent === 0) {
                        if (this.rootOption)
                            obj.depth = 1;
                        else
                            obj.depth = 0;
                    } else {
                        this.treeData.forEach((item) => {
                            if (item[this.option.value] == parent) {
                                obj.depth = item.depth + 1;
                            }
                        });
                    }
                    this.treeData.push(obj);
                    this.makeTreeData(row[this.option.value]);
                }
            });
        },
        async search() {
            let result = await this.option.service.getList(this.queryParameters);
            if (result.status === 200) {
                this.data = result.data;
                this.data.forEach((item) => {
                    item.labelValue = item[this.option.label[0]];
                    for (let i = 1; i < this.option.label.length; ++i)
                        item.labelValue += (' - ' + item[this.option.label[i]]);
                    item.hashCode = util.hashCode(JSON.stringify(item));
                });
                this.treeData = [];
                if (this.rootOption) {
                    let obj = { labelValue: this.$t('component.ROOT_CATEGORY'), depth: 0 };
                    obj[this.option.value] = 0;
                    this.treeData.push(obj);
                }
                this.makeTreeData(0);
                this.emitChange();
                this.model = this.value;
            }
        },
        optionStyles(row) {
            let style = {}
            style.paddingLeft = (row.depth*20+7) + 'px';
            return style;
        },
        updateValue() {
            this.$emit('input', this.model);
            this.dispatch('FormItem', 'on-form-change');
        },
        emitChange() {
            let items = this.data.filter((item) => { return item[this.option.value] == this.model });
            this.$emit('on-change', items.length == 0? null: items[0]);
        }
    },
    created() {
        this.initOption();
        this.search();
    },
    watch: {
        value(val) {
            if (this.treeData.length > 0) {
                this.model = val;
            }
        },
        model() {
            this.updateValue();
            this.emitChange();
        }
    }
}
</script>