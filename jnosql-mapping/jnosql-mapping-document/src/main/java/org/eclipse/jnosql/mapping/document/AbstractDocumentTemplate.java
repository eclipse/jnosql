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
package org.eclipse.jnosql.mapping.document;


import jakarta.data.exceptions.NonUniqueResultException;
import jakarta.nosql.PreparedStatement;
import jakarta.nosql.QueryMapper;
import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentEntity;
import org.eclipse.jnosql.communication.document.DocumentManager;
import org.eclipse.jnosql.communication.document.DocumentObserverParser;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.communication.document.DocumentQueryParser;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.IdNotFoundException;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldMapping;
import org.eclipse.jnosql.mapping.util.ConverterUtil;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

/**
 * This class provides a skeletal implementation of the {@link JNoSQLDocumentTemplate} interface,
 * to minimize the effort required to implement this interface.
 */
public abstract class AbstractDocumentTemplate implements JNoSQLDocumentTemplate {


    private static final DocumentQueryParser PARSER = new DocumentQueryParser();

    protected abstract DocumentEntityConverter getConverter();

    protected abstract DocumentManager getManager();

    protected abstract DocumentWorkflow getWorkflow();

    protected abstract DocumentEventPersistManager getEventManager();

    protected abstract EntitiesMetadata getEntities();

    protected abstract Converters getConverters();

    private final UnaryOperator<DocumentEntity> insert = e -> getManager().insert(e);

    private final UnaryOperator<DocumentEntity> update = e -> getManager().update(e);

    private DocumentObserverParser columnQueryParser;


    private DocumentObserverParser getObserver() {
        if (Objects.isNull(columnQueryParser)) {
            columnQueryParser = new DocumentMapperObserver(getEntities());
        }
        return columnQueryParser;
    }

    @Override
    public <T> T insert(T entity) {
        requireNonNull(entity, "entity is required");
        return getWorkflow().flow(entity, insert);
    }


    @Override
    public <T> T insert(T entity, Duration ttl) {
        requireNonNull(entity, "entity is required");
        requireNonNull(ttl, "ttl is required");
        return getWorkflow().flow(entity, e -> getManager().insert(e, ttl));
    }

    @Override
    public <T> Iterable<T> insert(Iterable<T> entities) {
        requireNonNull(entities, "entity is required");
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
    public <T> T update(T entity) {
        requireNonNull(entity, "entity is required");
        return getWorkflow().flow(entity, update);
    }

    @Override
    public <T> Iterable<T> update(Iterable<T> entities) {
        requireNonNull(entities, "entity is required");
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::update).collect(Collectors.toList());
    }

    @Override
    public void delete(DocumentDeleteQuery query) {
        requireNonNull(query, "query is required");
        getEventManager().firePreDeleteQuery(query);
        getManager().delete(query);
    }

    @Override
    public <T> Stream<T> select(DocumentQuery query) {
        Objects.requireNonNull(query, "query is required");
        return executeQuery(query);
    }

    @Override
    public boolean exists(DocumentQuery query) {
        return getManager().exists(query);
    }

    @Override
    public long count(DocumentQuery query) {
        return getManager().count(query);
    }

    @Override
    public <T> Optional<T> singleResult(DocumentQuery query) {
        Objects.requireNonNull(query, "query is required");
        final Stream<T> entities = select(query);
        final Iterator<T> iterator = entities.iterator();
        if(!iterator.hasNext()) {
            return Optional.empty();
        }
        final T entity = iterator.next();
        if(!iterator.hasNext()) {
            return Optional.of(entity);
        }
        throw new NonUniqueResultException("No unique result found to the query: " + query);
    }

    @Override
    public <T, K> Optional<T> find(Class<T> type, K id) {
        requireNonNull(type, "type is required");
        requireNonNull(id, "id is required");
        EntityMetadata entityMetadata = getEntities().get(type);
        FieldMapping idField = entityMetadata.getId()
                .orElseThrow(() -> IdNotFoundException.newInstance(type));

        Object value = ConverterUtil.getValue(id, entityMetadata, idField.getFieldName(), getConverters());
        DocumentQuery query = DocumentQuery.select().from(entityMetadata.getName())
                .where(idField.getName()).eq(value).build();

        return singleResult(query);
    }

    @Override
    public <T, K> void delete(Class<T> type, K id) {
        requireNonNull(type, "type is required");
        requireNonNull(id, "id is required");

        EntityMetadata entityMetadata = getEntities().get(type);
        FieldMapping idField = entityMetadata.getId()
                .orElseThrow(() -> IdNotFoundException.newInstance(type));

        Object value = ConverterUtil.getValue(id, entityMetadata, idField.getFieldName(), getConverters());
        DocumentDeleteQuery query = DocumentDeleteQuery.delete().from(entityMetadata.getName())
                .where(idField.getName()).eq(value).build();

        delete(query);
    }

    @Override
    public <T> Stream<T> query(String query) {
        requireNonNull(query, "query is required");
        return PARSER.query(query, getManager(), getObserver()).map(c -> (T) getConverter().toEntity(c));
    }

    @Override
    public <T> Optional<T> singleResult(String query) {
        requireNonNull(query, "query is required");
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
        return new DocumentPreparedStatement(PARSER.prepare(query, getManager(), getObserver()), getConverter());
    }


    @Override
    public long count(String documentCollection) {
        return getManager().count(documentCollection);
    }

    public <T> long count(Class<T> type) {
        requireNonNull(type, "type is required");
        EntityMetadata entityMetadata = getEntities().get(type);
        return getManager().count(entityMetadata.getName());
    }

    private <T> Stream<T> executeQuery(DocumentQuery query) {
        requireNonNull(query, "query is required");
        getEventManager().firePreQuery(query);
        Stream<DocumentEntity> entities = getManager().select(query);
        Function<DocumentEntity, T> function = e -> getConverter().toEntity(e);
        return entities.map(function).peek(getEventManager()::firePostEntity);
    }

    @Override
    public <T> QueryMapper.MapperFrom select(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        return new DocumentMapperSelect(metadata, getConverters(), this);
    }

    @Override
    public <T> QueryMapper.MapperDeleteFrom delete(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        return new DocumentMapperDelete(metadata, getConverters(), this);
    }

    @Override
    public <T> Stream<T> findAll(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        DocumentQuery query = DocumentQuery.select().from(metadata.getName()).build();
        return select(query);
    }

    @Override
    public <T> void deleteAll(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        DocumentDeleteQuery query = DocumentDeleteQuery.delete().from(metadata.getName()).build();
        delete(query);
    }


}
