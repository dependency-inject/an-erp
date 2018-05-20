package com.springmvc.pojo;

public class AdminRoleEntity {
    /**
     * 
     * 表字段 : admin_role.admin_role_id
     */
    private Integer adminRoleId;

    /**
     * 用户ID
     * 表字段 : admin_role.admin_id
     */
    private Integer adminId;

    /**
     * 角色ID
     * 表字段 : admin_role.role_id
     */
    private Integer roleId;

    /**
     * 获取 
     * 表字段 : admin_role.admin_role_id
     *
     * @return Integer
     */
    public Integer getAdminRoleId() {
        return adminRoleId;
    }

    /**
     * 设置 
     * 表字段 : admin_role.admin_role_id
     *
     * @param adminRoleId
     */
    public void setAdminRoleId(Integer adminRoleId) {
        this.adminRoleId = adminRoleId;
    }

    /**
     * 获取 用户ID
     * 表字段 : admin_role.admin_id
     *
     * @return Integer
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * 设置 用户ID
     * 表字段 : admin_role.admin_id
     *
     * @param adminId
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * 获取 角色ID
     * 表字段 : admin_role.role_id
     *
     * @return Integer
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置 角色ID
     * 表字段 : admin_role.role_id
     *
     * @param roleId
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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
        sb.append(", adminRoleId=").append(adminRoleId);
        sb.append(", adminId=").append(adminId);
        sb.append(", roleId=").append(roleId);
        sb.append("]");
        return sb.toString();
    }
}