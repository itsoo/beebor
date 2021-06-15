package org.beeborframework.web;

import lombok.SneakyThrows;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.beeborframework.core.resource.Environment;
import org.beeborframework.core.util.UriUtils;
import org.beeborframework.ioc.context.ApplicationContext;
import org.beeborframework.web.context.WebApplicationContext;
import org.beeborframework.web.core.HandlerMapping;
import org.beeborframework.web.exception.RepeatedServletContainerException;
import org.beeborframework.web.handler.DispatcherHandler;
import org.beeborframework.web.interceptor.Interceptor;
import org.beeborframework.web.interceptor.InterceptorRegister;

import java.util.Arrays;
import java.util.Objects;

/**
 * StartLauncher
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/11 13:15
 */
public class StartLauncher {

    private final Class<?> appClass;

    private final ApplicationContext context;

    private InterceptorRegister interceptorRegister;

    private static Tomcat tomcat;

    private static Environment env;


    private StartLauncher(Class<?> appClass) {
        this.appClass = appClass;
        this.context = new WebApplicationContext(appClass);
        this.context.refresh();
    }

    /**
     * Web application starter
     *
     * @param appClass main-class
     */
    public static void start(Class<?> appClass) {
        start(appClass, InterceptorRegister.newInstance());
    }

    /**
     * Web application starter
     *
     * @param appClass main-class
     * @param register InterceptorRegister
     */
    public static void start(Class<?> appClass, InterceptorRegister register) {
        if (Objects.nonNull(register)) {
            start(appClass, register.getInterceptors().toArray(new Interceptor[0]));
        } else {
            start(appClass);
        }
    }

    /**
     * (private) Web application starter
     *
     * @param appClass     main-class
     * @param interceptors Interceptor...
     */
    @SneakyThrows(LifecycleException.class)
    private static void start(Class<?> appClass, Interceptor... interceptors) {
        StartLauncher startLauncher = startLauncher(appClass);
        startLauncher.listenerRequestContext(env.getApplicationContext());
        startLauncher.registerInterceptors(interceptors);
        tomcat.start();
        tomcat.getServer().await();
    }

    /**
     * Get global-environment
     *
     * @return Environment
     */
    public static Environment getEnv() {
        return env;
    }

    /**
     * Register interceptors
     *
     * @param interceptors Interceptor[]
     */
    private void registerInterceptors(Interceptor... interceptors) {
        if (Objects.nonNull(interceptorRegister)) {
            interceptorRegister.registerInterceptor(Arrays.asList(interceptors));
        }
    }

    /**
     * Instance start-launcher
     *
     * @param appClass Class
     * @return StartLauncher
     */
    private static StartLauncher startLauncher(Class<?> appClass) {
        // Tomcat can only be initialized once
        if (Objects.nonNull(tomcat)) {
            throw new RepeatedServletContainerException();
        }
        // Set environment
        env = Environment.getInstance(appClass);
        // Set tomcat
        tomcat = new Tomcat();
        tomcat.setPort(env.getServerPort());
        tomcat.getConnector();
        return new StartLauncher(appClass);
    }

    /**
     * Register request-context
     *
     * @param appContext String
     */
    private void listenerRequestContext(String appContext) {
        // Initial handler-mapping
        HandlerMapping handlerMapping = HandlerMapping.newInstance(context.getAllBeans());
        Context ctx = tomcat.addContext(appContext, UriUtils.getBaseDir(appClass));
        ctx.setReloadable(false);
        // Core dispatcher-handler
        DispatcherHandler defaultHandler = new DispatcherHandler(handlerMapping);
        // Initial interceptors of application
        interceptorRegister = defaultHandler.getInterceptorRegister();
        // Mapping dispatcher servlet
        Tomcat.addServlet(ctx, defaultHandler.defaultName, defaultHandler);
        ctx.addServletMapping("/", defaultHandler.defaultName);
    }
}
