package com.springmvc.controller;

import com.springmvc.exception.BadRequestException;
import com.springmvc.pojo.Admin;
import com.springmvc.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    AdminService adminService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Admin add(@RequestParam String loginName, @RequestParam String trueName,
                     @RequestParam Boolean closed, @RequestParam String mobile) throws BadRequestException {
        return adminService.addAdmin(loginName, trueName, closed, mobile);
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public Admin getById(@RequestParam Integer adminId) {
        return adminService.getAdminById(adminId);
    }
}
