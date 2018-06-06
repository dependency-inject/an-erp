package com.springmvc.dto;

import com.springmvc.pojo.ProductOutstockBillProductEntity;

public class ProductOutstockBillProduct extends ProductOutstockBillProductEntity {

    private String productNo;

    private String productName;


    private String principalName;

    private String warehouseName;

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
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