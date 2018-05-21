package com.springmvc.controller;

import com.springmvc.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    @Resource
    WarehouseService warehouseService;
}
