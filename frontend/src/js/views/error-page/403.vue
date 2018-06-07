<template>
    <div :class="[prefixCls]">
        <div class="error403-body-con">
            <card>
                <div class="error403-body-con-title">4<span class="error403-0-span"><icon type="android-lock"></icon></span><span class="error403-key-span"><icon size="220" type="ios-bolt"></icon></span></div>
                <p class="error403-body-con-message">You don't have permission</p>
                <div class="error403-btn-con">
                    <i-button @click="goHome" size="large" style="width: 200px;" type="text">返回首页</i-button>
                    <i-button @click="backPage" size="large" style="width: 200px;margin-left: 40px;" type="primary">返回上一页</i-button>
                </div>
            </card>
        </div>
    </div>
</template>

<script>
export default {
    name: 'error-403',
    data() {
        return {
            prefixCls: 'di-error403'
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
