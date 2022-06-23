/*
 *  Copyright (c) 2019 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.mapping.util;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

/**
 * Utilitarian class to {@link javax.enterprise.inject.spi.BeanManager}
 */
public final class BeanManagers {

    /**
     * Get instance from the {@link BeanManager}
     *
     * @param clazz       the clazz to inject from the BeanManager
     * @param beanManager the BeanManager
     * @param <T>         the instance type
     * @return the instance from CDI context
     */
    public static <T> T getInstance(Class<T> clazz, BeanManager beanManager) {
        Objects.requireNonNull(clazz, "clazz is required");
        Objects.requireNonNull(beanManager, "beanManager is required");
        return getInstanceImpl(clazz, beanManager);
    }

    /**
     * Get instance from the {@link BeanManager}
     *
     * @param clazz       the clazz to inject from the BeanManager
     * @param beanManager the BeanManager
     * @param qualifier   the qualifier
     * @param <T>         the instance type
     * @return the instance from CDI context
     */
    public static <T> T getInstance(Class<T> clazz, Annotation qualifier, BeanManager beanManager) {
        Objects.requireNonNull(clazz, "clazz is required");
        Objects.requireNonNull(qualifier, "qualifier is required");
        Objects.requireNonNull(beanManager, "beanManager is required");

        return getInstanceImpl(clazz, qualifier, beanManager);
    }

    /**
     * Get the CDI BeanManager for the current CDI context
     *
     * @return the BeanManager
     */
    public static BeanManager getBeanManager() {
        return CDI.current().getBeanManager();
    }

    /**
     * Get instance from the {@link BeanManager} using the {@link BeanManagers#getBeanManager()}
     *
     * @param clazz the clazz to inject from the BeanManager
     * @param <T>   the instance type
     * @return the instance from CDI context
     */
    public static <T> T getInstance(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz is required");
        return getInstanceImpl(clazz, getBeanManager());
    }

    /**
     * Get instance from the {@link BeanManager} using the {@link BeanManagers#getBeanManager()}
     *
     * @param clazz     the clazz to inject from the BeanManager
     * @param qualifier the qualifier
     * @param <T>       the instance type
     * @return the instance from CDI context
     */
    public static <T> T getInstance(Class<T> clazz, Annotation qualifier) {
        Objects.requireNonNull(clazz, "clazz is required");
        return getInstanceImpl(clazz, qualifier, getBeanManager());
    }

    private static <T> T getInstanceImpl(Class<T> clazz, BeanManager beanManager) {
        Set<Bean<?>> beans = beanManager.getBeans(clazz);
        if (beans.isEmpty()) {
          throw new InjectionException("Does not find the bean class: " + clazz + " into CDI container");
        }
        Bean<T> bean = (Bean<T>) beans.iterator().next();
        CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
        return (T) beanManager.getReference(bean, clazz, ctx);
    }

    private static <T> T getInstanceImpl(Class<T> clazz, Annotation qualifier, BeanManager beanManager) {
        Set<Bean<?>> beans = beanManager.getBeans(clazz, qualifier);
        checkInjection(clazz, beans);
        Bean bean = beans.iterator().next();
        CreationalContext ctx = beanManager.createCreationalContext(bean);
        return (T) beanManager.getReference(bean, clazz, ctx);
    }

    private static <T> void checkInjection(Class<T> clazz, Set<Bean<?>> beans) {
        if (beans.isEmpty()) {
            throw new InjectionException("Does not find the bean class: " + clazz + " into CDI container");
        }
    }

    private BeanManagers() {
    }
}
