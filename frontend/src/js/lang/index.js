import Vue from 'vue';

import en_US from './en-US';
import zh_CN from './zh-CN';
import enLocale from 'iview/src/locale/lang/en-US';
import zhLocale from 'iview/src/locale/lang/zh-CN';

// 自动设置语言
const navLang = navigator.language;
const localLang = (navLang === 'zh-CN' || navLang === 'en-US') ? navLang : false;
const lang = window.localStorage.lang || localLang || 'zh-CN';

Vue.config.lang = lang;

// 多语言配置
const mergeEN = Object.assign(enLocale, en_US);
const mergeZH = Object.assign(zhLocale, zh_CN);

export default {
    'en-US': mergeEN,
    'zh-CN': mergeZH
}
