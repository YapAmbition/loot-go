package com.nikfce.http.handler;

import com.nikfce.http.archive.GameArchive;
import com.nikfce.http.archive.UserSpace;
import com.nikfce.http.dto.*;
import com.nikfce.http.request.FlowResponse;
import com.nikfce.http.user.TokenGenerator;
import com.nikfce.http.user.User;
import com.nikfce.http.util.CookieUtil;
import com.nikfce.recoder.BufferRecorder;
import com.nikfce.register.LooterRegisterCenter;
import com.nikfce.register.SceneRegisterCenter;
import com.nikfce.role.Looter;
import com.nikfce.scene.Flow;
import com.nikfce.scene.IntrudeContext;
import com.nikfce.scene.Scene;
import com.nikfce.thread.ThreadLocalMap;
import com.nikfce.util.StringUtil;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author shenzhencheng 2022/3/14
 */
@Component
public class GameHandler {

    /**
     * 注册用户,返回token
     */
    public String register(String name) {
        User user = new User();
        user.setName(name.trim());
        String token = TokenGenerator.nextToken();
        user.setToken(token);
        UserSpace userSpace = new UserSpace(user);
        // 保存这个用户的游戏进度
        GameArchive.save(userSpace);
        return token;
    }

    /**
     * 用户选择looter
     */
    public void choiceLooter(String looterCode) {
        UserSpace userSpace = getUserSpace();
        if (userSpace.getLooter() != null) {
            throw new RuntimeException("你已经选过Looter了,如果要重选的话,请联系并向开发者示好~");
        }
        Looter looter = LooterRegisterCenter.generateLooter(looterCode);
        userSpace.setLooter(looter);
        GameArchive.save(userSpace);
    }

    /**
     * 为用户展示接下来要看到的场景
     */
    public NextDto next() {
        UserSpace userSpace = getUserSpace();
        // 先判断是否选了Looter,如果没选则返回角色列表让用户选择
        if (userSpace.getLooter() == null) {
            List<LooterDTO> looterDTOList = new ArrayList<>();
            Set<String> looterCodeSet = LooterRegisterCenter.listLooters();
            for (String looterCode : looterCodeSet) {
                Looter looter = LooterRegisterCenter.generateLooter(looterCode);
                LooterDTO looterDTO = LooterDTO.generateByLooter(looter);
                looterDTOList.add(looterDTO);
            }
            NextDto nextDto = new NextDto();
            nextDto.setType("looter");
            nextDto.setDesc("选择属于你的Looter");
            nextDto.setLooterList(looterDTOList);
            return nextDto;
        } else if (userSpace.isLose()) {
            NextDto nextDto = new NextDto();
            nextDto.setType("lose");
            nextDto.setDesc(String.format("很遗憾, %s, 你就是这样不堪一击? hhhhh...", userSpace.getUser().getName()));
            return nextDto;
        } else if (allClear(userSpace)) {
            // 如果所有Scene都被清理了
            NextDto nextDto = new NextDto();
            nextDto.setType("allClear");
            nextDto.setDesc(String.format("恭喜你, %s, 你通关了,但是你以为Looter的世界就到此为止了吗? hhhhh...", userSpace.getUser().getName()));
            return nextDto;
        } else if (userSpace.getCurrentScene() == null) {
            // 然后判断是否已经进入某个场景了,如果没进则返回场景列表让用户选择
            // 表示用户还没有进入任何场景,需要返回场景供用户选择
            List<SceneDTO> sceneDTOList = new ArrayList<>();
            for (String sceneName : SceneRegisterCenter.showSceneList()) {
                Scene scene = SceneRegisterCenter.generateScene(sceneName);
                SceneDTO sceneDTO = SceneDTO.generateByScene(scene);
                sceneDTOList.add(sceneDTO);
            }
            NextDto nextDto = new NextDto();
            nextDto.setType("scene");
            nextDto.setDesc("选择要突突的地图");
            nextDto.setSceneList(sceneDTOList);
            return nextDto;
        } else {
            // 展示当前场景的Flow
            Scene currentScene = userSpace.getGoneSceneMap().get(userSpace.getCurrentScene());
            if (currentScene == null) {
                userSpace.setCurrentScene(null);
                GameArchive.save(userSpace);
                throw new RuntimeException("该用户的当前进度无效,请重新选择Scene");
            }
            List<FlowDTO> flowDTOList = new ArrayList<>();
            IntrudeContext intrudeContext = new IntrudeContext(Collections.singletonList(userSpace.getLooter()));
            List<Flow> flowList = currentScene.nextFlows(intrudeContext);
            for (Flow flow : flowList) {
                FlowDTO flowDTO = FlowDTO.generateByFlow(flow);
                flowDTOList.add(flowDTO);
            }
            NextDto nextDto = new NextDto();
            nextDto.setType("flow");
            nextDto.setDesc("选择要解决的关卡");
            nextDto.setFlowList(flowDTOList);
            return nextDto;
        }
    }

    /**
     * 选择场景
     */
    public void choiceScene(String sceneName) {
        UserSpace userSpace = getUserSpace();
        if (userSpace.getCurrentScene() != null) {
            throw new RuntimeException(String.format("你当前还在%s中探索,要退出的话,请联系并向开发者示好~", userSpace.getCurrentScene()));
        }
        // 检查合法性
        if (!SceneRegisterCenter.showSceneList().contains(sceneName)) {
            throw new RuntimeException("本游戏没有这个场景: " + sceneName);
        }
        // 设置当前scene
        userSpace.setCurrentScene(sceneName);
        // 检查之前是否去过这个Scene,如果没有去过,则加一下
        if (!userSpace.getGoneSceneMap().containsKey(sceneName)) {
            Scene scene = SceneRegisterCenter.generateScene(sceneName);
            userSpace.getGoneSceneMap().put(sceneName, scene);
        }
        GameArchive.save(userSpace);
    }

    /**
     * 退出Scene
     */
    public void exitScene() {
        UserSpace userSpace = getUserSpace();
        userSpace.setCurrentScene(null);
        GameArchive.save(userSpace);
    }

    /**
     * 选择场景flow,战斗!
     */
    public FlowResponse choiceFlow(String flow) {
        UserSpace userSpace = getUserSpace();
        if (userSpace.getCurrentScene() == null) {
            throw new RuntimeException("当前还没有进入任何场景");
        }
        // 检查合法性
        String currentScene = userSpace.getCurrentScene();
        if (!SceneRegisterCenter.showSceneList().contains(currentScene)) {
            throw new RuntimeException("本游戏根本没有这个场景啊: " + currentScene);
        }
        // 选择该场景中的指定Flow,并战斗
        if (!userSpace.getGoneSceneMap().containsKey(currentScene)) {
            userSpace.getGoneSceneMap().put(currentScene, SceneRegisterCenter.generateScene(currentScene));
        }
        Scene scene = userSpace.getGoneSceneMap().get(currentScene);
        for (Flow f : scene.getFlow()) {
            if (f.getName().equals(flow)) {
                if (f.isPass()) {
                    FlowResponse flowResponse = new FlowResponse();
                    flowResponse.setWin(true);
                    flowResponse.setLog("该房间早已被你洗劫一空了");
                    return flowResponse;
                } else {
                    StringBuilder logSb = new StringBuilder();
                    IntrudeContext intrudeContext = new IntrudeContext(Collections.singletonList(userSpace.getLooter()));
                    BufferRecorder bufferRecorder = new BufferRecorder();
                    ThreadLocalMap.setRecorder(bufferRecorder);
                    boolean win = f.executeBattle(intrudeContext);
                    logSb.append(bufferRecorder.flush());
                    ThreadLocalMap.useDefaultRecorder();
                    if (win) {
                        f.setPass(true);
                        // 检查是否所有房间都被打完了,如果是则退出Scene
                        String desc = scene.clearScene(intrudeContext);
                        if (desc != null) {
                            userSpace.setCurrentScene(null);
                            logSb.append(desc).append('\n');
                        }
                    } else {
                        userSpace.setLose(true);
                        logSb.append("很遗憾,").append(userSpace.getUser().getName()).append(", 你就是这样不堪一击? hhhhh...").append('\n');
                    }
                    GameArchive.save(userSpace);
                    FlowResponse flowResponse = new FlowResponse();
                    flowResponse.setWin(win);
                    flowResponse.setLog(logSb.toString());
                    return flowResponse;
                }
            }
        }
        throw new RuntimeException(String.format("没有找到对应的Flow, scene: %s, flow: %s", currentScene, flow));
    }

    /**
     * 展示用户所选择的looter
     */
    public LooterDTO showMyLooter() {
        UserSpace userSpace = getUserSpace();
        Looter looter = userSpace.getLooter();
        if (looter == null) {
            throw new RuntimeException("你还没有选择属于你的Looter呢");
        }
        return LooterDTO.generateByLooter(looter);
    }

    /**
     * 重开游戏,删除原来的存档
     */
    public boolean resetGame() {
        UserSpace userSpace = getUserSpace();
        GameArchive.remove(userSpace.getUser().getToken());
        return true;
    }

    /**
     * 从cookie中获取用户信息
     */
    private UserSpace getUserSpace() {
        String token = CookieUtil.getTokenFromCookie();
        if (token == null || StringUtil.isEmpty(token)) throw new RuntimeException("用户token缺失!");
        UserSpace userSpace = GameArchive.load(token);
        if (userSpace == null) throw new RuntimeException("该用户可能没有注册,请重新注册");
        return userSpace;
    }

    /**
     * 是否通过了所有的Scene
     */
    private boolean allClear(UserSpace userSpace) {
        Map<String, Scene> goneSceneMap = userSpace.getGoneSceneMap();
        if (goneSceneMap == null) return false;
        Set<String> sceneNameSet = SceneRegisterCenter.showSceneList();
        for (String sceneName : sceneNameSet) {
            if (!goneSceneMap.containsKey(sceneName)) {
                return false;
            }
            Scene scene = goneSceneMap.get(sceneName);
            IntrudeContext intrudeContext = new IntrudeContext(Collections.singletonList(userSpace.getLooter()));
            if (null == scene.clearScene(intrudeContext)) {
                return false;
            }
        }
        return true;
    }

}
