package com.springmvc.pojo;

import java.util.Date;

public class MaterialCategoryEntity {
    /**
     * 物料类别ID
     * 表字段 : material_category.category_id
     */
    private Integer categoryId;

    /**
     * 类别名称
     * 表字段 : material_category.category_name
     */
    private String categoryName;

    /**
     * 父类ID（呈现树形类别）
     * 表字段 : material_category.parent_id
     */
    private Integer parentId;

    /**
     * 排序值
     * 表字段 : material_category.sort
     */
    private Integer sort;

    /**
     * 创建时间
     * 表字段 : material_category.create_at
     */
    private Date createAt;

    /**
     * 创建者
     * 表字段 : material_category.create_by
     */
    private Integer createBy;

    /**
     * 更新时间
     * 表字段 : material_category.update_at
     */
    private Date updateAt;

    /**
     * 更新者
     * 表字段 : material_category.update_by
     */
    private Integer updateBy;

    /**
     * 获取 物料类别ID
     * 表字段 : material_category.category_id
     *
     * @return Integer
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * 设置 物料类别ID
     * 表字段 : material_category.category_id
     *
     * @param categoryId
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取 类别名称
     * 表字段 : material_category.category_name
     *
     * @return String
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置 类别名称
     * 表字段 : material_category.category_name
     *
     * @param categoryName
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    /**
     * 获取 父类ID（呈现树形类别）
     * 表字段 : material_category.parent_id
     *
     * @return Integer
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置 父类ID（呈现树形类别）
     * 表字段 : material_category.parent_id
     *
     * @param parentId
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取 排序值
     * 表字段 : material_category.sort
     *
     * @return Integer
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置 排序值
     * 表字段 : material_category.sort
     *
     * @param sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取 创建时间
     * 表字段 : material_category.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 创建时间
     * 表字段 : material_category.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 创建者
     * 表字段 : material_category.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 创建者
     * 表字段 : material_category.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 更新时间
     * 表字段 : material_category.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 更新时间
     * 表字段 : material_category.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 更新者
     * 表字段 : material_category.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 更新者
     * 表字段 : material_category.update_by
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
        sb.append(", categoryId=").append(categoryId);
        sb.append(", categoryName=").append(categoryName);
        sb.append(", parentId=").append(parentId);
        sb.append(", sort=").append(sort);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}