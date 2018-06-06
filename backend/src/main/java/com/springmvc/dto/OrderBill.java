package com.springmvc.dto;

import com.springmvc.pojo.OrderBillEntity;

import java.util.List;

public class OrderBill extends OrderBillEntity {

    private String salesName;

    private String clientName;

    private String auditName;

    private String produceName;

    private String deliveryName;

    private List<OrderBillProduct> productList;

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getProduceName() {
        return produceName;
    }

    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public List<OrderBillProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<OrderBillProduct> productList) {
        this.productList = productList;
    }
}