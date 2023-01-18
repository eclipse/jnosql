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

import jakarta.data.repository.PageableRepository;
import jakarta.nosql.column.ColumnTemplate;
import org.eclipse.jnosql.communication.column.ColumnManager;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.column.ColumnTemplateProducer;
import org.eclipse.jnosql.mapping.column.JNoSQLColumnTemplate;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * The producer of Repository
 */
@ApplicationScoped
public class ColumnRepositoryProducer {

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    @Inject
    private ColumnTemplateProducer producer;

    /**
     * Produces a Repository class from repository class and {@link ColumnManager}
     *
     * @param repositoryClass the repository class
     * @param manager         the manager
     * @param <T>             the entity of repository
     * @param <K>             the K of the entity
     * @param <R>             the repository type
     * @return a Repository interface
     * @throws NullPointerException when there is null parameter
     */
    public <T, K, R extends PageableRepository<T, K>> R get(Class<R> repositoryClass, ColumnManager manager) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(manager, "manager class is required");
        JNoSQLColumnTemplate template = producer.apply(manager);
        return get(repositoryClass, template);
    }

    /**
     * Produces a Repository class from repository class and {@link ColumnTemplate}
     *
     * @param repositoryClass the repository class
     * @param template        the template
     * @param <T>             the entity of repository
     * @param <K>             the K of the entity
     * @param <R>             the repository type
     * @return a Repository interface
     * @throws NullPointerException when there is null parameter
     */
    public <T, K, R extends PageableRepository<T, K>> R get(Class<R> repositoryClass, JNoSQLColumnTemplate template) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(template, "template class is required");

        ColumnRepositoryProxy<T, K> handler = new ColumnRepositoryProxy<>(template,
                entities, repositoryClass, converters);
        return (R) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }
}
