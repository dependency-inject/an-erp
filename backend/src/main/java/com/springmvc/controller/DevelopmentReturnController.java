package com.springmvc.controller;

import com.springmvc.service.DevelopmentReturnService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/development-return")
public class DevelopmentReturnController {

    @Resource
    DevelopmentReturnService developmentReturnService;
}
