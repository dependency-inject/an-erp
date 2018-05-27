package com.springmvc.controller;


import com.alibaba.fastjson.JSONObject;
import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.ReturnMaterialBillMaterial;
import com.springmvc.dto.ReturnMaterialBill;
import com.springmvc.service.DevelopmentReturnService;
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
@RequestMapping("/development-return")
public class DevelopmentReturnController {

    @Resource
    DevelopmentReturnService developmentReturnService;


    @RequestMapping(value = "/addmaterial", method = RequestMethod.POST)
    @ResponseBody
    public String addMaterial(int material_id,int quantity,String remark){
        this.developmentReturnService.addMaterial(material_id,quantity,remark);
        return "success";
    }

    @RequestMapping(value = "/addbill",method =RequestMethod.POST)
    @ResponseBody
    public ReturnMaterialBill addBill(String billno, String remark, int reason){
        ReturnMaterialBill re=this.developmentReturnService.add2Database(billno,remark,reason);
        this.developmentReturnService.cancelBill();
        return re;
    }
    @RequestMapping(value = "/canceladd",method =RequestMethod.POST)
    @ResponseBody
    public String cancelBill(String billno,int fromPrincipal,int warehousePrincipal,int reason){
        this.developmentReturnService.cancelBill();
        return "success";
    }
    @RequestMapping(value = "/deletematerial",method =RequestMethod.POST)
    @ResponseBody
    public String deleteitem(int material_id){
        this.developmentReturnService.remove(material_id);
        return "success";
    }
    @RequestMapping(value = "/deletebills",method =RequestMethod.POST)
    @ResponseBody
    public String deletebills(String bill_id){
        System.out.println("_______deletebill___________");
        System.out.print(bill_id);
        this.developmentReturnService.deleteBILL(ParamUtils.toIntList(bill_id));
        return "success";
    }
    @RequestMapping(value = "/deletematerials",method =RequestMethod.POST)
    @ResponseBody
    public String deletematerials(int bill_id,String material){
        this.developmentReturnService.deleteMaterial(bill_id,ParamUtils.toIntList(material));
        return "success";
    }
    @RequestMapping(value="/billstate",method = RequestMethod.POST)
    @ResponseBody
    public boolean getstate(int bill_id){
        return this.developmentReturnService.changeMaterialItem(bill_id);
    }
    @RequestMapping(value="/cahngestate",method = RequestMethod.POST)
    @ResponseBody
    public String changestatus(int bill_id,int status){

        this.developmentReturnService.setStatus(bill_id,status);
        return "success";
    }
    @RequestMapping(value="/updateremark",method = RequestMethod.POST)
    @ResponseBody
    public String updateRemark(int bill_id,String remark){

        this.developmentReturnService.update(bill_id,remark);
        return "success";
    }
    @RequestMapping(value="/allBills",method = RequestMethod.POST)
    @ResponseBody
    public List<ReturnMaterialBill> getAllBills(){
        List<ReturnMaterialBill> all=this.developmentReturnService.getAll();
        return all;
    }
    @RequestMapping(value="/allMaterial",method = RequestMethod.POST)
    @ResponseBody
    public List<ReturnMaterialBillMaterial> getAllBillMaterials(int bill_id){
        return this.developmentReturnService.getAllByID(bill_id);
    }
    @RequestMapping(value = "/billdetail",method = RequestMethod.POST)
    @ResponseBody
    public ReturnMaterialBill getBill(int bill_id){
        return this.developmentReturnService.getById(bill_id);
    }
    @RequestMapping(value = "/searchbill",method =RequestMethod.POST)
    @ResponseBody
    public List<ReturnMaterialBill> searchBill(String search){
        System.out.println(search);
        return this.developmentReturnService.getByMap(search);
    }
}
