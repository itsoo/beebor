package org.beeborframework.web.core;

import lombok.Getter;
import org.beeborframework.core.util.AnnotationUtils;
import org.beeborframework.core.util.ClassUtils;
import org.beeborframework.core.util.ServletUtils;
import org.beeborframework.web.exception.RepeatedHandlerMappingException;
import org.beeborframework.web.factory.HandlerInvokerFactory;
import org.beeborframework.web.handler.HandleInvoker;
import org.beeborframework.web.lang.rest.RestAction;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * HandlerMapping
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/12 8:43
 */
public class HandlerMapping {

    @Getter
    private final Map<String, HandleInvoker> mapping;


    private HandlerMapping() {
        this.mapping = new HashMap<>(1 << 6);
    }

    /**
     * Get handler-mapping instance
     *
     * @param allBeans Collection of Object
     * @return HandlerMapping
     */
    public static HandlerMapping newInstance(Collection<Object> allBeans) {
        HandlerMapping result = new HandlerMapping();
        // Filter @RestAction bean of all-beans
        for (Object bean : allBeans) {
            Class<?> clazz = bean.getClass();
            RestAction ann = AnnotationUtils.findAnnotation(clazz, RestAction.class);
            if (Objects.isNull(ann)) {
                continue;
            }

            for (Method method : ClassUtils.getMethods(clazz)) {
                // Not request method
                if (!AssertBeforeRegister.isRequestMethod(method)) {
                    continue;
                }
                // Request method cannot has 2+ @Body annotations
                AssertBeforeRegister.assertRequestMethod(method);
                // Get handler-invoker
                HandlerInvokerFactory factory = HandlerInvokerFactory.getInstance(clazz, method, bean);
                for (String requestPath : factory.getRequestPaths()) {
                    for (String basePath : ann.path()) {
                        factory.setStanderPath(ServletUtils.processStanderPath(basePath, requestPath));
                        HandleInvoker target = factory.getObject();
                        HandleInvoker exists = result.getMapping().putIfAbsent(factory.getStanderPath(), target);
                        // Cannot has same request mapping and request type
                        if (Objects.nonNull(exists)) {
                            throw new RepeatedHandlerMappingException(exists, target);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Register request mapping
     *
     * @param methodName String
     * @param requestUri String
     * @return HandlerInvoker
     */
    public HandleInvoker getHandlerInvoker(String methodName, String requestUri) {
        // Supported REST request
        return mapping.get(methodName + '+' + ServletUtils.processStanderPath(requestUri));
    }
}
