package com.springmvc.controller;

import com.springmvc.dto.MaterialLack;
import com.springmvc.dto.PageMode;
import com.springmvc.dto.SupplierMaterial;
import com.springmvc.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Resource
    StockService stockService;

    /**
     * 反查物料报价
     */
    @RequestMapping(value = "/findSupplierPrice", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<SupplierMaterial> findSupplierPrice(@RequestParam Integer materialId, @RequestParam Integer current, @RequestParam Integer limit,
                                                        String sortColumn, String sort) {
        return stockService.findSupplierPrice(materialId, current, limit, sortColumn, sort);
    }

    /**
     * 获取缺料情况
     */
    @RequestMapping(value = "/getMaterialLack", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<MaterialLack> getMaterialLack( @RequestParam Integer current, @RequestParam Integer limit,
                                                   String sortColumn, String sort, String searchKey) {
        return stockService.getMaterialLack(current, limit, sortColumn, sort, searchKey);
    }
}
