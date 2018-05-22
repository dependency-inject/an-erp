package com.springmvc.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class ProductEntity {
    /**
     * 货品ID
     * 表字段 : product.product_id
     */
    private Integer productId;

    /**
     * 货品编号
     * 表字段 : product.product_no
     */
    private String productNo;

    /**
     * 货品名称
     * 表字段 : product.product_name
     */
    private String productName;

    /**
     * 计量单位
     * 表字段 : product.unit
     */
    private String unit;

    /**
     * 类别ID
     * 表字段 : product.category_id
     */
    private Integer categoryId;

    /**
     * 规格
     * 表字段 : product.spec
     */
    private String spec;

    /**
     * 价格
     * 表字段 : product.price
     */
    private BigDecimal price;

    /**
     * 是否停用（0启用，1停用）
     * 表字段 : product.closed
     */
    private Byte closed;

    /**
     * 备注
     * 表字段 : product.remark
     */
    private String remark;

    /**
     * 创建时间
     * 表字段 : product.create_at
     */
    private Date createAt;

    /**
     * 创建者
     * 表字段 : product.create_by
     */
    private Integer createBy;

    /**
     * 更新时间
     * 表字段 : product.update_at
     */
    private Date updateAt;

    /**
     * 更新者
     * 表字段 : product.update_by
     */
    private Integer updateBy;

    /**
     * 获取 货品ID
     * 表字段 : product.product_id
     *
     * @return Integer
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置 货品ID
     * 表字段 : product.product_id
     *
     * @param productId
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * 获取 货品编号
     * 表字段 : product.product_no
     *
     * @return String
     */
    public String getProductNo() {
        return productNo;
    }

    /**
     * 设置 货品编号
     * 表字段 : product.product_no
     *
     * @param productNo
     */
    public void setProductNo(String productNo) {
        this.productNo = productNo == null ? null : productNo.trim();
    }

    /**
     * 获取 货品名称
     * 表字段 : product.product_name
     *
     * @return String
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置 货品名称
     * 表字段 : product.product_name
     *
     * @param productName
     */
    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    /**
     * 获取 计量单位
     * 表字段 : product.unit
     *
     * @return String
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置 计量单位
     * 表字段 : product.unit
     *
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    /**
     * 获取 类别ID
     * 表字段 : product.category_id
     *
     * @return Integer
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * 设置 类别ID
     * 表字段 : product.category_id
     *
     * @param categoryId
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取 规格
     * 表字段 : product.spec
     *
     * @return String
     */
    public String getSpec() {
        return spec;
    }

    /**
     * 设置 规格
     * 表字段 : product.spec
     *
     * @param spec
     */
    public void setSpec(String spec) {
        this.spec = spec == null ? null : spec.trim();
    }

    /**
     * 获取 价格
     * 表字段 : product.price
     *
     * @return BigDecimal
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置 价格
     * 表字段 : product.price
     *
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取 是否停用（0启用，1停用）
     * 表字段 : product.closed
     *
     * @return Byte
     */
    public Byte getClosed() {
        return closed;
    }

    /**
     * 设置 是否停用（0启用，1停用）
     * 表字段 : product.closed
     *
     * @param closed
     */
    public void setClosed(Byte closed) {
        this.closed = closed;
    }

    /**
     * 获取 备注
     * 表字段 : product.remark
     *
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注
     * 表字段 : product.remark
     *
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取 创建时间
     * 表字段 : product.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 创建时间
     * 表字段 : product.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 创建者
     * 表字段 : product.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 创建者
     * 表字段 : product.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 更新时间
     * 表字段 : product.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 更新时间
     * 表字段 : product.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 更新者
     * 表字段 : product.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 更新者
     * 表字段 : product.update_by
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
        sb.append(", productId=").append(productId);
        sb.append(", productNo=").append(productNo);
        sb.append(", productName=").append(productName);
        sb.append(", unit=").append(unit);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", spec=").append(spec);
        sb.append(", price=").append(price);
        sb.append(", closed=").append(closed);
        sb.append(", remark=").append(remark);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}