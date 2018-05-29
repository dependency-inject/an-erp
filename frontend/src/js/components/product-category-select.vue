<template>
    <i-select v-model="model" :disabled="disabled" transfer>
        <i-option 
            v-for="item in treeData" 
            :key="item.categoryId" 
            :value="item.categoryId"
        >
            {{ item.categoryName }}
        </i-option>
    </i-select>
</template>

<script>
import Emitter from '../mixins/emitter';
import productCategoryService from '../service/product-category'
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
            console.log(this.treeData);
        },
        async search() {
            let result = await productCategoryService.getAll();
            if (result.status === 200) {
                this.data = result.data;
                if (this.rootOption)
                    this.treeData = [ { categoryId: 0, categoryName: this.$t('component.ROOT_CATEGORY'), depth: 0 } ];
                else
                    this.treeData = [];
                this.makeTreeData(0);
            }
        },
        updateValue() {
            this.$emit('input', this.model);
            this.dispatch('FormItem', 'on-form-change');
        }
    },
    created() {
        this.search();
    },
    watch: {
        value(val) {
            this.model = val;
        },
        model() {
            this.updateValue();
        }
    }
}
</script>