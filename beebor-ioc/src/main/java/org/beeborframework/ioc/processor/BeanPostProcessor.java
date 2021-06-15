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
    @SneakyThrows(ReflectiveOperationException.class)
    public void postDependency(Set<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (BeanDefinition.Status.FINISHED.equals(beanDefinition.getBeanStatus())) {
                continue;
            }

            Object obj = beansCache.get(beanDefinition.getBeanName());
            // Post dependencies of methods
            for (Method method : ClassUtils.getMethods(beanDefinition.getBeanClass())) {
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
            for (Field field : ClassUtils.getFields(beanDefinition.getBeanClass())) {
                Inject ann = AnnotationUtils.findAnnotation(field, Inject.class);
                if (Objects.nonNull(ann)) {
                    String beanName = BeanUtils.getBeanName(ann, field);
                    field.set(obj, beansCache.get(beanName));
                }
            }

            // Recursively handle dependent dependencies
            for (BeanDefinition dependency : beanDefinition.getBeanDependencies()) {
                String beanName = dependency.getBeanName();
                Object fieldValue = ObjectUtils.getFieldValue(obj, beanName);
                if (Objects.isNull(fieldValue)) {
                    ObjectUtils.setFieldValue(obj, beanName, beansCache.get(beanName));
                    postDependency(dependency.getBeanDependencies());
                }
            }

            beanDefinition.setBeanStatus(BeanDefinition.Status.FINISHED);
        }
    }
}
