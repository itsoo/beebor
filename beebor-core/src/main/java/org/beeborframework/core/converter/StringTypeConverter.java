package org.beeborframework.core.converter;

import java.util.Optional;
import java.util.function.Function;

/**
 * StringTypeConverter
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 21:11
 */
public class StringTypeConverter implements TypeConverter {

    private final TypeConverter next;


    public StringTypeConverter(TypeConverter next) {
        this.next = next;
    }

    @Override
    public boolean parsable(Class<?> objClass) {
        return String.class.isAssignableFrom(objClass);
    }

    @Override
    public Object convert(Class<?> objClass, Object obj) {
        return parsable(objClass)
                ? Optional.ofNullable(obj).map((Function<Object, Object>) Object::toString).orElse(null)
                : Optional.ofNullable(next).map(t -> t.convert(objClass, obj)).orElse(null);
    }
}
