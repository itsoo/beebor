package com.ymm.sd.demoioc.domain;

import com.ymm.sd.demoioc.domain.entity.Entity2;
import lombok.Getter;
import lombok.ToString;
import org.beeborframework.ioc.lang.Bean;
import org.beeborframework.ioc.lang.Inject;

/**
 * DemoEntity2
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/13 12:52
 */
@Bean
@Getter
@ToString
public class DemoEntity2 {

    @Inject
    private Entity2 entity2;

}
