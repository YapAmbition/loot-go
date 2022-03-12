package com.nikfce.scene;

import com.nikfce.register.LooterRegisterCenter;
import com.nikfce.role.Looter;
import com.nikfce.util.CollectionUtil;
import com.nikfce.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 场景类
 * @author shenzhencheng 2022/3/12
 */
public class Scene implements Checkable {

    private static final Random random = new Random();
    private String name;
    private String desc;
    private List<Flow> flow;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Flow> getFlow() {
        return flow;
    }

    public void setFlow(List<Flow> flow) {
        this.flow = flow;
    }

    /**
     * 返回当前场景能展示的Flow
     */
    public List<Flow> showFlow(IntrudeContext intrudeContext) {
        List<Flow> displayFlow = new ArrayList<>();
        for (Flow f : flow) {
            if (f.satisfy(intrudeContext, this)) {
                displayFlow.add(f);
            }
        }
        return displayFlow;
    }

    public List<Looter> choiceFlow(String flowName) {
        for (Flow f : flow) {
            if (f.getName().equalsIgnoreCase(flowName)) {
                if (f.isPass()) {
                    throw new RuntimeException("此Flow已经通关了,不能重复攻击!" + flowName);
                }
                return genFlowLooters(f);
            }
        }
        throw new RuntimeException("改场景没有找到对应的FLow!" + flowName);
    }

    private List<Looter> genFlowLooters(Flow f) {
        List<String> list = f.getLooters();
        for (String exp : list) {
            String[] kv = exp.trim().split(";");
            String looterCode = kv[0];
            String numberExp = kv[1];
            int count = explainNumberExp(numberExp);
            List<Looter> result = new ArrayList<>();
            for (int i = 0 ; i < count ; i ++) {
                Looter looter = LooterRegisterCenter.generateLooter(looterCode);
                result.add(looter);
            }
            return result;
        }
        throw new RuntimeException("没有找到looter表达式!" + f.getName());
    }

    /**
     * 解析数量表达式,目前只支持整数开闭区间
     * 例如: [1,2], (2, 3], [1, 4)
     */
    private int explainNumberExp(String exp) {
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
    public void check() throws RuntimeException {
        if (StringUtil.isEmpty(name)) {
            throw new RuntimeException("Scene不能没有scene!");
        }
        if (CollectionUtil.isEmpty(flow)) {
            throw new RuntimeException("Scene不能没有flow!");
        }
        for (Flow f : flow) {
            f.check();
        }
    }

}
