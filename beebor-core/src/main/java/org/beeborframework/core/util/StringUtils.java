package org.beeborframework.core.util;

import org.slf4j.helpers.MessageFormatter;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * StringUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 9:56
 */
public class StringUtils {

    public static final String EMPTY = "";

    private static final Pattern SPLIT_UNDERLINE = Pattern.compile("_");


    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isEmpty(CharSequence cs) {
        return Objects.isNull(cs) || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        int length;
        if (Objects.isNull(cs) || (length = cs.length()) == 0) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    public static String replace(String str, String oldPattern, String newPattern) {
        if (isEmpty(str) || isEmpty(oldPattern) || newPattern == null) {
            return str;
        }

        int i = str.indexOf(oldPattern);
        if (i == -1) {
            return str;
        }

        int capacity = newPattern.length() > oldPattern.length() ? (str.length() + 16) : str.length();
        StringBuilder result = new StringBuilder(capacity);
        int j = 0, length = oldPattern.length();
        while (i >= 0) {
            result.append(str, j, i);
            result.append(newPattern);
            j = i + length;
            i = str.indexOf(oldPattern, j);
        }

        result.append(str.substring(j));
        return result.toString();
    }

    public static String trimBothCharacter(String str, char c) {
        String result = trimLeadingCharacter(str, c);
        return trimTrailingCharacter(result, c);
    }

    public static String trimLeadingCharacter(String str, char c) {
        if (Objects.isNull(str)) {
            return null;
        }

        int i = 0;
        for (; i < str.length(); i++) {
            if (str.charAt(i) != c) {
                break;
            }
        }

        return str.substring(i);
    }

    public static String trimTrailingCharacter(String str, char c) {
        if (Objects.isNull(str)) {
            return null;
        }

        int i = str.length() - 1;
        for (; i >= 0; i--) {
            if (str.charAt(i) != c) {
                break;
            }
        }

        return str.substring(0, i + 1);
    }

    public static String sneakCaseToCamelCase(String str) {
        if (Objects.isNull(str)) {
            return null;
        }

        String[] splits = SPLIT_UNDERLINE.split(str);
        if (splits.length > 1) {
            StringBuilder result = new StringBuilder();
            result.append(splits[0]);
            for (int i = 1; i < splits.length; i++) {
                result.append(upperFirstLetter(splits[i]));
            }

            return result.toString();
        }

        return str;
    }

    public static String getFormatString(String message, Object... args) {
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }

    public static String upperFirstLetter(String str) {
        if (isBlank(str)) {
            return str;
        }

        char c = str.charAt(0);
        if (needUpperFirstLetter(c)) {
            c -= 32;
        }

        return c + (str.length() > 1 ? str.substring(1) : EMPTY);
    }

    public static String lowerFirstLetter(String str) {
        if (isBlank(str)) {
            return str;
        }

        char c = str.charAt(0);
        if (needLowerFirstLetter(c)) {
            c += 32;
        }

        return c + (str.length() > 1 ? str.substring(1) : EMPTY);
    }

    private static boolean needUpperFirstLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    private static boolean needLowerFirstLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }
}
