package com.springmvc.controller;

import com.springmvc.dto.Material;
import com.springmvc.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/material")
public class MaterialController {

    @Resource
    MaterialService materialService;
    @RequestMapping(value = "/getall", method = RequestMethod.POST)
    @ResponseBody
    public List<Material> getAll(){
        return this.materialService.getAll();
    }

}
