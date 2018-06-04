package com.springmvc.dto;

import com.springmvc.pojo.ProductOutstockBillEntity;

import java.util.List;

public class ProductOutstockBill extends ProductOutstockBillEntity {
    private String toPrincipalName;

    private String warehousePrincipalName;

    private String auditName;

    private String finishName;

    private List<ProductOutstockBillProduct> productIdList;

    public String getToPrincipalName() {
        return toPrincipalName;
    }

    public void setToPrincipalName(String toPrincipalName) {
        this.toPrincipalName = toPrincipalName;
    }

    public String getWarehousePrincipalName() {
        return warehousePrincipalName;
    }

    public void setWarehousePrincipalName(String warehousePrincipalName) {
        this.warehousePrincipalName = warehousePrincipalName;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getFinishName() {
        return finishName;
    }

    public void setFinishName(String finishName) {
        this.finishName = finishName;
    }

    public List<ProductOutstockBillProduct> getProductIdList() {
        return productIdList;
    }

    public void setProductIdList(List<ProductOutstockBillProduct> productIdList) {
        this.productIdList= productIdList;
    }
}