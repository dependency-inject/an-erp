package com.springmvc.interceptor;

import com.springmvc.pojo.Admin;
import com.springmvc.utils.RequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 如果用户已经登录，且尝试访问login页面时，跳转到主页面
 */
public class RedirectIfAuthenticatedInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object o) throws Exception {
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache(request);
        if (loginAdmin != null && !loginAdmin.getClosed()
                && request.getServletPath().equals("/login")
                && !RequestUtils.isAjaxOrWantsJson(request)) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object o, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object o, Exception e) throws Exception {
    }
}
