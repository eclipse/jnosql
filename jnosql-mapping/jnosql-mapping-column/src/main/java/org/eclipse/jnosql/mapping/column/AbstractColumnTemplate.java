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
package org.eclipse.jnosql.mapping.column;


import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.column.ColumnQueryParser;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.IdNotFoundException;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.mapping.column.ColumnEventPersistManager;
import jakarta.nosql.mapping.column.ColumnQueryPagination;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.mapping.column.ColumnWorkflow;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;
import org.eclipse.jnosql.mapping.util.ConverterUtil;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

/**
 * The template method to {@link ColumnTemplate}
 */
public abstract class AbstractColumnTemplate implements ColumnTemplate {


    private static final ColumnQueryParser PARSER = ServiceLoaderProvider.get(ColumnQueryParser.class,
            ()-> ServiceLoader.load(ColumnQueryParser.class));

    protected abstract ColumnEntityConverter getConverter();

    protected abstract ColumnFamilyManager getManager();

    protected abstract ColumnWorkflow getFlow();

    protected abstract ColumnEventPersistManager getEventManager();

    protected abstract EntitiesMetadata getEntities();

    protected abstract Converters getConverters();

    private final UnaryOperator<ColumnEntity> insert = e -> getManager().insert(e);

    private final UnaryOperator<ColumnEntity> update = e -> getManager().update(e);

    private ColumnObserverParser observer;


    private ColumnObserverParser getObserver() {
        if (Objects.isNull(observer)) {
            observer = new ColumnMapperObserver(getEntities());
        }
        return observer;
    }

    @Override
    public <T> T insert(T entity) {
        requireNonNull(entity, "entity is required");
        return getFlow().flow(entity, insert);
    }


    @Override
    public <T> T insert(T entity, Duration ttl) {
        requireNonNull(entity, "entity is required");
        requireNonNull(ttl, "ttl is required");
        return getFlow().flow(entity, e -> getManager().insert(e, ttl));
    }


    @Override
    public <T> T update(T entity) {
        requireNonNull(entity, "entity is required");
        return getFlow().flow(entity, update);
    }

    @Override
    public <T> Iterable<T> update(Iterable<T> entities) {
        requireNonNull(entities, "entity is required");
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::update).collect(Collectors.toList());
    }

    @Override
    public <T> Iterable<T> insert(Iterable<T> entities) {
        requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::insert).collect(Collectors.toList());
    }

    @Override
    public <T> Iterable<T> insert(Iterable<T> entities, Duration ttl) {
        requireNonNull(entities, "entities is required");
        requireNonNull(ttl, "ttl is required");
        return StreamSupport.stream(entities.spliterator(), false)
                .map(e -> insert(e, ttl))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(ColumnDeleteQuery query) {
        requireNonNull(query, "query is required");
        getEventManager().firePreDeleteQuery(query);
        getManager().delete(query);
    }


    @Override
    public <T> Stream<T> select(ColumnQuery query) {
        requireNonNull(query, "query is required");
        return executeQuery(query);
    }


    @Override
    public <T> Page<T> select(ColumnQueryPagination query) {
        requireNonNull(query, "query is required");
        Stream<T> entities = executeQuery(query);
        return new ColumnPage<>(this, entities, query);
    }

    @Override
    public <T> Optional<T> singleResult(ColumnQuery query) {
        requireNonNull(query, "query is required");
        final Stream<T> select = select(query);

        final Iterator<T> iterator = select.iterator();

        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final T entity = iterator.next();

        if (!iterator.hasNext()) {
            return Optional.of(entity);
        }
        throw new NonUniqueResultException("No Unique result found to the query: " + query);
    }

    @Override
    public <T, K> Optional<T> find(Class<T> entityClass, K id) {
        requireNonNull(entityClass, "entityClass is required");
        requireNonNull(id, "id is required");
        EntityMetadata entityMetadata = getEntities().get(entityClass);
        FieldMapping idField = entityMetadata.getId()
                .orElseThrow(() -> IdNotFoundException.newInstance(entityClass));

        Object value = ConverterUtil.getValue(id, entityMetadata, idField.getFieldName(), getConverters());
        ColumnQuery query = ColumnQuery.select().from(entityMetadata.getName())
                .where(idField.getName()).eq(value).build();

        return singleResult(query);
    }

    @Override
    public <T, K> void delete(Class<T> entityClass, K id) {
        requireNonNull(entityClass, "entityClass is required");
        requireNonNull(id, "id is required");

        EntityMetadata entityMetadata = getEntities().get(entityClass);
        FieldMapping idField = entityMetadata.getId()
                .orElseThrow(() -> IdNotFoundException.newInstance(entityClass));
        Object value = ConverterUtil.getValue(id, entityMetadata, idField.getFieldName(), getConverters());

        ColumnDeleteQuery query = ColumnDeleteQuery.delete().from(entityMetadata.getName())
                .where(idField.getName()).eq(value).build();
        getManager().delete(query);
    }


    @Override
    public <T> Stream<T> query(String query) {
        requireNonNull(query, "query is required");
        return PARSER.query(query, getManager(), getObserver()).map(c -> (T) getConverter().toEntity(c));
    }

    @Override
    public <T> Optional<T> singleResult(String query) {
        Stream<T> entities = query(query);
        final Iterator<T> iterator = entities.iterator();

        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final T entity = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.of(entity);
        }
        throw new NonUniqueResultException("No unique result found to the query: " + query);
    }

    @Override
    public PreparedStatement prepare(String query) {
        return new ColumnPreparedStatement(PARSER.prepare(query, getManager(), getObserver()), getConverter());
    }


    @Override
    public long count(String columnFamily) {
        return getManager().count(columnFamily);
    }


    @Override
    public <T> long count(Class<T> entityClass) {
        requireNonNull(entityClass, "entity class is required");
        EntityMetadata entityMetadata = getEntities().get(entityClass);
        return getManager().count(entityMetadata.getName());
    }

    private <T> Stream<T> executeQuery(ColumnQuery query) {
        requireNonNull(query, "query is required");
        getEventManager().firePreQuery(query);
        Stream<ColumnEntity> entities = getManager().select(query);
        Function<ColumnEntity, T> function = e -> getConverter().toEntity(e);
        return entities.map(function);
    }
}
