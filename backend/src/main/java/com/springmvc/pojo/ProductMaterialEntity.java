package com.springmvc.pojo;

public class ProductMaterialEntity {
    /**
     * 
     * 表字段 : product_material.product_material_id
     */
    private Integer productMaterialId;

    /**
     * 货品ID
     * 表字段 : product_material.product_id
     */
    private Integer productId;

    /**
     * 物料ID
     * 表字段 : product_material.material_id
     */
    private Integer materialId;

    /**
     * 数量
     * 表字段 : product_material.quantity
     */
    private Integer quantity;

    /**
     * 配料属性
     * 表字段 : product_material.material_property
     */
    private String materialProperty;

    /**
     * 备注
     * 表字段 : product_material.remark
     */
    private String remark;

    /**
     * 获取 
     * 表字段 : product_material.product_material_id
     *
     * @return Integer
     */
    public Integer getProductMaterialId() {
        return productMaterialId;
    }

    /**
     * 设置 
     * 表字段 : product_material.product_material_id
     *
     * @param productMaterialId
     */
    public void setProductMaterialId(Integer productMaterialId) {
        this.productMaterialId = productMaterialId;
    }

    /**
     * 获取 货品ID
     * 表字段 : product_material.product_id
     *
     * @return Integer
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置 货品ID
     * 表字段 : product_material.product_id
     *
     * @param productId
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * 获取 物料ID
     * 表字段 : product_material.material_id
     *
     * @return Integer
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * 设置 物料ID
     * 表字段 : product_material.material_id
     *
     * @param materialId
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    /**
     * 获取 数量
     * 表字段 : product_material.quantity
     *
     * @return Integer
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置 数量
     * 表字段 : product_material.quantity
     *
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取 配料属性
     * 表字段 : product_material.material_property
     *
     * @return String
     */
    public String getMaterialProperty() {
        return materialProperty;
    }

    /**
     * 设置 配料属性
     * 表字段 : product_material.material_property
     *
     * @param materialProperty
     */
    public void setMaterialProperty(String materialProperty) {
        this.materialProperty = materialProperty == null ? null : materialProperty.trim();
    }

    /**
     * 获取 备注
     * 表字段 : product_material.remark
     *
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注
     * 表字段 : product_material.remark
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
        sb.append(", productMaterialId=").append(productMaterialId);
        sb.append(", productId=").append(productId);
        sb.append(", materialId=").append(materialId);
        sb.append(", quantity=").append(quantity);
        sb.append(", materialProperty=").append(materialProperty);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}