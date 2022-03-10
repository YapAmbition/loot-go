package com.nikfce.stage;

import com.nikfce.role.Looter;
import com.nikfce.util.CollectionUtil;

import java.util.*;

/**
 * 瓶子行动计划
 *
 * 每次一执行next的时候,每个looter都会用自己的当前速度值填充一个一定大小的容器
 * 找到所有填充满瓶子的looter,比较它们的填充值,找到填充值最大的那个looter,把它的填充值减去容器容量,并返回这个looter
 * 如果找到了多个填充值相同的looter,则比较它们的luck,如果luck相同,则随机取一个
 * @author shenzhencheng 2022/3/2
 */
public class BottleActionPlan implements ActionPlan {

    private final List<Looter> looterList;
    private final Map<Looter, Double> fillValueMap = new HashMap<>();
    private double capacity = Double.MIN_VALUE;

    public BottleActionPlan(List<Looter> looterList) {
        this.looterList = looterList;
        init();
    }

    /**
     * 初始化各个looter的瓶子(用速度值来填充)
     * 用looter的最大速度来初始化瓶子容量
     */
    private void init() {
        for (Looter looter : looterList) {
            fillValueMap.put(looter, looter.currentSpeed());
            if (looter.currentSpeed() > capacity) {
                capacity = looter.currentSpeed();
            }
        }
    }

    /**
     * 找到填充值超过或等于容量的looter列表
     *      如果列表为空,则每个looter用自己的速度填充自己的瓶子
     *      递归next()
     * 找到填充值最高的列表
     *      如果列表中只有一个,则返回
     * 找到luck最高的列表
     *      如果列表中只有一个,则返回
     * 随机返回一个
     */
    @Override
    public Looter next() {
        List<Looter> overflowList = findAliveOverflowList();
        if (CollectionUtil.isEmpty(overflowList)) {
            fillBottle();
            return next();
        }

        List<Looter> mostFillList = findMostFillList(overflowList);
        if (mostFillList.size() == 1) {
            eliminate(mostFillList.get(0));
            return mostFillList.get(0);
        }

        List<Looter> mostLuckList = findMostLuckList(mostFillList);
        if (mostLuckList.size() == 1) {
            eliminate(mostLuckList.get(0));
            return mostLuckList.get(0);
        }

        Looter randomLooter = randomLooter(mostLuckList);
        eliminate(randomLooter);
        return randomLooter;
    }

    /**
     * 把一个looter的填充值减少capacity,相当于消减一个瓶子,保留溢出部分
     */
    private void eliminate(Looter looter) {
        fillValueMap.put(looter, fillValueMap.get(looter) - capacity);
    }

    private Looter randomLooter(List<Looter> looterList) {
        return looterList.get(new Random().nextInt(looterList.size()));
    }

    private List<Looter> findMostLuckList(List<Looter> looterList) {
        List<Looter> result = new ArrayList<>();
        double maxLuck = looterList.get(0).currentLuck();
        for (int i = 1 ; i < looterList.size() ; i ++) {
            if (looterList.get(i).currentLuck() > maxLuck) {
                maxLuck = looterList.get(i).currentLuck();
            }
        }
        for (Looter looter : looterList) {
            if (looter.currentLuck() == maxLuck) {
                result.add(looter);
            }
        }
        return result;
    }

    /**
     * 从列表中找到填充值最高的列表
     */
    private List<Looter> findMostFillList(List<Looter> overflowList) {
        List<Looter> result = new ArrayList<>();
        double mostFill = Double.MIN_VALUE;
        for (Looter value : overflowList) {
            if (fillValueMap.get(value) > mostFill) {
                mostFill = fillValueMap.get(value);
            }
        }
        for (Looter looter : overflowList) {
            if (fillValueMap.get(looter) == mostFill) {
                result.add(looter);
            }
        }
        return result;
    }

    /**
     * 每个looter都填充自己的瓶子
     */
    private void fillBottle() {
        fillValueMap.replaceAll((looter, fillValue) -> fillValue + (looter.isDead() ? 0 : Math.max(looter.currentSpeed(), 0)));
    }

    /**
     * 找到活着的,且填充值超过或等于容量的looter列表
     */
    private List<Looter> findAliveOverflowList() {
        List<Looter> result = new ArrayList<>();
        for (Looter looter : fillValueMap.keySet()) {
            if (!looter.isDead() && fillValueMap.get(looter) >= capacity) {
                result.add(looter);
            }
        }
        return result;
    }

}
