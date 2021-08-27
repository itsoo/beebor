package org.beeborframework.ioc.processor;

import lombok.SneakyThrows;
import org.beeborframework.core.util.AnnotationUtils;
import org.beeborframework.core.util.ClassUtils;
import org.beeborframework.core.util.ObjectUtils;
import org.beeborframework.ioc.bean.BeanDefinition;
import org.beeborframework.ioc.lang.Inject;
import org.beeborframework.ioc.util.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * BeanPostProcessor
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/14 16:20
 */
public class BeanPostProcessor implements PostProcessor {

    private final Map<String, Object> beansCache;


    public BeanPostProcessor(Map<String, Object> beansCache) {
        this.beansCache = beansCache;
    }

    @Override
    public void postDependency(Set<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (!BeanDefinition.Status.FINISHED.equals(beanDefinition.getBeanStatus())) {
                for (BeanDefinition beanDependency : beanDefinition.getBeanDependencies()) {
                    postDependency(beanDefinition, beanDependency);
                }
            }
        }
    }

    @SneakyThrows(ReflectiveOperationException.class)
    private void postDependency(BeanDefinition rootBeanDefinition, BeanDefinition currBeanDefinition) {
        if (rootBeanDefinition.equals(currBeanDefinition)) {
            rootBeanDefinition.setBeanStatus(BeanDefinition.Status.FINISHED);
            return;
        }

        Object obj = beansCache.get(rootBeanDefinition.getBeanName());
        // Post dependencies of methods
        for (Method method : ClassUtils.getMethods(rootBeanDefinition.getBeanClass())) {
            Inject ann = AnnotationUtils.findAnnotation(method, Inject.class);
            if (Objects.nonNull(ann)) {
                List<Object> objs = new LinkedList<>();
                for (Parameter param : method.getParameters()) {
                    String beanName = BeanUtils.getBeanName(ann, param);
                    objs.add(beansCache.get(beanName));
                }

                method.invoke(obj, objs.toArray());
            }
        }

        // Post dependencies of fields
        for (Field field : ClassUtils.getFields(rootBeanDefinition.getBeanClass())) {
            Inject ann = AnnotationUtils.findAnnotation(field, Inject.class);
            if (Objects.nonNull(ann)) {
                String beanName = BeanUtils.getBeanName(ann, field);
                field.set(obj, beansCache.get(beanName));
            }
        }

        // Recursively handle dependencies
        for (BeanDefinition beanDependency : currBeanDefinition.getBeanDependencies()) {
            String beanName = currBeanDefinition.getBeanName();
            Object fieldValue = ObjectUtils.getFieldValue(obj, beanName);
            if (Objects.isNull(fieldValue)) {
                ObjectUtils.setFieldValue(obj, beanName, beansCache.get(beanName));
                postDependency(rootBeanDefinition, beanDependency);
            }
        }
    }
}
