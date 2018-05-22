package com.springmvc.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class OrderBillEntity {
    /**
     * 订单ID
     * 表字段 : order_bill.bill_id
     */
    private Integer billId;

    /**
     * 订单编号
     * 表字段 : order_bill.bill_no
     */
    private String billNo;

    /**
     * 销售人员（制单人员ID）
     * 表字段 : order_bill.salesman
     */
    private Integer salesman;

    /**
     * 客户ID
     * 表字段 : order_bill.client_id
     */
    private Integer clientId;

    /**
     * 联系人
     * 表字段 : order_bill.contact
     */
    private String contact;

    /**
     * 联系电话
     * 表字段 : order_bill.contact_phone
     */
    private String contactPhone;

    /**
     * 下单时间
     * 表字段 : order_bill.bill_time
     */
    private Date billTime;

    /**
     * 订单金额
     * 表字段 : order_bill.bill_amount
     */
    private BigDecimal billAmount;

    /**
     * 订单状态（1待审核，2已审核，3生产中，4已发货，5已取消）
     * 表字段 : order_bill.bill_state
     */
    private Integer billState;

    /**
     * 备注
     * 表字段 : order_bill.remark
     */
    private String remark;

    /**
     * 审核时间
     * 表字段 : order_bill.audit_at
     */
    private Date auditAt;

    /**
     * 审核者
     * 表字段 : order_bill.audit_by
     */
    private Integer auditBy;

    /**
     * 启动生产时间
     * 表字段 : order_bill.produce_at
     */
    private Date produceAt;

    /**
     * 生产负责人
     * 表字段 : order_bill.produce_by
     */
    private Integer produceBy;

    /**
     * 发货时间
     * 表字段 : order_bill.delivery_at
     */
    private Date deliveryAt;

    /**
     * 发货者
     * 表字段 : order_bill.delivery_by
     */
    private Integer deliveryBy;

    /**
     * 创建时间
     * 表字段 : order_bill.create_at
     */
    private Date createAt;

    /**
     * 创建者
     * 表字段 : order_bill.create_by
     */
    private Integer createBy;

    /**
     * 更新时间
     * 表字段 : order_bill.update_at
     */
    private Date updateAt;

    /**
     * 更新者
     * 表字段 : order_bill.update_by
     */
    private Integer updateBy;

    /**
     * 获取 订单ID
     * 表字段 : order_bill.bill_id
     *
     * @return Integer
     */
    public Integer getBillId() {
        return billId;
    }

    /**
     * 设置 订单ID
     * 表字段 : order_bill.bill_id
     *
     * @param billId
     */
    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    /**
     * 获取 订单编号
     * 表字段 : order_bill.bill_no
     *
     * @return String
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * 设置 订单编号
     * 表字段 : order_bill.bill_no
     *
     * @param billNo
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    /**
     * 获取 销售人员（制单人员ID）
     * 表字段 : order_bill.salesman
     *
     * @return Integer
     */
    public Integer getSalesman() {
        return salesman;
    }

    /**
     * 设置 销售人员（制单人员ID）
     * 表字段 : order_bill.salesman
     *
     * @param salesman
     */
    public void setSalesman(Integer salesman) {
        this.salesman = salesman;
    }

    /**
     * 获取 客户ID
     * 表字段 : order_bill.client_id
     *
     * @return Integer
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * 设置 客户ID
     * 表字段 : order_bill.client_id
     *
     * @param clientId
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * 获取 联系人
     * 表字段 : order_bill.contact
     *
     * @return String
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置 联系人
     * 表字段 : order_bill.contact
     *
     * @param contact
     */
    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    /**
     * 获取 联系电话
     * 表字段 : order_bill.contact_phone
     *
     * @return String
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 设置 联系电话
     * 表字段 : order_bill.contact_phone
     *
     * @param contactPhone
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    /**
     * 获取 下单时间
     * 表字段 : order_bill.bill_time
     *
     * @return Date
     */
    public Date getBillTime() {
        return billTime;
    }

    /**
     * 设置 下单时间
     * 表字段 : order_bill.bill_time
     *
     * @param billTime
     */
    public void setBillTime(Date billTime) {
        this.billTime = billTime;
    }

    /**
     * 获取 订单金额
     * 表字段 : order_bill.bill_amount
     *
     * @return BigDecimal
     */
    public BigDecimal getBillAmount() {
        return billAmount;
    }

    /**
     * 设置 订单金额
     * 表字段 : order_bill.bill_amount
     *
     * @param billAmount
     */
    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    /**
     * 获取 订单状态（1待审核，2已审核，3生产中，4已发货，5已取消）
     * 表字段 : order_bill.bill_state
     *
     * @return Integer
     */
    public Integer getBillState() {
        return billState;
    }

    /**
     * 设置 订单状态（1待审核，2已审核，3生产中，4已发货，5已取消）
     * 表字段 : order_bill.bill_state
     *
     * @param billState
     */
    public void setBillState(Integer billState) {
        this.billState = billState;
    }

    /**
     * 获取 备注
     * 表字段 : order_bill.remark
     *
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注
     * 表字段 : order_bill.remark
     *
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取 审核时间
     * 表字段 : order_bill.audit_at
     *
     * @return Date
     */
    public Date getAuditAt() {
        return auditAt;
    }

    /**
     * 设置 审核时间
     * 表字段 : order_bill.audit_at
     *
     * @param auditAt
     */
    public void setAuditAt(Date auditAt) {
        this.auditAt = auditAt;
    }

    /**
     * 获取 审核者
     * 表字段 : order_bill.audit_by
     *
     * @return Integer
     */
    public Integer getAuditBy() {
        return auditBy;
    }

    /**
     * 设置 审核者
     * 表字段 : order_bill.audit_by
     *
     * @param auditBy
     */
    public void setAuditBy(Integer auditBy) {
        this.auditBy = auditBy;
    }

    /**
     * 获取 启动生产时间
     * 表字段 : order_bill.produce_at
     *
     * @return Date
     */
    public Date getProduceAt() {
        return produceAt;
    }

    /**
     * 设置 启动生产时间
     * 表字段 : order_bill.produce_at
     *
     * @param produceAt
     */
    public void setProduceAt(Date produceAt) {
        this.produceAt = produceAt;
    }

    /**
     * 获取 生产负责人
     * 表字段 : order_bill.produce_by
     *
     * @return Integer
     */
    public Integer getProduceBy() {
        return produceBy;
    }

    /**
     * 设置 生产负责人
     * 表字段 : order_bill.produce_by
     *
     * @param produceBy
     */
    public void setProduceBy(Integer produceBy) {
        this.produceBy = produceBy;
    }

    /**
     * 获取 发货时间
     * 表字段 : order_bill.delivery_at
     *
     * @return Date
     */
    public Date getDeliveryAt() {
        return deliveryAt;
    }

    /**
     * 设置 发货时间
     * 表字段 : order_bill.delivery_at
     *
     * @param deliveryAt
     */
    public void setDeliveryAt(Date deliveryAt) {
        this.deliveryAt = deliveryAt;
    }

    /**
     * 获取 发货者
     * 表字段 : order_bill.delivery_by
     *
     * @return Integer
     */
    public Integer getDeliveryBy() {
        return deliveryBy;
    }

    /**
     * 设置 发货者
     * 表字段 : order_bill.delivery_by
     *
     * @param deliveryBy
     */
    public void setDeliveryBy(Integer deliveryBy) {
        this.deliveryBy = deliveryBy;
    }

    /**
     * 获取 创建时间
     * 表字段 : order_bill.create_at
     *
     * @return Date
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置 创建时间
     * 表字段 : order_bill.create_at
     *
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取 创建者
     * 表字段 : order_bill.create_by
     *
     * @return Integer
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 设置 创建者
     * 表字段 : order_bill.create_by
     *
     * @param createBy
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 更新时间
     * 表字段 : order_bill.update_at
     *
     * @return Date
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置 更新时间
     * 表字段 : order_bill.update_at
     *
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取 更新者
     * 表字段 : order_bill.update_by
     *
     * @return Integer
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置 更新者
     * 表字段 : order_bill.update_by
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
        sb.append(", salesman=").append(salesman);
        sb.append(", clientId=").append(clientId);
        sb.append(", contact=").append(contact);
        sb.append(", contactPhone=").append(contactPhone);
        sb.append(", billTime=").append(billTime);
        sb.append(", billAmount=").append(billAmount);
        sb.append(", billState=").append(billState);
        sb.append(", remark=").append(remark);
        sb.append(", auditAt=").append(auditAt);
        sb.append(", auditBy=").append(auditBy);
        sb.append(", produceAt=").append(produceAt);
        sb.append(", produceBy=").append(produceBy);
        sb.append(", deliveryAt=").append(deliveryAt);
        sb.append(", deliveryBy=").append(deliveryBy);
        sb.append(", createAt=").append(createAt);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}