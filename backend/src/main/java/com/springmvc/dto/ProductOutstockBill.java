package com.springmvc.dto;

import com.springmvc.pojo.ProductOutstockBillEntity;

import java.util.List;

public class ProductOutstockBill extends ProductOutstockBillEntity {
    private String toPrincipalName;

    private String warehousePrincipalName;

    private String relatedBillNo;

    private String auditName;

    private String finishName;

    private List<ProductOutstockBillProduct> productList;

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

    public String getRelatedBillNo() {
        return relatedBillNo;
    }

    public void setRelatedBillNo(String relatedBillNo) {
        this.relatedBillNo = relatedBillNo;
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

    public List<ProductOutstockBillProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductOutstockBillProduct> productList) {
        this.productList = productList;
    }
}