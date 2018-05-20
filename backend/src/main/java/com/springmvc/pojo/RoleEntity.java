package com.springmvc.pojo;

import java.util.Date;

public class RoleEntity {
    /**
     * 角色ID
     * 表字段 : role.role_id
     */
    private Integer roleId;

    /**
     * 角色名称
     * 表字段 : role.role_name
     */
    private String roleName;

    /**
     * 是否默认（默认角色不可删除）
     * 表字段 : role.sys_default
     */
    private Boolean sysDefault;

    /**
     * 创建时间
     * 表字段 : role.create_at
     */
    private Date createAt;

    /**
     * 创建者
     * 表字段 : role.create_by
     */
    private Integer createBy;

    /**
     * 更新时间
     * 表字段 : role.update_at
     */
    private Date updateAt;

    /**
     * 更新者
     * 表字段 : role.update_by
     */
    private Integer updateBy;

    /**
     * 获取 角色ID
     * 表字段 : role.role_id
     *
     * @return Integer
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置 角色ID
     * 表字段 : role.role_id
     *
     * @param roleId
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取 角色名称
     * 表字段 : role.role_name
     *
     * @return String
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置 角色名称
     * 表字段 : role.role_name
     *
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 获取 是否默认（默认角色不可删除）
     * 表字段 : role.sys_default
     *
     * @return Boolean
     */
    public Boolean getSysDefault() {
        return sysDefault;
    }

    /**
     * 设置 是否默认（默认角色不可删除）
     * 表字段 : role.sys_default
     *
     * @param sysDefault
     */
    public void setSysDefault(Boolean sysDefault) {
        this.sysDefault = sysDefault;
    }

    /**
     * 获取 创建时间
     * 表字段 : role.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 创建时间
     * 表字段 : role.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 创建者
     * 表字段 : role.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 创建者
     * 表字段 : role.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 更新时间
     * 表字段 : role.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 更新时间
     * 表字段 : role.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 更新者
     * 表字段 : role.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 更新者
     * 表字段 : role.update_by
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
        sb.append(", roleId=").append(roleId);
        sb.append(", roleName=").append(roleName);
        sb.append(", sysDefault=").append(sysDefault);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}