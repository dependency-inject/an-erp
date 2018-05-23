package com.springmvc.controller;

import com.springmvc.dto.Material;
import com.springmvc.dto.PageMode;
import com.springmvc.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/material")
public class MaterialController {

    @Resource
    MaterialService materialService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<Material> search(@RequestParam Integer current, @RequestParam Integer limit,
                                     String sortColumn, String sort, String searchKey, Integer closed) {
        return materialService.pageAdmin(current, limit, sortColumn, sort, searchKey, closed);
    }
}
