package com.nikfce.http.user;

import lombok.Data;

/**
 * 用户类
 * name由用户输入
 * token由系统自动生成,需要保证全局唯一
 * @author shenzhencheng 2022/3/14
 */
@Data
public class User {

    private String name;
    private String token;

}
