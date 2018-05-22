package com.springmvc.pojo;

import java.util.Date;

public class ProductInstockBillEntity {
    /**
     * 货品入库单ID
     * 表字段 : product_instock_bill.bill_id
     */
    private Integer billId;

    /**
     * 货品入库单编号
     * 表字段 : product_instock_bill.bill_no
     */
    private String billNo;

    /**
     * 交货人
     * 表字段 : product_instock_bill.from_principal
     */
    private Integer fromPrincipal;

    /**
     * 仓库负责人
     * 表字段 : product_instock_bill.warehouse_principal
     */
    private Integer warehousePrincipal;

    /**
     * 货品入库单创建时间
     * 表字段 : product_instock_bill.bill_time
     */
    private Date billTime;

    /**
     * 货品来源（1生产入库）
     * 表字段 : product_instock_bill.product_source
     */
    private Integer productSource;

    /**
     * 预留字段
     * 表字段 : product_instock_bill.related_bill
     */
    private Integer relatedBill;

    /**
     * 货品入库单状态（1待审核，2已审核，3已完成）
     * 表字段 : product_instock_bill.bill_state
     */
    private Integer billState;

    /**
     * 备注
     * 表字段 : product_instock_bill.remark
     */
    private String remark;

    /**
     * 审核时间
     * 表字段 : product_instock_bill.audit_at
     */
    private Date auditAt;

    /**
     * 审核者
     * 表字段 : product_instock_bill.audit_by
     */
    private Integer auditBy;

    /**
     * 完成时间
     * 表字段 : product_instock_bill.finish_at
     */
    private Date finishAt;

    /**
     * 完成者
     * 表字段 : product_instock_bill.finish_by
     */
    private Integer finishBy;

    /**
     * 创建时间
     * 表字段 : product_instock_bill.create_at
     */
    private Date createAt;

    /**
     * 创建者
     * 表字段 : product_instock_bill.create_by
     */
    private Integer createBy;

    /**
     * 更新时间
     * 表字段 : product_instock_bill.update_at
     */
    private Date updateAt;

    /**
     * 更新者
     * 表字段 : product_instock_bill.update_by
     */
    private Integer updateBy;

    /**
     * 获取 货品入库单ID
     * 表字段 : product_instock_bill.bill_id
     *
     * @return Integer
     */
    public Integer getBillId() {
        return billId;
    }

    /**
     * 设置 货品入库单ID
     * 表字段 : product_instock_bill.bill_id
     *
     * @param billId
     */
    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    /**
     * 获取 货品入库单编号
     * 表字段 : product_instock_bill.bill_no
     *
     * @return String
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * 设置 货品入库单编号
     * 表字段 : product_instock_bill.bill_no
     *
     * @param billNo
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    /**
     * 获取 交货人
     * 表字段 : product_instock_bill.from_principal
     *
     * @return Integer
     */
    public Integer getFromPrincipal() {
        return fromPrincipal;
    }

    /**
     * 设置 交货人
     * 表字段 : product_instock_bill.from_principal
     *
     * @param fromPrincipal
     */
    public void setFromPrincipal(Integer fromPrincipal) {
        this.fromPrincipal = fromPrincipal;
    }

    /**
     * 获取 仓库负责人
     * 表字段 : product_instock_bill.warehouse_principal
     *
     * @return Integer
     */
    public Integer getWarehousePrincipal() {
        return warehousePrincipal;
    }

    /**
     * 设置 仓库负责人
     * 表字段 : product_instock_bill.warehouse_principal
     *
     * @param warehousePrincipal
     */
    public void setWarehousePrincipal(Integer warehousePrincipal) {
        this.warehousePrincipal = warehousePrincipal;
    }

    /**
     * 获取 货品入库单创建时间
     * 表字段 : product_instock_bill.bill_time
     *
     * @return Date
     */
    public Date getBillTime() {
        return billTime;
    }

    /**
     * 设置 货品入库单创建时间
     * 表字段 : product_instock_bill.bill_time
     *
     * @param billTime
     */
    public void setBillTime(Date billTime) {
        this.billTime = billTime;
    }

    /**
     * 获取 货品来源（1生产入库）
     * 表字段 : product_instock_bill.product_source
     *
     * @return Integer
     */
    public Integer getProductSource() {
        return productSource;
    }

    /**
     * 设置 货品来源（1生产入库）
     * 表字段 : product_instock_bill.product_source
     *
     * @param productSource
     */
    public void setProductSource(Integer productSource) {
        this.productSource = productSource;
    }

    /**
     * 获取 预留字段
     * 表字段 : product_instock_bill.related_bill
     *
     * @return Integer
     */
    public Integer getRelatedBill() {
        return relatedBill;
    }

    /**
     * 设置 预留字段
     * 表字段 : product_instock_bill.related_bill
     *
     * @param relatedBill
     */
    public void setRelatedBill(Integer relatedBill) {
        this.relatedBill = relatedBill;
    }

    /**
     * 获取 货品入库单状态（1待审核，2已审核，3已完成）
     * 表字段 : product_instock_bill.bill_state
     *
     * @return Integer
     */
    public Integer getBillState() {
        return billState;
    }

    /**
     * 设置 货品入库单状态（1待审核，2已审核，3已完成）
     * 表字段 : product_instock_bill.bill_state
     *
     * @param billState
     */
    public void setBillState(Integer billState) {
        this.billState = billState;
    }

    /**
     * 获取 备注
     * 表字段 : product_instock_bill.remark
     *
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注
     * 表字段 : product_instock_bill.remark
     *
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取 审核时间
     * 表字段 : product_instock_bill.audit_at
     *
     * @return Date
     */
    public Date getAuditAt() {
        return auditAt;
    }

    /**
     * 设置 审核时间
     * 表字段 : product_instock_bill.audit_at
     *
     * @param auditAt
     */
    public void setAuditAt(Date auditAt) {
        this.auditAt = auditAt;
    }

    /**
     * 获取 审核者
     * 表字段 : product_instock_bill.audit_by
     *
     * @return Integer
     */
    public Integer getAuditBy() {
        return auditBy;
    }

    /**
     * 设置 审核者
     * 表字段 : product_instock_bill.audit_by
     *
     * @param auditBy
     */
    public void setAuditBy(Integer auditBy) {
        this.auditBy = auditBy;
    }

    /**
     * 获取 完成时间
     * 表字段 : product_instock_bill.finish_at
     *
     * @return Date
     */
    public Date getFinishAt() {
        return finishAt;
    }

    /**
     * 设置 完成时间
     * 表字段 : product_instock_bill.finish_at
     *
     * @param finishAt
     */
    public void setFinishAt(Date finishAt) {
        this.finishAt = finishAt;
    }

    /**
     * 获取 完成者
     * 表字段 : product_instock_bill.finish_by
     *
     * @return Integer
     */
    public Integer getFinishBy() {
        return finishBy;
    }

    /**
     * 设置 完成者
     * 表字段 : product_instock_bill.finish_by
     *
     * @param finishBy
     */
    public void setFinishBy(Integer finishBy) {
        this.finishBy = finishBy;
    }

    /**
     * 获取 创建时间
     * 表字段 : product_instock_bill.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 创建时间
     * 表字段 : product_instock_bill.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 创建者
     * 表字段 : product_instock_bill.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 创建者
     * 表字段 : product_instock_bill.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 更新时间
     * 表字段 : product_instock_bill.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 更新时间
     * 表字段 : product_instock_bill.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 更新者
     * 表字段 : product_instock_bill.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 更新者
     * 表字段 : product_instock_bill.update_by
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
        sb.append(", billId=").append(billId);
        sb.append(", billNo=").append(billNo);
        sb.append(", fromPrincipal=").append(fromPrincipal);
        sb.append(", warehousePrincipal=").append(warehousePrincipal);
        sb.append(", billTime=").append(billTime);
        sb.append(", productSource=").append(productSource);
        sb.append(", relatedBill=").append(relatedBill);
        sb.append(", billState=").append(billState);
        sb.append(", remark=").append(remark);
        sb.append(", auditAt=").append(auditAt);
        sb.append(", auditBy=").append(auditBy);
        sb.append(", finishAt=").append(finishAt);
        sb.append(", finishBy=").append(finishBy);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}