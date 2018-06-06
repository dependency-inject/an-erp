package com.springmvc.dto;

import com.springmvc.pojo.DrawMaterialBillEntity;

import java.util.List;

public class DrawMaterialBill extends DrawMaterialBillEntity {

    private String toPrincipalName;

    private String warehousePrincipalName;

    private String relatedBillNo;

    private String auditName;

    private String finishName;

    private List<DrawMaterialBillMaterial> materialList;

    public String getToPrincipalName() {
        return toPrincipalName;
    }

    public void setToPrincipalName(String toPrincipalName) {
        this.toPrincipalName = toPrincipalName;
    }

    public String getWarehousePrincipalName() {
        return warehousePrincipalName;
    }

    public void setWarehousePrincipalName(String warehousePrincipalName) {
        this.warehousePrincipalName = warehousePrincipalName;
    }

    public String getRelatedBillNo() {
        return relatedBillNo;
    }

    public void setRelatedBillNo(String relatedBillNo) {
        this.relatedBillNo = relatedBillNo;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getFinishName() {
        return finishName;
    }

    public void setFinishName(String finishName) {
        this.finishName = finishName;
    }

    public List<DrawMaterialBillMaterial> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<DrawMaterialBillMaterial> materialList) {
        this.materialList = materialList;
    }
}