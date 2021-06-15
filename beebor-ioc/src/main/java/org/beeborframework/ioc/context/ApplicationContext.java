package org.beeborframework.ioc.context;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * ApplicationContext
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/13 14:14
 */
public interface ApplicationContext {

    /**
     * Refresh context
     */
    void refresh();

    /**
     * Get bean by name
     *
     * @param beanName String
     * @return Object
     */
    Object getObject(String beanName);

    /**
     * All register beans of context
     *
     * @return Map of String, Object
     */
    Map<String, Object> getBeanMapping();

    /**
     * All register bean-instances of context
     *
     * @return Collection of Object
     */
    Collection<Object> getAllBeans();

    /**
     * All register bean names of context
     *
     * @return Set of String
     */
    Set<String> getAllBeanNames();

}
