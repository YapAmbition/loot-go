package com.nikfce.scene;

import com.nikfce.annotation.LooterCode;
import com.nikfce.role.Looter;
import com.nikfce.util.CollectionUtil;
import com.nikfce.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author shenzhencheng 2022/3/12
 */
public class Condition implements Checkable {

    private static final Random random = new Random();
    private static final String KEYWORD_FINISH = "$finish";
    private static final String KEYWORD_UNFINISH = "$unfinish";
    private static final String KEYWORD_HAS_LOOTER = "$has_looter";
    private static final String KEYWORD_RANDOM = "$random";

    private String name;
    private List<String> condition; // 多个条件是且关系

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCondition() {
        return condition;
    }

    public void setCondition(List<String> condition) {
        this.condition = condition;
    }

    public boolean satisfy(IntrudeContext intrudeContext, Scene scene) {
        for (String exp : condition) {
            if (!satisfySubCondition(exp, intrudeContext, scene)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 解析子条件表达式
     */
    private boolean satisfySubCondition(String exp, IntrudeContext intrudeContext, Scene scene) {
        String[] kv = exp.trim().split("\\s");
        String keyword = kv[0].trim();
        String value = kv[1].trim();
        switch(keyword) {
            case KEYWORD_FINISH:
                return finishFlow(value, scene);
            case KEYWORD_UNFINISH:
                return !finishFlow(value, scene);
            case KEYWORD_HAS_LOOTER:
                return hasLooter(value, intrudeContext);
            case KEYWORD_RANDOM:
                return randomPercent(value);
            default:
                throw new RuntimeException("不认识的关键字!" + keyword);
        }
    }

    /**
     * 根据概率随机返回是否成功
     * @param probability 概率,0.3表示30%的概率
     */
    private boolean randomPercent(String probability) {
        double p = Double.parseDouble(probability);
        return random.nextInt(100) < p * 100;
    }

    private boolean hasLooter(String looterCode, IntrudeContext intrudeContext) {
        List<Looter> looterList = intrudeContext.getIntruders();
        for (Looter looter : looterList) {
            LooterCode looterCodeAnnotation = looter.getClass().getAnnotation(LooterCode.class);
            if (looterCode.equalsIgnoreCase(looterCodeAnnotation.value())) {
                return true;
            }
        }
        return false;
    }

    private boolean finishFlow(String name, Scene scene) {
        List<Flow> flowList = scene.getFlow();
        for (Flow flow : flowList) {
            if (flow.getName().equalsIgnoreCase(name) && flow.isPass()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void check() throws RuntimeException {
        if (StringUtil.isEmpty(name)) {
            throw new RuntimeException("Condition的name不能为空");
        }
        if (CollectionUtil.isEmpty(condition)) {
            throw new RuntimeException("Condition的condition不能为空");
        }
    }

    /**
     * 返回一个本对象的深拷贝
     */
    public Condition snapshot() {
        Condition copy = new Condition();
        copy.setName(name);
        if (condition != null) {
            copy.setCondition(new ArrayList<>(condition));
        }
        return copy;
    }
}
