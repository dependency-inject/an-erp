package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.*;
import com.springmvc.service.ProductionDrawService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/production-draw")
public class ProductionDrawController {

    @Resource
    ProductionDrawService productionDrawService;

    @RequestMapping(value = "/billDetail", method = RequestMethod.POST)
    @ResponseBody
    public DrawMaterialBill getBill(int billId){
        return productionDrawService.getBillById(billId);
    }

    @RequestMapping(value = "/searchBill", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<DrawMaterialBill> searchBill(@RequestParam Integer current, @RequestParam Integer limit,
                                                 String sortColumn, String sort, String searchKey, Integer state,
                                                 Long beginTime, Long endTime){
        return productionDrawService.pageBill(current, limit, sortColumn, sort, searchKey, state,
                ParamUtils.toDate(beginTime), ParamUtils.toDate(endTime));
    }

    @RequestMapping(value="/auditBill", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCTION_DRAW_AUDIT)
    public String audit(String idList){
        productionDrawService.auditBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value="/unauditBill", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCTION_DRAW_AUDIT)
    public String unaudit(String idList){
        productionDrawService.unauditBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/addBill", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCTION_DRAW_ADD)
    public DrawMaterialBill addBill(Integer relatedBill, String remark, String materialList){
        return productionDrawService.addBill(relatedBill, remark, ParamUtils.jsonToList(materialList, DrawMaterialBillMaterial.class));
    }

    @RequestMapping(value = "/updateBill", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCTION_DRAW_UPDATE)
    public DrawMaterialBill updateBill(Integer billId, Integer relatedBill, String remark, String materialList){
        return productionDrawService.updateBill(billId, relatedBill, remark, ParamUtils.jsonToList(materialList, DrawMaterialBillMaterial.class));
    }

    @RequestMapping(value = "/deleteBill", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCTION_DRAW_REMOVE)
    public String deleteBill(String idList){
        productionDrawService.removeBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/allOrderBillMaterial", method = RequestMethod.POST)
    @ResponseBody
    public List<DrawMaterialBillMaterial> getAllMaterial(int relatedBillId){
        return this.productionDrawService.getAllMaterial(relatedBillId);
    }
}
