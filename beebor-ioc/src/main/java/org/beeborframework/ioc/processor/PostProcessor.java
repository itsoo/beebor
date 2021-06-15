package org.beeborframework.ioc.processor;

import org.beeborframework.ioc.bean.BeanDefinition;

import java.util.Set;

/**
 * PostProcessor
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/14 16:28
 */
public interface PostProcessor {

    /**
     * Post dependency
     *
     * @param beanDefinitions BeanDefinition of Set
     */
    void postDependency(Set<BeanDefinition> beanDefinitions);

}
