package com.springmvc.controller;

import com.springmvc.service.MaterialCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/material-category")
public class MaterialCategoryController {

    @Resource
    MaterialCategoryService materialCategoryService;
}
