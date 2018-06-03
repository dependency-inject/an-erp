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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/material-instock")
public class MaterialInstockController {

    @Resource
    MaterialInstockService materialInstockService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_ADD)
    public MaterialInstockBill add(@RequestParam String billNo, @RequestParam Integer selectedPrincipal,
                                   @RequestParam Integer warehousePrincipal, @RequestParam Integer relatedBill, @RequestParam String remark,
                                   @RequestParam Integer materialInstockState, @RequestParam String materials, @RequestParam Integer selectedSource) {
        return materialInstockService.addMaterialInstockBillByPurchase(billNo, selectedPrincipal, warehousePrincipal, relatedBill,
                remark, materialInstockState, materials, selectedSource);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_UPDATE)
    public MaterialInstockBill update(@RequestParam Integer billId, @RequestParam String billNo, @RequestParam Integer selectedPrincipal,
                                      @RequestParam Integer warehousePrincipal, @RequestParam Integer relatedBill, @RequestParam Integer materialInstockState,
                                      @RequestParam String remark,  @RequestParam Integer selectedSource) {
        return materialInstockService.updateMateialInstockBill(billId, billNo, selectedPrincipal, warehousePrincipal,
                relatedBill, materialInstockState, remark, selectedSource);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<MaterialInstockBill> search(@RequestParam Integer current, @RequestParam Integer limit,
                                  String sortColumn, String sort, String searchKey, Integer materialInstockState) {
        return materialInstockService.pageMaterialInstockBill(current, limit, sortColumn, sort, searchKey, materialInstockState);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_REMOVE)
    public String remove(@RequestParam String idList) {
        materialInstockService.removeMaterialBill(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public MaterialInstockBill getById(@RequestParam Integer billId) {
        return materialInstockService.getMaterialInstockBillById(billId);
    }

    @RequestMapping(value = "getMaterials", method = RequestMethod.POST)
    @ResponseBody
    public List<Material> getMaterials() {
        return materialInstockService.getMaterials();
    }

    @RequestMapping(value = "getMaterial", method = RequestMethod.POST)
    @ResponseBody
    public List<MaterialInstockBillMaterial> getMaterial(@RequestParam Integer billId) {
        return materialInstockService.getMaterial(billId);
    }

    @RequestMapping(value = "getWarehouses", method = RequestMethod.POST)
    @ResponseBody
    public List<Warehouse> getWarehouses() {
        return materialInstockService.getWarehouses();
    }

    @RequestMapping(value = "getAdmins", method = RequestMethod.POST)
    @ResponseBody
    public List<Admin> getAdmins() {
        return materialInstockService.getAdmins();
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
