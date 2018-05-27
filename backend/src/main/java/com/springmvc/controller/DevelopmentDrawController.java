package com.springmvc.controller;


import com.alibaba.fastjson.JSONObject;
import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.DrawMaterialBill;
import com.springmvc.dto.DrawMaterialBillMaterial;
import com.springmvc.service.DevelopmentDrawService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/development-draw")
public class DevelopmentDrawController {

    @Resource
    DevelopmentDrawService developmentDrawService;


    @RequestMapping(value = "/addmaterial", method = RequestMethod.POST)
    @ResponseBody
    public String addMaterial(int material_id,int quantity,String remark){
        this.developmentDrawService.addMaterial(material_id,quantity,remark);
        return "success";
    }

    @RequestMapping(value = "/addbill",method =RequestMethod.POST)
    @ResponseBody
    public DrawMaterialBill addBill(String billno,String remark,int reason){
        DrawMaterialBill re=this.developmentDrawService.add2Database(billno,remark,reason);
        this.developmentDrawService.cancelBill();
        return re;
    }
    @RequestMapping(value = "/canceladd",method =RequestMethod.POST)
    @ResponseBody
    public String cancelBill(String billno,int toPrincipal,int warehousePrincipal,int reason){
        this.developmentDrawService.cancelBill();
        return "success";
    }
    @RequestMapping(value = "/deletematerial",method =RequestMethod.POST)
    @ResponseBody
    public String deleteitem(int material_id){
        this.developmentDrawService.remove(material_id);
        return "success";
    }
    @RequestMapping(value = "/deletebills",method =RequestMethod.POST)
    @ResponseBody
    public String deletebills(String bill_id){
        System.out.println("_______deletebill___________");
        System.out.print(bill_id);
        this.developmentDrawService.deleteBILL(ParamUtils.toIntList(bill_id));
        return "success";
    }
    @RequestMapping(value = "/deletematerials",method =RequestMethod.POST)
    @ResponseBody
    public String deletematerials(int bill_id,String material){
        this.developmentDrawService.deleteMaterial(bill_id,ParamUtils.toIntList(material));
        return "success";
    }
    @RequestMapping(value="/billstate",method = RequestMethod.POST)
    @ResponseBody
    public boolean getstate(int bill_id){
        return this.developmentDrawService.changeMaterialItem(bill_id);
    }
    @RequestMapping(value="/cahngestate",method = RequestMethod.POST)
    @ResponseBody
    public String changestatus(int bill_id,int status){

        this.developmentDrawService.setStatus(bill_id,status);
        return "success";
    }
    @RequestMapping(value="/updateremark",method = RequestMethod.POST)
    @ResponseBody
    public String updateRemark(int bill_id,String remark){

        this.developmentDrawService.update(bill_id,remark);
        return "success";
    }
    @RequestMapping(value="/allBills",method = RequestMethod.POST)
    @ResponseBody
    public List<DrawMaterialBill> getAllBills(){
        List<DrawMaterialBill> all=this.developmentDrawService.getAll();
        return all;
    }
    @RequestMapping(value="/allMaterial",method = RequestMethod.POST)
    @ResponseBody
    public List<DrawMaterialBillMaterial> getAllBillMaterials(int bill_id){
        return this.developmentDrawService.getAllByID(bill_id);
    }
    @RequestMapping(value = "/billdetail",method = RequestMethod.POST)
    @ResponseBody
    public DrawMaterialBill getBill(int bill_id){
        return this.developmentDrawService.getById(bill_id);
    }
    @RequestMapping(value = "/searchbill",method =RequestMethod.POST)
    @ResponseBody
    public List<DrawMaterialBill> searchBill(String search){
        System.out.println(search);
        return this.developmentDrawService.getByMap(search);
    }
}
