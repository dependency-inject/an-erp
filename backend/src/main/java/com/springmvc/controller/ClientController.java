package com.springmvc.controller;

import com.springmvc.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Resource
    ClientService clientService;
}
