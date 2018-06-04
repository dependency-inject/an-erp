package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.*;
import com.springmvc.service.ProductInstockService;
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
@RequestMapping("/product-instock")
public class ProductInstockController {

    @Resource
    ProductInstockService productInstockService;

    @RequestMapping(value="/audit",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_OUTSTOCK_AUDIT)
    public String audit(String idList){
        productInstockService.audit(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value="/unaudit",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_DRAW_AUDIT)
    public String unaudit(String idList){
        productInstockService.unaudit(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/getAdmins", method = RequestMethod.POST)
    @ResponseBody
    public List<Admin> getAdmins() {
        return productInstockService.getAdmins();
    }

    @RequestMapping(value = "/getWarehouses", method = RequestMethod.POST)
    @ResponseBody
    public List<Warehouse> getWarehouses() {
        return productInstockService.getWarehouses();
    }



    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_ADD)
    public ProductInstockBill add(@RequestParam Integer billId, @RequestParam String billNo, @RequestParam Integer fromPrincipal,
                                  @RequestParam Integer warehousePrincipal, @RequestParam Integer productWhereabins,
                                  @RequestParam Integer relatedBill, @RequestParam Integer status,
                                  @RequestParam String remark, @RequestParam String productIdList) {
        return productInstockService.addProductInsockBill(billNo, fromPrincipal, warehousePrincipal, productWhereabins,
                relatedBill, status, remark, ParamUtils.jsonToList(productIdList,ProductInstockBillProduct.class));
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCT_UPDATE)
    public ProductInstockBill update(@RequestParam Integer billId, @RequestParam String billNo, @RequestParam Integer fromPrincipal,
                                     @RequestParam Integer warehousePrincipal, @RequestParam Integer productSource,
                                     @RequestParam Integer relatedBill, @RequestParam Integer status,
                                     @RequestParam String remark, @RequestParam String productIdList) {
        return productInstockService.updateProductInsockBill(billId,billNo, fromPrincipal, warehousePrincipal, productSource,
                relatedBill, status, remark, ParamUtils.jsonToList(productIdList,ProductInstockBillProduct.class));
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

    @RequestMapping(value="/getProductIdList",method = RequestMethod.POST)
    @ResponseBody
    public List<Product> getProductIdList() {
        return productInstockService.getProductIdList();
    }


}
