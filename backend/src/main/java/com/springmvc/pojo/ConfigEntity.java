package com.springmvc.pojo;

import java.util.Date;

public class ConfigEntity {
    /**
     * 
     * 表字段 : config.config_id
     */
    private Integer configId;

    /**
     * 
     * 表字段 : config.config_name
     */
    private String configName;

    /**
     * 
     * 表字段 : config.config_type
     */
    private Integer configType;

    /**
     * 
     * 表字段 : config.config_value
     */
    private String configValue;

    /**
     * 
     * 表字段 : config.sort
     */
    private Integer sort;

    /**
     * 
     * 表字段 : config.update_at
     */
    private Date updateAt;

    /**
     * 
     * 表字段 : config.update_by
     */
    private Integer updateBy;

    /**
     * 获取 
     * 表字段 : config.config_id
     *
     * @return Integer
     */
    public Integer getConfigId() {
        return configId;
    }

    /**
     * 设置 
     * 表字段 : config.config_id
     *
     * @param configId
     */
    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    /**
     * 获取 
     * 表字段 : config.config_name
     *
     * @return String
     */
    public String getConfigName() {
        return configName;
    }

    /**
     * 设置 
     * 表字段 : config.config_name
     *
     * @param configName
     */
    public void setConfigName(String configName) {
        this.configName = configName == null ? null : configName.trim();
    }

    /**
     * 获取 
     * 表字段 : config.config_type
     *
     * @return Integer
     */
    public Integer getConfigType() {
        return configType;
    }

    /**
     * 设置 
     * 表字段 : config.config_type
     *
     * @param configType
     */
    public void setConfigType(Integer configType) {
        this.configType = configType;
    }

    /**
     * 获取 
     * 表字段 : config.config_value
     *
     * @return String
     */
    public String getConfigValue() {
        return configValue;
    }

    /**
     * 设置 
     * 表字段 : config.config_value
     *
     * @param configValue
     */
    public void setConfigValue(String configValue) {
        this.configValue = configValue == null ? null : configValue.trim();
    }

    /**
     * 获取 
     * 表字段 : config.sort
     *
     * @return Integer
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置 
     * 表字段 : config.sort
     *
     * @param sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取 
     * 表字段 : config.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 
     * 表字段 : config.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 
     * 表字段 : config.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 
     * 表字段 : config.update_by
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
        sb.append(", configId=").append(configId);
        sb.append(", configName=").append(configName);
        sb.append(", configType=").append(configType);
        sb.append(", configValue=").append(configValue);
        sb.append(", sort=").append(sort);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}