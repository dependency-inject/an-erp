package com.springmvc.interceptor;

import com.springmvc.dto.Admin;
import com.springmvc.utils.LogUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 如果用户已经登录，且尝试访问login页面时，跳转到主页面
 *
 * 拦截器不应对“静态资源”进行拦截
 */
public class RedirectIfAuthenticatedInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object o) throws Exception {
        // 只拦截登录页面
        if (!RequestUtils.isLoginPath(request.getServletPath()) || RequestUtils.isAjaxOrWantsJson(request)) {
            return true;
        }

        Admin loginAdmin = RequestUtils.getLoginAdminFromCache(request);
        if (loginAdmin != null && !loginAdmin.getClosed()) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
