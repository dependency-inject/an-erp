package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.ProductCategory;
import com.springmvc.service.ProductCategoryService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/product-category")
public class ProductCategoryController {

    @Resource
    ProductCategoryService productCategoryService;

    // 返回分类所有信息
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public List<ProductCategory> getList(){
        return productCategoryService.getList();
    }

    // 新增记录
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_CATEGORY)
    public ProductCategory add(@RequestParam String categoryName, @RequestParam Integer parentId){
        return productCategoryService.addProductCategory(categoryName, parentId);
    }

    // 更新记录
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_CATEGORY)
    public ProductCategory update(@RequestParam Integer categoryId, @RequestParam String categoryName,
                                   @RequestParam Integer parentId){
        return productCategoryService.updateProductCategory(categoryId, categoryName, parentId);
    }

    // 删除记录
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_CATEGORY)
    public String remove(@RequestParam String idList){
        productCategoryService.removeProductCategory(ParamUtils.toIntList(idList));
        return "success";
    }
}