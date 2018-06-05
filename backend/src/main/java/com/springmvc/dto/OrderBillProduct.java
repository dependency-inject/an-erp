package com.springmvc.dto;

import com.springmvc.pojo.OrderBillProductEntity;

public class OrderBillProduct extends OrderBillProductEntity {

    private String productNo;

    private String productName;

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}