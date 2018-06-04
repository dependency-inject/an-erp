<template>
    <i-select v-model="model" :disabled="disabled" transfer>
        <i-option v-for="item in treeData" :key="item.categoryId" :value="item.categoryId" :style="optionStyles(item)">{{ item.categoryName }}</i-option>
    </i-select>
</template>

<script>
import Emitter from '../mixins/emitter';

import materialCategoryService from '../service/material-category'

export default {
    mixins: [ Emitter ],
    data() {
        return {
            data: [],
            treeData: [],
            model: this.value
        }
    },
    props: {
        value: {
            type: [String, Number],
            required: true
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
        makeTreeData(parent) {
            this.data.forEach((row) => {
                if (row.parentId === parent) {
                    let obj = Object.assign({}, row);
                    if (parent === 0) {
                        if (this.rootOption)
                            obj.depth = 1;
                        else
                            obj.depth = 0;
                    } else {
                        this.treeData.forEach((item) => {
                            if (item.categoryId == parent) {
                                obj.depth = item.depth + 1;
                            }
                        });
                    }
                    this.treeData.push(obj);
                    this.makeTreeData(row.categoryId);
                }
            });
        },
        async search() {
            let result = await materialCategoryService.getAll();
            if (result.status === 200) {
                this.data = result.data;
                this.treeData = this.rootOption ? [ { categoryId: 0, categoryName: this.$t('component.ROOT_CATEGORY'), depth: 0 } ] : [];
                this.makeTreeData(0);
                this.treeData.forEach((item) => {
                    item.categoryId = item.categoryId;
                });
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
            let items = this.data.filter((item) => { return item.categoryId == this.model });
            this.$emit('on-change', items.length == 0? { categoryId: '', categoryName: '' }: items[0]);
        }
    },
    created() {
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