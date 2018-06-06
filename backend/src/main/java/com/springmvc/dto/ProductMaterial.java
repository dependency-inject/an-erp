package com.springmvc.dto;

import com.springmvc.pojo.ProductMaterialEntity;

public class ProductMaterial extends ProductMaterialEntity {
    /**
     * 货品物料列表：物料编号
     * 表字段 : material.material_no
     */
    private String materialNo;

    /**
     * 货品物料列表：物料名称
     * 表字段 : material.material_name
     */
    private String materialName;

    /**
     * 货品物料列表：计量单位
     * 表字段 : material.unit
     */
    private String unit;

    /**
     * 货品物料列表：类别ID
     * 表字段 : material.category_id
     */
    private Integer categoryId;

    /**
     * 货品物料列表：规格
     * 表字段 : material.spec
     */
    private String spec;

    /**
     * 获取 物料编号
     * 表字段 : material.material_no
     *
     * @return String
     */
    public String getMaterialNo() {
        return materialNo;
    }

    /**
     * 设置 物料编号
     * 表字段 : material.material_no
     *
     * @param materialNo
     */
    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo == null ? null : materialNo.trim();
    }

    /**
     * 获取 物料名称
     * 表字段 : material.material_name
     *
     * @return String
     */
    public String getMaterialName() {
        return materialName;
    }

    /**
     * 设置 物料名称
     * 表字段 : material.material_name
     *
     * @param materialName
     */
    public void setMaterialName(String materialName) {
        this.materialName = materialName == null ? null : materialName.trim();
    }

    /**
     * 获取 计量单位
     * 表字段 : material.unit
     *
     * @return String
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置 计量单位
     * 表字段 : material.unit
     *
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    /**
     * 获取 类别ID
     * 表字段 : material.category_id
     *
     * @return Integer
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * 设置 类别ID
     * 表字段 : material.category_id
     *
     * @param categoryId
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取 规格
     * 表字段 : material.spec
     *
     * @return String
     */
    public String getSpec() {
        return spec;
    }

    /**
     * 设置 规格
     * 表字段 : material.spec
     *
     * @param spec
     */
    public void setSpec(String spec) {
        this.spec = spec == null ? null : spec.trim();
    }

    /**
     * 获取
     * 表字段 : material.cost
     *
     * @return BigDecimal
     */
}