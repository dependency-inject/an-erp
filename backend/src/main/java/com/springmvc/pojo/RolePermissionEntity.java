package com.springmvc.pojo;

public class RolePermissionEntity {
    /**
     * 
     * 表字段 : role_permission.role_permission_id
     */
    private Integer rolePermissionId;

    /**
     * 角色ID
     * 表字段 : role_permission.role_id
     */
    private Integer roleId;

    /**
     * 权限ID
     * 表字段 : role_permission.permission_id
     */
    private Integer permissionId;

    /**
     * 获取 
     * 表字段 : role_permission.role_permission_id
     *
     * @return Integer
     */
    public Integer getRolePermissionId() {
        return rolePermissionId;
    }

    /**
     * 设置 
     * 表字段 : role_permission.role_permission_id
     *
     * @param rolePermissionId
     */
    public void setRolePermissionId(Integer rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    /**
     * 获取 角色ID
     * 表字段 : role_permission.role_id
     *
     * @return Integer
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置 角色ID
     * 表字段 : role_permission.role_id
     *
     * @param roleId
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取 权限ID
     * 表字段 : role_permission.permission_id
     *
     * @return Integer
     */
    public Integer getPermissionId() {
        return permissionId;
    }

    /**
     * 设置 权限ID
     * 表字段 : role_permission.permission_id
     *
     * @param permissionId
     */
    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
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
        sb.append(", rolePermissionId=").append(rolePermissionId);
        sb.append(", roleId=").append(roleId);
        sb.append(", permissionId=").append(permissionId);
        sb.append("]");
        return sb.toString();
    }
}