/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.column.spi;


import org.jnosql.artemis.DatabaseQualifier;
import org.jnosql.artemis.DatabaseType;
import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.artemis.column.ColumnTemplateProducer;
import org.jnosql.artemis.spi.AbstractBean;
import org.jnosql.diana.api.column.ColumnFamilyManager;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

class TemplateBean extends AbstractBean<ColumnTemplate> {

    private final Set<Type> types;

    private final String provider;

    private final Set<Annotation> qualifiers;

    /**
     * Constructor
     *
     * @param beanManager the beanManager
     * @param provider    the provider name, that must be a
     */
    public TemplateBean(BeanManager beanManager, String provider) {
        super(beanManager);
        this.types = Collections.singleton(ColumnTemplate.class);
        this.provider = provider;
        this.qualifiers = Collections.singleton(DatabaseQualifier.ofColumn(provider));
    }

    @Override
    public Class<?> getBeanClass() {
        return ColumnTemplate.class;
    }


    @Override
    public ColumnTemplate create(CreationalContext<ColumnTemplate> creationalContext) {

        ColumnTemplateProducer producer = getInstance(ColumnTemplateProducer.class);
        ColumnFamilyManager columnFamilyManager = getColumnFamilyManager();
        return producer.get(columnFamilyManager);
    }

    private ColumnFamilyManager getColumnFamilyManager() {
        Bean<ColumnFamilyManager> bean = (Bean<ColumnFamilyManager>) getBeanManager().getBeans(ColumnFamilyManager.class,
                DatabaseQualifier.ofColumn(provider) ).iterator().next();
        CreationalContext<ColumnFamilyManager> ctx = getBeanManager().createCreationalContext(bean);
        return (ColumnFamilyManager) getBeanManager().getReference(bean, ColumnFamilyManager.class, ctx);
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
    public String getId() {
        return ColumnTemplate.class.getName() + DatabaseType.COLUMN + "-" + provider;
    }

}
