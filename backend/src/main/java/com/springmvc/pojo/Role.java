package com.springmvc.pojo;

public class Role extends RoleEntity {

    // region selectWithRolePermissionByExample

    /**
     * 权限id列表
     * 表字段 : group_concat(role_permission.permission_id)
     */
    private String permissionIdList;

    /**
     * 获取 权限id列表
     * 表字段 : group_concat(role_permission.permission_id)
     *
     * @return String
     */
    public String getPermissionIdList() {
        return permissionIdList;
    }

    /**
     * 设置 权限id列表
     * 表字段 : group_concat(role_permission.permission_id)
     *
     * @param permissionIdList
     */
    public void setPermissionIdList(String permissionIdList) {
        this.permissionIdList = permissionIdList == null ? null : permissionIdList.trim();
    }

    // endregion
}