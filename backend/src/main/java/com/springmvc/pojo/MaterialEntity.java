package com.springmvc.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class MaterialEntity {
    /**
     * 物料ID
     * 表字段 : material.material_id
     */
    private Integer materialId;

    /**
     * 物料编号
     * 表字段 : material.material_no
     */
    private String materialNo;

    /**
     * 物料名称
     * 表字段 : material.material_name
     */
    private String materialName;

    /**
     * 计量单位
     * 表字段 : material.unit
     */
    private String unit;

    /**
     * 类别ID
     * 表字段 : material.category_id
     */
    private Integer categoryId;

    /**
     * 规格
     * 表字段 : material.spec
     */
    private String spec;

    /**
     * 
     * 表字段 : material.cost
     */
    private BigDecimal cost;

    /**
     * 备注
     * 表字段 : material.remark
     */
    private String remark;

    /**
     * 创建时间
     * 表字段 : material.create_at
     */
    private Date createAt;

    /**
     * 创建者
     * 表字段 : material.create_by
     */
    private Integer createBy;

    /**
     * 更新时间
     * 表字段 : material.update_at
     */
    private Date updateAt;

    /**
     * 更新者
     * 表字段 : material.update_by
     */
    private Integer updateBy;

    /**
     * 获取 物料ID
     * 表字段 : material.material_id
     *
     * @return Integer
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * 设置 物料ID
     * 表字段 : material.material_id
     *
     * @param materialId
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

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
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置 
     * 表字段 : material.cost
     *
     * @param cost
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取 备注
     * 表字段 : material.remark
     *
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注
     * 表字段 : material.remark
     *
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取 创建时间
     * 表字段 : material.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 创建时间
     * 表字段 : material.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 创建者
     * 表字段 : material.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 创建者
     * 表字段 : material.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 更新时间
     * 表字段 : material.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 更新时间
     * 表字段 : material.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 更新者
     * 表字段 : material.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 更新者
     * 表字段 : material.update_by
     *
     * @param updateBy
     */
    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
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
        sb.append(", materialId=").append(materialId);
        sb.append(", materialNo=").append(materialNo);
        sb.append(", materialName=").append(materialName);
        sb.append(", unit=").append(unit);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", spec=").append(spec);
        sb.append(", cost=").append(cost);
        sb.append(", remark=").append(remark);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}