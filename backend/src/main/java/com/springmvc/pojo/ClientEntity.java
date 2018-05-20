package com.springmvc.pojo;

import java.util.Date;

public class ClientEntity {
    /**
     * 客户ID
     * 表字段 : client.client_id
     */
    private Integer clientId;

    /**
     * 客户姓名
     * 表字段 : client.client_name
     */
    private String clientName;

    /**
     * 联系人
     * 表字段 : client.contact
     */
    private String contact;

    /**
     * 联系电话
     * 表字段 : client.contact_phone
     */
    private String contactPhone;

    /**
     * 创建时间
     * 表字段 : client.create_at
     */
    private Date createAt;

    /**
     * 创建者
     * 表字段 : client.create_by
     */
    private Integer createBy;

    /**
     * 更新时间
     * 表字段 : client.update_at
     */
    private Date updateAt;

    /**
     * 更新者
     * 表字段 : client.update_by
     */
    private Integer updateBy;

    /**
     * 获取 客户ID
     * 表字段 : client.client_id
     *
     * @return Integer
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * 设置 客户ID
     * 表字段 : client.client_id
     *
     * @param clientId
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * 获取 客户姓名
     * 表字段 : client.client_name
     *
     * @return String
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 设置 客户姓名
     * 表字段 : client.client_name
     *
     * @param clientName
     */
    public void setClientName(String clientName) {
        this.clientName = clientName == null ? null : clientName.trim();
    }

    /**
     * 获取 联系人
     * 表字段 : client.contact
     *
     * @return String
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置 联系人
     * 表字段 : client.contact
     *
     * @param contact
     */
    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    /**
     * 获取 联系电话
     * 表字段 : client.contact_phone
     *
     * @return String
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置 联系电话
     * 表字段 : client.contact_phone
     *
     * @param contactPhone
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    /**
     * 获取 创建时间
     * 表字段 : client.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 创建时间
     * 表字段 : client.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 创建者
     * 表字段 : client.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 创建者
     * 表字段 : client.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 更新时间
     * 表字段 : client.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 更新时间
     * 表字段 : client.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 更新者
     * 表字段 : client.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 更新者
     * 表字段 : client.update_by
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
        sb.append(", clientId=").append(clientId);
        sb.append(", clientName=").append(clientName);
        sb.append(", contact=").append(contact);
        sb.append(", contactPhone=").append(contactPhone);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}