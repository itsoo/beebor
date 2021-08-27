package org.beeborframework.web.context;

import lombok.SneakyThrows;
import org.beeborframework.core.util.AnnotationUtils;
import org.beeborframework.core.util.ClassUtils;
import org.beeborframework.ioc.context.DefaultApplicationContext;
import org.beeborframework.ioc.lang.Bean;
import org.beeborframework.web.lang.component.Business;
import org.beeborframework.web.lang.component.Dao;
import org.beeborframework.web.lang.component.Service;
import org.beeborframework.web.lang.rest.RestAction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.beeborframework.ioc.util.BeanUtils.getBeanName;

/**
 * WebApplicationContext
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/14 22:48
 */
public class WebApplicationContext extends DefaultApplicationContext {

    private final List<Class<? extends Annotation>> componentList;


    public WebApplicationContext(Class<?> appClass) {
        super(appClass);

        // Ext components annotations
        this.componentList = this.getDefaultComponents();
    }

    @Override
    @SneakyThrows(ReflectiveOperationException.class)
    protected void initBeanDefinition() {
        super.initBeanDefinition();

        // Web application scanner-scope
        for (Class<?> clazz : scanner.getClasses(this::isHandlerComponent)) {
            for (Class<? extends Annotation> annClass : componentList) {
                Annotation ann = AnnotationUtils.findAnnotation(clazz, annClass);
                if (Objects.nonNull(ann)) {
                    Method valueMethod = ClassUtils.getNoArgsValueMethod(annClass);
                    if (Objects.nonNull(valueMethod)) {
                        String name = valueMethod.invoke(ann).toString();
                        registerSimpleMemberBean(clazz, getBeanName(name, clazz));
                    }
                }
            }
        }
    }

    @Override
    protected boolean isHandlerComponent(Class<?> clazz) {
        return (!clazz.isAnnotation() && !clazz.isAnonymousClass() && !clazz.isInterface())
                && componentList.stream().anyMatch(t -> AnnotationUtils.hasAnnotation(clazz, t));
    }

    private List<Class<? extends Annotation>> getDefaultComponents() {
        List<Class<? extends Annotation>> result = new ArrayList<>();
        result.add(RestAction.class);
        result.add(Service.class);
        result.add(Business.class);
        result.add(Dao.class);
        appendComponents(result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private void appendComponents(List<Class<? extends Annotation>> componentList) {
        Predicate<Class<?>> exp = (t -> t.isAnnotation() && t.isAnnotationPresent(Bean.class));
        for (Class<?> clazz : scanner.getClasses(exp)) {
            componentList.add((Class<? extends Annotation>) clazz);
        }
    }
}
