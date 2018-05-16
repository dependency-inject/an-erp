package com.springmvc.pojo;

import java.math.BigDecimal;

public class OrderBillProductEntity {
    /**
     * 
     * 表字段 : order_bill_product.bill_product_id
     */
    private Integer billProductId;

    /**
     * 订单ID
     * 表字段 : order_bill_product.bill_id
     */
    private Integer billId;

    /**
     * 货品ID
     * 表字段 : order_bill_product.product_id
     */
    private Integer productId;

    /**
     * 数量
     * 表字段 : order_bill_product.quantity
     */
    private Integer quantity;

    /**
     * 单价
     * 表字段 : order_bill_product.price
     */
    private BigDecimal price;

    /**
     * 小计
     * 表字段 : order_bill_product.total
     */
    private BigDecimal total;

    /**
     * 备注
     * 表字段 : order_bill_product.remark
     */
    private String remark;

    /**
     * 获取 
     * 表字段 : order_bill_product.bill_product_id
     *
     * @return Integer
     */
    public Integer getBillProductId() {
        return billProductId;
    }

    /**
     * 设置 
     * 表字段 : order_bill_product.bill_product_id
     *
     * @param billProductId
     */
    public void setBillProductId(Integer billProductId) {
        this.billProductId = billProductId;
    }

    /**
     * 获取 订单ID
     * 表字段 : order_bill_product.bill_id
     *
     * @return Integer
     */
    public Integer getBillId() {
        return billId;
    }

    /**
     * 设置 订单ID
     * 表字段 : order_bill_product.bill_id
     *
     * @param billId
     */
    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    /**
     * 获取 货品ID
     * 表字段 : order_bill_product.product_id
     *
     * @return Integer
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置 货品ID
     * 表字段 : order_bill_product.product_id
     *
     * @param productId
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * 获取 数量
     * 表字段 : order_bill_product.quantity
     *
     * @return Integer
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置 数量
     * 表字段 : order_bill_product.quantity
     *
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取 单价
     * 表字段 : order_bill_product.price
     *
     * @return BigDecimal
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置 单价
     * 表字段 : order_bill_product.price
     *
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取 小计
     * 表字段 : order_bill_product.total
     *
     * @return BigDecimal
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * 设置 小计
     * 表字段 : order_bill_product.total
     *
     * @param total
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * 获取 备注
     * 表字段 : order_bill_product.remark
     *
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注
     * 表字段 : order_bill_product.remark
     *
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", billProductId=").append(billProductId);
        sb.append(", billId=").append(billId);
        sb.append(", productId=").append(productId);
        sb.append(", quantity=").append(quantity);
        sb.append(", price=").append(price);
        sb.append(", total=").append(total);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}