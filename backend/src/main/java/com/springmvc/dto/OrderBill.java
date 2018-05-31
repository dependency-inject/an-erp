package com.springmvc.dto;

import com.springmvc.pojo.OrderBillEntity;

public class OrderBill extends OrderBillEntity {

    public String getSalesName() {
        return salesName;
    }

    public String getAuditName() {
        return auditName;
    }

    public String getProduceName() {
        return produceName;
    }

    public String getDeliverName() {
        return deliverName;
    }

    private String salesName;
    private String auditName;
    private String produceName;
    private String deliverName;
    public void setAuditName(String auditName) { this.auditName = auditName; }

    public void setProduceName(String produceName) { this.produceName = produceName; }

    public void setDeliverName(String deliverName) { this.deliverName = deliverName; }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

}