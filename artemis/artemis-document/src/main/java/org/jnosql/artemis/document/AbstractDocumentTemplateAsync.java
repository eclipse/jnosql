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
package org.jnosql.artemis.document;


import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentObserverParser;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.document.DocumentQueryParserAsync;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.IdNotFoundException;
import jakarta.nosql.mapping.PreparedStatementAsync;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.mapping.document.DocumentTemplateAsync;
import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.mapping.reflection.ClassMappings;
import jakarta.nosql.mapping.reflection.FieldMapping;
import org.jnosql.artemis.util.ConverterUtil;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * This class provides a skeletal implementation of the {@link DocumentTemplateAsync} interface,
 * to minimize the effort required to implement this interface.
 */
public abstract class AbstractDocumentTemplateAsync implements DocumentTemplateAsync {


    private static final Consumer EMPTY = t -> {
    };

    private static final DocumentQueryParserAsync PARSER = ServiceLoaderProvider.get(DocumentQueryParserAsync.class);

    protected abstract DocumentEntityConverter getConverter();

    protected abstract DocumentCollectionManagerAsync getManager();

    protected abstract ClassMappings getClassMappings();

    protected abstract Converters getConverters();

    private DocumentObserverParser observer;


    private DocumentObserverParser getObserver() {
        if (Objects.isNull(observer)) {
            observer = new DocumentMapperObserver(getClassMappings());
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
        Consumer<DocumentEntity> dianaCallBack = c -> callBack.accept((T) getConverter().toEntity(entity.getClass(), c));
        getManager().insert(getConverter().toDocument(entity), dianaCallBack);
    }

    @Override
    public <T> void insert(T entity, Duration ttl, Consumer<T> callBack) {
        requireNonNull(entity, "entity is required");
        requireNonNull(ttl, "ttl is required");
        requireNonNull(callBack, "callBack is required");
        Consumer<DocumentEntity> dianaCallBack = c -> callBack.accept((T) getConverter().toEntity(entity.getClass(), c));
        getManager().insert(getConverter().toDocument(entity), ttl, dianaCallBack);
    }

    @Override
    public <T> void update(T entity) {
        requireNonNull(entity, "entity is required");
        update(entity, EMPTY);
    }

    @Override
    public <T> void update(T entity, Consumer<T> callBack) {
        requireNonNull(entity, "entity is required");
        requireNonNull(callBack, "callBack is required");
        Consumer<DocumentEntity> dianaCallBack = c -> callBack.accept((T) getConverter().toEntity(entity.getClass(), c));
        getManager().update(getConverter().toDocument(entity), dianaCallBack);
    }

    @Override
    public void delete(DocumentDeleteQuery query) {
        requireNonNull(query, "query is required");
        getManager().delete(query);
    }

    @Override
    public void delete(DocumentDeleteQuery query, Consumer<Void> callback) {
        requireNonNull(query, "query is required");
        requireNonNull(callback, "callBack is required");
        getManager().delete(query, callback);
    }

    @Override
    public <T> void select(DocumentQuery query, Consumer<Stream<T>> callback) {
        requireNonNull(query, "query is required");
        requireNonNull(callback, "callBack is required");

        Consumer<Stream<DocumentEntity>> dianaCallBack = d -> callback.accept(
                d.map(getConverter()::toEntity)
                        .map(o -> (T) o));
        getManager().select(query, dianaCallBack);
    }


    @Override
    public <T, K> void find(Class<T> entityClass, K id, Consumer<Optional<T>> callBack) {

        requireNonNull(entityClass, "entityClass is required");
        requireNonNull(id, "id is required");
        requireNonNull(callBack, "callBack is required");

        ClassMapping classMapping = getClassMappings().get(entityClass);
        FieldMapping idField = classMapping.getId()
                .orElseThrow(() -> IdNotFoundException.newInstance(entityClass));

        Object value = ConverterUtil.getValue(id, classMapping, idField.getFieldName(), getConverters());
        DocumentQuery query = DocumentQuery.select().from(classMapping.getName())
                .where(idField.getName()).eq(value).build();

        singleResult(query, callBack);
    }

    @Override
    public <T, K> void delete(Class<T> entityClass, K id, Consumer<Void> callBack) {

        requireNonNull(entityClass, "entityClass is required");
        requireNonNull(id, "id is required");
        requireNonNull(callBack, "callBack is required");

        DocumentDeleteQuery query = getDeleteQuery(entityClass, id);
        delete(query, callBack);
    }

    @Override
    public <T, K> void delete(Class<T> entityClass, K id) {

        requireNonNull(entityClass, "entityClass is required");
        requireNonNull(id, "id is required");

        DocumentDeleteQuery query = getDeleteQuery(entityClass, id);

        delete(query);
    }


    @Override
    public <T> void query(String query, Consumer<Stream<T>> callback) {
        requireNonNull(query, "query is required");
        requireNonNull(callback, "callback is required");
        Consumer<Stream<DocumentEntity>> mapper = columnEntities -> callback
                .accept(columnEntities.map(c -> (T) getConverter().toEntity(c)));
        PARSER.query(query, getManager(), mapper, getObserver());
    }

    @Override
    public <T> void singleResult(String query, Consumer<Optional<T>> callback) {
        requireNonNull(query, "query is required");
        requireNonNull(callback, "callBack is required");
        Consumer<Stream<DocumentEntity>> mapper = columnEntities -> {
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
        return new DocumentPreparedStatementAsync(PARSER.prepare(query, getManager(), getObserver()), getConverter());
    }

    @Override
    public void count(String documentCollection, Consumer<Long> callback) {
        getManager().count(documentCollection, callback);
    }

    @Override
    public <T> void count(Class<T> entityClass, Consumer<Long> callback) {
        Objects.requireNonNull(entityClass, "entityClass is required");
        Objects.requireNonNull(callback, "callback is required");
        ClassMapping classMapping = getClassMappings().get(entityClass);
        getManager().count(classMapping.getName(), callback);
    }

    private <T, K> DocumentDeleteQuery getDeleteQuery(Class<T> entityClass, K id) {
        ClassMapping classMapping = getClassMappings().get(entityClass);
        FieldMapping idField = classMapping.getId()
                .orElseThrow(() -> IdNotFoundException.newInstance(entityClass));

        Object value = ConverterUtil.getValue(id, classMapping, idField.getFieldName(), getConverters());

        return DocumentDeleteQuery.delete().from(classMapping.getName())
                .where(idField.getName()).eq(value).build();
    }
}
