<template>
    <breadcrumb>
        <breadcrumb-item 
            v-for="item in currentPath" 
            :href="item.path" 
            :key="item.name"
        >{{ itemTitle(item) }}</breadcrumb-item>
    </breadcrumb>
</template>

<script>
export default {
    name: 'breadcrumbNav',
    props: {
        currentPath: Array
    },
    methods: {
        itemTitle (item) {
            if (typeof item.title === 'object') {
                return this.$t(item.title.i18n);
            } else {
                return item.title;
            }
        }
    }
};
</script>

