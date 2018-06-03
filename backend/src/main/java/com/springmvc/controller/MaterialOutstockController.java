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
    public MaterialOutstockBill add(@RequestParam String billNo, @RequestParam Integer selectedPrincipal,
                                    @RequestParam Integer warehousePrincipal, @RequestParam Integer relatedBill, @RequestParam String remark,
                                    @RequestParam Integer materialOutstockState, @RequestParam String materials, @RequestParam Integer selectedSource) {
        return materialOutstockService.addMaterialOutstockBill(billNo, selectedPrincipal, warehousePrincipal, relatedBill,
                remark, materialOutstockState, materials, selectedSource);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_OUTSTOCK_UPDATE)
    public MaterialOutstockBill update(@RequestParam Integer billId, @RequestParam String billNo, @RequestParam Integer selectedPrincipal,
                                      @RequestParam Integer warehousePrincipal, @RequestParam Integer relatedBill, @RequestParam Integer materialOutstockState,
                                      @RequestParam String remark, @RequestParam Integer selectedSource) {
        return materialOutstockService.updateMaterialOutstockBill(billId, billNo, selectedPrincipal, warehousePrincipal,
                relatedBill, materialOutstockState, remark, selectedSource);
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

    @RequestMapping(value = "getMaterials", method = RequestMethod.POST)
    @ResponseBody
    public List<Material> getMaterials() {
        return materialOutstockService.getMaterials();
    }

    @RequestMapping(value = "getMaterial", method = RequestMethod.POST)
    @ResponseBody
    public List<MaterialOutstockBillMaterial> getMaterial(@RequestParam Integer billId) {
        return materialOutstockService.getMaterial(billId);
    }

    @RequestMapping(value = "getWarehouses", method = RequestMethod.POST)
    @ResponseBody
    public List<Warehouse> getWarehouses() {
        return materialOutstockService.getWarehouses();
    }

    @RequestMapping(value = "getAdmins", method = RequestMethod.POST)
    @ResponseBody
    public List<Admin> getAdmins() {
        return materialOutstockService.getAdmins();
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
