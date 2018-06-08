import main from '../views/main.vue';

// 不作为main组件的子页面展示的页面单独写，如下
export const page404 = {
    path: '/*',
    meta: {
        title: { i18n: 'navigate.ERROR_404' }
    },
    name: 'error-404',
    component: require('../views/error-page/404.vue')
};

export const page403 = {
    path: '/403',
    meta: {
        title: { i18n: 'navigate.ERROR_403' }
    },
    name: 'error-403',
    component: require('../views/error-page/403.vue')
};

export const page500 = {
    path: '/500',
    meta: {
        title: { i18n: 'navigate.ERROR_500' }
    },
    name: 'error-500',
    component: require('../views/error-page/500.vue')
};

// 作为main组件的子页面展示但是不在左侧菜单显示的路由写在otherRouter里
export const otherRouter = {
    path: '/',
    name: 'otherRouter',
    redirect: '/home',
    component: main,
    children: [
        { path: 'home', title: { i18n: 'navigate.HOME' }, name: 'home', component: require('../views/home/home.vue') },

        // BOM管理
        { path: 'product/:id', title: { i18n: 'navigate.PRODUCT' }, name: 'product-detail', access: 'PRODUCT@SEARCH', component: require('../views/bom-manage/product-detail.vue') },
        { path: 'material/:id', title: { i18n: 'navigate.MATERIAL' }, name: 'material-detail', access: 'MATERIAL@SEARCH', component: require('../views/bom-manage/material-detail.vue') },
        
        // 销售管理
        { path: 'order/:id', title: { i18n: 'navigate.ORDER' }, name: 'order-detail', access: 'ORDER@SEARCH', component: require('../views/sales-manage/order-detail.vue') },
        
        // 生产管理
        { path: 'production-draw/:id', title: { i18n: 'navigate.DRAW_MATERIAL' }, name: 'production-draw-detail', access: 'PRODUCTION@DRAW_SEARCH', component: require('../views/production-manage/draw-material-detail.vue') },
        { path: 'production-return/:id', title: { i18n: 'navigate.RETURN_MATERIAL' }, name: 'production-return-detail', access: 'PRODUCTION@RETURN_SEARCH', component: require('../views/production-manage/return-material-detail.vue') },
        
        // 库存管理
        { path: 'product-instock/:id', title: { i18n: 'navigate.PRODUCT_INSTOCK' }, name: 'product-instock-detail', access: 'PRODUCT_STOCK@INSTOCK_SEARCH', component: require('../views/stock-manage/product-instock-detail.vue') },
        { path: 'product-outstock/:id', title: { i18n: 'navigate.PRODUCT_OUTSTOCK' }, name: 'product-outstock-detail', access: 'PRODUCT_STOCK@OUTSTOCK_SEARCH', component: require('../views/stock-manage/product-outstock-detail.vue') },
        { path: 'material-instock/:id', title: { i18n: 'navigate.MATERIAL_INSTOCK' }, name: 'material-instock-detail', access: 'MATERIAL_STOCK@INSTOCK_SEARCH', component: require('../views/stock-manage/material-instock-detail.vue') },
        { path: 'material-outstock/:id', title: { i18n: 'navigate.MATERIAL_OUTSTOCK' }, name: 'material-outstock-detail', access: 'MATERIAL_STOCK@OUTSTOCK_SEARCH', component: require('../views/stock-manage/material-outstock-detail.vue') },
        
        // 采购辅助管理
        { path: 'supplier/:id', title: { i18n: 'navigate.SUPPLIER' }, name: 'supplier-detail', access: 'PURCHASE@SUPPLIER_SEARCH', component: require('../views/purchase-manage/supplier-detail.vue') },
        { path: 'supplier-material/:id', title: { i18n: 'navigate.SUPPLIER_MATERIAL' }, name: 'supplier-material-list', access: 'PURCHASE@SUPPLIER_SEARCH', component: require('../views/purchase-manage/supplier-material-list.vue') },
        
        // 研发辅助管理
        { path: 'development-draw/:id', title: { i18n: 'navigate.DRAW_MATERIAL' }, name: 'development-draw-detail', access: 'DEVELOPMENT@DRAW_SEARCH', component: require('../views/development-manage/draw-material-detail.vue') },
        { path: 'development-return/:id', title: { i18n: 'navigate.RETURN_MATERIAL' }, name: 'development-return-detail', access: 'DEVELOPMENT@RETURN_SEARCH', component: require('../views/development-manage/return-material-detail.vue') },
        
        // 人员管理
        { path: 'admin/:id', title: { i18n: 'navigate.ADMIN' }, name: 'admin-detail', access: 'ADMIN@SEARCH', component: require('../views/user-manage/admin-detail.vue') },
    ]
};

// 作为main组件的子页面展示并且在左侧菜单显示的路由写在appRouter里
export const appRouter = [
    { // BOM管理
        path: '',
        icon: 'disc',
        name: 'bom-manage',
        title: { i18n: 'navigate.BOM_MANAGE' },
        component: main,
        children: [
            { path: 'product', title: { i18n: 'navigate.PRODUCTS' }, name: 'product-list', access: 'PRODUCT@SEARCH', component: require('../views/bom-manage/product-list.vue') },
            { path: 'product-category', title: { i18n: 'navigate.PRODUCT_CATEGORYS' }, name: 'product-category-list', access: 'PRODUCT@CATEGORY', component: require('../views/bom-manage/product-category-list.vue') },
            { path: 'material', title: { i18n: 'navigate.MATERIALS' }, name: 'material-list', access: 'MATERIAL@SEARCH', component: require('../views/bom-manage/material-list.vue') },
            { path: 'material-category', title: { i18n: 'navigate.MATERIAL_CATEGORYS' }, name: 'material-category-list', access: 'MATERIAL@CATEGORY', component: require('../views/bom-manage/material-category-list.vue') },
        ]
    },
    { // 销售管理
        path: '',
        icon: 'document-text',
        name: 'sales-manage',
        title: { i18n: 'navigate.SALES_MANAGE' },
        component: main,
        children: [
            { path: 'order', title: { i18n: 'navigate.ORDERS' }, name: 'order-list', access: 'ORDER@SEARCH', component: require('../views/sales-manage/order-list.vue') },
            { path: 'client', title: { i18n: 'navigate.CLIENTS' }, name: 'client-list', access: 'CLIENT@SEARCH', component: require('../views/sales-manage/client-list.vue') },
        ]
    },
    { // 生产管理
        path: '',
        icon: 'settings',
        name: 'production-manage',
        title: { i18n: 'navigate.PRODUCTION_MANAGE' },
        component: main,
        children: [
            { path: 'production-draw', title: { i18n: 'navigate.DRAW_MATERIALS' }, name: 'production-draw-list', access: 'PRODUCTION@DRAW_SEARCH', component: require('../views/production-manage/draw-material-list.vue') },
            { path: 'production-return', title: { i18n: 'navigate.RETURN_MATERIALS' }, name: 'production-return-list', access: 'PRODUCTION@RETURN_SEARCH', component: require('../views/production-manage/return-material-list.vue') },
        ]
    },
    { // 库存管理
        path: '',
        icon: 'home',
        name: 'stock-manage',
        title: { i18n: 'navigate.STOCK_MANAGE' },
        component: main,
        children: [
            { path: 'product-instock', title: { i18n: 'navigate.PRODUCT_INSTOCKS' }, name: 'product-instock-list', access: 'PRODUCT_STOCK@INSTOCK_SEARCH', component: require('../views/stock-manage/product-instock-list.vue') },
            { path: 'product-outstock', title: { i18n: 'navigate.PRODUCT_OUTSTOCKS' }, name: 'product-outstock-list', access: 'PRODUCT_STOCK@OUTSTOCK_SEARCH', component: require('../views/stock-manage/product-outstock-list.vue') },
            { path: 'product-stock', title: { i18n: 'navigate.PRODUCT_STOCK' }, name: 'product-stock', access: 'PRODUCT_STOCK@STOCK', component: require('../views/stock-manage/product-stock.vue') },
            { path: 'material-instock', title: { i18n: 'navigate.MATERIAL_INSTOCKS' }, name: 'material-instock-list', access: 'MATERIAL_STOCK@INSTOCK_SEARCH', component: require('../views/stock-manage/material-instock-list.vue') },
            { path: 'material-outstock', title: { i18n: 'navigate.MATERIAL_OUTSTOCKS' }, name: 'material-outstock-list', access: 'MATERIAL_STOCK@OUTSTOCK_SEARCH', component: require('../views/stock-manage/material-outstock-list.vue') },
            { path: 'material-stock', title: { i18n: 'navigate.MATERIAL_STOCK' }, name: 'material-stock', access: 'MATERIAL_STOCK@STOCK', component: require('../views/stock-manage/material-stock.vue') },
            { path: 'warehouse', title: { i18n: 'navigate.WAREHOUSES' }, name: 'warehouse-list', access: 'WAREHOUSE@SEARCH', component: require('../views/stock-manage/warehouse-list.vue') },
        ]
    },
    { // 采购辅助管理
        path: '',
        icon: 'bag',
        name: 'purchase-manage',
        title: { i18n: 'navigate.PURCHASE_MANAGE' },
        component: main,
        children: [
            { path: 'supplier', title: { i18n: 'navigate.SUPPLIERS' }, name: 'supplier-list', access: 'PURCHASE@SUPPLIER_SEARCH', component: require('../views/purchase-manage/supplier-list.vue') },
            { path: 'lack-material', title: { i18n: 'navigate.LACK_MATERIAL' }, name: 'lack-material', access: 'PURCHASE@LACK_MATERIAL', component: require('../views/purchase-manage/lack-material.vue') },
        ]
    },
    { // 成本管理
        path: '',
        icon: 'pie-graph',
        name: 'cost-manage',
        title: { i18n: 'navigate.COST_MANAGE' },
        component: main,
        children: [
            { path: 'material-cost', title: { i18n: 'navigate.MATERIAL_COSTS' }, name: 'material-cost-list', access: 'COST@MATERIAL_COST', component: require('../views/cost-manage/material-cost-list.vue') },
            { path: 'stock-cost', title: { i18n: 'navigate.STOCK_COST' }, name: 'stock-cost', access: 'COST@STOCK_COST', component: require('../views/cost-manage/stock-cost.vue') },
        ]
    },
    { // 研发辅助管理
        path: '',
        icon: 'android-search',
        name: 'development-manage',
        title: { i18n: 'navigate.DEVELOPMENT_MANAGE' },
        component: main,
        children: [
            { path: 'development-draw', title: { i18n: 'navigate.DRAW_MATERIALS' }, name: 'development-draw-list', access: 'DEVELOPMENT@DRAW_SEARCH', component: require('../views/development-manage/draw-material-list.vue') },
            { path: 'development-return', title: { i18n: 'navigate.RETURN_MATERIALS' }, name: 'development-return-list', access: 'DEVELOPMENT@RETURN_SEARCH', component: require('../views/development-manage/return-material-list.vue') },
        ]
    },
    { // 人员管理
        path: '',
        icon: 'ios-people',
        name: 'user-manage',
        title: { i18n: 'navigate.USER_MANAGE' },
        component: main,
        children: [
            { path: 'admin', title: { i18n: 'navigate.ADMINS' }, name: 'admin-list', access: 'ADMIN@SEARCH', component: require('../views/user-manage/admin-list.vue') },
            { path: 'role', title: { i18n: 'navigate.ROLES' }, name: 'role-list', access: 'ADMIN@ROLE', component: require('../views/user-manage/role-list.vue') },
        ]
    }
];

// 所有上面定义的路由都要写在下面的routers里
export const routers = [
    otherRouter,
    ...appRouter,
    page500,
    page403,
    page404
];
