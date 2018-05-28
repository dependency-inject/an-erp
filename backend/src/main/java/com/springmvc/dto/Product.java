package com.springmvc.dto;

import com.springmvc.pojo.ProductEntity;

import java.util.List;

public class Product extends ProductEntity {

    /**
     * 货品物料列表
     * 表字段 :
     *      *       product_material.material_id，
     *      *       material.material_no
     *      *       material.material_name
     *      *       material.unit
     *      *       material.category_id
     *      *       material.spec
     *      *       product_material.quantity，
     *      *       product_material.material_property，
     *      *       product_material.remark
     */
    private List<ProductMaterial> productMaterialList;

    /**
     * 设置 货品物料列表
     * 表字段 :
     *      *       product_material.material_id，
     *      *       material.material_no
     *      *       material.material_name
     *      *       material.unit
     *      *       material.category_id
     *      *       material.spec
     *      *       product_material.quantity，
     *      *       product_material.material_property，
     *      *       product_material.remark
     *
     * @param productMaterialList
     */

    public void setProductMaterialList(List<ProductMaterial> productMaterialList) { this.productMaterialList = productMaterialList; }

    /**
     * 获取 货品物料列表
     * 表字段 :
     *      *       product_material.material_id，
     *      *       material.material_no
     *      *       material.material_name
     *      *       material.unit
     *      *       material.category_id
     *      *       material.spec
     *      *       product_material.quantity，
     *      *       product_material.material_property，
     *      *       product_material.remark
     *
     * @return productMaterialList
     */

    public List<ProductMaterial> getProductMaterialList() { return productMaterialList; }
}