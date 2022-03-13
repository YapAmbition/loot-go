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

    /**
     * 进入指定的Flow,执行Flow的executeXXX方法
     * @return 是否进入成功,如果返回false则表示进入失败,游戏结束
     */
    public boolean interFlow(IntrudeContext intrudeContext, String flowName) {
        for (Flow f : flow) {
            if (f.getName().equalsIgnoreCase(flowName)) {
                if (f.isPass()) {
                    throw new RuntimeException("此Flow已经通关了,不能重复进入!" + flowName);
                }
                return f.executeBattle(intrudeContext);
            }
        }
        throw new RuntimeException("改场景没有找到对应的FLow!" + flowName);
    }

    /**
     * 这个场景是否全部通过
     */
    public boolean passAll() {
        for (Flow f : flow) {
            if (!f.isPass()) {
                return false;
            }
        }
        return true;
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
