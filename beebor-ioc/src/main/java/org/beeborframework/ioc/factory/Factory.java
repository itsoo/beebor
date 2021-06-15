package org.beeborframework.ioc.factory;

/**
 * Factory
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/13 15:39
 */
public interface Factory<T> {

    /**
     * create object
     *
     * @return T
     */
    T getObject();

}
