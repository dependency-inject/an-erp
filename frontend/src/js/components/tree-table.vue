<template>
    <div :class="[prefixCls+'-wrapper']" :style="styles">
        <table :class="[prefixCls]">
            <colgroup>
                <col v-for="(column, index) in cloneColumns" :width="column.width">
                <col>
            </colgroup>
            <tbody>
                <tr v-for="item in treeData" v-if="item.show" @mouseenter.stop="item.active=true"
                @mouseleave.stop="item.active=false">
                    <td :class="[prefixCls + '-node']" :style="nodeStyles(item)"><span @click="toggle(item)"><icon type="folder"></icon><span class="sign-vertical" v-if="item.hasChild&&!item.open"></span><span class="sign-horizontal" v-if="item.hasChild"></span></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{ item[cloneColumns[0].key] }}</td>
                    <td v-for="column in filterFirst(cloneColumns)">{{ item[column.key] }}</td>
                    <td :class="operateClasses(item)"><span v-for="operate in operates" :class="{ 'disabled': operate.checkChild&&item.hasChild }" @click="handleClick(operate,item)"><icon :type="operate.icon"></icon> {{ operate.text }}</span></td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import util from '../libs/util';

const prefixCls = 'di-tree-table';

export default {
    name: 'treeTable',
    props: {
        data: {
            type: Array,
            default () {
                return [];
            }
        },
        columns: {
            type: Array,
            default () {
                return [];
            }
        },
        operates: {
            type: Array,
            default () {
                return [];
            }
        },
        width: {
            type: [Number, String]
        },
        height: {
            type: [Number, String]
        }
    },
    data () {
        return {
            prefixCls: prefixCls,
            treeData: [],
            cloneColumns: util.deepCopy(this.columns)
        }
    },
    computed: {
        styles () {
            let style = {};
            if (this.height) style.height = `${this.height}px`
            if (this.width) style.width = `${this.width}px`;
            return style;
        }
    },
    methods: {
        makeTreeData(parent) {
            this.data.forEach((row, index) => {
                if (row.parent === parent) {
                    let obj = util.deepCopy(row);
                    obj._index = index;
                    obj.open = false;
                    obj.hasChild = false;
                    obj.active = false;
                    if (parent === -1) {
                        obj.show = true;
                        obj.depth = 0;
                    } else {
                        obj.show = false;
                        this.treeData.forEach((item) => {
                            if (item._index == parent) {
                                obj.depth = item.depth + 1;
                                item.hasChild = true;
                            }
                        });
                    }
                    this.treeData.push(obj);
                    this.makeTreeData(index);
                }
            });
        },
        nodeStyles(row) {
            let style = {}
            let paddingLeft = row.depth*30 + 20;
            style.paddingLeft = `${paddingLeft}px`;
            return style;
        },
        operateClasses(row) {
            return [
                `${prefixCls}-operate`,
                {
                    [`active`]: row.active,
                }
            ];
        },
        filterFirst(columns) {
            return columns.filter((item, index) => { return index != 0 });
        },
        toggle(item) {
            item.open = !item.open;
            if (item.open) {
                this.showChild(item._index);
            } else {
                this.hideChild(item._index);
            }
        },
        showChild(index) {
            this.treeData.forEach((obj) => {
                if (obj.parent === index) {
                    obj.show = true;
                    if (obj.open)
                        this.showChild(obj._index);
                }
            });
        },
        hideChild(index) {
            this.treeData.forEach((obj) => {
                if (obj.parent === index) {
                    obj.show = false;
                    if (obj.open)
                        this.hideChild(obj._index);
                }
            });
        },
        handleClick(operate, item) {
            if (operate.checkChild && item.hasChild) return;
            this.$emit(operate.event, this.data[item._index]);
        }
    },
    mounted() {
        this.treeData = [];
        this.makeTreeData(-1);
    },
    watch: {
        data: {
            handler () {
                this.treeData = [];
                this.makeTreeData(-1);
            },
            deep: true
        },
        columns: {
            handler () {
                this.treeData = [];
                this.makeTreeData(-1);
            },
            deep: true
        }
    }
};
</script>
