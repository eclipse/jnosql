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
package org.eclipse.jnosql.mapping.column;


import jakarta.data.exceptions.NonUniqueResultException;
import jakarta.nosql.PreparedStatement;
import jakarta.nosql.QueryMapper;
import jakarta.nosql.column.ColumnTemplate;
import org.eclipse.jnosql.communication.column.ColumnDeleteQuery;
import org.eclipse.jnosql.communication.column.ColumnEntity;
import org.eclipse.jnosql.communication.column.ColumnManager;
import org.eclipse.jnosql.communication.column.ColumnObserverParser;
import org.eclipse.jnosql.communication.column.ColumnQuery;
import org.eclipse.jnosql.communication.column.ColumnQueryParser;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.IdNotFoundException;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.util.ConverterUtil;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

/**
 * The template method to {@link ColumnTemplate}
 */
public abstract class AbstractColumnTemplate implements JNoSQLColumnTemplate {

    private static final ColumnQueryParser PARSER = new ColumnQueryParser();

    protected abstract ColumnEntityConverter getConverter();

    protected abstract ColumnManager getManager();

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
        return persist(entity, insert);
    }


    @Override
    public <T> T insert(T entity, Duration ttl) {
        requireNonNull(entity, "entity is required");
        requireNonNull(ttl, "ttl is required");
        return persist(entity, e -> getManager().insert(e, ttl));
    }


    @Override
    public <T> T update(T entity) {
        requireNonNull(entity, "entity is required");
        return persist(entity, update);
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
        getManager().delete(query);
    }


    @Override
    public <T> Stream<T> select(ColumnQuery query) {
        requireNonNull(query, "query is required");
        return executeQuery(query);
    }

    @Override
    public long count(ColumnQuery query) {
        return getManager().count(query);
    }

    @Override
    public boolean exists(ColumnQuery query) {
        return getManager().exists(query);
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
    public <T, K> Optional<T> find(Class<T> type, K id) {
        requireNonNull(type, "type is required");
        requireNonNull(id, "id is required");
        EntityMetadata entityMetadata = getEntities().get(type);
        FieldMetadata idField = entityMetadata.id()
                .orElseThrow(() -> IdNotFoundException.newInstance(type));

        Object value = ConverterUtil.getValue(id, entityMetadata, idField.fieldName(), getConverters());
        ColumnQuery query = ColumnQuery.select().from(entityMetadata.name())
                .where(idField.name()).eq(value).build();

        return singleResult(query);
    }

    @Override
    public <T, K> void delete(Class<T> type, K id) {
        requireNonNull(type, "type is required");
        requireNonNull(id, "id is required");

        EntityMetadata entityMetadata = getEntities().get(type);
        FieldMetadata idField = entityMetadata.id()
                .orElseThrow(() -> IdNotFoundException.newInstance(type));
        Object value = ConverterUtil.getValue(id, entityMetadata, idField.fieldName(), getConverters());

        ColumnDeleteQuery query = ColumnDeleteQuery.delete().from(entityMetadata.name())
                .where(idField.name()).eq(value).build();
        getManager().delete(query);
    }

    @Override
    public <T> void delete(T entity) {
        Objects.requireNonNull(entity, "entity is required");
        EntityMetadata entityMetadata = getEntities().get(entity.getClass());
        FieldMetadata idField = entityMetadata.id()
                .orElseThrow(() -> IdNotFoundException.newInstance(entity.getClass()));
        Object value = ConverterUtil.getValue(idField.type(), entityMetadata, idField.fieldName(), getConverters());
        ColumnDeleteQuery query = ColumnDeleteQuery.delete().from(entityMetadata.name())
                .where(idField.name()).eq(value).build();
        getManager().delete(query);
    }

    @Override
    public <T> void delete(Iterable<? extends T> entities) {
        Objects.requireNonNull(entities, "entities is required");
        entities.forEach(this::delete);
    }


    @Override
    public <T> Stream<T> query(String query) {
        requireNonNull(query, "query is required");
        return PARSER.query(query, getManager(), getObserver()).map(c -> getConverter().toEntity(c));
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
    public <T> long count(Class<T> type) {
        requireNonNull(type, "entity class is required");
        EntityMetadata entityMetadata = getEntities().get(type);
        return getManager().count(entityMetadata.name());
    }

    private <T> Stream<T> executeQuery(ColumnQuery query) {
        requireNonNull(query, "query is required");
        Stream<ColumnEntity> entities = getManager().select(query);
        Function<ColumnEntity, T> function = e -> getConverter().toEntity(e);
        return entities.map(function).peek(getEventManager()::firePostEntity);
    }

    @Override
    public <T> QueryMapper.MapperFrom select(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        return new ColumnMapperSelect(metadata, getConverters(), this);
    }

    @Override
    public <T> QueryMapper.MapperDeleteFrom delete(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        return new ColumnMapperDelete(metadata, getConverters(), this);
    }

    @Override
    public <T> Stream<T> findAll(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        ColumnQuery query = ColumnQuery.select().from(metadata.name()).build();
        return select(query);
    }

    @Override
    public <T> void deleteAll(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        ColumnDeleteQuery query = ColumnDeleteQuery.delete().from(metadata.name()).build();
        delete(query);
    }

    protected <T> T persist(T entity, UnaryOperator<ColumnEntity> persistAction) {
        return Stream.of(entity)
                .map(toUnary(getEventManager()::firePreEntity))
                .map(getConverter()::toColumn)
                .map(persistAction)
                .map(t -> getConverter().toEntity(entity, t))
                .map(toUnary(getEventManager()::firePostEntity))
                .findFirst()
                .orElseThrow();
    }

    private <T> UnaryOperator<T> toUnary(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return t;
        };
    }
}
