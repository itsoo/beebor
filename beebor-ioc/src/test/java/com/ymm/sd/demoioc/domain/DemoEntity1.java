package com.ymm.sd.demoioc.domain;

import lombok.Getter;
import lombok.ToString;
import org.beeborframework.ioc.lang.Bean;
import org.beeborframework.ioc.lang.Inject;

/**
 * DemoEntity1
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/13 12:52
 */
@Bean
@Getter
@ToString
public class DemoEntity1 {

    @Inject
    private DemoEntity2 demoEntity2;

}
