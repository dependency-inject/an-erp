package com.springmvc.interceptor;

import com.springmvc.annotation.AccessPermission;
import com.springmvc.annotation.PermissionRequired;
import com.springmvc.dto.Admin;
import com.springmvc.exception.BadRequestException;
import com.springmvc.utils.LogUtils;
import com.springmvc.utils.ParamUtils;
import com.springmvc.utils.RequestUtils;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 检测用户是否有权限访问对应Controller方法
 * 权限信息由注解标识
 * @see PermissionRequired
 *
 * 拦截器不应对“静态资源”进行拦截
 */
public class AccessPermissionInterceptor extends HandlerInterceptorAdapter {

    private static final String NO_PERMISSION_FOR_OPERATION = "没有权限进行此操作";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object o) throws Exception {
        // 不拦截登录
        if (RequestUtils.isLoginPath(request.getServletPath())) {
            return true;
        }

        // 不是方法拦截，不检查
        if (!(o instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) o;
        Method method = handlerMethod.getMethod();

        PermissionRequired permissionAnnotation = method.getAnnotation(PermissionRequired.class);
        // 没有标记PermissionRequired注解的方法，不检查
        if (permissionAnnotation == null) {
            return true;
        }
        AccessPermission[] permissions = permissionAnnotation.value();
        Admin loginAdmin = RequestUtils.getLoginAdminFromCache(request);
        Assert.notNull(loginAdmin, "here loginAdmin should not be null");
        List<String> permissionList = ParamUtils.toStrList(loginAdmin.getPermissionNameList());
        for (AccessPermission permission : permissions) {
            LogUtils.error(permission.permission);
            if (!permissionList.contains(permission.permission)) {
                throw new BadRequestException(NO_PERMISSION_FOR_OPERATION);
            }
        }
        return true;
    }
}
