package com.nikfce.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户注册注解
 * 有次注解的接口,在进入之前会检查该用户是否带有规定的token
 * @author shenzhencheng 2022/3/21
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPermission {
}
