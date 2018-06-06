package com.springmvc.dto;

import com.springmvc.pojo.MaterialInstockBillEntity;

import java.util.List;

public class MaterialInstockBill extends MaterialInstockBillEntity {

    private String fromPrincipalName;

    private String warehousePrincipalName;

    private String relatedBillNo;

    private String auditName;

    private String finishName;

    private List<MaterialInstockBillMaterial> materialList;

    public String getFromPrincipalName() {
        return fromPrincipalName;
    }

    public void setFromPrincipalName(String fromPrincipalName) {
        this.fromPrincipalName = fromPrincipalName;
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

    public List<MaterialInstockBillMaterial> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<MaterialInstockBillMaterial> materialList) {
        this.materialList = materialList;
    }
}