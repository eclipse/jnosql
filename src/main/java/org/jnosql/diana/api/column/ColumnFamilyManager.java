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

package org.jnosql.diana.api.column;


import org.jnosql.diana.api.NonUniqueResultException;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Interface used to interact with the persistence context to {@link ColumnEntity}
 * The ColumnFamilyManager API is used to create and remove persistent {@link ColumnEntity} instances,
 * to select entities by their primary key, and to select over entities.
 */
public interface ColumnFamilyManager extends AutoCloseable {

    /**
     * Saves a Column family entity
     *
     * @param entity column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entity is null
     */
    ColumnEntity insert(ColumnEntity entity) throws NullPointerException;

    /**
     * Updates a Column family entity
     *
     * @param entity column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entity is null
     */
    ColumnEntity update(ColumnEntity entity) throws NullPointerException;

    /**
     * Updates a Column family entities, by default it's just run for each saving using
     * {@link ColumnFamilyManager#update(ColumnEntity)}, each NoSQL vendor might
     * replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default Iterable<ColumnEntity> update(Iterable<ColumnEntity> entities) throws NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::update).collect(Collectors.toList());
    }

    /**
     * Saves a Column family entity with time to live
     *
     * @param entity column family to be saved
     * @param ttl    time to live
     * @return the entity saved
     * @throws NullPointerException          when either entity or ttl are null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    ColumnEntity insert(ColumnEntity entity, Duration ttl) throws NullPointerException, UnsupportedOperationException;

    /**
     * Saves a Column family entities, by default it's just run for each saving using
     * {@link ColumnFamilyManager#insert(ColumnEntity)}, each NoSQL vendor might
     * replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default Iterable<ColumnEntity> insert(Iterable<ColumnEntity> entities) throws NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::insert).collect(Collectors.toList());
    }

    /**
     * Saves a Column family entity with time to live, by default it's just run for each saving using
     * {@link ColumnFamilyManager#insert(ColumnEntity, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @param ttl      time to live
     * @return the entity saved
     * @throws NullPointerException          when either entity or ttl are null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    default Iterable<ColumnEntity> insert(Iterable<ColumnEntity> entities, Duration ttl) throws NullPointerException, UnsupportedOperationException {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        return StreamSupport.stream(entities.spliterator(), false).map(c -> this.insert(c, ttl)).collect(Collectors.toList());
    }


    /**
     * Deletes an entity
     *
     * @param query the select to delete an entity
     * @throws NullPointerException when either select or collection are null
     */
    void delete(ColumnDeleteQuery query) throws NullPointerException;

    /**
     * Finds {@link ColumnEntity} from select
     *
     * @param query - select to figure out entities
     * @return entities found by select
     * @throws NullPointerException when select is null
     */
    List<ColumnEntity> select(ColumnQuery query) throws NullPointerException;

    /**
     * Returns a single entity from select
     *
     * @param query - select to figure out entities
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NonUniqueResultException when the result has more than 1 entity
     * @throws NullPointerException     when select is null
     */
    default Optional<ColumnEntity> singleResult(ColumnQuery query) throws NonUniqueResultException, NullPointerException {
        List<ColumnEntity> entities = select(query);
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }

        throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
    }

    /**
     * closes a resource
     */
    void close();

}
