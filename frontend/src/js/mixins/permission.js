import { mapState, mapMutations } from 'vuex'

function hasPermission(permissions, permission) {
    return (','+permissions+',').indexOf(','+permission+',') > -1;
}

export default {
    computed: {
        ...mapState({
            permissions: state => state.app.loginAdmin? state.app.loginAdmin.permissionNameList: ''
        }),

        // 货品
        productSearchPermission() {
            return hasPermission(this.permissions, 'PRODUCT@SEARCH');
        },
        productAddPermission() {
            return hasPermission(this.permissions, 'PRODUCT@ADD');
        },
        productUpdatePermission() {
            return hasPermission(this.permissions, 'PRODUCT@UPDATE');
        },
        productRemovePermission() {
            return hasPermission(this.permissions, 'PRODUCT@REMOVE');
        },
        productCategoryPermission() {
            return hasPermission(this.permissions, 'PRODUCT@CATEGORY');
        },

        // 物料
        materialSearchPermission() {
            return hasPermission(this.permissions, 'MATERIAL@SEARCH');
        },
        materialAddPermission() {
            return hasPermission(this.permissions, 'MATERIAL@ADD');
        },
        materialUpdatePermission() {
            return hasPermission(this.permissions, 'MATERIAL@UPDATE');
        },
        materialRemovePermission() {
            return hasPermission(this.permissions, 'MATERIAL@REMOVE');
        },
        materialCategoryPermission() {
            return hasPermission(this.permissions, 'MATERIAL@CATEGORY');
        },

        // 订单
        orderSearchPermission() {
            return hasPermission(this.permissions, 'ORDER@SEARCH');
        },
        orderAddPermission() {
            return hasPermission(this.permissions, 'ORDER@ADD');
        },
        orderUpdatePermission() {
            return hasPermission(this.permissions, 'ORDER@UPDATE');
        },
        orderRemovePermission() {
            return hasPermission(this.permissions, 'ORDER@REMOVE');
        },
        orderAuditPermission() {
            return hasPermission(this.permissions, 'ORDER@AUDIT');
        },
        orderProducePermission() {
            return hasPermission(this.permissions, 'ORDER@PRODUCE');
        },
        orderDeliveryPermission() {
            return hasPermission(this.permissions, 'ORDER@DELIVERY');
        },
        orderCancelPermission() {
            return hasPermission(this.permissions, 'ORDER@CANCEL');
        },

        // 客户
        clientSearchPermission() {
            return hasPermission(this.permissions, 'CLIENT@SEARCH');
        },
        clientAddPermission() {
            return hasPermission(this.permissions, 'CLIENT@ADD');
        },
        clientUpdatePermission() {
            return hasPermission(this.permissions, 'CLIENT@UPDATE');
        },
        clientRemovePermission() {
            return hasPermission(this.permissions, 'CLIENT@REMOVE');
        },

        // 生产
        productionDrawSearchPermission() {
            return hasPermission(this.permissions, 'PRODUCTION@DRAW_SEARCH');
        },
        productionDrawAddPermission() {
            return hasPermission(this.permissions, 'PRODUCTION@DRAW_ADD');
        },
        productionDrawUpdatePermission() {
            return hasPermission(this.permissions, 'PRODUCTION@DRAW_UPDATE');
        },
        productionDrawRemovePermission() {
            return hasPermission(this.permissions, 'PRODUCTION@DRAW_REMOVE');
        },
        productionDrawAuditPermission() {
            return hasPermission(this.permissions, 'PRODUCTION@DRAW_AUDIT');
        },
        productionReturnSearchPermission() {
            return hasPermission(this.permissions, 'PRODUCTION@RETURN_SEARCH');
        },
        productionReturnAddPermission() {
            return hasPermission(this.permissions, 'PRODUCTION@RETURN_ADD');
        },
        productionReturnUpdatePermission() {
            return hasPermission(this.permissions, 'PRODUCTION@RETURN_UPDATE');
        },
        productionReturnRemovePermission() {
            return hasPermission(this.permissions, 'PRODUCTION@RETURN_REMOVE');
        },
        productionReturnAuditPermission() {
            return hasPermission(this.permissions, 'PRODUCTION@RETURN_AUDIT');
        },

        // 货品库存
        productInstockSearchPermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@INSTOCK_SEARCH');
        },
        productInstockAddPermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@INSTOCK_ADD');
        },
        productInstockUpdatePermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@INSTOCK_UPDATE');
        },
        productInstockRemovePermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@INSTOCK_REMOVE');
        },
        productInstockAuditPermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@INSTOCK_AUDIT');
        },
        productInstockFinishPermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@INSTOCK_FINISH');
        },
        productOutstockSearchPermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@OUTSTOCK_SEARCH');
        },
        productOutstockAddPermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@OUTSTOCK_ADD');
        },
        productOutstockUpdatePermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@OUTSTOCK_UPDATE');
        },
        productOutstockRemovePermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@OUTSTOCK_REMOVE');
        },
        productOutstockAuditPermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@OUTSTOCK_AUDIT');
        },
        productOutstockFinishPermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@OUTSTOCK_FINISH');
        },
        productStockPermission() {
            return hasPermission(this.permissions, 'PRODUCT_STOCK@STOCK');
        },

        // 物料库存
        materialInstockSearchPermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@INSTOCK_SEARCH');
        },
        materialInstockAddPermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@INSTOCK_ADD');
        },
        materialInstockUpdatePermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@INSTOCK_UPDATE');
        },
        materialInstockRemovePermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@INSTOCK_REMOVE');
        },
        materialInstockAuditPermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@INSTOCK_AUDIT');
        },
        materialInstockFinishPermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@INSTOCK_FINISH');
        },
        materialOutstockSearchPermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@OUTSTOCK_SEARCH');
        },
        materialOutstockAddPermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@OUTSTOCK_ADD');
        },
        materialOutstockUpdatePermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@OUTSTOCK_UPDATE');
        },
        materialOutstockRemovePermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@OUTSTOCK_REMOVE');
        },
        materialOutstockAuditPermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@OUTSTOCK_AUDIT');
        },
        materialOutstockFinishPermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@OUTSTOCK_FINISH');
        },
        materialStockPermission() {
            return hasPermission(this.permissions, 'MATERIAL_STOCK@STOCK');
        },

        // 仓库
        warehouseSearchPermission() {
            return hasPermission(this.permissions, 'WAREHOUSE@SEARCH');
        },
        warehouseAddPermission() {
            return hasPermission(this.permissions, 'WAREHOUSE@ADD');
        },
        warehouseUpdatePermission() {
            return hasPermission(this.permissions, 'WAREHOUSE@UPDATE');
        },
        warehouseRemovePermission() {
            return hasPermission(this.permissions, 'WAREHOUSE@REMOVE');
        },

        // 采购
        supplierSearchPermission() {
            return hasPermission(this.permissions, 'PURCHASE@SUPPLIER_SEARCH');
        },
        supplierAddPermission() {
            return hasPermission(this.permissions, 'PURCHASE@SUPPLIER_ADD');
        },
        supplierUpdatePermission() {
            return hasPermission(this.permissions, 'PURCHASE@SUPPLIER_UPDATE');
        },
        supplierRemovePermission() {
            return hasPermission(this.permissions, 'PURCHASE@SUPPLIER_REMOVE');
        },
        lackMaterialPermission() {
            return hasPermission(this.permissions, 'PURCHASE@LACK_MATERIAL');
        },

        // 成本
        materialCostPermission() {
            return hasPermission(this.permissions, 'COST@MATERIAL_COST');
        },
        stockCostPermission() {
            return hasPermission(this.permissions, 'COST@STOCK_COST');
        },

        // 研发
        developmentDrawSearchPermission() {
            return hasPermission(this.permissions, 'DEVELOPMENT@DRAW_SEARCH');
        },
        developmentDrawAddPermission() {
            return hasPermission(this.permissions, 'DEVELOPMENT@DRAW_ADD');
        },
        developmentDrawUpdatePermission() {
            return hasPermission(this.permissions, 'DEVELOPMENT@DRAW_UPDATE');
        },
        developmentDrawRemovePermission() {
            return hasPermission(this.permissions, 'DEVELOPMENT@DRAW_REMOVE');
        },
        developmentDrawAuditPermission() {
            return hasPermission(this.permissions, 'DEVELOPMENT@DRAW_AUDIT');
        },
        developmentReturnSearchPermission() {
            return hasPermission(this.permissions, 'DEVELOPMENT@RETURN_SEARCH');
        },
        developmentReturnAddPermission() {
            return hasPermission(this.permissions, 'DEVELOPMENT@RETURN_ADD');
        },
        developmentReturnUpdatePermission() {
            return hasPermission(this.permissions, 'DEVELOPMENT@RETURN_UPDATE');
        },
        developmentReturnRemovePermission() {
            return hasPermission(this.permissions, 'DEVELOPMENT@RETURN_REMOVE');
        },
        developmentReturnAuditPermission() {
            return hasPermission(this.permissions, 'DEVELOPMENT@RETURN_AUDIT');
        },

        // 用户
        adminSearchPermission() {
            return hasPermission(this.permissions, 'ADMIN@SEARCH');
        },
        adminAddPermission() {
            return hasPermission(this.permissions, 'ADMIN@ADD');
        },
        adminUpdatePermission() {
            return hasPermission(this.permissions, 'ADMIN@UPDATE');
        },
        adminRemovePermission() {
            return hasPermission(this.permissions, 'ADMIN@REMOVE');
        },
        rolePermission() {
            return hasPermission(this.permissions, 'ADMIN@ROLE');
        }
    }
};
