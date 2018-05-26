package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.MaterialCategory;
import com.springmvc.service.MaterialCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.management.StringValueExp;
import java.util.List;

@Controller
@RequestMapping("/material-category")
public class MaterialCategoryController {


    @Resource
    MaterialCategoryService materialCategoryService;

    //返回全部信息列表
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public List<MaterialCategory> search(@RequestParam Integer getAll){
        return materialCategoryService.getAll(getAll);
    }

    //增添记录
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_CATEGORY)
    public MaterialCategory add(@RequestParam String category_name, Integer parent_id){
        return materialCategoryService.addMaterialCategory(category_name, parent_id);
    }

    //修改记录
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_CATEGORY)
    public MaterialCategory update(@RequestParam Integer categoryId, String category_name, Integer parent_id){
        return materialCategoryService.updateMaterialCategory(categoryId, category_name, parent_id);
    }

    //删除记录
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_CATEGORY)
    public String delete(@RequestParam List<Integer> idList){
        materialCategoryService.removeMaterialCategory(idList);
        return "success";
    }
}
