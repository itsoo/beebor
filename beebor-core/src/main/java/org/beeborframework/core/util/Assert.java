package org.beeborframework.core.util;

import java.util.Objects;

/**
 * Assert
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/11 21:19
 */
public class Assert {

    private Assert() {
        throw new IllegalStateException("Utility class");
    }

    public static void notNull(Object obj, String msg) {
        Objects.requireNonNull(obj, msg);
    }

    public static void isTrue(boolean exp, String msg) {
        if (!exp) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isFalse(boolean exp, String msg) {
        isTrue(!exp, msg);
    }
}
