package com.springmvc.controller;

import com.springmvc.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/material")
public class MaterialController {

    @Resource
    MaterialService materialService;
}
