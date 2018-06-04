package com.springmvc.dto;

import com.springmvc.pojo.MaterialEntity;

public class Material extends MaterialEntity {

    /**
     * 分类名.
     * 表字段：material_category.category_name
     */
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}