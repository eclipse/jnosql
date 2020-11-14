/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.mapping.column.reactive.spi;

import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.mapping.column.ColumnTemplateProducer;
import org.eclipse.jnosql.mapping.DatabaseQualifier;
import org.eclipse.jnosql.mapping.column.reactive.ReactiveColumnTemplate;
import org.eclipse.jnosql.mapping.spi.AbstractBean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

public class ReactiveTemplateBean extends AbstractBean<ReactiveColumnTemplate> {

    private final Set<Type> types;

    private final String provider;

    private final Set<Annotation> qualifiers;

    /**
     * Constructor
     *
     * @param beanManager the beanManager
     * @param provider    the provider name, that must be a
     */
    public ReactiveTemplateBean(BeanManager beanManager, String provider) {
        super(beanManager);
        this.types = Collections.singleton(ReactiveColumnTemplate.class);
        this.provider = provider;
        this.qualifiers = Collections.singleton(DatabaseQualifier.ofColumn(provider));
    }

    @Override
    public Class<?> getBeanClass() {
        return ReactiveColumnTemplate.class;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public ReactiveColumnTemplate create(CreationalContext<ReactiveColumnTemplate> context) {

        ColumnTemplateProducer producer = getInstance(ColumnTemplateProducer.class);
        ColumnFamilyManager manager = getManager();
        return producer.get(manager);
    }

    private ColumnFamilyManager getManager() {
        return getInstance(ColumnFamilyManager.class, DatabaseQualifier.ofColumn(provider));
    }

    @Override
    public void destroy(ReactiveColumnTemplate instance, CreationalContext<ReactiveColumnTemplate> context) {

    }

    @Override
    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return ApplicationScoped.class;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public String getId() {
        return ReactiveColumnTemplate.class.getName() + DatabaseType.COLUMN + "-" + provider;
    }

}