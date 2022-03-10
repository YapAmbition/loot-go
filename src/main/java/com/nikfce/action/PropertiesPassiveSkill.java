package com.nikfce.action;

import com.nikfce.role.Looter;
import com.nikfce.role.Properties;

/**
 * 属性被动技能,即只要获得了这个技能,就能增加自己的属性
 * @author shenzhencheng 2022/3/9
 */
public interface PropertiesPassiveSkill {

    /**
     * 返回对特定looter造成影响的属性
     * @return 产生的属性变化
     */
    Properties handle(Looter looter);

}
