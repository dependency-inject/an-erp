package com.springmvc.exception;

import com.springmvc.utils.LogUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class CustomExceptionResolver extends SimpleMappingExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
                                              HttpServletResponse response, Object handler, Exception ex) {

        // 判断是否 Ajax 请求
        if ((request.getHeader("accept").contains("application/json") ||
                (request.getHeader("X-Requested-With") != null &&
                        request.getHeader("X-Requested-With").contains("XMLHttpRequest")))) {

            try {
                response.setContentType("text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");

                // 状态码
                if (ex instanceof StatusException) {
                    response.setStatus(((StatusException) ex).getStatus());
                }

                PrintWriter writer = response.getWriter();
                writer.write("{\"message\":\"" + ex.getMessage() + "\"}");
                writer.flush();
                writer.close();
            } catch (Exception e) {
                LogUtils.error(e);
            }
            return null;
        }

        return super.doResolveException(request, response, handler, ex);
    }
}
