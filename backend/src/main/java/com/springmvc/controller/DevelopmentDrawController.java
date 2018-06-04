package com.springmvc.controller;

import com.springmvc.service.DevelopmentDrawService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/development-draw")
public class DevelopmentDrawController {

    @Resource
    DevelopmentDrawService developmentDrawService;
}
