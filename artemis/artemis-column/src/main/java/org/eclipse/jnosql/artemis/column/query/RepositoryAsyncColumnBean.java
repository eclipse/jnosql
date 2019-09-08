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
package org.eclipse.jnosql.artemis.column.query;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.mapping.RepositoryAsync;
import jakarta.nosql.mapping.column.ColumnTemplateAsync;
import jakarta.nosql.mapping.reflection.ClassMappings;
import org.eclipse.jnosql.artemis.DatabaseQualifier;
import org.eclipse.jnosql.artemis.spi.AbstractBean;
import org.eclipse.jnosql.artemis.util.AnnotationLiteralUtil;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Artemis discoveryBean to CDI extension to register {@link RepositoryAsync}
 */
public class RepositoryAsyncColumnBean extends AbstractBean<RepositoryAsync> {

    private final Class type;

    private final Set<Type> types;

    private final String provider;

    private final Set<Annotation> qualifiers;

    /**
     * Constructor
     *
     * @param type        the tye
     * @param beanManager the beanManager
     * @param provider    the provider name, that must be a
     */
    public RepositoryAsyncColumnBean(Class type, BeanManager beanManager, String provider) {
        super(beanManager);
        this.type = type;
        this.types = Collections.singleton(type);
        this.provider = provider;
        if (provider.isEmpty()) {
            this.qualifiers = new HashSet<>();
            qualifiers.add(DatabaseQualifier.ofColumn());
            qualifiers.add(AnnotationLiteralUtil.DEFAULT_ANNOTATION);
        } else {
            this.qualifiers = Collections.singleton(DatabaseQualifier.ofColumn(provider));
        }
    }

    @Override
    public Class<?> getBeanClass() {
        return type;
    }


    @Override
    public RepositoryAsync create(CreationalContext<RepositoryAsync> creationalContext) {
        ClassMappings classMappings = getInstance(ClassMappings.class);
        ColumnTemplateAsync repository = provider.isEmpty() ? getInstance(ColumnTemplateAsync.class) :
                getInstance(ColumnTemplateAsync.class, DatabaseQualifier.ofColumn(provider));
        Converters converters = getInstance(Converters.class);

        ColumnRepositoryAsyncProxy handler = new ColumnRepositoryAsyncProxy(repository,
                classMappings, type, converters);
        return (RepositoryAsync) Proxy.newProxyInstance(type.getClassLoader(),
                new Class[]{type},
                handler);
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
        return type.getName() + "Async@" + DatabaseType.COLUMN + "-" + provider;
    }


}