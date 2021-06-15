package org.beeborframework.core.converter;

/**
 * BasicConvertChain
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 23:03
 */
public class BasicConvertChain {

    private static final TypeConverter SHORT_CONVERTER = new ShortTypeConverter(null);

    private static final TypeConverter FLOAT_CONVERTER = new FloatTypeConverter(SHORT_CONVERTER);

    private static final TypeConverter BYTE_CONVERTER = new ByteTypeConverter(FLOAT_CONVERTER);

    private static final TypeConverter CHARACTER_CONVERTER = new CharacterTypeConverter(BYTE_CONVERTER);

    private static final TypeConverter DOUBLE_CONVERTER = new DoubleTypeConverter(CHARACTER_CONVERTER);

    private static final TypeConverter BOOLEAN_CONVERTER = new BooleanTypeConverter(DOUBLE_CONVERTER);

    private static final TypeConverter LONG_CONVERTER = new LongTypeConverter(BOOLEAN_CONVERTER);

    private static final TypeConverter INTEGER_CONVERTER = new IntegerTypeConverter(LONG_CONVERTER);

    private static final TypeConverter STRING_CONVERTER = new StringTypeConverter(INTEGER_CONVERTER);


    public static Object convert(Class<?> objClass, Object obj) {
        return STRING_CONVERTER.convert(objClass, obj);
    }
}
