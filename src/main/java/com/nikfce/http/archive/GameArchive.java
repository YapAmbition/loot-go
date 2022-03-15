package com.nikfce.http.archive;

import com.nikfce.http.user.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 游戏存档
 * @author shenzhencheng 2022/3/14
 */
public class GameArchive {

    /**
     * 所有的存档
     * {用户token: [已经过的游戏场景]}
     */
    private static final Map<String, UserSpace> STORE = new ConcurrentHashMap<>();

    /**
     * 存档
     * 记住,这里的存档是存下现在UserSpace的快照!!!
     * 也就是说,这里的UserSpace应该是一个深拷贝!!!
     */
    public static void save(UserSpace userSpace) {
        UserSpace snapshot = userSpace.snapshot();
        STORE.put(snapshot.getUser().getToken(), snapshot);
    }

    /**
     * 加载存档,这里也要返回一个深拷贝
     */
    public static UserSpace load(String token) {
        if (!STORE.containsKey(token)) {
            return null;
        }
        return STORE.get(token).snapshot();
    }

    /**
     * 删除token对应的用户的存档
     * @param token 用户的token
     */
    public static void remove(String token) {
        if (token != null) {
            STORE.remove(token);
        }
    }

    /**
     * 列出用户的存档
     */
    public static List<User> userList() {
        List<User> result = new ArrayList<>();
        Set<String> tokenSet = STORE.keySet();
        for (String token : tokenSet) {
            User user = new User();
            user.setName(STORE.get(token).getUser().getName());
            user.setToken(token);
            result.add(user);
        }
        result.sort(Comparator.comparing(User::getName));
        return result;
    }

}
