package com.springmvc.pojo;

import java.util.Date;

public class AdminEntity {
    /**
     * 用户ID
     * 表字段 : admin.admin_id
     */
    private Integer adminId;

    /**
     * 登录名（唯一）
     * 表字段 : admin.login_name
     */
    private String loginName;

    /**
     * 密码（加密）
     * 表字段 : admin.password
     */
    private String password;

    /**
     * 真实姓名
     * 表字段 : admin.true_name
     */
    private String trueName;

    /**
     * 是否停用（0启用，1停用）
     * 表字段 : admin.closed
     */
    private Boolean closed;

    /**
     * 手机
     * 表字段 : admin.mobile
     */
    private String mobile;

    /**
     * 是否默认（默认用户不可删除）
     * 表字段 : admin.sys_default
     */
    private Boolean sysDefault;

    /**
     * 创建时间
     * 表字段 : admin.create_at
     */
    private Date createAt;

    /**
     * 创建者
     * 表字段 : admin.create_by
     */
    private Integer createBy;

    /**
     * 更新时间
     * 表字段 : admin.update_at
     */
    private Date updateAt;

    /**
     * 更新者
     * 表字段 : admin.update_by
     */
    private Integer updateBy;

    /**
     * 获取 用户ID
     * 表字段 : admin.admin_id
     *
     * @return Integer
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * 设置 用户ID
     * 表字段 : admin.admin_id
     *
     * @param adminId
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * 获取 登录名（唯一）
     * 表字段 : admin.login_name
     *
     * @return String
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置 登录名（唯一）
     * 表字段 : admin.login_name
     *
     * @param loginName
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    /**
     * 获取 密码（加密）
     * 表字段 : admin.password
     *
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置 密码（加密）
     * 表字段 : admin.password
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取 真实姓名
     * 表字段 : admin.true_name
     *
     * @return String
     */
    public String getTrueName() {
        return trueName;
    }

    /**
     * 设置 真实姓名
     * 表字段 : admin.true_name
     *
     * @param trueName
     */
    public void setTrueName(String trueName) {
        this.trueName = trueName == null ? null : trueName.trim();
    }

    /**
     * 获取 是否停用（0启用，1停用）
     * 表字段 : admin.closed
     *
     * @return Boolean
     */
    public Boolean getClosed() {
        return closed;
    }

    /**
     * 设置 是否停用（0启用，1停用）
     * 表字段 : admin.closed
     *
     * @param closed
     */
    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    /**
     * 获取 手机
     * 表字段 : admin.mobile
     *
     * @return String
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置 手机
     * 表字段 : admin.mobile
     *
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取 是否默认（默认用户不可删除）
     * 表字段 : admin.sys_default
     *
     * @return Boolean
     */
    public Boolean getSysDefault() {
        return sysDefault;
    }

    /**
     * 设置 是否默认（默认用户不可删除）
     * 表字段 : admin.sys_default
     *
     * @param sysDefault
     */
    public void setSysDefault(Boolean sysDefault) {
        this.sysDefault = sysDefault;
    }

    /**
     * 获取 创建时间
     * 表字段 : admin.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 创建时间
     * 表字段 : admin.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 创建者
     * 表字段 : admin.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 创建者
     * 表字段 : admin.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 更新时间
     * 表字段 : admin.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 更新时间
     * 表字段 : admin.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 更新者
     * 表字段 : admin.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 更新者
     * 表字段 : admin.update_by
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
        sb.append(", adminId=").append(adminId);
        sb.append(", loginName=").append(loginName);
        sb.append(", password=").append(password);
        sb.append(", trueName=").append(trueName);
        sb.append(", closed=").append(closed);
        sb.append(", mobile=").append(mobile);
        sb.append(", sysDefault=").append(sysDefault);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}