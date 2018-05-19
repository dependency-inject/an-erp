package com.springmvc.controller;

import com.alibaba.fastjson.JSON;
import com.springmvc.pojo.Admin;
import com.springmvc.utils.RequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommonController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "redirect:dashboard";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(HttpServletRequest request, Model model) {
        Admin admin = RequestUtils.getLoginAdminFromCache(request);
        model.addAttribute("loginAdmin", JSON.toJSONString(admin));
        return "dashboard";
    }
}
