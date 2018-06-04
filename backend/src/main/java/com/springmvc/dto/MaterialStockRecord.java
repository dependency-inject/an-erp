package com.springmvc.dto;

import com.springmvc.pojo.MaterialEntity;

public class MaterialStockRecord extends MaterialEntity {
    /**
     * 已完成的入库 - 出库
     */
    private int totalAmount;

    /**
     * 未完成的出库
     */
    private int orderedAmount;

    /**
     * 以上两值相减，真实可用的数量
     */
    private int leftAmount;

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getOrderedAmount() {
        return orderedAmount;
    }

    public void setOrderedAmount(int orderedAmount) {
        this.orderedAmount = orderedAmount;
    }

    public int getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(int leftAmount) {
        this.leftAmount = leftAmount;
    }
}
