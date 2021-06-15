package org.beeborframework.core.util;

import org.junit.Test;

import java.util.List;

/**
 * JsonUtilsTests
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/15 14:05
 */
public class JsonUtilsTests {

    @Test
    public void testIsConvertibleClass() {
        System.out.println("String    : " + JsonUtils.isConvertibleClass(String.class));
        System.out.println("Long      : " + JsonUtils.isConvertibleClass(Long.class));
        System.out.println("List      : " + JsonUtils.isConvertibleClass(List.class));
        System.out.println("Integer   : " + JsonUtils.isConvertibleClass(Integer.class));
        System.out.println("Boolean   : " + JsonUtils.isConvertibleClass(Boolean.class));
        System.out.println("Object    : " + JsonUtils.isConvertibleClass(Object.class));
    }
}
