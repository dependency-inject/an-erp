import Vue from 'vue';
import iView from 'iview';
import util from '../libs/util';
import VueRouter from 'vue-router';
import { routers, otherRouter, appRouter } from './router';

Vue.use(VueRouter);

// 路由配置
const RouterConfig = {
    // mode: 'history',
    routes: routers
};

export const router = new VueRouter(RouterConfig);

router.beforeEach((to, from, next) => {
    iView.LoadingBar.start();
    util.title(to.meta.title);
    const curRouterObj = util.getRouterObjByName([otherRouter, ...appRouter], to.name);
    if (curRouterObj && curRouterObj.access !== undefined) { // 需要判断权限的路由
        let permissions = router.app.$store.state.app.loginAdmin.permissionNameList.split(",");
        if (util.showThisRoute(permissions, curRouterObj.access)) {
            util.toDefaultPage([otherRouter, ...appRouter], to.name, router, next); // 如果在地址栏输入的是一级菜单则默认打开其第一个二级菜单的页面
        } else {
            next({
                replace: true,
                name: 'error-403'
            });
        }
    } else { // 没有配置权限的路由, 直接通过
        util.toDefaultPage([...routers], to.name, router, next);
    }
});

router.afterEach((to) => {
    util.openNewPage(router.app, to.name, to.params, to.query);
    iView.LoadingBar.finish();
    window.scrollTo(0, 0);
});
