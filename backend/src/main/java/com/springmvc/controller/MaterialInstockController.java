package com.springmvc.controller;

import com.springmvc.service.MaterialInstockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/material-instock")
public class MaterialInstockController {

    @Resource
    MaterialInstockService materialInstockService;
}
