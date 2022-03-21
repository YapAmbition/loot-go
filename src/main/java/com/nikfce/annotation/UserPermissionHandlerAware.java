package com.nikfce.annotation;

import com.nikfce.http.dto.CommonResponse;
import com.nikfce.http.util.CookieUtil;
import com.nikfce.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 验证用户请求是否有带Cookie,如果没有的话,返回401错误
 * @author shenzhencheng 2022/3/21
 */
@Aspect
@Component
public class UserPermissionHandlerAware {

    @Around(value = "@annotation(userPermission)")
    public Object checkUserPermission(ProceedingJoinPoint joinPoint, UserPermission userPermission) throws Throwable {
        String lootCookie = CookieUtil.getTokenFromCookie();
        if (StringUtil.isEmpty(lootCookie)) {
            return new CommonResponse.CommonResponseBuilder<>().setCode(401).setType("no-permission").setErrMsg("没有注册用户!").build();
        } else {
            return joinPoint.proceed();
        }
    }

}
