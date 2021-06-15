package org.beeborframework.web.factory;

import lombok.Getter;
import org.beeborframework.core.util.AnnotationUtils;
import org.beeborframework.core.util.Assert;
import org.beeborframework.ioc.factory.Factory;
import org.beeborframework.web.core.RequestMethod;
import org.beeborframework.web.handler.HandlerInvoker;
import org.beeborframework.web.lang.rest.Delete;
import org.beeborframework.web.lang.rest.Get;
import org.beeborframework.web.lang.rest.Post;
import org.beeborframework.web.lang.rest.Put;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.beeborframework.web.core.RequestMethod.RequestType.*;

/**
 * HandlerInvokerFactory
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 15:18
 */
public class HandlerInvokerFactory implements Factory<HandlerInvoker> {

    private final Class<?> classType;

    private final Method method;

    private final Object instance;

    private final RequestMethod rm;

    @Getter
    private String standerPath;


    private HandlerInvokerFactory(RequestMethod rm, Class<?> classType, Method method, Object instance) {
        this.rm = rm;
        this.classType = classType;
        this.method = method;
        this.instance = instance;
    }

    public static HandlerInvokerFactory getInstance(Class<?> clazz, Method method, Object instance) {
        return new HandlerInvokerFactory(getRequestMethod(method), clazz, method, instance);
    }

    @Override
    public HandlerInvoker getObject() {
        Assert.notNull(standerPath, "'standerPath' cannot be null");
        return new HandlerInvoker(rm.getRequestType(), standerPath, classType, method, instance);
    }

    public String[] getRequestPaths() {
        return Optional.ofNullable(rm.getPaths()).orElse(new String[0]);
    }

    public void setStanderPath(String standerPath) {
        // Supported REST request
        this.standerPath = (rm.getRequestType().name() + '+' + standerPath);
    }

    private static RequestMethod getRequestMethod(Method method) {
        if (AnnotationUtils.hasAnnotation(method, Get.class)) {
            return RequestMethod.getInstance(GET, AnnotationUtils.getAnnotation(method, Get.class).value());
        } else if (AnnotationUtils.hasAnnotation(method, Post.class)) {
            return RequestMethod.getInstance(POST, AnnotationUtils.getAnnotation(method, Post.class).value());
        } else if (AnnotationUtils.hasAnnotation(method, Put.class)) {
            return RequestMethod.getInstance(PUT, AnnotationUtils.getAnnotation(method, Put.class).value());
        } else if (AnnotationUtils.hasAnnotation(method, Delete.class)) {
            return RequestMethod.getInstance(DELETE, AnnotationUtils.getAnnotation(method, Delete.class).value());
        } else {
            return RequestMethod.getInstance(NONE, null);
        }
    }
}
