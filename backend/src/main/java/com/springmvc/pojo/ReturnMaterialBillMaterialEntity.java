package com.springmvc.pojo;

public class ReturnMaterialBillMaterialEntity {
    /**
     *
     * 表字段 : return_material_bill_material.bill_material_id
     */
    private Integer billMaterialId;

    /**
     * 退料单ID
     * 表字段 : return_material_bill_material.bill_id
     */
    private Integer billId;

    /**
     * 物料ID
     * 表字段 : return_material_bill_material.material_id
     */
    private Integer materialId;
    private String materialName;

    /**
     * 数量
     * 表字段 : return_material_bill_material.quantity
     */
    private Integer quantity;

    /**
     * 备注
     * 表字段 : return_material_bill_material.remark
     */
    private String remark;

    /**
     * 获取
     * 表字段 : return_material_bill_material.bill_material_id
     *
     * @return Integer
     */
    public Integer getBillMaterialId() {
        return billMaterialId;
    }

    /**
     * 设置
     * 表字段 : return_material_bill_material.bill_material_id
     *
     * @param billMaterialId
     */
    public void setBillMaterialId(Integer billMaterialId) {
        this.billMaterialId = billMaterialId;
    }

    /**
     * 获取 退料单ID
     * 表字段 : return_material_bill_material.bill_id
     *
     * @return Integer
     */
    public Integer getBillId() {
        return billId;
    }

    /**
     * 设置 退料单ID
     * 表字段 : return_material_bill_material.bill_id
     *
     * @param billId
     */
    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    /**
     * 获取 物料ID
     * 表字段 : return_material_bill_material.material_id
     *
     * @return Integer
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * 设置 物料ID
     * 表字段 : return_material_bill_material.material_id
     *
     * @param materialId
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }
    public void setMaterialName(String materialName) {
        this.materialName=materialName;
    }
    public String getMaterialName(){ return this.materialName;}


    /**
     * 获取 数量
     * 表字段 : return_material_bill_material.quantity
     *
     * @return Integer
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置 数量
     * 表字段 : return_material_bill_material.quantity
     *
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取 备注
     * 表字段 : return_material_bill_material.remark
     *
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注
     * 表字段 : return_material_bill_material.remark
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
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}