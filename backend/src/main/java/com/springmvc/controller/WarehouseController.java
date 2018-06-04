package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.Warehouse;
import com.springmvc.service.WarehouseService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    @Resource
    WarehouseService warehouseService;

    // 查询
    // 点击查询框获取信息列表，搜索字段：no/name
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    //current 当前页数 limit每页显示的条数 sortColumn按那一列进行排序 sort正序/反序 searchKey搜索关键字
    public PageMode<Warehouse> search(@RequestParam Integer current, @RequestParam Integer limit,
                                      String sortColumn, String sort, String searchKey){
        return warehouseService.pageWarehouse(current,limit,sortColumn ,sort,searchKey);
    }

    // 查询
    // 备用：点击单一列表获取详细信息（利用ID获取）
    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public Warehouse getById(@RequestParam Integer warehouseId){return warehouseService.getWarehouseById(warehouseId);}

    // 增添记录
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.WAREHOUSE_ADD)
    public Warehouse add(@RequestParam String warehouseNo,@RequestParam String warehouseName){
        return warehouseService.addWarehouse(warehouseNo,warehouseName);
    }

    //修改记录
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.WAREHOUSE_UPDATE)
    public Warehouse update(@RequestParam Integer warehouseId, @RequestParam String warehouseNo,@RequestParam String warehouseName){
        return warehouseService.updateWarehouse(warehouseId,warehouseNo,warehouseName);
    }

    //删除记录
    @RequestMapping(value = "remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.WAREHOUSE_REMOVE)
    public String remove(@RequestParam String idList){
        warehouseService.removeWarehouse(ParamUtils.toIntList(idList));
        return "success";
    }
}
