package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.Admin;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.ProductOutstockBill;
import com.springmvc.service.ProductOutstockService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import static com.springmvc.utils.ParamUtils.toIntList;


@Controller
@RequestMapping("/product-outstock")
public class ProductOutstockController {

    @Resource
    ProductOutstockService productOutstockService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_ADD)
    public ProductOutstockBill add(@RequestParam Integer billId, @RequestParam String proOutstockNo, @RequestParam Integer proOutstockPerson,
                                   @RequestParam Integer stockPerson, @RequestParam Integer productSource,
                                   @RequestParam Integer relatedBill, @RequestParam Integer status,
                                   @RequestParam String remark, @RequestParam String productIdList) {
        return productOutstockService.addProductOutsockBill(proOutstockNo, proOutstockPerson, stockPerson, productSource,
                relatedBill, status, remark, productIdList);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_UPDATE)
    public ProductOutstockBill update(@RequestParam Integer billId, @RequestParam String proOutstockNo, @RequestParam Integer proOutstockPerson,
                                      @RequestParam Integer stockPerson, @RequestParam Integer productSource,
                                      @RequestParam Integer relatedBill, @RequestParam Integer status,
                                      @RequestParam String remark, @RequestParam String productIdList) {
        return productOutstockService.updateProductOutsockBill(billId,proOutstockNo, proOutstockPerson, stockPerson, productSource,
                relatedBill, status, remark, toIntList(productIdList));
    }



    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<ProductOutstockBill> search(@RequestParam Integer current, @RequestParam Integer limit,
                                                String sortColumn, String sort, String searchKey, Integer productOutstockState, Integer total) {
        return productOutstockService.pageProductOutstockBill(current, limit, sortColumn, sort, searchKey, productOutstockState);
    }


    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public ProductOutstockBill getById(@RequestParam Integer billId) {
        return productOutstockService.getProductOutstockBillById(billId);
    }


    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_REMOVE)
    public String remove(@RequestParam String idList) {
        productOutstockService.removeProductBill(toIntList(idList));
        return "success";
    }
}

