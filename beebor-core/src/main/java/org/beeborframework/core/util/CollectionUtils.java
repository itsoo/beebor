package org.beeborframework.core.util;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * CollectionUtils
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/15 13:57
 */
public class CollectionUtils {

    private CollectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> boolean anyMatch(Collection<T> collection, Predicate<T> predicate) {
        for (T t : collection) {
            if (predicate.test(t)) {
                return true;
            }
        }

        return false;
    }

    public static <T> boolean allMatch(Collection<T> collection, Predicate<T> predicate) {
        for (T t : collection) {
            if (!predicate.test(t)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}
