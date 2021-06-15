package org.beeborframework.web.lang.rest;

import java.lang.annotation.*;

/**
 * Param
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 13:06
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

    /**
     * Parameter name
     *
     * @return String
     */
    String value() default "";

}
