package com.springmvc.dto;

import com.springmvc.pojo.ReturnMaterialBillEntity;

import java.util.List;

public class ReturnMaterialBill extends ReturnMaterialBillEntity {

    private String FromPrincipalName;

    private String warehousePrincipalName;

    private String relatedBillNo;

    private String auditName;

    private String finishName;

    private List<ReturnMaterialBillMaterial> materialList;

    public String getFromPrincipalName() {
        return FromPrincipalName;
    }

    public void setFromPrincipalName(String FromPrincipalName) {
        this.FromPrincipalName=FromPrincipalName;
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

    public List<ReturnMaterialBillMaterial> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<ReturnMaterialBillMaterial> materialList) {
        this.materialList = materialList;
    }
}
