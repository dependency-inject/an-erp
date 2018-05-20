package com.springmvc.pojo;

import java.util.Date;

public class LogEntity {
    /**
     * 
     * 表字段 : log.log_id
     */
    private Integer logId;

    /**
     * 
     * 表字段 : log.admin_id
     */
    private Integer adminId;

    /**
     * 
     * 表字段 : log.log_type
     */
    private String logType;

    /**
     * 
     * 表字段 : log.operate
     */
    private String operate;

    /**
     * 
     * 表字段 : log.log_time
     */
    private Date logTime;

    /**
     * 
     * 表字段 : log.description
     */
    private String description;

    /**
     * 
     * 表字段 : log.ip
     */
    private String ip;

    /**
     * 
     * 表字段 : log.create_at
     */
    private Date createAt;

    /**
     * 
     * 表字段 : log.create_by
     */
    private Integer createBy;

    /**
     * 
     * 表字段 : log.sql
     */
    private String sql;

    /**
     * 获取 
     * 表字段 : log.log_id
     *
     * @return Integer
     */
    public Integer getLogId() {
        return logId;
    }

    /**
     * 设置 
     * 表字段 : log.log_id
     *
     * @param logId
     */
    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    /**
     * 获取 
     * 表字段 : log.admin_id
     *
     * @return Integer
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * 设置 
     * 表字段 : log.admin_id
     *
     * @param adminId
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * 获取 
     * 表字段 : log.log_type
     *
     * @return String
     */
    public String getLogType() {
        return logType;
    }

    /**
     * 设置 
     * 表字段 : log.log_type
     *
     * @param logType
     */
    public void setLogType(String logType) {
        this.logType = logType == null ? null : logType.trim();
    }

    /**
     * 获取 
     * 表字段 : log.operate
     *
     * @return String
     */
    public String getOperate() {
        return operate;
    }

    /**
     * 设置 
     * 表字段 : log.operate
     *
     * @param operate
     */
    public void setOperate(String operate) {
        this.operate = operate == null ? null : operate.trim();
    }

    /**
     * 获取 
     * 表字段 : log.log_time
     *
     * @return Date
     */
    public Date getLogTime() {
        return logTime;
    }

    /**
     * 设置 
     * 表字段 : log.log_time
     *
     * @param logTime
     */
    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    /**
     * 获取 
     * 表字段 : log.description
     *
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置 
     * 表字段 : log.description
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取 
     * 表字段 : log.ip
     *
     * @return String
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置 
     * 表字段 : log.ip
     *
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * 获取 
     * 表字段 : log.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 
     * 表字段 : log.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 
     * 表字段 : log.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 
     * 表字段 : log.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 
     * 表字段 : log.sql
     *
     * @return String
     */
    public String getSql() {
        return sql;
    }

    /**
     * 设置 
     * 表字段 : log.sql
     *
     * @param sql
     */
    public void setSql(String sql) {
        this.sql = sql == null ? null : sql.trim();
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
        sb.append(", logId=").append(logId);
        sb.append(", adminId=").append(adminId);
        sb.append(", logType=").append(logType);
        sb.append(", operate=").append(operate);
        sb.append(", logTime=").append(logTime);
        sb.append(", description=").append(description);
        sb.append(", ip=").append(ip);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", sql=").append(sql);
        sb.append("]");
        return sb.toString();
    }
}