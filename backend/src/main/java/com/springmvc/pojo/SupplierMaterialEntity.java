package com.springmvc.pojo;

import java.math.BigDecimal;

public class SupplierMaterialEntity {
    /**
     * 
     * 表字段 : supplier_material.supplier_material_id
     */
    private Integer supplierMaterialId;

    /**
     * 供应商ID
     * 表字段 : supplier_material.supplier_id
     */
    private Integer supplierId;

    /**
     * 物料ID
     * 表字段 : supplier_material.material_id
     */
    private Integer materialId;

    /**
     * 价格
     * 表字段 : supplier_material.price
     */
    private BigDecimal price;

    /**
     * 备注
     * 表字段 : supplier_material.remark
     */
    private String remark;

    /**
     * 获取 
     * 表字段 : supplier_material.supplier_material_id
     *
     * @return Integer
     */
    public Integer getSupplierMaterialId() {
        return supplierMaterialId;
    }

    /**
     * 设置 
     * 表字段 : supplier_material.supplier_material_id
     *
     * @param supplierMaterialId
     */
    public void setSupplierMaterialId(Integer supplierMaterialId) {
        this.supplierMaterialId = supplierMaterialId;
    }

    /**
     * 获取 供应商ID
     * 表字段 : supplier_material.supplier_id
     *
     * @return Integer
     */
    public Integer getSupplierId() {
        return supplierId;
    }

    /**
     * 设置 供应商ID
     * 表字段 : supplier_material.supplier_id
     *
     * @param supplierId
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * 获取 物料ID
     * 表字段 : supplier_material.material_id
     *
     * @return Integer
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * 设置 物料ID
     * 表字段 : supplier_material.material_id
     *
     * @param materialId
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    /**
     * 获取 价格
     * 表字段 : supplier_material.price
     *
     * @return BigDecimal
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置 价格
     * 表字段 : supplier_material.price
     *
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取 备注
     * 表字段 : supplier_material.remark
     *
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注
     * 表字段 : supplier_material.remark
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
        sb.append(", supplierMaterialId=").append(supplierMaterialId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", materialId=").append(materialId);
        sb.append(", price=").append(price);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}