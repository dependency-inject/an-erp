package com.springmvc.dto;

import com.springmvc.pojo.ProductInstockBillProductEntity;

public class ProductInstockBillProduct extends ProductInstockBillProductEntity {
    private String productNo;

    private String productName;

    private String principalName;

    private String warehouseName;

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String toPrincipalName) {
        this.principalName = toPrincipalName;
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