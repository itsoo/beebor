package org.beeborframework.web.lang.rest;

import java.lang.annotation.*;

/**
 * Action
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/11 12:57
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Put {

    /**
     * Mapping path
     *
     * @return String of Array
     */
    String[] value() default "";

}
