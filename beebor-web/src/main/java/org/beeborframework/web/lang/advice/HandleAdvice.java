package org.beeborframework.web.lang.advice;

import java.lang.annotation.*;

/**
 * HandleAdvice
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/14 18:46
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandleAdvice {

    /**
     * Handle exception class
     *
     * @return Class of SubException
     */
    Class<? extends Exception> value() default Exception.class;

}
