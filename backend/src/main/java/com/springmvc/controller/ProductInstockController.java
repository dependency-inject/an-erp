package com.springmvc.controller;

import com.springmvc.service.ProductInstockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/product-instock")
public class ProductInstockController {

    @Resource
    ProductInstockService productInstockService;
}
