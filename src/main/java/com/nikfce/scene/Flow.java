package com.nikfce.scene;

import com.nikfce.action.Skill;
import com.nikfce.register.LooterRegisterCenter;
import com.nikfce.role.Looter;
import com.nikfce.stage.Battle;
import com.nikfce.thread.ThreadLocalMap;
import com.nikfce.util.CollectionUtil;
import com.nikfce.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 场景流,在这里定义场景怪物的生成规则,规则请看scene/sample.yml
 * 一个Flow包含它的唯一名,出现条件和looters
 * @author shenzhencheng 2022/3/12
 */
public class Flow implements Checkable {

    private static final Random random = new Random();

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
     * 判断是否满足这个Flow的条件
     * 必须isPass == false且满足其中一个Condition
     */
    public boolean satisfy(IntrudeContext intrudeContext, Scene scene) {
        if (isPass()) {
            return false;
        }
        if (CollectionUtil.isEmpty(conditions)) {
            return true;
        }
        for (Condition condition : conditions) {
            if (satisfyCondition(condition, intrudeContext, scene)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否满足Condition
     */
    private boolean satisfyCondition(Condition condition, IntrudeContext intrudeContext, Scene scene) {
        return condition.satisfy(intrudeContext, scene);
    }

    /**
     * 执行此Flow
     * @return 闯入者是否胜利
     */
    public boolean executeBattle(IntrudeContext intrudeContext) {
        List<Looter> intruderList = intrudeContext.getIntruders();
        List<Looter> flowLooters = genFlowLootersByExp(looters);
        String intruderNames = intruderList.stream().map(Looter::getName).collect(Collectors.joining(","));
        String flowLooterNames = flowLooters.stream().map(Looter::getName).collect(Collectors.joining(","));
        ThreadLocalMap.getRecorder().record_f("%s与%s即将进入战斗!", intruderNames, flowLooterNames);
        Battle battle = new Battle(intruderList, flowLooters);
        boolean win = battle.battleStart();
        if (win) {
            setPass(true);
            ThreadLocalMap.getRecorder().record_f("%s胜利,即将获得奖励!", intruderNames);
            reward(intrudeContext, flowLooters);
        } else {
            ThreadLocalMap.getRecorder().record_f("%s失败了,结束游戏!", intruderNames);
        }
        return win;
    }

    /**
     * 奖励机制,现在最简单的奖励就是让每个looter获得每个怪物的技能
     */
    private void reward(IntrudeContext intrudeContext, List<Looter> flowLooters) {
        ThreadLocalMap.getRecorder().record_f("-------------------------------");
        ThreadLocalMap.getRecorder().record_f("> Reward: 所有存活的looter将获得敌人的所有技能 <");
        List<Looter> intruderList = intrudeContext.getIntruders();
        for (Looter looter : intruderList) {
            if (looter.isDead()) {
                continue;
            }
            for (Looter flowLooter : flowLooters) {
                List<Skill> skillList = flowLooter.getSkillList();
                for (Skill skill : skillList) {
                    ThreadLocalMap.getRecorder().record_f("> Reward: %s获得技能: [%s] <", looter.getName(), skill.name());
                    looter.addSkill(skill);
                }
            }
        }
    }

    /**
     * 根据Flow.looters表达式生成Looter实例
     */
    public static List<Looter> genFlowLootersByExp(List<String> exps) {
        if (CollectionUtil.isEmpty(exps)) {
            return new ArrayList<>();
        }
        List<Looter> result = new ArrayList<>();
        for (String exp : exps) {
            String[] kv = exp.trim().split(";");
            String looterCode = kv[0];
            String numberExp = kv[1];
            int count = explainNumberExp(numberExp);
            for (int i = 0 ; i < count ; i ++) {
                Looter looter = LooterRegisterCenter.generateLooter(looterCode);
                result.add(looter);
            }
        }
        return result;
    }

    /**
     * 解析数量表达式,目前只支持整数开闭区间
     * 例如: [1,2], (2, 3], [1, 4)
     */
    private static int explainNumberExp(String exp) {
        String[] kv = exp.trim().split(",");
        char lc = kv[0].charAt(0);
        char rc = kv[1].charAt(kv[1].length() - 1);
        int ln = Integer.parseInt(kv[0].substring(1).trim());
        int rn = Integer.parseInt(kv[1].substring(0, kv[1].length() - 1));
        if (lc != '[' && lc != '(') throw new RuntimeException("左区间必须用'('或者'['表示:" + exp);
        if (rc != ']' && rc != ')') throw new RuntimeException("右区间必须用')'或者']'表示:" + exp);
        if (lc == '(') ln ++;
        if (rc == ')') rn --;
        if (ln > rn) throw new RuntimeException("左区间必须小于右区间:" + exp);
        if (ln == rn) return ln;
        return ln + random.nextInt(rn - ln + 1);
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

    /**
     * 返回一个本对象的深拷贝
     */
    public Flow snapshot() {
        Flow copy = new Flow();
        copy.setName(name);
        copy.setPass(isPass);
        copy.setConditions(copyConditions());
        if (looters != null) {
            copy.setLooters(new ArrayList<>(looters));
        }
        return copy;
    }

    private List<Condition> copyConditions() {
        if (conditions == null) return null;
        List<Condition> copy = new ArrayList<>();
        for (Condition condition : conditions) {
            Condition copyCondition = condition.snapshot();
            copy.add(copyCondition);
        }
        return copy;
    }

}
