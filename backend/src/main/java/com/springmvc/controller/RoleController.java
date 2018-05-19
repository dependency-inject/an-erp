package com.springmvc.controller;

import com.springmvc.pojo.Admin;
import com.springmvc.pojo.Permission;
import com.springmvc.pojo.Role;
import com.springmvc.service.RoleService;
import com.springmvc.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Resource
    RoleService roleService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Role add(@RequestParam String roleName) {
        return roleService.addRole(roleName);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public List<Role> getList() {
        return roleService.getRoleList();
    }

    @RequestMapping(value = "/getPermissionList", method = RequestMethod.POST)
    @ResponseBody
    public List<Permission> getPermissionList() {
        return roleService.getPermissionList();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public String remove(@RequestParam String idList) {
        roleService.removeRole(ParamUtils.strToIntList(idList));
        return "success";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Role update(@RequestParam Integer roleId, @RequestParam String roleName) {
        return roleService.updateRole(roleId, roleName);
    }

    @RequestMapping(value = "/updatePermissions", method = RequestMethod.POST)
    @ResponseBody
    public String updatePermissions(@RequestParam Integer roleId, @RequestParam String permissionIdList) {
        roleService.updateRolePermissions(roleId, ParamUtils.strToIntList(permissionIdList));
        return "success";
    }
}
