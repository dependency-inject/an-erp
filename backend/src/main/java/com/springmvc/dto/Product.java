package com.springmvc.dto;

import com.springmvc.pojo.ProductEntity;

import java.util.List;

public class Product extends ProductEntity {

    /**
     * 类别名.
     * 表字段：material_category.category_name
     */
    private String categoryName;

    /**
     * 货品物料列表
     * 表字段 :
     * product_material.material_id，
     * material.material_no
     * material.material_name
     * material.unit
     * material.category_id
     * material.spec
     * product_material.quantity，
     * product_material.material_property，
     * product_material.remark
     */
    private List<ProductMaterial> productMaterialList;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setProductMaterialList(List<ProductMaterial> productMaterialList) { this.productMaterialList = productMaterialList; }

    public List<ProductMaterial> getProductMaterialList() { return productMaterialList; }
}