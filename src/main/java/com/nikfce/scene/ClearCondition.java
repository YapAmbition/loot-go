package com.nikfce.scene;

import com.nikfce.util.CollectionUtil;
import com.nikfce.util.StringUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Scene的结束条件
 * @author shenzhencheng 2022/3/15
 */
@Data
public class ClearCondition implements Checkable {

    private String name;
    // 场景都被清理结束后,返回的描述
    private String clearDesc;
    private List<Condition> conditions;

    /**
     * 是否满足Scene的结束条件
     * @return 如果满足则返回清除描述,如果不满足则返回null
     */
    public String satisfy(IntrudeContext intrudeContext, Scene scene) {
        for (Condition condition : conditions) {
            if (condition.satisfy(intrudeContext, scene)) {
                return clearDesc;
            }
        }
        return null;
    }

    @Override
    public void check() throws RuntimeException {
        if (StringUtil.isEmpty(clearDesc)) {
            throw new RuntimeException("场景的结束描述不能为空: " + name);
        }
        if (CollectionUtil.isEmpty(conditions)) {
            throw new RuntimeException("场景的结束条件不能为空: " + name);
        }
    }

    public ClearCondition snapshot() {
        ClearCondition copy = new ClearCondition();
        copy.setName(name);
        copy.setClearDesc(clearDesc);
        copy.setConditions(copyConditions());
        return copy;
    }

    private List<Condition> copyConditions() {
        if (conditions == null) return null;
        List<Condition> copy = new ArrayList<>();
        for (Condition condition : conditions) {
            copy.add(condition.snapshot());
        }
        return copy;
    }
}
