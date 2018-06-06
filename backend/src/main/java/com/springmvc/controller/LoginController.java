package com.springmvc.controller;

import com.springmvc.dto.Admin;
import com.springmvc.service.AdminService;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Resource
    AdminService adminService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, Model model,
                        @RequestParam String loginName, @RequestParam String password) {
        try {
            Admin admin = adminService.login(loginName, password);
            RequestUtils.setLoginAdminToSession(request, admin.getAdminId());
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpServletRequest request) {
        RequestUtils.clearLoginAdminInSession(request);
        return "redirect:login";
    }
}
