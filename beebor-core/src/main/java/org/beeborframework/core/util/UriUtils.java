package org.beeborframework.core.util;

import java.net.URL;

/**
 * UriUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/3 13:40
 */
public class UriUtils {

    private UriUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getBaseDir(Class<?> appClass) {
        return getBaseDir(appClass, "");
    }

    public static String getBaseDir(Class<?> appClass, String basePath) {
        URL resource = appClass.getClassLoader().getResource(basePath);
        Assert.notNull(resource, "'resource' cannot be null");
        return resource.getPath();
    }
}
