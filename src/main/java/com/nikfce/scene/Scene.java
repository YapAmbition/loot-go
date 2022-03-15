package com.nikfce.scene;

import com.nikfce.util.CollectionUtil;
import com.nikfce.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 地图类
 * @author shenzhencheng 2022/3/12
 */
public class Scene implements Checkable {

    private String name;
    private String desc;
    private List<Flow> flow;
    private List<ClearCondition> clearCondition;

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

    public List<ClearCondition> getClearCondition() {
        return clearCondition;
    }

    public void setClearCondition(List<ClearCondition> clearCondition) {
        this.clearCondition = clearCondition;
    }

    /**
     * 返回当前地图能展示的Flow
     */
    public List<Flow> nextFlows(IntrudeContext intrudeContext) {
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
        throw new RuntimeException("该地图没有找到对应的FLow!" + flowName);
    }

    /**
     * 这个地图是否清理完毕
     * @return 如果清理完毕,则返回清理完毕的描述,否则返回null
     */
    public String clearScene(IntrudeContext intrudeContext) {
        for (ClearCondition cc : clearCondition) {
            String desc = cc.satisfy(intrudeContext, this);
            if (desc != null) {
                return desc;
            }
        }
        return null;
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

    @Override
    public int hashCode() {
        return name == null ? -1 : name.hashCode();
    }

    /**
     * 重写equals方法和hashCode方法,在存档时,只要name相同,就认为是同一个Scene
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Scene && Objects.equals(name, ((Scene)obj).name);
    }

    /**
     * 生成一个当前对象的快照(深拷贝)
     */
    public Scene snapshot() {
        Scene copy = new Scene();
        copy.setName(name);
        copy.setDesc(desc);
        copy.setFlow(copyFlow());
        copy.setClearCondition(copyClearCondition());
        return copy;
    }

    private List<ClearCondition> copyClearCondition() {
        if (clearCondition == null) return null;
        List<ClearCondition> copy = new ArrayList<>();
        for (ClearCondition cc : clearCondition) {
            copy.add(cc.snapshot());
        }
        return copy;
    }

    private List<Flow> copyFlow() {
        if (flow == null) return null;
        List<Flow> copy = new ArrayList<>();
        for (Flow f : flow) {
            Flow copyFlow = f.snapshot();
            copy.add(copyFlow);
        }
        return copy;
    }
}
