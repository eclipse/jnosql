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
package org.jnosql.artemis.column;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.column.ColumnQueryParserAsync;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.IdNotFoundException;
import jakarta.nosql.mapping.PreparedStatementAsync;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.mapping.column.ColumnTemplateAsync;
import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.mapping.reflection.ClassMappings;
import jakarta.nosql.mapping.reflection.FieldMapping;
import org.jnosql.artemis.util.ConverterUtil;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;


/**
 * The template method to {@link ColumnTemplateAsync}
 */
public abstract class AbstractColumnTemplateAsync implements ColumnTemplateAsync {

    private static final Consumer EMPTY = t -> {
    };
    private static final ColumnQueryParserAsync PARSER = ServiceLoaderProvider.get(ColumnQueryParserAsync.class);

    protected abstract ColumnEntityConverter getConverter();

    protected abstract ColumnFamilyManagerAsync getManager();

    protected abstract ClassMappings getClassMappings();

    protected abstract Converters getConverters();

    private ColumnObserverParser observer;


    private ColumnObserverParser getObserver() {
        if (Objects.isNull(observer)) {
            observer = new ColumnMapperObserver(getClassMappings());
        }
        return observer;
    }


    @Override
    public <T> void insert(T entity) {
        insert(entity, EMPTY);
    }

    @Override
    public <T> void insert(T entity, Duration ttl) {
        insert(entity, ttl, EMPTY);
    }

    @Override
    public <T> void insert(T entity, Consumer<T> callBack) {
        requireNonNull(entity, "entity is required");
        requireNonNull(callBack, "callBack is required");
        Consumer<ColumnEntity> dianaCallBack = c -> callBack.accept((T) getConverter().toEntity(entity.getClass(), c));
        getManager().insert(getConverter().toColumn(entity), dianaCallBack);
    }

    @Override
    public <T> void insert(T entity, Duration ttl, Consumer<T> callback) {
        requireNonNull(entity, "entity is required");
        requireNonNull(ttl, "ttl is required");
        requireNonNull(callback, "callBack is required");
        Consumer<ColumnEntity> dianaCallBack = c -> callback.accept((T) getConverter().toEntity(entity.getClass(), c));
        getManager().insert(getConverter().toColumn(entity), ttl, dianaCallBack);
    }

    @Override
    public <T> void update(T entity) {
        requireNonNull(entity, "entity is required");
        update(entity, EMPTY);
    }

    @Override
    public <T> void update(Iterable<T> entities) {
        requireNonNull(entities, "entities is required");
        final List<ColumnEntity> columnEntities = Stream.of(entities).map(getConverter()::toColumn).collect(Collectors.toList());
        getManager().update(columnEntities);
    }

    @Override
    public <T> void update(T entity, Consumer<T> callback) {
        requireNonNull(entity, "entity is required");
        requireNonNull(callback, "callBack is required");
        Consumer<ColumnEntity> dianaCallBack = c -> callback.accept((T) getConverter().toEntity(entity.getClass(), c));
        getManager().update(getConverter().toColumn(entity), dianaCallBack);
    }

    @Override
    public void delete(ColumnDeleteQuery query) {
        requireNonNull(query, "query is required");
        getManager().delete(query);
    }

    @Override
    public void delete(ColumnDeleteQuery query, Consumer<Void> callback) {
        requireNonNull(query, "query is required");
        requireNonNull(callback, "callback is required");
        getManager().delete(query, callback);
    }

    @Override
    public <T> void select(ColumnQuery query, Consumer<Stream<T>> callback) {
        requireNonNull(query, "query is required");
        requireNonNull(callback, "callBack is required");

        Consumer<Stream<ColumnEntity>> dianaCallBack = d -> callback.accept(
                d.map(getConverter()::toEntity)
                        .map(o -> (T) o));
        getManager().select(query, dianaCallBack);
    }

    @Override
    public <T> void singleResult(ColumnQuery query, Consumer<Optional<T>> callback) {
        requireNonNull(query, "query is required");
        requireNonNull(callback, "callback is required");
        select(query, entities -> {
            final Iterator<T> iterator = (Iterator<T>) entities.iterator();
            if (!iterator.hasNext()) {
                callback.accept(Optional.empty());
            }
            final T entity = iterator.next();
            if (!iterator.hasNext()) {
                callback.accept(Optional.of(entity));
            }
            throw new NonUniqueResultException("No unique result found to the query: " + query);
        });
    }

    @Override
    public <T, K> void find(Class<T> entityClass, K id, Consumer<Optional<T>> callback) {

        requireNonNull(entityClass, "entityClass is required");
        requireNonNull(id, "id is required");
        requireNonNull(callback, "callBack is required");

        ClassMapping classMapping = getClassMappings().get(entityClass);
        FieldMapping idField = classMapping.getId()
                .orElseThrow(() -> IdNotFoundException.newInstance(entityClass));

        Object value = ConverterUtil.getValue(id, classMapping, idField.getFieldName(), getConverters());

        ColumnQuery query = ColumnQuery.select().from(classMapping.getName())
                .where(idField.getName()).eq(value).build();

        singleResult(query, callback);
    }

    @Override
    public <T, K> void delete(Class<T> entityClass, K id, Consumer<Void> callback) {

        requireNonNull(entityClass, "entityClass is required");
        requireNonNull(id, "id is required");
        requireNonNull(callback, "callBack is required");

        ColumnDeleteQuery query = getDeleteQuery(entityClass, id);

        delete(query, callback);
    }


    @Override
    public <T, K> void delete(Class<T> entityClass, K id) {
        requireNonNull(entityClass, "entityClass is required");
        requireNonNull(id, "id is required");

        ColumnDeleteQuery query = getDeleteQuery(entityClass, id);

        delete(query);
    }

    @Override
    public <T> void query(String query, Consumer<Stream<T>> callback) {
        requireNonNull(query, "query is required");
        requireNonNull(callback, "callback is required");
        Consumer<Stream<ColumnEntity>> mapper = columnEntities ->
                callback.accept(columnEntities.map(c -> (T) getConverter().toEntity(c)));
        PARSER.query(query, getManager(), mapper, getObserver());
    }

    @Override
    public <T> void singleResult(String query, Consumer<Optional<T>> callback) {
        requireNonNull(query, "query is required");
        requireNonNull(callback, "callBack is required");
        Consumer<Stream<ColumnEntity>> mapper = columnEntities -> {
            Stream<T> entities = columnEntities.map(c -> (T) getConverter().toEntity(c));
            final Iterator<T> iterator = entities.iterator();
            if (!iterator.hasNext()) {
                callback.accept(Optional.empty());
            }
            final T entity = iterator.next();
            if (!iterator.hasNext()) {
                callback.accept(Optional.of(entity));
            }
            throw new UnsupportedOperationException("This query does not return a unique result: " + query);
        };
        PARSER.query(query, getManager(), mapper, getObserver());
    }

    @Override
    public PreparedStatementAsync prepare(String query) {
        requireNonNull(query, "query is required");
        return new ColumnPreparedStatementAsync(PARSER.prepare(query, getManager(), getObserver()), getConverter());
    }

    @Override
    public void count(String columnFamily, Consumer<Long> callback) {
        getManager().count(columnFamily, callback);
    }


    @Override
    public <T> void count(Class<T> entityClass, Consumer<Long> callback) {
        requireNonNull(entityClass, "entity class is required");
        requireNonNull(callback, "callback is required");
        ClassMapping classMapping = getClassMappings().get(entityClass);
        getManager().count(classMapping.getName(), callback);
    }


    private <T, K> ColumnDeleteQuery getDeleteQuery(Class<T> entityClass, K id) {
        ClassMapping classMapping = getClassMappings().get(entityClass);
        FieldMapping idField = classMapping.getId()
                .orElseThrow(() -> IdNotFoundException.newInstance(entityClass));

        Object value = ConverterUtil.getValue(id, classMapping, idField.getFieldName(), getConverters());
        return ColumnDeleteQuery.delete().from(classMapping.getName())
                .where(idField.getName()).eq(value).build();
    }
}

