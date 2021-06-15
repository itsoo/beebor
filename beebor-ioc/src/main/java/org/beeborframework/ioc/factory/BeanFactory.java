package org.beeborframework.ioc.factory;

import lombok.SneakyThrows;
import org.beeborframework.core.util.AnnotationUtils;
import org.beeborframework.core.util.Assert;
import org.beeborframework.core.util.ClassUtils;
import org.beeborframework.core.util.CollectionUtils;
import org.beeborframework.ioc.bean.BeanDefinition;
import org.beeborframework.ioc.lang.Inject;
import org.beeborframework.ioc.util.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BeanFactory
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/12 17:41
 */
public class BeanFactory implements Factory<Object> {

    private final BeanDefinition beanDefinition;

    private final Map<BeanDefinition, Object> creatingBeans = new HashMap<>();


    public BeanFactory(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }

    @Override
    public Object getObject() {
        if (creatingBeans.containsKey(beanDefinition)) {
            return creatingBeans.get(beanDefinition);
        }

        return getDependencyObject(beanDefinition);
    }

    /**
     * Parse bean-definition to bean-object
     *
     * @param beanDependency BeanDefinition
     * @return Object
     */
    @SneakyThrows(ReflectiveOperationException.class)
    private Object getDependencyObject(BeanDefinition beanDependency) {
        if (creatingBeans.containsKey(beanDependency)) {
            return creatingBeans.get(beanDependency);
        }

        Constructor<?> constructor = getConstructor(beanDependency);
        Assert.notNull(constructor, "Cannot found constructor to instance bean: " + beanDependency);

        // Constructor parameters
        List<Object> constructorParams = new LinkedList<>();
        for (Parameter param : constructor.getParameters()) {
            Inject ann = AnnotationUtils.findAnnotation(param, Inject.class);
            if (Objects.nonNull(ann)) {
                String beanName = BeanUtils.getBeanName(ann, param);
                // Filter bean-definition of dependencies
                BeanDefinition dependency = filterBeanDefinitions(beanName, beanDependency.getBeanDependencies());
                if (Objects.nonNull(dependency)) {
                    if (creatingBeans.containsKey(dependency)) {
                        constructorParams.add(creatingBeans.get(dependency));
                        continue;
                    }

                    // Has more dependencies
                    if (CollectionUtils.isNotEmpty(dependency.getBeanDependencies())) {
                        Object obj = getDependencyObject(dependency);
                        creatingBeans.put(dependency, obj);
                        constructorParams.add(obj);
                    }
                    // Current object is bean
                    else {
                        Object obj = getConstructor(dependency).newInstance();
                        creatingBeans.put(dependency, obj);
                        constructorParams.add(obj);
                    }
                } else {
                    constructorParams.add(null);
                }
            }
        }

        // Instance current bean
        Object result = constructor.newInstance(constructorParams.toArray());
        creatingBeans.put(beanDependency, result);
        return result;
    }

    /**
     * Get the most suitable constructor
     *
     * @param beanDefinition BeanDefinition
     * @return Constructor
     */
    private Constructor<?> getConstructor(BeanDefinition beanDefinition) {
        Constructor<?>[] constructors = ClassUtils.getConstructors(beanDefinition.getBeanClass());
        Arrays.sort(constructors, (a, b) -> b.getParameterCount() - a.getParameterCount());
        Constructor<?> defaultConstructor = constructors[0];
        Set<Class<?>> classSet = getBeanDependencyClasses(beanDefinition);

        boolean isAllContains;
        for (Constructor<?> constructor : constructors) {
            // Default is no args
            if (constructor.getParameterCount() == 0) {
                defaultConstructor = constructor;
            }

            // Find all constructor parameters in dependency-classes
            isAllContains = true;
            for (Class<?> paramType : constructor.getParameterTypes()) {
                if (!classSet.contains(paramType)) {
                    isAllContains = false;
                    break;
                }
            }

            if (isAllContains) {
                return constructor;
            }
        }

        return defaultConstructor;
    }

    /**
     * Find bean of dependencies
     *
     * @param beanName         String
     * @param beanDependencies Set of BeanDefinition
     * @return BeanDefinition
     */
    private BeanDefinition filterBeanDefinitions(String beanName, Set<BeanDefinition> beanDependencies) {
        for (BeanDefinition dependency : beanDependencies) {
            if (Objects.equals(beanName, dependency.getBeanName())) {
                Assert.isFalse(dependency.isCircleDependency(), "Cannot resolve circle-dependency: "
                        + dependency.getBeanName());
                return dependency;
            }
        }

        return null;
    }

    /**
     * Convert bean-dependencies to class-set
     *
     * @param beanDefinition BeanDefinition
     * @return Set of Class
     */
    private Set<Class<?>> getBeanDependencyClasses(BeanDefinition beanDefinition) {
        return beanDefinition.getBeanDependencies()
                .stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toSet());
    }
}
