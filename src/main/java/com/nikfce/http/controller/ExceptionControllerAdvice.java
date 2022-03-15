package com.nikfce.http.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * 异常统一处理
 * @author shenzhencheng 2022/3/15
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(value = Exception.class)
    public Object dealException(Throwable ex, WebRequest req) throws Throwable {
        String uri = "unknown-uri";
        try {
            uri = ((ServletWebRequest) req).getRequest().getRequestURI();
            throw ex;
        } catch (Throwable throwable) {
            LOG.error("访问接口出错, 接口: {}", uri, ex);
            return new ResponseEntity<>(throwable.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
