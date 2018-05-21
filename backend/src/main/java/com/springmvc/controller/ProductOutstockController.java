package com.springmvc.controller;

import com.springmvc.service.ProductOutstockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/product-outstock")
public class ProductOutstockController {

    @Resource
    ProductOutstockService productOutstockService;
}
