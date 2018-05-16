package com.springmvc.pojo;

public class PermissionEntity {
    /**
     * 权限ID
     * 表字段 : permission.permission_id
     */
    private Integer permissionId;

    /**
     * 权限名称
     * 表字段 : permission.permission_name
     */
    private String permissionName;

    /**
     * 模块ID
     * 表字段 : permission.module_id
     */
    private Integer moduleId;

    /**
     * 排序值
     * 表字段 : permission.sort
     */
    private Integer sort;

    /**
     * 获取 权限ID
     * 表字段 : permission.permission_id
     *
     * @return Integer
     */
    public Integer getPermissionId() {
        return permissionId;
    }

    /**
     * 设置 权限ID
     * 表字段 : permission.permission_id
     *
     * @param permissionId
     */
    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    /**
     * 获取 权限名称
     * 表字段 : permission.permission_name
     *
     * @return String
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * 设置 权限名称
     * 表字段 : permission.permission_name
     *
     * @param permissionName
     */
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }

    /**
     * 获取 模块ID
     * 表字段 : permission.module_id
     *
     * @return Integer
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * 设置 模块ID
     * 表字段 : permission.module_id
     *
     * @param moduleId
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * 获取 排序值
     * 表字段 : permission.sort
     *
     * @return Integer
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置 排序值
     * 表字段 : permission.sort
     *
     * @param sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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
        sb.append(", permissionId=").append(permissionId);
        sb.append(", permissionName=").append(permissionName);
        sb.append(", moduleId=").append(moduleId);
        sb.append(", sort=").append(sort);
        sb.append("]");
        return sb.toString();
    }
}