package org.beeborframework.core.converter;

import java.util.Objects;
import java.util.Optional;

/**
 * CharacterTypeConverter
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 21:11
 */
public class CharacterTypeConverter implements TypeConverter {

    private final TypeConverter next;


    public CharacterTypeConverter(TypeConverter next) {
        this.next = next;
    }

    @Override
    public boolean parsable(Class<?> objClass) {
        return Character.class.isAssignableFrom(objClass);
    }

    @Override
    public Object convert(Class<?> objClass, Object obj) {
        if (parsable(objClass)) {
            return Objects.nonNull(obj)
                    ? (obj.toString().isEmpty() ? null : obj.toString().charAt(0))
                    : null;
        }

        return Optional.ofNullable(next).map(t -> t.convert(objClass, obj)).orElse(null);
    }
}
