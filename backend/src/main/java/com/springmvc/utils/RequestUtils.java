package com.springmvc.utils;

import com.springmvc.exception.UnauthorizedException;
import com.springmvc.pojo.Admin;
import com.springmvc.service.AdminService;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

    private static final String LOGIN_ADMIN = "LOGIN_ADMIN";

    private static AdminService adminService = SpringContextHolder.getBean(AdminService.class);

    private static ThreadLocal<Admin> loginAdmin = new ThreadLocal<Admin>();

    /**
     * 注册用户ID到Session，登录成功时调用
     * @param request
     * @param adminId
     */
    public static void setLoginAdminToSession(HttpServletRequest request, Integer adminId) {
        request.getSession().setAttribute(LOGIN_ADMIN, adminId);
    }

    /**
     * 从Session中取得用户ID，据此从数据库获取相应用户信息，同时对用户信息做缓存（ThreadLocal）
     * 用户信息缓存在同一次请求中有效，需在请求开始时清除缓存
     * @see RequestUtils#clearLoginAdminInCache
     * @param request
     * @return
     */
    public static Admin getLoginAdminFromSession(HttpServletRequest request) {
        Object object = request.getSession().getAttribute(LOGIN_ADMIN);
        if (object != null && object instanceof Integer) {
            Admin admin = adminService.getAdminById((Integer) object);
            loginAdmin.set(admin);
            return admin;
        }
        return null;
    }

    /**
     * 清除Session中的用户ID，注销时调用
     * @param request
     */
    public static void clearLoginAdminInSession(HttpServletRequest request) {
        request.getSession().removeAttribute(LOGIN_ADMIN);
    }

    /**
     * 获取用户信息（只从缓存中获取），若获取失败抛出UnauthorizedException（RuntimeException）
     * 用户信息缓存在同一次请求中有效，需在请求开始时清除缓存
     * @see RequestUtils#clearLoginAdminInCache
     * @return
     *
     * 该方法在Service中使用
     */
    public static Admin getLoginAdminFromCache() {
        Admin admin = loginAdmin.get();
        if (admin == null) {
            throw new UnauthorizedException("请登录");
        }
        return admin;
    }

    /**
     * 获取用户信息（优先从缓存中获取）
     * 用户信息缓存在同一次请求中有效，需在请求开始时清除缓存
     * @see RequestUtils#clearLoginAdminInCache
     * @param request
     * @return
     */
    public static Admin getLoginAdminFromCache(HttpServletRequest request) {
        Admin admin = loginAdmin.get();
        if (admin == null) {
            admin = getLoginAdminFromSession(request);
        }
        return admin;
    }

    /**
     * 清除缓存中的用户信息，请求开始时调用，防止其他请求（使用相同线程）的数据窜入
     */
    public static void clearLoginAdminInCache() {
        loginAdmin.set(null);
    }

    public static boolean isAjaxOrWantsJson(HttpServletRequest request) {
        if (request.getHeader("accept").contains("application/json")) {
            return true;
        }
        if (request.getHeader("X-Requested-With") != null &&
                request.getHeader("X-Requested-With").contains("XMLHttpRequest")) {
            return true;
        }
        return false;
    }
}
