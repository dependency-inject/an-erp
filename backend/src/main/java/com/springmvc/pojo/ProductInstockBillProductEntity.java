package com.springmvc.pojo;

public class ProductInstockBillProductEntity {
    /**
     * 
     * 表字段 : product_instock_bill_product.bill_product_id
     */
    private Integer billProductId;

    /**
     * 货品入库单ID
     * 表字段 : product_instock_bill_product.bill_id
     */
    private Integer billId;

    /**
     * 货品ID
     * 表字段 : product_instock_bill_product.product_id
     */
    private Integer productId;

    /**
     * 数量
     * 表字段 : product_instock_bill_product.quantity
     */
    private Integer quantity;

    /**
     * 盘点负责人
     * 表字段 : product_instock_bill_product.principal
     */
    private Integer principal;

    /**
     * 仓库ID
     * 表字段 : product_instock_bill_product.warehouse
     */
    private Integer warehouse;

    /**
     * 仓库内位置
     * 表字段 : product_instock_bill_product.place
     */
    private String place;

    /**
     * 备注
     * 表字段 : product_instock_bill_product.remark
     */
    private String remark;

    /**
     * 获取 
     * 表字段 : product_instock_bill_product.bill_product_id
     *
     * @return Integer
     */
    public Integer getBillProductId() {
        return billProductId;
    }

    /**
     * 设置 
     * 表字段 : product_instock_bill_product.bill_product_id
     *
     * @param billProductId
     */
    public void setBillProductId(Integer billProductId) {
        this.billProductId = billProductId;
    }

    /**
     * 获取 货品入库单ID
     * 表字段 : product_instock_bill_product.bill_id
     *
     * @return Integer
     */
    public Integer getBillId() {
        return billId;
    }

    /**
     * 设置 货品入库单ID
     * 表字段 : product_instock_bill_product.bill_id
     *
     * @param billId
     */
    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    /**
     * 获取 货品ID
     * 表字段 : product_instock_bill_product.product_id
     *
     * @return Integer
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置 货品ID
     * 表字段 : product_instock_bill_product.product_id
     *
     * @param productId
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * 获取 数量
     * 表字段 : product_instock_bill_product.quantity
     *
     * @return Integer
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置 数量
     * 表字段 : product_instock_bill_product.quantity
     *
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取 盘点负责人
     * 表字段 : product_instock_bill_product.principal
     *
     * @return Integer
     */
    public Integer getPrincipal() {
        return principal;
    }

    /**
     * 设置 盘点负责人
     * 表字段 : product_instock_bill_product.principal
     *
     * @param principal
     */
    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }

    /**
     * 获取 仓库ID
     * 表字段 : product_instock_bill_product.warehouse
     *
     * @return Integer
     */
    public Integer getWarehouse() {
        return warehouse;
    }

    /**
     * 设置 仓库ID
     * 表字段 : product_instock_bill_product.warehouse
     *
     * @param warehouse
     */
    public void setWarehouse(Integer warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * 获取 仓库内位置
     * 表字段 : product_instock_bill_product.place
     *
     * @return String
     */
    public String getPlace() {
        return place;
    }

    /**
     * 设置 仓库内位置
     * 表字段 : product_instock_bill_product.place
     *
     * @param place
     */
    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
    }

    /**
     * 获取 备注
     * 表字段 : product_instock_bill_product.remark
     *
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注
     * 表字段 : product_instock_bill_product.remark
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
        sb.append(", principal=").append(principal);
        sb.append(", warehouse=").append(warehouse);
        sb.append(", place=").append(place);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}