package com.springmvc.dto;

import com.springmvc.pojo.AdminEntity;

public class Admin extends AdminEntity {

    // region selectWithRoleByExample

    /**
     * 角色id列表
     * 表字段 : group_concat(admin_role.role_id)
     */
    private String roleIdList;

    /**
     * 角色名称列表
     * 表字段 : group_concat(role.role_name)
     */
    private String roleNameList;

    /**
     * 获取 角色id列表
     * 表字段 : group_concat(admin_role.role_id)
     *
     * @return String
     */
    public String getRoleIdList() {
        return roleIdList;
    }

    /**
     * 设置 角色id列表
     * 表字段 : group_concat(admin_role.role_id)
     *
     * @param roleIdList
     */
    public void setRoleIdList(String roleIdList) {
        this.roleIdList = roleIdList == null ? null : roleIdList.trim();
    }

    /**
     * 获取 角色名称列表
     * 表字段 : group_concat(role.role_name)
     *
     * @return String
     */
    public String getRoleNameList() {
        return roleNameList;
    }

    /**
     * 设置 角色名称列表
     * 表字段 : group_concat(role.role_name)
     *
     * @param roleNameList
     */
    public void setRoleNameList(String roleNameList) {
        this.roleNameList = roleNameList == null ? null : roleNameList.trim();
    }

    // endregion

    // AdminService: getAdminWithPermissionById

    /**
     * 权限id列表
     */
    private String permissionNameList;

    /**
     * 获取 权限id列表
     *
     * @return String
     */
    public String getPermissionNameList() {
        return permissionNameList;
    }

    /**
     * 设置 权限id列表
     *
     * @param permissionNameList
     */
    public void setPermissionNameList(String permissionNameList) {
        this.permissionNameList = permissionNameList == null ? null : permissionNameList.trim();
    }

    // endregion

}