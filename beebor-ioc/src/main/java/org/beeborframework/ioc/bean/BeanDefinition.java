package org.beeborframework.ioc.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * BeanDefinition
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/13 11:23
 */
@Getter
public class BeanDefinition {

    private final Class<?> beanClass;

    private final String beanName;

    @Setter
    private Status beanStatus;

    @Setter
    private boolean isCircleDependency;

    private final Set<BeanDefinition> beanDependencies = new HashSet<>();


    public BeanDefinition(Class<?> beanClass, String beanName) {
        this.beanClass = beanClass;
        this.beanName = beanName;
        this.beanStatus = Status.WAITING;
        this.isCircleDependency = false;
    }

    public void appendDependency(BeanDefinition beanDefinition) {
        beanDependencies.add(beanDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanClass, beanName);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BeanDefinition)
                && Objects.equals(this.beanClass, ((BeanDefinition) obj).beanClass)
                && Objects.equals(this.beanName, ((BeanDefinition) obj).beanName);
    }

    @Override
    public String toString() {
        return "BeanDefinition(" +
                "beanClass=" + beanClass +
                ", beanName=" + beanName +
                ", beanStatus=" + beanStatus +
                ", isCircleDependency=" + isCircleDependency +
                ')';
    }

    /**
     * BeanDefinitionStatus
     */
    public enum Status {

        /*** wait */
        WAITING,

        /*** creating */
        CREATING,

        /*** finished */
        FINISHED;

    }
}
