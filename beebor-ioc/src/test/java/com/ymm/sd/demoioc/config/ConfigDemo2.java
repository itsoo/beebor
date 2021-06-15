package com.ymm.sd.demoioc.config;

import com.ymm.sd.demoioc.domain.entity.Entity1;
import com.ymm.sd.demoioc.domain.entity.Entity2;
import org.beeborframework.ioc.lang.Bean;
import org.beeborframework.ioc.lang.Inject;

/**
 * ConfigDemo2
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/13 12:51
 */
@Bean(isConfig = true)
public class ConfigDemo2 {

    @Bean
    public Entity2 entity2(@Inject Entity1 entity1) {
        Entity2 result = new Entity2();
        result.setEntity1(entity1);
        return result;
    }
}
