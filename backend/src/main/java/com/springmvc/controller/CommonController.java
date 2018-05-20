package com.springmvc.controller;

import com.alibaba.fastjson.JSON;
import com.springmvc.dto.Admin;
import com.springmvc.service.AdminService;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CommonController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "redirect:dashboard";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(HttpServletRequest request, Model model) {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache(request);
        model.addAttribute("loginAdmin", JSON.toJSONString(loginAdmin));
        return "dashboard";
    }
}
