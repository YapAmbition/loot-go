package com.nikfce.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 技能码注解,一个技能的唯一标识
 * @author shenzhencheng 2022/3/11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillCode {

    String value();

}
