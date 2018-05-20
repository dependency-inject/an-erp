<template>
    <div :class="[prefixCls]">
        <div class="error500-body-con">
            <card>
                <div class="error500-body-con-title">
                    5<span class="error500-0-span"><icon type="social-freebsd-devil"></icon></span><span class="error500-0-span"><icon type="social-freebsd-devil"></icon></span>
                </div>
                <p class="error500-body-con-message">Oops! the server is wrong</p>
                <div class="error500-btn-con">
                    <i-button @click="goHome" size="large" style="width: 200px;" type="text">返回首页</i-button>
                    <i-button @click="backPage" size="large" style="width: 200px;margin-left: 40px;" type="primary">返回上一页</i-button>
                </div>
            </card>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Error500',
    data() {
        return {
            prefixCls: 'di-error500'
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
