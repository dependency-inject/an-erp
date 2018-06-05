package com.springmvc.dto;

import com.springmvc.pojo.DrawMaterialBillEntity;

import java.util.List;

public class DrawMaterialBill extends DrawMaterialBillEntity {

    private List<DrawMaterialBillMaterial> materialList;

    public List<DrawMaterialBillMaterial> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<DrawMaterialBillMaterial> materialList) {
        this.materialList = materialList;
    }
}