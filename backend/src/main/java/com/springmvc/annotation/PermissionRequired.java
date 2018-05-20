package com.springmvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限检查注解，标记了该注解的Controller方法需进行相应的权限检查
 *
 * 注意：对于查询类不作标记（权限检查），因此其他页面可能会调用到相关的查询接口
 * 例如，从出库单可以看到对应领料单信息
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionRequired {

    AccessPermission[] value() default {};
}
