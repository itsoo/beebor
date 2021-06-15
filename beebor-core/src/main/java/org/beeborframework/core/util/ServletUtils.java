package org.beeborframework.core.util;

/**
 * ServletUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 12:08
 */
public class ServletUtils {

    private ServletUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getAbsolutePath(String path) {
        String absolutePath = '/' + StringUtils.trimBothCharacter(path, '/');
        return StringUtils.replace(absolutePath, "//", "/");
    }

    public static String processStanderPath(String path) {
        String reqPath = getAbsolutePath(path);
        return "/".equals(reqPath) ? "" : reqPath;
    }

    public static String processStanderPath(String ctx, String path) {
        String ctxPath = getAbsolutePath(ctx);
        String reqPath = getAbsolutePath(path);
        return ctxPath + ("/".equals(reqPath) ? "" : reqPath);
    }
}
