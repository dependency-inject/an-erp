package com.springmvc.controller;

import com.springmvc.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Resource
    ProductService productService;
}
