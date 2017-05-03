/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package org.jnosql.diana.api.document;


import org.jnosql.diana.api.ExecuteAsyncQueryException;
import org.jnosql.diana.api.NonUniqueResultException;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

/**
 * Interface used to interact with the persistence context to {@link DocumentEntity}
 * The DocumentCollectionManager API is used to create and remove persistent {@link DocumentEntity} instances,
 * to select entities by their primary key, and to query over entities. The main difference to {@link DocumentCollectionManager}
 * is because all the operation works asynchronously.
 */
public interface DocumentCollectionManagerAsync extends AutoCloseable {


    /**
     * Saves an entity asynchronously
     *
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entity is null
     */
    void insert(DocumentEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException, NullPointerException;

    /**
     * Saves an entity asynchronously with time to live
     *
     * @param entity entity to be saved
     * @param ttl    the time to live
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity or ttl are null
     */
    void insert(DocumentEntity entity, Duration ttl) throws ExecuteAsyncQueryException, UnsupportedOperationException, NullPointerException;

    /**
     * Saves entities asynchronously, by default it's just run for each saving using
     * {@link DocumentCollectionManagerAsync#insert(DocumentEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entities is null
     */
    default void insert(Iterable<DocumentEntity> entities) throws ExecuteAsyncQueryException, UnsupportedOperationException, NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(this::insert);
    }

    /**
     * Saves entities asynchronously with time to live, by default it's just run for each saving using
     * {@link DocumentCollectionManagerAsync#insert(DocumentEntity, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param ttl      time to live
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entities or ttl are null
     */
    default void insert(Iterable<DocumentEntity> entities, Duration ttl) throws ExecuteAsyncQueryException,
            UnsupportedOperationException, NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(d -> insert(d, ttl));
    }

    /**
     * Saves an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the saved entity within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either callback or entity are null
     */
    void insert(DocumentEntity entity, Consumer<DocumentEntity> callBack) throws
            ExecuteAsyncQueryException, UnsupportedOperationException, NullPointerException;

    /**
     * Saves an entity asynchronously with time to live
     *
     * @param entity   entity to be saved
     * @param ttl      time to live
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the saved entity within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity or ttl or callback are null
     */
    void insert(DocumentEntity entity, Duration ttl, Consumer<DocumentEntity> callBack) throws
            ExecuteAsyncQueryException, UnsupportedOperationException, NullPointerException;


    /**
     * Updates an entity asynchronously
     *
     * @param entity entity to be updated
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entity is null
     */
    void update(DocumentEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException, NullPointerException;

    /**
     * Updates entities asynchronously, by default it's just run for each saving using
     * {@link DocumentCollectionManagerAsync#update(DocumentEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entities is null
     */
    default void update(Iterable<DocumentEntity> entities) throws ExecuteAsyncQueryException, UnsupportedOperationException, NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(this::update);
    }

    /**
     * Updates an entity asynchronously
     *
     * @param entity   entity to be updated
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the updated entity within parametersa
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity or callback are null
     */
    void update(DocumentEntity entity, Consumer<DocumentEntity> callBack) throws
            ExecuteAsyncQueryException, UnsupportedOperationException, NullPointerException;


    /**
     * Deletes an entity asynchronously
     *
     * @param query query to delete an entity
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    void delete(DocumentDeleteQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Deletes an entity asynchronously
     *
     * @param query    query to delete an entity
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the null within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either query or callback are null
     */
    void delete(DocumentDeleteQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException,
            UnsupportedOperationException, NullPointerException;

    /**
     * Finds {@link DocumentEntity} from query asynchronously
     *
     * @param query    query to select entities
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the result of query within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either query or callback are null
     */
    void query(DocumentQuery query, Consumer<List<DocumentEntity>> callBack) throws
            ExecuteAsyncQueryException, UnsupportedOperationException, NullPointerException;

    /**
     * Returns a single entity from query
     *
     * @param query    - query to figure out entities
     * @param callBack the callback
     * @throws NonUniqueResultException when the result has more than 1 entity
     * @throws NullPointerException     when query is null
     */
    default void singleResult(DocumentQuery query, Consumer<Optional<DocumentEntity>> callBack) throws NonUniqueResultException,
            ExecuteAsyncQueryException, UnsupportedOperationException {

        query(query, entities -> {
            if (entities.isEmpty()) {
                callBack.accept(Optional.empty());
                return;
            } else if (entities.size() == 1) {
                callBack.accept(Optional.of(entities.get(0)));
                return;
            }
            throw new NonUniqueResultException("The query returns more than one entity, query: " + query);
        });

    }

    /**
     * closes a resource
     */
    void close();

}
