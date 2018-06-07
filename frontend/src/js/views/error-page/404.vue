<template>
    <div :class="[prefixCls]">
        <div class="error404-body-con">
            <card>
                <div class="error404-body-con-title">4<span><icon type="ios-navigate-outline"></icon></span>4</div>
                <p class="error404-body-con-message">YOU&nbsp;&nbsp;LOOK&nbsp;&nbsp;LOST</p>
                <div class="error404-btn-con">
                    <i-button @click="goHome" size="large" style="width: 200px;" type="text">返回首页</i-button>
                    <i-button @click="backPage" size="large" style="width: 200px;margin-left: 40px;" type="primary">返回上一页</i-button>
                </div>
            </card>
        </div>
    </div>
</template>

<script>
export default {
    name: 'error-404',
    data() {
        return {
            prefixCls: 'di-error404'
        }
    },
    methods: {
        backPage () {
            this.$router.go(-1);
        },
        goHome () {
            this.$router.push({
                name: 'home'
            });
        }
    }
};
</script>
