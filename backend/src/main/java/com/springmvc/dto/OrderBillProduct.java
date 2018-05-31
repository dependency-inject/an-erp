package com.springmvc.dto;

import com.springmvc.pojo.OrderBillProductEntity;

public class OrderBillProduct extends OrderBillProductEntity {

    String productName;

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

}