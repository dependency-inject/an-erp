package com.springmvc.controller;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.Admin;
import com.springmvc.dto.PageMode;
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
    @PermissionRequired(AccessPermission.ADMIN_ADD)
    public Admin add(@RequestParam String loginName, @RequestParam String trueName,
                     @RequestParam Boolean closed, @RequestParam String mobile,
                     @RequestParam String roleIdList) {
        return adminService.addAdmin(loginName, trueName, closed, mobile, ParamUtils.toIntList(roleIdList));
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
        return adminService.getAdminWithRoleById(adminId);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public List<Admin> getList(Integer closed) {
        return adminService.getList(closed);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ADMIN_REMOVE)
    public String remove(@RequestParam String idList) {
        adminService.removeAdmin(ParamUtils.toIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public PageMode<Admin> search(@RequestParam Integer current, @RequestParam Integer limit,
                           String sortColumn, String sort, String searchKey, Integer closed) {
        return adminService.pageAdmin(current, limit, sortColumn, sort, searchKey, closed);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ADMIN_UPDATE)
    public Admin update(@RequestParam Integer adminId, @RequestParam String trueName,
                        @RequestParam Boolean closed, @RequestParam String mobile,
                        @RequestParam String roleIdList) {
        return adminService.updateAdmin(adminId, trueName, closed, mobile, ParamUtils.toIntList(roleIdList));
    }

    @RequestMapping(value = "/updateClosedState", method = RequestMethod.POST)
    @ResponseBody
    @PermissionRequired(AccessPermission.ADMIN_UPDATE)
    public String updateClosedState(@RequestParam String idList, @RequestParam Boolean closed) {
        adminService.updateAdminClosedState(ParamUtils.toIntList(idList), closed);
        return "success";
    }
}
