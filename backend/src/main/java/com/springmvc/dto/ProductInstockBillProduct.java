package com.springmvc.dto;

import com.springmvc.pojo.ProductInstockBillProductEntity;

public class ProductInstockBillProduct extends ProductInstockBillProductEntity {
    private String productNo;

    private String productName;

    private String fromPrincipalName;

    private String warehouseName;

    public String getFromPrincipalName() {
        return fromPrincipalName;
    }

    public void setFromPrincipalName(String toPrincipalName) {
        this.fromPrincipalName = toPrincipalName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehousePrincipalName) {
        this.warehouseName = warehousePrincipalName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String materialNo) {
        this.productNo = materialNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}