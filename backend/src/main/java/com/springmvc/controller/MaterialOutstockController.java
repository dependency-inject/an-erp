package com.springmvc.controller;

import com.springmvc.service.MaterialOutstockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/material-outstock")
public class MaterialOutstockController {

    @Resource
    MaterialOutstockService materialOutstockService;
}
