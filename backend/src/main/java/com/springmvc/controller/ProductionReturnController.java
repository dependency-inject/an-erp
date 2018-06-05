package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.*;
import com.springmvc.service.ProductionDrawService;
import com.springmvc.service.ProductionReturnService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/production-return")
public class ProductionReturnController {

    @Resource
    ProductionReturnService productionReturnService;

    @RequestMapping(value = "/billDetail",method = RequestMethod.POST)
    @ResponseBody
    public ReturnMaterialBill getBill(int billId){
        return productionReturnService.getBillById(billId);
    }
    @RequestMapping(value="/getMaterial",method = RequestMethod.POST)
    @ResponseBody
    public List<ReturnMaterialBillMaterial> getmaterial(int billid){
        return this.productionReturnService.getMaterials(billid);
    }
    @RequestMapping(value = "/searchBill",method =RequestMethod.POST)
    @ResponseBody
    public PageMode<ReturnMaterialBill> searchBill(@RequestParam Integer current, @RequestParam Integer limit,
                                                   String sortColumn, String sort, String searchKey, Integer state,
                                                   Long beginTime, Long endTime){
        return this.productionReturnService.pageBill(current, limit, sortColumn, sort, searchKey, state,
                ParamUtils.toDate(beginTime), ParamUtils.toDate(endTime));
    }

    @RequestMapping(value="/auditBill",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCTION_RETURN_AUDIT)
    public String audit(String idList){
        productionReturnService.auditBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value="/unauditBill",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCTION_RETURN_AUDIT)
    public String unaudit(String idList){
        productionReturnService.unauditBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value="/getMaterialList",method = RequestMethod.POST)
    @ResponseBody
    public List<Material> getMaterialList() {
        return productionReturnService.getMaterialList();
    }

    @RequestMapping(value = "/addBill",method =RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCTION_RETURN_ADD)
    public ReturnMaterialBill addBill(String remark, String materialList){
        return productionReturnService.addBill(remark, ParamUtils.jsonToList(materialList, ReturnMaterialBillMaterial.class));
    }

    @RequestMapping(value = "/updateBill",method =RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCTION_RETURN_UPDATE)
    public ReturnMaterialBill updateBill(Integer billId, String remark, String materialList){
        return productionReturnService.updateBill(billId, remark, ParamUtils.jsonToList(materialList, ReturnMaterialBillMaterial.class));
    }

    @RequestMapping(value = "/deleteBill",method =RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.PRODUCTION_RETURN_REMOVE)
    public String deleteBill(String idList){
        productionReturnService.removeBill(ParamUtils.toIntList(idList));
        return "success";
    }
    @RequestMapping(value = "/allMaterial",method =RequestMethod.POST)
    @ResponseBody
    public List<Material> getAllMaterial(){
        return this.productionReturnService.getAllmaterials();
    }
}
