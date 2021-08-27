package com.ymm.sd.demoweb.lang;

import org.beeborframework.ioc.lang.Bean;

import java.lang.annotation.*;

/**
 * CustomBean
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/7/22 19:30
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Bean
public @interface CustomBean {

    String value() default "";

}
