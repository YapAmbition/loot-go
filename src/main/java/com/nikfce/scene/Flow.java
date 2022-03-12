package com.nikfce.scene;

import com.nikfce.util.CollectionUtil;
import com.nikfce.util.StringUtil;

import java.util.List;

/**
 * 场景流,在这里定义场景怪物的生成规则,规则请看scene/sample.yml
 * @author shenzhencheng 2022/3/12
 */
public class Flow implements Checkable {

    private String name;
    private List<Condition> conditions; // 多个Condition对象是或关系
    private List<String> looters;
    private boolean isPass = false; // 这个Flow是否已经被打过了

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<String> getLooters() {
        return looters;
    }

    public void setLooters(List<String> looters) {
        this.looters = looters;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    /**
     * 判断是否满足条件
     * 必须isPass == false且满足其中一个Condition
     */
    public boolean satisfy(IntrudeContext intrudeContext, Scene scene) {
        if (isPass()) {
            return false;
        }
        if (CollectionUtil.isEmpty(conditions)) {
            return true;
        }
        boolean result = false;
        for (Condition condition : conditions) {
            result |= satisfyCondition(condition, intrudeContext, scene);
        }
        return result;
    }

    private boolean satisfyCondition(Condition condition, IntrudeContext intrudeContext, Scene scene) {
        return condition.satisfy(intrudeContext, scene, this);
    }

    @Override
    public String toString() {
        return "Flow{" +
                "name='" + name + '\'' +
                ", looters=" + looters +
                '}';
    }

    @Override
    public void check() throws RuntimeException {
        if (StringUtil.isEmpty(name)) {
            throw new RuntimeException("Flow的name不能为空");
        }
        if (conditions != null) {
            for (Condition condition : conditions) {
                condition.check();
            }
        }
        if (CollectionUtil.isEmpty(looters)) {
            throw new RuntimeException("Flow的looters不能为空");
        }
    }

}
