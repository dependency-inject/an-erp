package com.springmvc.controller;

import com.springmvc.dto.DrawMaterialBill;
import com.springmvc.service.DrawMaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/draw-material")
public class DrawMaterialController {

    @Resource
    DrawMaterialService drawMaterialService;

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public DrawMaterialBill getById(@RequestParam Integer billId) {
        return drawMaterialService.getById(billId);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public List<DrawMaterialBill> getList(Integer state, Boolean onlyNotOutstock) {
        return drawMaterialService.getList(state, onlyNotOutstock);
    }
}
