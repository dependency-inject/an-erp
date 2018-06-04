package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.MaterialCategory;
import com.springmvc.service.MaterialCategoryService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/material-category")
public class MaterialCategoryController {


    @Resource
    MaterialCategoryService materialCategoryService;

    //返回全部信息列表
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public List<MaterialCategory> getList(){
        return materialCategoryService.getList();
    }

    //增添记录
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_CATEGORY)
    public MaterialCategory add(@RequestParam String categoryName, @RequestParam Integer parentId){
        return materialCategoryService.addMaterialCategory(categoryName, parentId);
    }

    //修改记录
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_CATEGORY)
    public MaterialCategory update(@RequestParam Integer categoryId, @RequestParam String categoryName,
                                   @RequestParam Integer parentId){
        return materialCategoryService.updateMaterialCategory(categoryId, categoryName, parentId);
    }

    //删除记录
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.MATERIAL_CATEGORY)
    public String remove(@RequestParam String idList){
        materialCategoryService.removeMaterialCategory(ParamUtils.toIntList(idList));
        return "success";
    }
}
