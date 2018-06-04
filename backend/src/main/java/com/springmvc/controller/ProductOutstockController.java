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
import com.springmvc.utils.ParamUtils;
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

    @RequestMapping(value = "/getAdmins", method = RequestMethod.POST)
    @ResponseBody
    public List<Admin> getAdmins() {
        return productOutstockService.getAdmins();
    }

    @RequestMapping(value = "/getWarehouses", method = RequestMethod.POST)
    @ResponseBody
    public List<Warehouse> getWarehouses() {
        return productOutstockService.getWarehouses();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_ADD)
    public ProductOutstockBill add(@RequestParam Integer billId, @RequestParam String billNo, @RequestParam Integer toPrincipal,
                                   @RequestParam Integer warehousePrincipal, @RequestParam Integer productWhereabouts,
                                   @RequestParam Integer relatedBill, @RequestParam Integer status,
                                   @RequestParam String remark, @RequestParam String productIdList) {
        return productOutstockService.addProductOutsockBill(billNo, toPrincipal, warehousePrincipal, productWhereabouts,
                relatedBill, status, remark, ParamUtils.jsonToList(productIdList,ProductOutstockBillProduct.class));
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_UPDATE)
    public ProductOutstockBill update(@RequestParam Integer billId, @RequestParam String billNo, @RequestParam Integer toPrincipal,
                                      @RequestParam Integer warehousePrincipal, @RequestParam Integer productWhereabouts,
                                      @RequestParam Integer relatedBill, @RequestParam Integer status,
                                      @RequestParam String remark, @RequestParam String productIdList) {
        return productOutstockService.updateProductOutsockBill(billId,billNo, toPrincipal, warehousePrincipal, productWhereabouts,
                relatedBill, status, remark, ParamUtils.jsonToList(productIdList,ProductOutstockBillProduct.class));
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

    @RequestMapping(value="/getProductIdList",method = RequestMethod.POST)
    @ResponseBody
    public List<Product> getProductIdList() {
        return productOutstockService.getProductIdList();
    }
}

