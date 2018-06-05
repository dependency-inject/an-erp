package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.*;
import com.springmvc.service.MaterialInstockService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/material-instock")
public class MaterialInstockController {

    @Resource
    MaterialInstockService materialInstockService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_ADD)
    public MaterialInstockBill add(@RequestParam Integer fromPrincipal, @RequestParam Integer materialSource, @RequestParam Integer relatedBill,
                                   @RequestParam String remark, @RequestParam String materialList) {
        return materialInstockService.addMaterialInstockBill(fromPrincipal, materialSource, relatedBill,
                remark, ParamUtils.jsonToList(materialList, MaterialInstockBillMaterial.class));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_UPDATE)
    public MaterialInstockBill update(@RequestParam Integer billId, @RequestParam Integer fromPrincipal, @RequestParam Integer materialSource,
                                      @RequestParam Integer relatedBill, @RequestParam String remark, @RequestParam String materialList) {
        return materialInstockService.updateMateialInstockBill(billId, fromPrincipal, materialSource,
                relatedBill, remark, ParamUtils.jsonToList(materialList, MaterialInstockBillMaterial.class));
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<MaterialInstockBill> search(@RequestParam Integer current, @RequestParam Integer limit,
                                                String sortColumn, String sort, String searchKey, Integer state,
                                                Long beginTime, Long endTime) {
        return materialInstockService.pageMaterialInstockBill(current, limit, sortColumn, sort, searchKey, state,
                ParamUtils.toDate(beginTime), ParamUtils.toDate(endTime));
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_REMOVE)
    public String remove(@RequestParam String idList) {
        materialInstockService.removeMaterialInstockBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public MaterialInstockBill getById(@RequestParam Integer billId) {
        return materialInstockService.getMaterialInstockBillById(billId);
    }

    @RequestMapping(value = "audit", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_AUDIT)
    public String audit(@RequestParam String idList) {
        materialInstockService.audit(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "unaudit", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_AUDIT)
    public String unaudit(@RequestParam String idList) {
        materialInstockService.unaudit(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "finish", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_FINISH)
    public String finish(@RequestParam String idList) {
        materialInstockService.finish(ParamUtils.toIntList(idList));
        return "success";
    }
}
