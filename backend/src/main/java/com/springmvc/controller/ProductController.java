package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.Product;
import com.springmvc.dto.ProductCategory;
import com.springmvc.dto.ProductMaterial;
import com.springmvc.service.ProductService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Resource
    ProductService productService;

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public Product getById(@RequestParam Integer productId) {
        return productService.getProductWithMaterialById(productId);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_ADD)
    public Product add(@RequestParam String productNo, @RequestParam String productName,
                       @RequestParam String unit, @RequestParam Integer categoryId,
                       @RequestParam String spec, @RequestParam BigDecimal price,
                       @RequestParam Boolean closed, @RequestParam String remark,
                       @RequestParam String productMaterialList) {
        return productService.addProduct(productNo, productName, unit, categoryId, spec, price, closed, remark, ParamUtils.jsonToList(productMaterialList,ProductMaterial.class));
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_REMOVE)
    public String remove(@RequestParam String productIdList) {
        productService.removeProduct(ParamUtils.toIntList(productIdList));
        return "success";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<Product> search(@RequestParam Integer current, @RequestParam Integer limit,
                                    String sortColumn, String sort, String searchKey, Integer closed, Integer categoryId) {
        return productService.pageProduct(current, limit, sortColumn, sort, searchKey, closed, categoryId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_UPDATE)
    public Product update(@RequestParam Integer productId, @RequestParam String productNo,
                          @RequestParam String productName, @RequestParam String unit,
                          @RequestParam Integer categoryId, @RequestParam String spec,
                          @RequestParam BigDecimal price, @RequestParam Boolean closed,
                          @RequestParam String remark, @RequestParam String productMaterialList) {
        return productService.updateProduct(productId, productNo, productName, unit, categoryId, spec, price, closed, remark,  ParamUtils.jsonToList(productMaterialList,ProductMaterial.class));
    }

    @RequestMapping(value = "/updateClosedState", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_UPDATE)
    public String updateClosedState(@RequestParam String productIdList, @RequestParam Boolean closed) {
        productService.updateProductClosedState(ParamUtils.toIntList(productIdList), closed);
        return "success";
    }

    @RequestMapping(value = "/getCategoryList", method = RequestMethod.POST)
    @ResponseBody
    public List<ProductCategory> getCategoryList() {
        return productService.getCategoryList();
    }
}
