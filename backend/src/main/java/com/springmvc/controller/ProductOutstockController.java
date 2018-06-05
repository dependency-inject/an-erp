package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.*;
import com.springmvc.service.ProductOutstockService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.util.List;

import static com.springmvc.utils.ParamUtils.toIntList;


@Controller
@RequestMapping("/product-outstock")
public class ProductOutstockController {

    @Resource
    ProductOutstockService productOutstockService;

    @RequestMapping(value="/audit",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_OUTSTOCK_AUDIT)
    public String audit(String idList){
        productOutstockService.audit(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value="/unaudit",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_DRAW_AUDIT)
    public String unaudit(String idList){
        productOutstockService.unaudit(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_ADD)
    public ProductOutstockBill add(@RequestParam Integer toPrincipal, @RequestParam Integer productWhereabouts, @RequestParam Integer relatedBill,
                                   @RequestParam String remark, @RequestParam String productList) {
        return productOutstockService.addProductOutsockBill(toPrincipal, productWhereabouts, relatedBill,
                remark, ParamUtils.jsonToList(productList,ProductOutstockBillProduct.class));
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_UPDATE)
    public ProductOutstockBill update(@RequestParam Integer billId, @RequestParam Integer toPrincipal, @RequestParam Integer productWhereabouts,
                                      @RequestParam Integer relatedBill, @RequestParam String remark, @RequestParam String productList) {
        return productOutstockService.updateProductOutsockBill(billId, toPrincipal, productWhereabouts, relatedBill,
                remark, ParamUtils.jsonToList(productList,ProductOutstockBillProduct.class));
    }



    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<ProductOutstockBill> search(@RequestParam Integer current, @RequestParam Integer limit,
                                                String sortColumn, String sort, String searchKey, Integer state,
                                                Long beginTime, Long endTime) {
        return productOutstockService.pageProductOutstockBill(current, limit, sortColumn, sort, searchKey, state,
                ParamUtils.toDate(beginTime), ParamUtils.toDate(endTime));
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
        productOutstockService.removeProductBill(ParamUtils.toIntList(idList));
        return "success";
    }
}

