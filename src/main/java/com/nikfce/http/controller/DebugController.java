package com.nikfce.http.controller;

import com.nikfce.http.archive.GameArchive;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenzhencheng 2022/3/15
 */
@RestController
@RequestMapping("/debug")
public class DebugController {

    /**
     * 展示都有哪些用户
     */
    @RequestMapping("/archiveList")
    public Object archiveList() {
        return GameArchive.userList();
    }

    /**
     * 删除一个用户的存档
     */
    @RequestMapping("/remove")
    public Object removeArchive(String token) {
        GameArchive.remove(token);
        return true;
    }

    /**
     * 查看一个用户的存档
     */
    @RequestMapping("/query")
    public Object query(String token) {
        return GameArchive.load(token);
    }

}
