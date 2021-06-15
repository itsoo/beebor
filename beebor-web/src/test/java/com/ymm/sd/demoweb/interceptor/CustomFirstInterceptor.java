package com.ymm.sd.demoweb.interceptor;

import org.beeborframework.web.interceptor.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CustomFirstInterceptor
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/14 22:42
 */
public class CustomFirstInterceptor implements Interceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("这是自定义拦截器1 ==== preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("这是自定义拦截器1 ==== postHandle");
    }
}
