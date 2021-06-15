package org.beeborframework.web.context;

import org.beeborframework.core.util.AnnotationUtils;
import org.beeborframework.ioc.context.DefaultApplicationContext;
import org.beeborframework.web.lang.component.Business;
import org.beeborframework.web.lang.component.Dao;
import org.beeborframework.web.lang.component.Service;
import org.beeborframework.web.lang.rest.RestAction;

import java.util.Objects;

import static org.beeborframework.ioc.util.BeanUtils.getBeanName;

/**
 * WebApplicationContext
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/6/14 22:48
 */
public class WebApplicationContext extends DefaultApplicationContext {

    public WebApplicationContext(Class<?> appClass) {
        super(appClass);
    }

    @Override
    protected void initBeanDefinition() {
        super.initBeanDefinition();

        // Web application scanner-scope
        for (Class<?> clazz : scanner.getClasses(this::isHandlerComponent)) {
            RestAction restAnn = AnnotationUtils.findAnnotation(clazz, RestAction.class);
            if (Objects.nonNull(restAnn)) {
                registerSimpleMemberBean(clazz, getBeanName(restAnn.name(), clazz));
            }

            Service serviceAnn = AnnotationUtils.findAnnotation(clazz, Service.class);
            if (Objects.nonNull(serviceAnn)) {
                registerSimpleMemberBean(clazz, getBeanName(serviceAnn.value(), clazz));
            }

            Business businessAnn = AnnotationUtils.findAnnotation(clazz, Business.class);
            if (Objects.nonNull(businessAnn)) {
                registerSimpleMemberBean(clazz, getBeanName(businessAnn.value(), clazz));
            }

            Dao daoAnn = AnnotationUtils.findAnnotation(clazz, Dao.class);
            if (Objects.nonNull(daoAnn)) {
                registerSimpleMemberBean(clazz, getBeanName(daoAnn.value(), clazz));
            }
        }
    }

    @Override
    protected boolean isHandlerComponent(Class<?> clazz) {
        return (!clazz.isAnnotation() && !clazz.isAnonymousClass() && !clazz.isInterface())
                && (AnnotationUtils.hasAnnotation(clazz, RestAction.class)
                || AnnotationUtils.hasAnnotation(clazz, Service.class)
                || AnnotationUtils.hasAnnotation(clazz, Business.class)
                || AnnotationUtils.hasAnnotation(clazz, Dao.class));
    }
}
