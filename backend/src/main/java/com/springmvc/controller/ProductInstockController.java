package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.ProductInstockBill;
import com.springmvc.service.ProductInstockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import static com.springmvc.utils.ParamUtils.toIntList;

@Controller
@RequestMapping("/product-instock")
public class ProductInstockController {

    @Resource
    ProductInstockService productInstockService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_ADD)
    public ProductInstockBill add(@RequestParam Integer billId, @RequestParam String proInstockNo, @RequestParam Integer proInstockPerson,
                                  @RequestParam Integer stockPerson, @RequestParam Integer productSource,
                                  @RequestParam Integer relatedBill, @RequestParam Integer status,
                                  @RequestParam String remark, @RequestParam String productIdList) {
        return productInstockService.addProductInsockBill(proInstockNo, proInstockPerson, stockPerson, productSource,
                relatedBill, status, remark, productIdList);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_UPDATE)
    public ProductInstockBill update(@RequestParam Integer billId, @RequestParam String proInstockNo, @RequestParam Integer proInstockPerson,
                                  @RequestParam Integer stockPerson, @RequestParam Integer productSource,
                                  @RequestParam Integer relatedBill, @RequestParam Integer status,
                                  @RequestParam String remark, @RequestParam String productIdList) {
        return productInstockService.updateProductInsockBill(billId,proInstockNo, proInstockPerson, stockPerson, productSource,
                relatedBill, status, remark, toIntList(productIdList));
    }



    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<ProductInstockBill> search(@RequestParam Integer current, @RequestParam Integer limit,
                                               String sortColumn, String sort, String searchKey, Integer productInstockState, Integer total) {
        return productInstockService.pageProductInstockBill(current, limit, sortColumn, sort, searchKey, productInstockState);
    }


    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public ProductInstockBill getById(@RequestParam Integer billId) {
        return productInstockService.getProductInstockBillById(billId);
    }


    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_REMOVE)
    public String remove(@RequestParam String idList) {
        productInstockService.removeProductBill(toIntList(idList));
        return "success";
    }




}
