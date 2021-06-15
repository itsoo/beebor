package org.beeborframework.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JsonUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 21:18
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper DEFAULT_MAPPER =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final ObjectMapper IGNORE_NULL_VALUE_MAPPER =
            new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static final Class<?>[] BOXING_TYPES = {Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class};

    private static final Set<Class<?>> INCONVERTIBLE_CLASSES = new HashSet<>(
            ArrayUtils.concatOfList(BOXING_TYPES, Void.class, String.class));


    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isConvertibleClass(Class<?> clazz) {
        return !clazz.isPrimitive() && !clazz.isAnnotation()
                && CollectionUtils.allMatch(INCONVERTIBLE_CLASSES, t -> !t.isAssignableFrom(clazz));
    }

    @SneakyThrows(IOException.class)
    public static <T> T parseObject(String json, Class<T> clazz) {
        return DEFAULT_MAPPER.readValue(json, clazz);
    }

    @SneakyThrows(IOException.class)
    public static <T> T parseObject(String json, JavaType javaType) {
        return DEFAULT_MAPPER.readValue(json, javaType);
    }

    @SneakyThrows(IOException.class)
    public static String toJsonString(Object params) {
        return DEFAULT_MAPPER.writeValueAsString(params);
    }

    @SneakyThrows(IOException.class)
    public static String toJsonStringIgnoreNull(Object params) {
        return IGNORE_NULL_VALUE_MAPPER.writeValueAsString(params);
    }

    @SneakyThrows(IOException.class)
    public static <T> List<T> parseList(String json, Class<T> clazz) {
        return DEFAULT_MAPPER.readValue(json, getListType(clazz));
    }

    @SneakyThrows(IOException.class)
    public static <T> List<T> parseList(String json, JavaType javaType) {
        return DEFAULT_MAPPER.readValue(json, javaType);
    }

    public static JavaType getListType(Class<?> clazz) {
        return DEFAULT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
    }

    public static JavaType getObjectType(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) genericType;
            Type[] actualTypes = paramType.getActualTypeArguments();
            JavaType[] javaTypes = new JavaType[actualTypes.length];
            for (int i = 0; i < actualTypes.length; i++) {
                javaTypes[i] = getObjectType(actualTypes[i]);
            }

            return getParametricType(paramType.getRawType(), javaTypes);
        }

        return getParametricType(genericType, new JavaType[0]);
    }

    private static JavaType getParametricType(Type rawType, JavaType[] javaTypes) {
        return TypeFactory.defaultInstance().constructParametricType((Class<?>) rawType, javaTypes);
    }
}
