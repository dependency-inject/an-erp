package com.springmvc.controller;

import com.springmvc.dto.ReturnMaterialBill;
import com.springmvc.service.ReturnMaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/return-material")
public class ReturnMaterialController {

    @Resource
    ReturnMaterialService returnMaterialService;

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public ReturnMaterialBill getById(@RequestParam Integer billId) {
        return returnMaterialService.getById(billId);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public List<ReturnMaterialBill> getList(Integer state, Boolean onlyNotInstock) {
        return returnMaterialService.getList(state, onlyNotInstock);
    }
}
