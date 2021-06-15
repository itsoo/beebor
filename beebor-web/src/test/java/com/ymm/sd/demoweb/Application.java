package com.ymm.sd.demoweb;

import com.ymm.sd.demoweb.interceptor.CustomFirstInterceptor;
import com.ymm.sd.demoweb.interceptor.CustomSecondInterceptor;
import org.beeborframework.web.StartLauncher;
import org.beeborframework.web.interceptor.InterceptorRegister;
import org.junit.Test;

/**
 * Application
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 17:14
 */
public class Application {

    @Test
    public void testStart() {
        InterceptorRegister register = InterceptorRegister.newInstance();
        register.registerInterceptor(new CustomFirstInterceptor());
        register.registerInterceptor(new CustomSecondInterceptor());

        StartLauncher.start(Application.class, register);
    }
}
