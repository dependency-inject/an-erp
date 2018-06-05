package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.*;
import com.springmvc.service.MaterialOutstockService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/material-outstock")
public class MaterialOutstockController {

    @Resource
    MaterialOutstockService materialOutstockService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_OUTSTOCK_ADD)
    public MaterialOutstockBill add(@RequestParam Integer toPrincipal, @RequestParam Integer materialWhereabouts, @RequestParam Integer relatedBill,
                                    @RequestParam String remark, @RequestParam String materialList) {
        return materialOutstockService.addMaterialOutstockBill(toPrincipal, materialWhereabouts, relatedBill,
                remark, ParamUtils.jsonToList(materialList, MaterialOutstockBillMaterial.class));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_OUTSTOCK_UPDATE)
    public MaterialOutstockBill update(@RequestParam Integer billId, @RequestParam Integer toPrincipal, @RequestParam Integer materialWhereabouts,
                                       @RequestParam Integer relatedBill, @RequestParam String remark, @RequestParam String materialList) {
        return materialOutstockService.updateMaterialOutstockBill(billId, toPrincipal, materialWhereabouts, relatedBill,
                remark, ParamUtils.jsonToList(materialList, MaterialOutstockBillMaterial.class));
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<MaterialOutstockBill> search(@RequestParam Integer current, @RequestParam Integer limit,
                                                 String sortColumn, String sort, String searchKey, Integer state,
                                                 Long beginTime, Long endTime) {
        return materialOutstockService.pageMaterialOutstockBill(current, limit, sortColumn, sort, searchKey, state,
                ParamUtils.toDate(beginTime), ParamUtils.toDate(endTime));
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_OUTSTOCK_REMOVE)
    public String remove(@RequestParam String idList) {
        materialOutstockService.removeMaterialOutstockBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public MaterialOutstockBill getById(@RequestParam Integer billId) {
        return materialOutstockService.getMaterialOutstockBillById(billId);
    }
    
    @RequestMapping(value = "audit", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_OUTSTOCK_AUDIT)
    public String audit(@RequestParam String idList) {
        materialOutstockService.audit(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "unaudit", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_OUTSTOCK_AUDIT)
    public String unaudit(@RequestParam String idList) {
        materialOutstockService.unaudit(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "finish", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_OUTSTOCK_FINISH)
    public String finish(@RequestParam String idList) {
        materialOutstockService.finish(ParamUtils.toIntList(idList));
        return "success";
    }

}
