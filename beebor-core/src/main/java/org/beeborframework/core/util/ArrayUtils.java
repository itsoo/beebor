package org.beeborframework.core.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * ArrayUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/15 13:42
 */
public class ArrayUtils {

    private ArrayUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> boolean isEmpty(T[] array) {
        return Objects.isNull(array) || array.length == 0;
    }

    public static <T> boolean isEmpty(T[][] array) {
        return Objects.isNull(array) || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static <T> boolean isNotEmpty(T[][] array) {
        return !isEmpty(array);
    }

    @SafeVarargs
    public static <T> List<T> concatOfList(T[] array, T... elements) {
        return Arrays.asList(concat(array, elements));
    }

    @SafeVarargs
    public static <T> T[] concat(T[] array, T... elements) {
        if (Objects.isNull(array) || isEmpty(elements)) {
            return array;
        }

        T[] result = newArray(array, elements);
        int start = concatAndGetLength(array, result, 0);
        concatAndGetLength(elements, result, start);
        return result;
    }

    @SafeVarargs
    public static <T> T[] merge(T[] array, T[]... arrays) {
        if (isEmpty(arrays)) {
            return array;
        }

        T[] result = newArray(array, arrays);
        int start = 0;
        if (Objects.nonNull(array)) {
            start = concatAndGetLength(array, result, 0);
        }
        // other arrays
        for (T[] arr : arrays) {
            if (Objects.nonNull(arr)) {
                start += concatAndGetLength(arr, result, start);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(T[] array, T[]... elements) {
        return (T[]) Array.newInstance(getArrayType(array, elements), getTotalLength(array, elements));
    }

    @SafeVarargs
    public static <T> int getTotalLength(T[] array, T[]... arrays) {
        int length = Objects.isNull(array) ? 0 : array.length;
        int total = Arrays.stream(arrays).filter(Objects::nonNull).mapToInt(t -> t.length).sum();
        return (length + total);
    }

    @SafeVarargs
    public static <T> Class<?> getArrayType(T[] array, T[]... arrays) {
        Class<?> result = getArrayType(array);
        if (Objects.isNull(result)) {
            if (Objects.nonNull(arrays)) {
                for (T[] arr : arrays) {
                    if (Objects.nonNull(arr)) {
                        result = getArrayType(arrays[0]);
                        break;
                    }
                }
            }
        }

        Assert.notNull(result, "Cannot found array type.");
        return result;
    }

    public static <T> Class<?> getArrayType(T[] array) {
        return Objects.nonNull(array) ? array.getClass().getComponentType() : null;
    }

    public static <T> int concatAndGetLength(T[] src, T[] dest, int start) {
        System.arraycopy(src, 0, dest, start, src.length);
        return src.length;
    }
}
