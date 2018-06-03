package com.springmvc.dto;

import com.springmvc.pojo.MaterialInstockBillMaterialEntity;

public class MaterialInstockBillMaterial extends MaterialInstockBillMaterialEntity {
    String materialName;
    String warehouseName;
    String principalName;

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public String getMaterialName() {
        return materialName;
    }

    public void setWarehouseName(String warehouseName) {

        this.warehouseName = warehouseName;
    }
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setPrincipalName(String principalName) {

        this.principalName = principalName;
    }
    public String getPrincipalName() {
        return principalName;
    }

}