/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.column.query;

import jakarta.data.repository.CrudRepository;
import jakarta.enterprise.context.spi.CreationalContext;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.DatabaseQualifier;
import org.eclipse.jnosql.mapping.DatabaseType;
import org.eclipse.jnosql.mapping.column.JNoSQLColumnTemplate;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.spi.AbstractBean;
import org.eclipse.jnosql.mapping.util.AnnotationLiteralUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Artemis discoveryBean to CDI extension to register Repository
 */
public class RepositoryColumnBean<T extends CrudRepository> extends AbstractBean<T> {

    private final Class type;

    private final Set<Type> types;

    private final String provider;

    private final Set<Annotation> qualifiers;

    /**
     * Constructor
     *
     * @param type        the tye
     * @param provider    the provider name, that must be a
     */
    public RepositoryColumnBean(Class type, String provider) {
        this.type = type;
        this.types = Collections.singleton(type);
        this.provider = provider;
        if (provider.isEmpty()) {
            this.qualifiers = new HashSet<>();
            qualifiers.add(DatabaseQualifier.ofColumn());
            qualifiers.add(AnnotationLiteralUtil.DEFAULT_ANNOTATION);
            qualifiers.add(AnnotationLiteralUtil.ANY_ANNOTATION);
        } else {
            this.qualifiers = Collections.singleton(DatabaseQualifier.ofColumn(provider));
        }
    }

    @Override
    public Class<?> getBeanClass() {
        return type;
    }

    @Override
    public T create(CreationalContext<T> creationalContext) {
        EntitiesMetadata entities = getInstance(EntitiesMetadata.class);
        JNoSQLColumnTemplate template = provider.isEmpty() ? getInstance(JNoSQLColumnTemplate.class) :
                getInstance(JNoSQLColumnTemplate.class, DatabaseQualifier.ofColumn(provider));
        Converters converters = getInstance(Converters.class);

        ColumnRepositoryProxy handler = new ColumnRepositoryProxy(template,
                entities, type, converters);
        return (T) Proxy.newProxyInstance(type.getClassLoader(),
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
        return type.getName() + '@' + DatabaseType.COLUMN + "-" + provider;
    }

}