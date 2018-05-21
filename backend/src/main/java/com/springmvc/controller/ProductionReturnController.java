package com.springmvc.controller;

import com.springmvc.service.ProductionReturnService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/production-return")
public class ProductionReturnController {

    @Resource
    ProductionReturnService productionReturnService;
}
