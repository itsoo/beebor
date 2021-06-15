package org.beeborframework.core.util;

import lombok.SneakyThrows;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ObjectUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/18 12:23
 */
public class ObjectUtils {

    private ObjectUtils() {
        throw new IllegalStateException("Utility class");
    }

    @SneakyThrows(ReflectiveOperationException.class)
    public static Class<?> getFieldType(Object instance, String fieldName) {
        Field field = instance.getClass().getDeclaredField(fieldName);
        return field.getType();
    }

    public static void setFieldValue(Object instance, String fieldName, Object value) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(fieldName, instance.getClass());
            Method method = pd.getWriteMethod();
            method.invoke(instance, value);
        } catch (ReflectiveOperationException | IntrospectionException ignore) {}
    }

    public static Object getFieldValue(Object instance, String fieldName) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(fieldName, instance.getClass());
            Method method = pd.getReadMethod();
            return method.invoke(instance);
        } catch (ReflectiveOperationException | IntrospectionException e) {
            return null;
        }
    }
}
