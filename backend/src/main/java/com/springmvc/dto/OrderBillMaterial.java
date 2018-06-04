package com.springmvc.dto;

import com.springmvc.pojo.MaterialEntity;

public class OrderBillMaterial {

    private String materialNo;

    private String materialName;

    private Integer quantity;

    private String productNo;

    private String productName;

    private String materialProperty;

    private String productMaterialRemark;

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

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

    public String getMaterialProperty() {
        return materialProperty;
    }

    public void setMaterialProperty(String materialProperty) {
        this.materialProperty = materialProperty;
    }

    public String getProductMaterialRemark() {
        return productMaterialRemark;
    }

    public void setProductMaterialRemark(String productMaterialRemark) {
        this.productMaterialRemark = productMaterialRemark;
    }
}