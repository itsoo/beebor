package org.beeborframework.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * IoUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 19:39
 */
public class IoUtils {

    private IoUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String readToUnicode(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(""));
        } catch (IOException e) {
            return null;
        }
    }
}
