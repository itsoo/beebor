package org.beeborframework.ioc.context;

import org.beeborframework.ioc.bean.BeanDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * ContextHolder
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/17 12:44
 */
public class ContextHolder {

    private static final Map<Class<?>, Object> IVK_INSTANCE_CACHE = new HashMap<>();

    private static final Map<String, BeanDefinition> BEAN_DEFINITION_CACHE = new HashMap<>();


    private ContextHolder() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Register invoke-handler
     *
     * @param classType classType
     * @param func      Function
     * @return Object
     */
    public static Object computeIvkInstance(Class<?> classType, Function<Class<?>, Object> func) {
        return IVK_INSTANCE_CACHE.computeIfAbsent(classType, func);
    }

    /**
     * Register singleton bean
     *
     * @param beanDefinition BeanDefinition
     * @return BeanDefinition
     */
    public static BeanDefinition computeBeanDefinition(BeanDefinition beanDefinition) {
        return BEAN_DEFINITION_CACHE.computeIfAbsent(beanDefinition.getBeanName(), k -> beanDefinition);
    }
}
