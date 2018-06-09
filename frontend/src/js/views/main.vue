<template>
    <div class="di-main" :class="{'main-hide-text': shrink }">
        <div class="sidebar-menu-con" :style="{width: shrink?'60px':'200px', overflow: shrink ? 'visible' : 'auto'}">
            <scroll-bar ref="scrollBar">
                <shrinkable-menu 
                    :shrink="shrink"
                    @on-change="handleSubmenuChange"
                    :theme="menuTheme" 
                    :before-push="beforePush"
                    :open-names="openedSubmenuArr"
                    :menu-list="menuList">
                    <div slot="top" class="logo-con">
                        <!-- <img v-show="!shrink"  src="../../images/logo.jpg" key="max-logo" />
                        <img v-show="shrink" src="../../images/logo-min.jpg" key="min-logo" /> -->
                    </div>
                </shrinkable-menu>
            </scroll-bar>
        </div>
        <div class="main-header-con" :style="{paddingLeft: shrink?'60px':'200px'}">
            <div class="main-header">
                <div class="navicon-con">
                    <i-button :style="{transform: 'rotateZ(' + (this.shrink ? '-90' : '0') + 'deg)'}" type="text" @click="toggleClick">
                        <icon type="navicon" size="32"></icon>
                    </i-button>
                </div>
                <div class="header-middle-con">
                    <div class="main-breadcrumb">
                        <breadcrumb-nav :currentPath="currentPath"></breadcrumb-nav>
                    </div>
                </div>
                <div class="header-avator-con">
                    <full-screen v-model="isFullScreen" @on-change="fullscreenChange"></full-screen>
                    <div class="user-dropdown-menu-con">
                        <row type="flex" justify="end" align="middle" class="user-dropdown-innercon">
                            <dropdown transfer trigger="click" @on-click="handleClickUserDropdown">
                                <a href="javascript:void(0)">
                                    <span class="main-user-name">{{ userName }}</span>
                                    <icon type="arrow-down-b"></icon>
                                </a>
                                <dropdown-menu slot="list">
                                    <dropdown-item name="changePassword">修改密码</dropdown-item>
                                    <dropdown-item name="logout" divided>退出登录</dropdown-item>
                                </dropdown-menu>
                            </dropdown>
                            <avatar :src="avatorPath" style="background:#619fe7;margin-left:10px"></avatar>
                        </row>
                    </div>
                </div>
            </div>
            <div class="tags-con">
                <tags-page-opened :pageTagsList="pageTagsList"></tags-page-opened>
            </div>
        </div>
        <div class="single-page-con" :style="{left: shrink?'60px':'200px'}">
            <div class="single-page" :class="[prefixCls]">
                <keep-alive :include="cachePage">
                    <router-view></router-view>
                </keep-alive>
            </div>
        </div>
        <modal ref="modal" v-model="modal.visible" :title="modal.title" :mask-closable="false" @on-ok="save" :loading="true">
            <i-form ref="formValidate" :model="modal.item" :rules="rules" :label-width="90">
                <form-item :label="$t('field.CHANGE_PASSWORD.OLD_PASSWORD')" prop="oldPassword"><i-input type="password" v-model="modal.item.oldPassword"></i-input></form-item>
                <form-item :label="$t('field.CHANGE_PASSWORD.NEW_PASSWORD')" prop="newPassword"><i-input type="password" v-model="modal.item.newPassword"></i-input></form-item>
                <form-item :label="$t('field.CHANGE_PASSWORD.CONFIRM_PASSWORD')" prop="confirmPassword"><i-input type="password" v-model="modal.item.confirmPassword"></i-input></form-item>
            </i-form>
        </modal>
    </div>
</template>
<script>
import shrinkableMenu from '../components/shrinkable-menu/shrinkable-menu.vue';
import tagsPageOpened from '../components/tags-page-opened.vue';
import breadcrumbNav from '../components/breadcrumb-nav.vue';
import fullScreen from '../components/fullscreen.vue';
import scrollBar from '../components/scroll-bar/scroll-bar.vue';

import util from '../libs/util.js';

import adminService from '../service/admin';

export default {
    components: {
        shrinkableMenu,
        tagsPageOpened,
        breadcrumbNav,
        fullScreen,
        scrollBar
    },
    data () {
        return {
            prefixCls: 'di-main-view',
            shrink: false,
            userName: '',
            isFullScreen: false,
            openedSubmenuArr: this.$store.state.app.openedSubmenuArr,
            modal: {
                title: 'title',
                item: {},
                visible: false
            }
        };
    },
    computed: {
        menuList () {
            return this.$store.state.app.menuList;
        },
        pageTagsList () {
            return this.$store.state.app.pageOpenedList; // 打开的页面的页面对象
        },
        currentPath () {
            return this.$store.state.app.currentPath; // 当前面包屑数组
        },
        avatorPath () {
            return localStorage.avatorImgPath;
        },
        cachePage () {
            return this.$store.state.app.cachePage;
        },
        lang () {
            return this.$store.state.app.lang;
        },
        menuTheme () {
            return this.$store.state.app.menuTheme;
        },
        rules() {
            return {
                oldPassword: [
                    { required: true, message: this.$t('field.CHANGE_PASSWORD.OLD_PASSWORD')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                newPassword: [
                    { required: true, message: this.$t('field.CHANGE_PASSWORD.NEW_PASSWORD')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ],
                confirmPassword: [
                    { required: true, message: this.$t('field.CHANGE_PASSWORD.CONFIRM_PASSWORD')+this.$t('field.NOT_BE_NULL'), trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        init () {
            let pathArr = util.setCurrentPath(this, this.$route.name);
            this.$store.commit('updateMenulist');
            if (pathArr.length >= 2) {
                this.$store.commit('addOpenSubmenu', pathArr[1].name);
            }
            this.userName = 'admin';
            this.checkTag(this.$route.name);
        },
        toggleClick () {
            this.shrink = !this.shrink;
        },
        handleClickUserDropdown (name) {
            if (name === 'changePassword') {
                // 修改密码
                this.changePassword();
            } else if (name === 'logout') {
                // 退出登录
                this.logout();
            }
        },
        checkTag (name) {
            let openpageHasTag = this.pageTagsList.some(item => {
                if (item.name === name) {
                    return true;
                }
            });
            if (!openpageHasTag) { //  解决关闭当前标签后再点击回退按钮会退到当前页时没有标签的问题
                util.openNewPage(this, name, this.$route.params || {}, this.$route.query || {});
            }
        },
        handleSubmenuChange (val) {
        },
        beforePush (name) {
            return true;
        },
        fullscreenChange (isFullScreen) {
        },
        scrollBarResize () {
            this.$refs.scrollBar.resize();
        },
        changePassword() {
            this.$refs.formValidate.resetFields();
            this.modal.title = this.$t('navigate.CHANGE_PASSWORD');
            this.modal.item.oldPassword = '';
            this.modal.item.newPassword = '';
            this.modal.item.confirmPassword = '';
            this.modal.visible = true;
        },
        async save() {
            this.$refs.formValidate.validate(async (valid) => {
                if (valid) {
                    let result = await adminService.changePassword(this.modal.item);
                    if (result.status === 200) {
                        this.$Message.success(this.$t('common.SAVE_SUCCESS'));
                        this.modal.visible = false;
                    } else {
                        this.$Message.error(result.data);
                        this.$refs.modal.buttonLoading = false;
                    }
                } else {
                    this.$Message.error(this.$t('common.VALIDATE_ERROR'));
                    this.$refs.modal.buttonLoading = false;
                }
            });
        },
        logout() {
            this.$Modal.confirm({
                content: this.$t('common.LOGOUT_CONFIRM'),
                onOk: () => {
                    let form = document.createElement('form');
                    form.setAttribute('style', 'display:none');
                    form.setAttribute('method', 'post');
                    form.setAttribute('action', '/logout');
                    // var input = document.createElement('input');
                    // input.setAttribute('type', 'hidden');
                    // input.setAttribute('name', '_token');
                    // input.setAttribute('value', window.Laravel.csrfToken);
                    // form.appendChild(input);
                    document.body.appendChild(form);
                    form.submit();
                }
            });
        }
    },
    watch: {
        '$route' (to) {
            this.$store.commit('setCurrentPageName', to.name);
            let pathArr = util.setCurrentPath(this, to.name);
            if (pathArr.length > 2) {
                this.$store.commit('addOpenSubmenu', pathArr[1].name);
            }
            this.checkTag(to.name);
            localStorage.currentPageName = to.name;
        },
        lang () {
            util.setCurrentPath(this, this.$route.name); // 在切换语言时用于刷新面包屑
        },
        openedSubmenuArr () {
            setTimeout(() => {
                this.scrollBarResize();
            }, 300);
        }
    },
    mounted () {
        this.init();
        window.addEventListener('resize', this.scrollBarResize);
    },
    created () {
        // 显示打开的页面的列表
        this.$store.commit('setOpenedList');
    },
    dispatch () {
        window.removeEventListener('resize', this.scrollBarResize);
    }
};
</script>
