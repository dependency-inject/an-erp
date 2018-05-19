package com.springmvc.controller;

import com.springmvc.pojo.Admin;
import com.springmvc.service.AdminService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    AdminService adminService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Admin add(@RequestParam String loginName, @RequestParam String trueName,
                     @RequestParam Boolean closed, @RequestParam String mobile) {
        return adminService.addAdmin(loginName, trueName, closed, mobile);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
                                 @RequestParam String confirmPassword) {
        adminService.changePassword(oldPassword, newPassword, confirmPassword);
        return "success";
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    @ResponseBody
    public Admin getById(@RequestParam Integer adminId) {
        return adminService.getAdminById(adminId);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public String remove(@RequestParam String idList) {
        adminService.removeAdmin(ParamUtils.strToIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public List<Admin> search(@RequestParam Integer current, @RequestParam Integer limit,
                              String sortColumn, String sort, String searchKey, Integer closed) {
        return adminService.searchAdmin(current, limit, sortColumn, sort, searchKey, closed);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Admin update(@RequestParam Integer adminId, @RequestParam String trueName,
                        @RequestParam Boolean closed, @RequestParam String mobile) {
        return adminService.updateAdmin(adminId, trueName, closed, mobile);
    }

    @RequestMapping(value = "/updateClosedState", method = RequestMethod.POST)
    @ResponseBody
    public String updateClosedState(@RequestParam String idList, @RequestParam Boolean closed) {
        adminService.updateAdminClosedState(ParamUtils.strToIntList(idList), closed);
        return "success";
    }
}
