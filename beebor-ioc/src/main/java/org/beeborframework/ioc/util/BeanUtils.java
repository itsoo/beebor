package org.beeborframework.ioc.util;

import org.beeborframework.core.util.ClassUtils;
import org.beeborframework.core.util.StringUtils;
import org.beeborframework.ioc.lang.Bean;
import org.beeborframework.ioc.lang.Inject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * BeanUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/13 12:24
 */
public class BeanUtils {

    private BeanUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getBeanName(Inject ann, Field field) {
        return getBeanName(ann, field.getType());
    }

    public static String getBeanName(Inject ann, Parameter param) {
        return getBeanName(ann, param.getType());
    }

    public static String getBeanName(Inject ann, Class<?> clazz) {
        return StringUtils.isBlank(ann.value()) ? ClassUtils.getPropertyName(clazz) : ann.value();
    }

    public static String getBeanName(Bean ann, Method method) {
        return getBeanName(ann, method.getReturnType());
    }

    public static String getBeanName(Bean ann, Class<?> clazz) {
        return getBeanName(ann.value(), clazz);
    }

    public static String getBeanName(String beanName, Class<?> clazz) {
        return StringUtils.isBlank(beanName) ? ClassUtils.getPropertyName(clazz) : beanName;
    }
}
