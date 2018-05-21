package com.springmvc.controller;

import com.springmvc.service.ProductCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/product-category")
public class ProductCategoryController {

    @Resource
    ProductCategoryService productCategoryService;
}
