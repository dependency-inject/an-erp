package com.springmvc.controller;

import com.springmvc.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    SupplierService supplierService;
}
