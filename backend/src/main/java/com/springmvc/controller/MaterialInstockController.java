package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.Admin;
import com.springmvc.dto.MaterialInstockBill;
import com.springmvc.dto.PageMode;
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
    public MaterialInstockBill add(@RequestParam String billNo, @RequestParam Integer fromPrincipal,
                                   @RequestParam Integer warehousePrincipal, @RequestParam Integer relatedBill, @RequestParam String remark,
                                   @RequestParam Integer materialInstockState, @RequestParam String materialIdList) {
        return materialInstockService.addMaterialInstockBillByPurchase(billNo, fromPrincipal, warehousePrincipal, relatedBill,
                remark, materialInstockState, ParamUtils.toIntList(materialIdList));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_INSTOCK_UPDATE)
    public MaterialInstockBill update(@RequestParam Integer billId, @RequestParam String billNo, @RequestParam Integer fromPrincipal,
                                      @RequestParam Integer warehousePrincipal, @RequestParam Integer relatedBill, @RequestParam Integer materialInstockState,
                                      @RequestParam String remark,  @RequestParam String materialIdList) {
        System.out.println("bill Id "+ billId);
        return materialInstockService.updateMateialInstockBill(billId, billNo, fromPrincipal, warehousePrincipal,
                relatedBill, materialInstockState, remark, ParamUtils.toIntList(materialIdList));
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
}
