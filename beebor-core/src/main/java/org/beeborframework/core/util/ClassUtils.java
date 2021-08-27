package org.beeborframework.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * ClassUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/12 16:29
 */
public class ClassUtils {

    private static final String VALUE_METHOD_NAME = "value";


    private ClassUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Constructor<?>[] getConstructors(Class<?> clazz) {
        Constructor<?>[] result = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : result) {
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
        }

        return result;
    }

    public static Method[] getMethods(Class<?> clazz) {
        Method[] result = clazz.getDeclaredMethods();
        for (Method method : result) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
        }

        return result;
    }

    public static Method getMethod(Class<?> clazz, String methodName) {
        for (Method method : getMethods(clazz)) {
            if (Objects.equals(methodName, method.getName())) {
                return method;
            }
        }

        return null;
    }

    public static Method getNoArgsValueMethod(Class<?> clazz) {
        return getMethod(clazz, VALUE_METHOD_NAME);
    }

    public static Field[] getFields(Class<?> clazz) {
        Field[] result = clazz.getDeclaredFields();
        for (Field field : result) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
        }

        return result;
    }

    public static String getPropertyName(Class<?> clazz) {
        return StringUtils.lowerFirstLetter(clazz.getSimpleName());
    }

    public static Parameter[] getParameters(Method method) {
        return method.getParameters();
    }

    public static Parameter[] getParameters(Constructor<?> constructor) {
        return constructor.getParameters();
    }
}
