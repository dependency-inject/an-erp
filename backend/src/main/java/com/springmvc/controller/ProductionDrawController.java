package com.springmvc.controller;

import com.springmvc.service.ProductionDrawService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/production-draw")
public class ProductionDrawController {

    @Resource
    ProductionDrawService productionDrawService;
}
