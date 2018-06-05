package com.springmvc.dto;

import com.springmvc.pojo.ReturnMaterialBillEntity;

import java.util.List;

public class ReturnMaterialBill extends ReturnMaterialBillEntity {

    private List<ReturnMaterialBillMaterial> materialList;

    public List<ReturnMaterialBillMaterial> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<ReturnMaterialBillMaterial> materialList) {
        this.materialList = materialList;
    }
}