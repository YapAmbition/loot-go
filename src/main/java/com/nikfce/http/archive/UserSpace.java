package com.nikfce.http.archive;

import com.nikfce.action.Skill;
import com.nikfce.register.SkillRegisterCenter;
import com.nikfce.role.Looter;
import com.nikfce.scene.Scene;
import com.nikfce.http.user.User;
import com.nikfce.util.StringUtil;

import java.util.*;

/**
 * 用户空间,里边记录了用户的一些行动
 * @author shenzhencheng 2022/3/14
 */
public class UserSpace {

    /**
     * 用户
     */
    private User user;
    /**
     * 当前正在访问的场景,打完flow之后会记录当前Scene
     */
    private String currentScene;
    /**
     * 用户操纵的looter
     */
    private Looter looter;
    /**
     * 是否失败
     */
    private boolean lose = false;
    /**
     * 记录了该用户已经去过的Scene(无论是否通关该场景都要记录一下)
     */
    private Map<String, Scene> goneSceneMap = new HashMap<>();

    public UserSpace() {

    }

    public UserSpace(User user) {
        if (user == null || StringUtil.isEmpty(user.getName()) || StringUtil.isEmpty(user.getToken())) {
            throw new RuntimeException("用户名和用户token不能为空");
        }
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Map<String, Scene> getGoneSceneMap() {
        return goneSceneMap;
    }

    public void setGoneSceneMap(Map<String, Scene> goneSceneMap) {
        this.goneSceneMap = goneSceneMap;
    }

    public String getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(String currentScene) {
        this.currentScene = currentScene;
    }

    public Looter getLooter() {
        return looter;
    }

    public void setLooter(Looter looter) {
        this.looter = looter;
    }

    public boolean isLose() {
        return lose;
    }

    public void setLose(boolean lose) {
        this.lose = lose;
    }

    /**
     * 获得当前对象的深拷贝
     */
    public UserSpace snapshot() {
        UserSpace userSpace = new UserSpace();
        userSpace.setUser(copyUser());
        userSpace.setLooter(copyLooter());
        userSpace.setCurrentScene(currentScene);
        userSpace.setLose(lose);
        userSpace.setGoneSceneMap(copyGoneSceneMap());
        return userSpace;
    }

    private Map<String, Scene> copyGoneSceneMap() {
        if (goneSceneMap == null) return null;
        Map<String, Scene> copy = new HashMap<>();
        for (String sceneName : goneSceneMap.keySet()) {
            copy.put(sceneName, goneSceneMap.get(sceneName).snapshot());
        }
        return copy;
    }

    private Looter copyLooter() {
        if (looter == null) return null;
        Looter copy = new Looter();
        copy.setName(looter.getName());
        copy.setCode(looter.getCode());
        copy.setBasicProperties(looter.copyBasicProperties());
        copy.setEffectProperties(looter.copyEffectProperties());
        copy.setTmpProperties(looter.copyTmpProperties());
        List<Skill> copySkillList = new ArrayList<>();
        for (Skill skill : looter.getSkillList()) {
            String skillCode = skill.code();
            Skill copySkill = SkillRegisterCenter.generateSkill(skillCode);
            copySkillList.add(copySkill);
        }
        copy.setSkillList(copySkillList);
        return copy;
    }

    private User copyUser() {
        User copy = new User();
        copy.setName(user.getName());
        copy.setToken(user.getToken());
        return copy;
    }

    @Override
    public int hashCode() {
        return user == null ? -1 : user.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UserSpace && Objects.equals(user, ((UserSpace) obj).user);
    }

}
