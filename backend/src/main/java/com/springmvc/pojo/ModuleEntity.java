package com.springmvc.pojo;

public class ModuleEntity {
    /**
     * 模块ID
     * 表字段 : module.module_id
     */
    private Integer moduleId;

    /**
     * 模块名称
     * 表字段 : module.module_name
     */
    private String moduleName;

    /**
     * 排序值
     * 表字段 : module.sort
     */
    private Integer sort;

    /**
     * 获取 模块ID
     * 表字段 : module.module_id
     *
     * @return Integer
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * 设置 模块ID
     * 表字段 : module.module_id
     *
     * @param moduleId
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

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
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置 排序值
     * 表字段 : module.sort
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
        sb.append(", moduleId=").append(moduleId);
        sb.append(", moduleName=").append(moduleName);
        sb.append(", sort=").append(sort);
        sb.append("]");
        return sb.toString();
    }
}