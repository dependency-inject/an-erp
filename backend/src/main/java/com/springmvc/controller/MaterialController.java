package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.Material;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.Product;
import com.springmvc.service.MaterialService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/material")
public class MaterialController {

    @Resource
    MaterialService materialService;

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public List<Material> getList() {
        return materialService.getList();
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<Material> search(@RequestParam Integer current, @RequestParam Integer limit,
                                     String sortColumn, String sort, String searchKey, Integer categoryId) {
        return materialService.pageMaterial(current, limit, sortColumn, sort, searchKey, categoryId);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_REMOVE)
    public String remove(@RequestParam String idList){
        materialService.removeMaterial(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public Material getById(@RequestParam Integer materialId) {
        return materialService.getMaterialById(materialId);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_ADD)
    public Material add(@RequestParam String materialNo, @RequestParam String materialName,
                        @RequestParam String unit, @RequestParam int categoryId,
                        @RequestParam String spec, @RequestParam String remark) {
        return materialService.addMaterial(materialNo, materialName, unit, categoryId, spec, remark);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_UPDATE)
    public Material update(@RequestParam Integer materialId,
                           @RequestParam String materialNo, @RequestParam String materialName,
                           @RequestParam String unit, @RequestParam int categoryId,
                           @RequestParam String spec, @RequestParam String remark) {
        return materialService.updateMaterial(materialId, materialNo, materialName, unit, categoryId, spec, remark);
    }

    @RequestMapping(value = "/updateCost", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_COST)
    public Material updateCost(@RequestParam Integer materialId, @RequestParam BigDecimal cost) {
        return materialService.updateCost(materialId, cost);
    }
}