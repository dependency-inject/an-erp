package com.springmvc.controller;

import com.springmvc.pojo.Admin;
import com.springmvc.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class AdminController {

    @Resource
    AdminService adminService;

    @RequestMapping("/")
    @ResponseBody
    public String index(){
        return "... ......";
    }

    @RequestMapping(value = "/admin",method = RequestMethod.POST)
    @ResponseBody
    public int insertUser(Admin admin){
        return adminService.insertAdmin(admin);

    }
    @RequestMapping(value = "/admin/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Admin getUser(@PathVariable  Integer id){
        return adminService.getAdmin(id);

    }
}
