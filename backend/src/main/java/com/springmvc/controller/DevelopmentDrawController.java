package com.springmvc.controller;


import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.DrawMaterialBill;
import com.springmvc.dto.DrawMaterialBillMaterial;
import com.springmvc.dto.PageMode;
import com.springmvc.service.DevelopmentDrawService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/development-draw")
public class DevelopmentDrawController {

    @Resource
    DevelopmentDrawService developmentDrawService;

    @RequestMapping(value = "/billDetail",method = RequestMethod.POST)
    @ResponseBody
    public DrawMaterialBill getBill(int billId){
        return developmentDrawService.getBillById(billId);
    }

    @RequestMapping(value = "/searchBill",method =RequestMethod.POST)
    @ResponseBody
    public PageMode<DrawMaterialBill> searchBill(@RequestParam Integer current, @RequestParam Integer limit,
                                                 String sortColumn, String sort, String searchKey, Integer state,
                                                 Long beginTime, Long endTime){
        return developmentDrawService.pageBill(current, limit, sortColumn, sort, searchKey, state,
                ParamUtils.toDate(beginTime), ParamUtils.toDate(endTime));
    }

    @RequestMapping(value="/auditBill",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_DRAW_AUDIT)
    public String audit(String idList){
        developmentDrawService.auditBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value="/unauditBill",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_DRAW_AUDIT)
    public String unaudit(String idList){
        developmentDrawService.unauditBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/addBill",method =RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_DRAW_ADD)
    public DrawMaterialBill addBill(String remark, String materialList){
        return developmentDrawService.addBill(remark, ParamUtils.jsonToList(materialList, DrawMaterialBillMaterial.class));
    }

    @RequestMapping(value = "/updateBill",method =RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_DRAW_UPDATE)
    public DrawMaterialBill updateBill(Integer billId, String remark, String materialList){
        return developmentDrawService.updateBill(billId, remark, ParamUtils.jsonToList(materialList, DrawMaterialBillMaterial.class));
    }

    @RequestMapping(value = "/deleteBill",method =RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.DEVELOPMENT_DRAW_REMOVE)
    public String deleteBill(String idList){
        developmentDrawService.removeBill(ParamUtils.toIntList(idList));
        return "success";
    }
}
