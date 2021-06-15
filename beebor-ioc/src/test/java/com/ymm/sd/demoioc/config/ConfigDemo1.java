package com.ymm.sd.demoioc.config;

import com.ymm.sd.demoioc.domain.entity.Entity1;
import com.ymm.sd.demoioc.domain.entity.Entity2;
import org.beeborframework.ioc.lang.Bean;
import org.beeborframework.ioc.lang.Inject;

/**
 * ConfigDemo1
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/13 12:51
 */
@Bean(isConfig = true)
public class ConfigDemo1 {

    @Bean
    public Entity1 entity1(@Inject Entity2 entity2) {
        Entity1 result = new Entity1();
        result.setEntity2(entity2);
        return result;
    }
}
