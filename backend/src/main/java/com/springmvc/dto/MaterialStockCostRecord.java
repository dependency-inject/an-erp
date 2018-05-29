package com.springmvc.dto;

import com.springmvc.pojo.MaterialEntity;

import java.math.BigDecimal;

public class MaterialStockCostRecord extends MaterialEntity {

    /**
     * 实际库存
     */
    private Integer totalAmount;

    /**
     * 库存成本
     */
    private BigDecimal totalCost;

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}