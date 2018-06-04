package com.springmvc.dto;

import com.springmvc.pojo.ProductOutstockBillProductEntity;

public class ProductOutstockBillProduct extends ProductOutstockBillProductEntity {

    private String productNo;

    private String productName;


    private String toPrincipalName;

    private String warehouseName;

    public String getToPrincipalName() {
        return toPrincipalName;
    }

    public void setToPrincipalName(String toPrincipalName) {
        this.toPrincipalName = toPrincipalName;
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