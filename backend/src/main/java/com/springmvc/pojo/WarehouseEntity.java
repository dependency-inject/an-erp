package com.springmvc.pojo;

import java.util.Date;

public class WarehouseEntity {
    /**
     * 仓库ID
     * 表字段 : warehouse.warehouse_id
     */
    private Integer warehouseId;

    /**
     * 仓库编号
     * 表字段 : warehouse.warehouse_no
     */
    private String warehouseNo;

    /**
     * 仓库名称
     * 表字段 : warehouse.warehouse_name
     */
    private String warehouseName;

    /**
     * 创建时间
     * 表字段 : warehouse.create_at
     */
    private Date createAt;

    /**
     * 创建者
     * 表字段 : warehouse.create_by
     */
    private Integer createBy;

    /**
     * 更新时间
     * 表字段 : warehouse.update_at
     */
    private Date updateAt;

    /**
     * 更新者
     * 表字段 : warehouse.update_by
     */
    private Integer updateBy;

    /**
     * 获取 仓库ID
     * 表字段 : warehouse.warehouse_id
     *
     * @return Integer
     */
    public Integer getWarehouseId() {
        return warehouseId;
    }

    /**
     * 设置 仓库ID
     * 表字段 : warehouse.warehouse_id
     *
     * @param warehouseId
     */
    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    /**
     * 获取 仓库编号
     * 表字段 : warehouse.warehouse_no
     *
     * @return String
     */
    public String getWarehouseNo() {
        return warehouseNo;
    }

    /**
     * 设置 仓库编号
     * 表字段 : warehouse.warehouse_no
     *
     * @param warehouseNo
     */
    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo == null ? null : warehouseNo.trim();
    }

    /**
     * 获取 仓库名称
     * 表字段 : warehouse.warehouse_name
     *
     * @return String
     */
    public String getWarehouseName() {
        return warehouseName;
    }

    /**
     * 设置 仓库名称
     * 表字段 : warehouse.warehouse_name
     *
     * @param warehouseName
     */
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName == null ? null : warehouseName.trim();
    }

    /**
     * 获取 创建时间
     * 表字段 : warehouse.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 创建时间
     * 表字段 : warehouse.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 创建者
     * 表字段 : warehouse.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 创建者
     * 表字段 : warehouse.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 更新时间
     * 表字段 : warehouse.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 更新时间
     * 表字段 : warehouse.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 更新者
     * 表字段 : warehouse.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 更新者
     * 表字段 : warehouse.update_by
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
        sb.append(", warehouseId=").append(warehouseId);
        sb.append(", warehouseNo=").append(warehouseNo);
        sb.append(", warehouseName=").append(warehouseName);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}