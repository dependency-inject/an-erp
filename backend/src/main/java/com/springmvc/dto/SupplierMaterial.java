package com.springmvc.dto;

import com.springmvc.pojo.SupplierMaterialEntity;

import java.math.BigDecimal;

public class SupplierMaterial extends SupplierMaterialEntity {

    /**
     * 物料编号
     */
    private String materialName;

    /**
     * 物料编号
     */
    private String materialNo;

    /**
     * 供应商名称
     */
    private String supplierName;

    public String getMaterialName(){
        return materialName;
    }
    public String getMaterialNo(){
        return materialNo;
    }
    public String getSupplierName(){
        return supplierName;
    }
    public void setMaterialName(String materialName){this.materialName=materialName;}
    public void setMaterialNo(String materialNo){this.materialNo=materialNo;}
    public void setSupplierName(String supplierName){this.supplierName=supplierName;}

}