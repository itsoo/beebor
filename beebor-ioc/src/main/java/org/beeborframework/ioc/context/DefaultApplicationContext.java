package org.beeborframework.ioc.context;

import lombok.extern.slf4j.Slf4j;
import org.beeborframework.core.resource.ResourceScanner;
import org.beeborframework.core.util.AnnotationUtils;
import org.beeborframework.core.util.Assert;
import org.beeborframework.core.util.ClassUtils;
import org.beeborframework.ioc.bean.BeanDefinition;
import org.beeborframework.ioc.exception.WrongBeanDefinitionException;
import org.beeborframework.ioc.factory.BeanFactory;
import org.beeborframework.ioc.lang.Bean;
import org.beeborframework.ioc.lang.Inject;
import org.beeborframework.ioc.processor.BeanPostProcessor;
import org.beeborframework.ioc.processor.PostProcessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static org.beeborframework.ioc.util.BeanUtils.getBeanName;

/**
 * DefaultApplicationContext
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/12 22:47
 */
@Slf4j
public class DefaultApplicationContext implements ApplicationContext {

    protected Class<?> appClass;

    protected ResourceScanner scanner;

    protected Set<BeanDefinition> beanDefinitions = new HashSet<>();

    private final Map<String, Object> beansCache = new HashMap<>();

    private final PostProcessor postProcessor = new BeanPostProcessor(beansCache);


    public DefaultApplicationContext(Class<?> appClass) {
        this.appClass = appClass;
        this.scanner = new ResourceScanner(appClass);
    }

    @Override
    public Object getObject(String beanName) {
        Object result = beansCache.get(beanName);
        Assert.notNull(result, "Cannot found bean of name: [" + beanName + ']');
        return result;
    }

    @Override
    public Map<String, Object> getBeanMapping() {
        return Collections.unmodifiableMap(beansCache);
    }

    @Override
    public Collection<Object> getAllBeans() {
        return Collections.unmodifiableCollection(beansCache.values());
    }

    @Override
    public Set<String> getAllBeanNames() {
        return Collections.unmodifiableSet(beansCache.keySet());
    }

    @Override
    public void refresh() {
        // Scan classes and init bean-definition
        initBeanDefinition();
        // Process bean-dependencies
        processBeanDependencies();
    }

    /**
     * Initial bean-definitions
     */
    protected void initBeanDefinition() {
        for (Class<?> clazz : scanner.getClasses(this::isHandlerComponent)) {
            Bean ann = AnnotationUtils.findAnnotation(clazz, Bean.class);
            if (Objects.nonNull(ann)) {
                // Is config class
                if (ann.isConfig()) {
                    registerConfigurationBean(clazz);
                } else {
                    registerSimpleMemberBean(clazz, getBeanName(ann, clazz));
                }
            }
        }
    }

    /**
     * Scan components condition
     *
     * @param clazz Class
     * @return boolean
     */
    protected boolean isHandlerComponent(Class<?> clazz) {
        return (!clazz.isAnnotation() && !clazz.isAnonymousClass() && !clazz.isInterface())
                && AnnotationUtils.hasAnnotation(clazz, Bean.class);
    }

    /**
     * Process bean dependencies
     */
    protected void processBeanDependencies() {
        // Process bean-definition of dependencies
        for (BeanDefinition beanDefinition : beanDefinitions) {
            computeCircleDependencies(beanDefinition);
        }

        // Create bean instances
        for (BeanDefinition beanDefinition : beanDefinitions) {
            BeanFactory beanFactory = new BeanFactory(beanDefinition);
            beanDefinition.setBeanStatus(BeanDefinition.Status.CREATING);
            beansCache.put(beanDefinition.getBeanName(), beanFactory.getObject());
        }

        // Bean post-processor, resolve dependencies
        postProcessor.postDependency(beanDefinitions);
    }

    /**
     * Register configuration type beans
     *
     * @param clazz Class
     */
    protected void registerConfigurationBean(Class<?> clazz) {
        for (Method method : ClassUtils.getMethods(clazz)) {
            Bean inAnn = AnnotationUtils.findAnnotation(method, Bean.class);
            if (Objects.isNull(inAnn)) {
                continue;
            } else if (inAnn.isConfig()) {
                throw new WrongBeanDefinitionException(method);
            }

            // Append @Bean method params to dependencies
            BeanDefinition beanDefinition = getBeanDefinition(method.getReturnType(), getBeanName(inAnn, method));
            for (Parameter param : ClassUtils.getParameters(method)) {
                Inject depAnn = AnnotationUtils.findAnnotation(param, Inject.class);
                if (Objects.nonNull(depAnn)) {
                    beanDefinition.appendDependency(getBeanDefinition(param.getType(), getBeanName(depAnn, param)));
                }
            }

            beanDefinitions.add(beanDefinition);
        }
    }

    /**
     * Register simple and independent class as member bean
     *
     * @param clazz    Class
     * @param beanName String
     */
    protected void registerSimpleMemberBean(Class<?> clazz, String beanName) {
        BeanDefinition beanDefinition = getBeanDefinition(clazz, beanName);
        // Append constructor params to dependencies
        for (Constructor<?> constructor : ClassUtils.getConstructors(clazz)) {
            appendDependency(beanDefinition, ClassUtils.getParameters(constructor));
        }

        // Append method params to dependencies
        for (Method method : ClassUtils.getMethods(clazz)) {
            appendDependency(beanDefinition, ClassUtils.getParameters(method));
        }

        // Append fields to dependencies
        for (Field field : ClassUtils.getFields(clazz)) {
            Inject depAnn = AnnotationUtils.findAnnotation(field, Inject.class);
            if (Objects.nonNull(depAnn)) {
                beanDefinition.appendDependency(getBeanDefinition(field.getType(), getBeanName(depAnn, field)));
            }
        }

        beanDefinitions.add(beanDefinition);
    }

    /**
     * Get singleton bean-definition
     *
     * @param beanType Class
     * @param beanName String
     * @return BeanDefinition
     */
    private BeanDefinition getBeanDefinition(Class<?> beanType, String beanName) {
        return ContextHolder.computeBeanDefinition(new BeanDefinition(beanType, beanName));
    }

    /**
     * Bean-definition circular dependency resolution
     *
     * @param currBeanDefinition BeanDefinition
     */
    private void computeCircleDependencies(BeanDefinition currBeanDefinition) {
        if (currBeanDefinition.isCircleDependency()) {
            return;
        }

        for (BeanDefinition beanDependency : currBeanDefinition.getBeanDependencies()) {
            for (BeanDefinition dependency : beanDependency.getBeanDependencies()) {
                if (Objects.equals(currBeanDefinition, dependency)) {
                    currBeanDefinition.setCircleDependency(true);
                    dependency.setCircleDependency(true);
                } else if (Objects.equals(beanDependency, dependency)) {
                    beanDependency.setCircleDependency(true);
                    dependency.setCircleDependency(true);
                }
            }

            computeCircleDependencies(beanDependency);
        }
    }

    /**
     * Append Dependency
     *
     * @param beanDefinition BeanDefinition
     * @param parameters     Parameter[]
     */
    private void appendDependency(BeanDefinition beanDefinition, Parameter[] parameters) {
        for (Parameter param : parameters) {
            Inject depAnn = AnnotationUtils.findAnnotation(param, Inject.class);
            if (Objects.nonNull(depAnn)) {
                beanDefinition.appendDependency(getBeanDefinition(param.getType(), getBeanName(depAnn, param)));
            }
        }
    }
}
