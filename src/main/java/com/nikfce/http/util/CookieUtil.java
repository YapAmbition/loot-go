package com.nikfce.http.util;

import com.nikfce.config.LootConfig;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author shenzhencheng 2022/3/14
 */
public class CookieUtil {

    /**
     * 从cookie中获取用户token
     */
    public static String getTokenFromCookie() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        Cookie cookie = WebUtils.getCookie(httpServletRequest, LootConfig.getInstance().getTokenCookieKey());
        if (cookie == null) {
            return null;
        }
        return cookie.getValue();
    }

}
