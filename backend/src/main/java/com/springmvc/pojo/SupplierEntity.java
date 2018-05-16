package com.springmvc.pojo;

import java.util.Date;

public class SupplierEntity {
    /**
     * 供应商ID
     * 表字段 : supplier.supplier_id
     */
    private Integer supplierId;

    /**
     * 供应商名称
     * 表字段 : supplier.supplier_name
     */
    private String supplierName;

    /**
     * 联系人
     * 表字段 : supplier.contact
     */
    private String contact;

    /**
     * 联系电话
     * 表字段 : supplier.contact_phone
     */
    private String contactPhone;

    /**
     * 地区
     * 表字段 : supplier.region
     */
    private String region;

    /**
     * 地址
     * 表字段 : supplier.address
     */
    private String address;

    /**
     * 创建时间
     * 表字段 : supplier.create_at
     */
    private Date createAt;

    /**
     * 创建者
     * 表字段 : supplier.create_by
     */
    private Integer createBy;

    /**
     * 更新时间
     * 表字段 : supplier.update_at
     */
    private Date updateAt;

    /**
     * 更新者
     * 表字段 : supplier.update_by
     */
    private Integer updateBy;

    /**
     * 获取 供应商ID
     * 表字段 : supplier.supplier_id
     *
     * @return Integer
     */
    public Integer getSupplierId() {
        return supplierId;
    }

    /**
     * 设置 供应商ID
     * 表字段 : supplier.supplier_id
     *
     * @param supplierId
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * 获取 供应商名称
     * 表字段 : supplier.supplier_name
     *
     * @return String
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * 设置 供应商名称
     * 表字段 : supplier.supplier_name
     *
     * @param supplierName
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    /**
     * 获取 联系人
     * 表字段 : supplier.contact
     *
     * @return String
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置 联系人
     * 表字段 : supplier.contact
     *
     * @param contact
     */
    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    /**
     * 获取 联系电话
     * 表字段 : supplier.contact_phone
     *
     * @return String
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置 联系电话
     * 表字段 : supplier.contact_phone
     *
     * @param contactPhone
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    /**
     * 获取 地区
     * 表字段 : supplier.region
     *
     * @return String
     */
    public String getRegion() {
        return region;
    }

    /**
     * 设置 地区
     * 表字段 : supplier.region
     *
     * @param region
     */
    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    /**
     * 获取 地址
     * 表字段 : supplier.address
     *
     * @return String
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置 地址
     * 表字段 : supplier.address
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取 创建时间
     * 表字段 : supplier.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 创建时间
     * 表字段 : supplier.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 创建者
     * 表字段 : supplier.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 创建者
     * 表字段 : supplier.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 更新时间
     * 表字段 : supplier.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 更新时间
     * 表字段 : supplier.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 更新者
     * 表字段 : supplier.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 更新者
     * 表字段 : supplier.update_by
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
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierName=").append(supplierName);
        sb.append(", contact=").append(contact);
        sb.append(", contactPhone=").append(contactPhone);
        sb.append(", region=").append(region);
        sb.append(", address=").append(address);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}