package org.beeborframework.web.handler;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.beeborframework.web.core.RequestMethod.RequestType;

/**
 * HandlerInvoker
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/12 16:04
 */
@Getter
@ToString
public class HandlerInvoker {

    private final RequestType requestType;

    private final String path;

    private final Class<?> classType;

    private final Method handleMethod;

    private final Parameter[] paramTypes;

    private final Object instance;


    public HandlerInvoker(
            RequestType requestType, String path, Class<?> classType, Method handleMethod, Object instance) {

        this.requestType = requestType;
        this.path = path;
        this.classType = classType;
        this.handleMethod = handleMethod;
        this.paramTypes = handleMethod.getParameters();
        this.instance = instance;
    }

    /**
     * Request handler-method invoke
     *
     * @param args Object...
     * @return Object
     */
    @SneakyThrows(ReflectiveOperationException.class)
    public Object invoke(Object... args) {
        return getHandleMethod().invoke(getInstance(), args);
    }
}
