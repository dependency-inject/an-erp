package com.springmvc.dto;

import com.springmvc.pojo.PermissionEntity;

public class Permission extends PermissionEntity {

    // region selectWithModuleByExample

    /**
     * 模块名称
     * 表字段 : module.module_name
     */
    private String moduleName;

    /**
     * 模块排序值
     * 表字段 : module.sort
     */
    private Integer moduleSort;

    /**
     * 获取 模块名称
     * 表字段 : module.module_name
     *
     * @return String
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * 设置 模块名称
     * 表字段 : module.module_name
     *
     * @param moduleName
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    /**
     * 获取 排序值
     * 表字段 : module.sort
     *
     * @return Integer
     */
    public Integer getModuleSort() {
        return moduleSort;
    }

    /**
     * 设置 排序值
     * 表字段 : module.sort
     *
     * @param moduleSort
     */
    public void setModuleSort(Integer moduleSort) {
        this.moduleSort = moduleSort;
    }

    // endregion

}