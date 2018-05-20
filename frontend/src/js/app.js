
/**
 * First we will load all of this project's JavaScript dependencies which
 * includes Vue and other libraries. It is a great starting point when
 * building robust, powerful web applications using Vue.
 */

require('./bootstrap');

import Vue from 'vue';
import VueI18n from 'vue-i18n';
import iView from 'iview';

import store from './store';
import { router } from './router/index';
import { appRouter } from './router/router';
import locales from './lang';

import App from './App.vue';

Vue.use(VueI18n);
Vue.use(iView);

const i18n = new VueI18n({
    locale: Vue.config.lang,
    messages: locales
});

new Vue({
	el: '#app',
	router,
	store,
    i18n,
	render: h => h(App),
    data: {
        currentPageName: ''
    },
    mounted () {
        this.currentPageName = this.$route.name;
        // 显示打开的页面的列表
        this.$store.commit('setOpenedList');
        this.$store.commit('initCachepage');
        // 权限菜单过滤相关
        this.$store.commit('updateMenulist');
    },
    created () {
        let tagsList = [];
        appRouter.map((item) => {
            if (item.children.length <= 1) {
                tagsList.push(item.children[0]);
            } else {
                tagsList.push(...item.children);
            }
        });
        this.$store.commit('setTagsList', tagsList);
    }
});
