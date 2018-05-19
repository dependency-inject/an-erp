package com.springmvc.interceptor;

import com.springmvc.exception.UnauthorizedException;
import com.springmvc.pojo.Admin;
import com.springmvc.utils.LogUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 检测用户是否登录，同时对用户信息做缓存
 * @see RequestUtils#getLoginAdminFromCache
 * @see RequestUtils#getLoginAdminFromSession
 *
 * 在请求开始时，清除用户信息缓存
 * @see RequestUtils#clearLoginAdminInCache
 *
 * 需注册为第一个拦截器，以确保能清除用户信息缓存
 * 附：多个拦截器执行顺序
 * 1、当俩个拦截器都实现放行操作时，顺序为preHandle1，preHandle2，postHandle2，postHandle1，afterCompletion2，afterCompletion1
 * 2、当第一个拦截器preHandle返回false，也就是对其进行拦截时，第二个拦截器是完全不执行的，第一个拦截器只执行preHandle部分。
 * 3、当第一个拦截器preHandle返回true，第二个拦截器preHandle返回false，顺序为preHandle1，preHandle2 ，afterCompletion1
 */
public class AuthenticateInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object o) throws Exception {
        RequestUtils.clearLoginAdminInCache();
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache(request);
        if (loginAdmin != null && !loginAdmin.getClosed()) {
            return true;
        }

        RequestUtils.clearLoginAdminInSession(request);
        if (RequestUtils.isAjaxOrWantsJson(request)) {
            throw new UnauthorizedException("请登录");
        }
        response.sendRedirect("/login");
        return false;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object o, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object o, Exception e) throws Exception {
    }
}
