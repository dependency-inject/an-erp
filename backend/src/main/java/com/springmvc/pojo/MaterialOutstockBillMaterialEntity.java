package com.springmvc.pojo;

public class MaterialOutstockBillMaterialEntity {
    /**
     * 
     * 表字段 : material_outstock_bill_material.bill_material_id
     */
    private Integer billMaterialId;

    /**
     * 物料出库单ID
     * 表字段 : material_outstock_bill_material.bill_id
     */
    private Integer billId;

    /**
     * 物料ID
     * 表字段 : material_outstock_bill_material.material_id
     */
    private Integer materialId;

    /**
     * 数量
     * 表字段 : material_outstock_bill_material.quantity
     */
    private Integer quantity;

    /**
     * 盘点负责人
     * 表字段 : material_outstock_bill_material.principal
     */
    private Integer principal;

    /**
     * 仓库ID
     * 表字段 : material_outstock_bill_material.warehouse
     */
    private Integer warehouse;

    /**
     * 仓库内位置
     * 表字段 : material_outstock_bill_material.place
     */
    private String place;

    /**
     * 备注
     * 表字段 : material_outstock_bill_material.remark
     */
    private String remark;

    /**
     * 获取 
     * 表字段 : material_outstock_bill_material.bill_material_id
     *
     * @return Integer
     */
    public Integer getBillMaterialId() {
        return billMaterialId;
    }

    /**
     * 设置 
     * 表字段 : material_outstock_bill_material.bill_material_id
     *
     * @param billMaterialId
     */
    public void setBillMaterialId(Integer billMaterialId) {
        this.billMaterialId = billMaterialId;
    }

    /**
     * 获取 物料出库单ID
     * 表字段 : material_outstock_bill_material.bill_id
     *
     * @return Integer
     */
    public Integer getBillId() {
        return billId;
    }

    /**
     * 设置 物料出库单ID
     * 表字段 : material_outstock_bill_material.bill_id
     *
     * @param billId
     */
    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    /**
     * 获取 物料ID
     * 表字段 : material_outstock_bill_material.material_id
     *
     * @return Integer
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * 设置 物料ID
     * 表字段 : material_outstock_bill_material.material_id
     *
     * @param materialId
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    /**
     * 获取 数量
     * 表字段 : material_outstock_bill_material.quantity
     *
     * @return Integer
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置 数量
     * 表字段 : material_outstock_bill_material.quantity
     *
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取 盘点负责人
     * 表字段 : material_outstock_bill_material.principal
     *
     * @return Integer
     */
    public Integer getPrincipal() {
        return principal;
    }

    /**
     * 设置 盘点负责人
     * 表字段 : material_outstock_bill_material.principal
     *
     * @param principal
     */
    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }

    /**
     * 获取 仓库ID
     * 表字段 : material_outstock_bill_material.warehouse
     *
     * @return Integer
     */
    public Integer getWarehouse() {
        return warehouse;
    }

    /**
     * 设置 仓库ID
     * 表字段 : material_outstock_bill_material.warehouse
     *
     * @param warehouse
     */
    public void setWarehouse(Integer warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * 获取 仓库内位置
     * 表字段 : material_outstock_bill_material.place
     *
     * @return String
     */
    public String getPlace() {
        return place;
    }

    /**
     * 设置 仓库内位置
     * 表字段 : material_outstock_bill_material.place
     *
     * @param place
     */
    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
    }

    /**
     * 获取 备注
     * 表字段 : material_outstock_bill_material.remark
     *
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注
     * 表字段 : material_outstock_bill_material.remark
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
        sb.append(", billMaterialId=").append(billMaterialId);
        sb.append(", billId=").append(billId);
        sb.append(", materialId=").append(materialId);
        sb.append(", quantity=").append(quantity);
        sb.append(", principal=").append(principal);
        sb.append(", warehouse=").append(warehouse);
        sb.append(", place=").append(place);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}