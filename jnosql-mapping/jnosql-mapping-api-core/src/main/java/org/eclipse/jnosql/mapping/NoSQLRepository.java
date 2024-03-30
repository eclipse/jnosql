/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping;

import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Delete;

import java.util.stream.Stream;

/**
 * Interface for NoSQL repositories, providing additional operations beyond basic {@link BasicRepository}.
 *
 * @param <T> the type of the entity managed by this repository
 * @param <K> the type of the entity's primary key
 */
public interface NoSQLRepository<T, K> extends BasicRepository<T, K>, CrudRepository<T, K> {

    /**
     * Deletes all persistent entities of the primary entity type that are managed by the repository.
     *
     * @throws UnsupportedOperationException for Key-Value and Wide-Column databases that are not capable of the {@code deleteAll} operation.
     */
    @Delete
    void deleteAll();

    /**
     * Retrieves the total number of persistent entities of the specified type in the database.
     *
     * @return the total number of entities.
     * @throws UnsupportedOperationException for Key-Value and Wide-Column databases that are not capable of the {@code count} operation.
     */
    long countBy();

    /**
     * Returns whether an entity with the given Id exists.
     *
     * @param id must not be {@code null}.
     * @return {@code true} if an entity with the given Id exists, {@code false} otherwise.
     * @throws NullPointerException when the Id is {@code null}.
     */
    boolean existsById(K id);

    /**
     * Returns all instances of the type {@code T} with the given Ids.
     * <p>
     * If some or all Ids are not found, no entities are returned for these Ids.
     * <p>
     * Note that the order of elements in the result is not guaranteed.
     *
     * @param ids must not be {@code null} nor contain any {@code null} values.
     * @return guaranteed to be not {@code null}. The size can be equal or less than the number of given
     * ids.
     * @throws NullPointerException in case the given {@link Iterable ids} or one of its items is {@code null}.
     */
    Stream<T> findByIdIn(Iterable<K> ids);


    /**
     * Deletes all instances of the type {@code T} with the given Ids.
     * <p>
     * Entities that are not found in the persistent store are silently ignored.
     *
     * @param ids must not be {@code null}. Must not contain {@code null} elements.
     * @throws NullPointerException when the iterable is {@code null} or contains {@code null} elements.
     */
    void deleteByIdIn(Iterable<K> ids);
}
