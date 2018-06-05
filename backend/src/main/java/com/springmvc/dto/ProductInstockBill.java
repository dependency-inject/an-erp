package com.springmvc.dto;

import com.springmvc.pojo.ProductInstockBillEntity;

import java.util.List;

public class ProductInstockBill extends ProductInstockBillEntity {
    private String fromPrincipalName;

    private String warehousePrincipalName;

    private String auditName;

    private String finishName;

    private List<ProductInstockBillProduct> productList;

    public String getFromPrincipalName() {
        return fromPrincipalName;
    }

    public void setFromPrincipalName(String fromPrincipalName) {
        this.fromPrincipalName = fromPrincipalName;
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

    public List<ProductInstockBillProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductInstockBillProduct> productList) {
        this.productList = productList;
    }



}