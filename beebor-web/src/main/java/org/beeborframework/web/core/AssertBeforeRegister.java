package org.beeborframework.web.core;

import org.beeborframework.core.util.AnnotationUtils;
import org.beeborframework.core.util.Assert;
import org.beeborframework.web.lang.rest.*;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * AssertBeforeRegister
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/19 12:53
 */
public class AssertBeforeRegister {

    private AssertBeforeRegister() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isRequestMethod(Method method) {
        return AnnotationUtils.hasAnnotation(method, Get.class)
                || AnnotationUtils.hasAnnotation(method, Post.class)
                || AnnotationUtils.hasAnnotation(method, Put.class)
                || AnnotationUtils.hasAnnotation(method, Delete.class);
    }

    public static void assertRequestMethod(Method method) {
        long count = Arrays.stream(method.getParameters())
                .filter(t -> AnnotationUtils.findAnnotation(t, Body.class) != null)
                .count();
        String methodQualifier = method.getDeclaringClass().toGenericString() + '#' + method.getName();
        Assert.isTrue(count <= 1L, methodQualifier + ": @Body of the method cannot has more than one");
    }
}
