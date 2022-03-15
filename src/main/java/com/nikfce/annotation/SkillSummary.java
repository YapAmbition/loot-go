package com.nikfce.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 技能描述
 * 包含技能码,技能名和技能简介
 * @author shenzhencheng 2022/3/15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillSummary {

    String code();

    String name();

    String desc() default "这个技能很神秘,什么描述都没有";

}
