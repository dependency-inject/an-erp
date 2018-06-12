package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.ReturnMaterialBill;
import com.springmvc.dto.ReturnMaterialBillMaterial;
import com.springmvc.dto.StatisticsMode;
import com.springmvc.service.DevelopmentReturnService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/development-return")
public class DevelopmentReturnController {

    @Resource
    DevelopmentReturnService developmentReturnService;

    @RequestMapping(value = "/getStatistics", method = RequestMethod.POST)
    @ResponseBody
    public StatisticsMode getStatistics() {
        return developmentReturnService.getStatistics();
    }

    @RequestMapping(value = "/billDetail",method = RequestMethod.POST)
    @ResponseBody
    public ReturnMaterialBill getBill(int billId){
        return developmentReturnService.getBillById(billId);
    }

    @RequestMapping(value = "/searchBill",method =RequestMethod.POST)
    @ResponseBody
    public PageMode<ReturnMaterialBill> searchBill(@RequestParam Integer current, @RequestParam Integer limit,
                                                   String sortColumn, String sort, String searchKey, Integer state,
                                                   Long beginTime, Long endTime){
        return this.developmentReturnService.pageBill(current, limit, sortColumn, sort, searchKey, state,
                ParamUtils.toDate(beginTime), ParamUtils.toDate(endTime));
    }

    @RequestMapping(value="/auditBill",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_RETURN_AUDIT)
    public String audit(String idList){
        developmentReturnService.auditBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value="/unauditBill",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_RETURN_AUDIT)
    public String unaudit(String idList){
        developmentReturnService.unauditBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/addBill",method =RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_RETURN_ADD)
    public ReturnMaterialBill addBill(String remark, String materialList){
        return developmentReturnService.addBill(remark, ParamUtils.jsonToList(materialList, ReturnMaterialBillMaterial.class));
    }

    @RequestMapping(value = "/updateBill",method =RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_RETURN_UPDATE)
    public ReturnMaterialBill updateBill(Integer billId, String remark, String materialList){
        return developmentReturnService.updateBill(billId, remark, ParamUtils.jsonToList(materialList, ReturnMaterialBillMaterial.class));
    }

    @RequestMapping(value = "/deleteBill",method =RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_RETURN_REMOVE)
    public String deleteBill(String idList){
        developmentReturnService.removeBill(ParamUtils.toIntList(idList));
        return "success";
    }
}