package org.beeborframework.core.util;

/**
 * ExceptionUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 17:41
 */
public class ExceptionUtils {

    private ExceptionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static int computeSubclassLevel(Class<? extends Exception> clazz) {
        if (Exception.class == clazz) {
            return 0;
        }

        int result = 1;
        Class<?> superClass = clazz.getSuperclass();
        while (!superClass.isAssignableFrom(Exception.class)) {
            superClass = superClass.getSuperclass();
            result++;
        }

        return result;
    }
}
