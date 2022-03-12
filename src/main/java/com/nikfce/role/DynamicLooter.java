package com.nikfce.role;

import com.nikfce.action.Skill;

import java.util.List;

/**
 * 动态looter类
 * 通过配置动态加载的角色类,实例化时都用这个类
 * @author shenzhencheng 2022/3/11
 */
public class DynamicLooter extends Looter {

    public DynamicLooter(String name, Properties properties, List<Skill> skillList) {
        super(name, properties, skillList);
    }
}
