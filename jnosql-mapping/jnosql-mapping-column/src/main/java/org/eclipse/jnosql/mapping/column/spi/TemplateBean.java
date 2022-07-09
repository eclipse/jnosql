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
package org.eclipse.jnosql.mapping.column.spi;


import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.mapping.column.ColumnTemplateProducer;
import org.eclipse.jnosql.mapping.DatabaseQualifier;
import org.eclipse.jnosql.mapping.spi.AbstractBean;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
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
    public TemplateBean(String provider) {
        this.types = Collections.singleton(ColumnTemplate.class);
        this.provider = provider;
        this.qualifiers = Collections.singleton(DatabaseQualifier.ofColumn(provider));
    }

    @Override
    public Class<?> getBeanClass() {
        return ColumnTemplate.class;
    }


    @Override
    public ColumnTemplate create(CreationalContext<ColumnTemplate> context) {

        ColumnTemplateProducer producer = getInstance(ColumnTemplateProducer.class);
        ColumnFamilyManager columnFamilyManager = getColumnFamilyManager();
        return producer.get(columnFamilyManager);
    }

    private ColumnFamilyManager getColumnFamilyManager() {
        return getInstance(ColumnFamilyManager.class, DatabaseQualifier.ofColumn(provider));
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
