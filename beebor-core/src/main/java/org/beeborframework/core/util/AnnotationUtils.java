package org.beeborframework.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.Optional;

/**
 * AnnotationUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/12 18:43
 */
public class AnnotationUtils {

    private AnnotationUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationClass) {
        A result = findAnnotation(clazz, annotationClass);
        Assert.notNull(result, "Cannot find annotation of [" + clazz.getCanonicalName() + ']');
        return result;
    }

    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationClass) {
        return Optional.ofNullable(clazz).map(t -> t.getDeclaredAnnotation(annotationClass)).orElse(null);
    }

    public static <A extends Annotation> boolean hasAnnotation(Class<?> clazz, Class<A> annotationClass) {
        return Objects.nonNull(clazz) && clazz.isAnnotationPresent(annotationClass);
    }

    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationClass) {
        A result = findAnnotation(method, annotationClass);
        Assert.notNull(result, "Cannot find annotation of ["
                + method.getDeclaringClass().getCanonicalName() + '#' + method.getName() + ']');
        return result;
    }

    public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationClass) {
        return Optional.ofNullable(method).map(t -> t.getDeclaredAnnotation(annotationClass)).orElse(null);
    }

    public static <A extends Annotation> boolean hasAnnotation(Method method, Class<A> annotationClass) {
        return Objects.nonNull(method) && method.isAnnotationPresent(annotationClass);
    }

    public static <A extends Annotation> A getAnnotation(Parameter param, Class<A> annotationClass) {
        A result = findAnnotation(param, annotationClass);
        Assert.notNull(result, "Cannot find annotation of [" + param.getName() + ']');
        return result;
    }

    public static <A extends Annotation> A findAnnotation(Parameter param, Class<A> annotationClass) {
        return Optional.ofNullable(param).map(t -> t.getDeclaredAnnotation(annotationClass)).orElse(null);
    }

    public static <A extends Annotation> boolean hasAnnotation(Parameter param, Class<A> annotationClass) {
        return Objects.nonNull(param) && param.isAnnotationPresent(annotationClass);
    }

    public static <A extends Annotation> A getAnnotation(Field field, Class<A> annotationClass) {
        A result = findAnnotation(field, annotationClass);
        Assert.notNull(result, "Cannot find annotation of [" + field.getName() + ']');
        return result;
    }

    public static <A extends Annotation> A findAnnotation(Field field, Class<A> annotationClass) {
        return Optional.ofNullable(field).map(t -> t.getDeclaredAnnotation(annotationClass)).orElse(null);
    }

    public static <A extends Annotation> boolean hasAnnotation(Field field, Class<A> annotationClass) {
        return Objects.nonNull(field) && field.isAnnotationPresent(annotationClass);
    }
}
