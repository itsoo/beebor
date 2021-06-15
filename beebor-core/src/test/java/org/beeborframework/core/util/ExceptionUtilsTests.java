package org.beeborframework.core.util;

import org.junit.Test;

/**
 * ExceptionUtilsTests
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 17:47
 */
public class ExceptionUtilsTests {

    @Test
    public void testComputeSubclassLevel() {
        int b = ExceptionUtils.computeSubclassLevel(RuntimeException.class);
        int c = ExceptionUtils.computeSubclassLevel(Exception.class);
        System.out.println("b: " + b + ", c: " + c);
    }
}
