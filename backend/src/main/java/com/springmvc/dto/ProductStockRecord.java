package com.springmvc.dto;

import com.springmvc.pojo.ProductEntity;

public class ProductStockRecord extends ProductEntity {
    /**
     * 总数量，即当前已完成的 入库单 - 出库单
     */
    private int totalAmount;

    /**
     * 已经预定出库的数量，但没有结单
     */
    private int orderedAmount;

    /**
     * 真实可用的数量，总数量 - 已经预定出库的数量
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
