<template>
    <div id="main" style="width:100%;height:100%">
        <router-view></router-view>
    </div>
</template>

<script>
export default {
    data () {
        return {
            theme: this.$store.state.app.themeColor
        };
    },
    mounted () {

    },
    beforeDestroy () {

    },
    methods: {

    }
};
</script>
