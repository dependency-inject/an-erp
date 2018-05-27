package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.MaterialOutstockBill;
import com.springmvc.dto.PageMode;
import com.springmvc.service.MaterialOutstockService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/material-outstock")
public class MaterialOutstockController {

    @Resource
    MaterialOutstockService materialOutstockService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_OUTSTOCK_ADD)
    public MaterialOutstockBill add(@RequestParam String billNo, @RequestParam Integer toPrincipal,
                                    @RequestParam Integer warehousePrincipal, @RequestParam Integer relatedBill, @RequestParam String remark,
                                    @RequestParam Integer materialOutstockState, @RequestParam String materialIdList) {
        System.out.println("control");
        return materialOutstockService.addMaterialOutstockBill(billNo, toPrincipal, warehousePrincipal, relatedBill,
                remark, materialOutstockState, ParamUtils.toIntList(materialIdList));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_OUTSTOCK_UPDATE)
    public MaterialOutstockBill update(@RequestParam Integer billId, @RequestParam String billNo, @RequestParam Integer toPrincipal,
                                      @RequestParam Integer warehousePrincipal, @RequestParam Integer relatedBill, @RequestParam Integer materialOutstockState,
                                      @RequestParam String remark,  @RequestParam String materialIdList) {
        return materialOutstockService.updateMaterialOutstockBill(billId, billNo, toPrincipal, warehousePrincipal,
                relatedBill, materialOutstockState, remark, ParamUtils.toIntList(materialIdList));
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<MaterialOutstockBill> search(@RequestParam Integer current, @RequestParam Integer limit,
                                                String sortColumn, String sort, String searchKey, Integer materialOutstockState) {
        return materialOutstockService.pageMaterialOutstockBill(current, limit, sortColumn, sort, searchKey, materialOutstockState);
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


}
