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
package org.jnosql.artemis;

import org.jnosql.diana.ExecuteAsyncQueryException;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Interface to generic async CRUD operations on a repository for a specific type.
 * The query builder mechanism built into Artemis repository infrastructure is useful for building constraining queries
 * over entities of the repository. The mechanism strips the prefixes is defined by:
 * <p>findBy: to select any information T</p>
 * <p>deleteBy: To delete any information T</p>
 * Artemis has some keywords on method:
 * <p><b>And</b></p>
 * <p><b>Or</b></p>
 * <p><b>Between</b></p>
 * <p><b>LessThan</b></p>
 * <p><b>GreaterThan</b></p>
 * <p><b>LessThanEqual</b></p>
 * <p><b>GreaterThanEqual</b></p>
 * <p><b>Like</b></p>
 * <p><b>In</b></p>
 * <p><b>OrderBy</b></p>
 * <p><b>OrderBy____Desc</b></p>
 * <p><b>OrderBy_____ASC</b></p>
 *
 * @param <T>  the bean type
 * @param <K> the K type
 */
public interface RepositoryAsync<T, K> {


    /**
     * Saves an entity asynchronously
     *
     * @param <S>    the entity type
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when entity are null
     */
    <S extends T> void save(S entity);

    /**
     * Saves entities asynchronously
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param <S>      the entity type
     * @param entities entities to be saved
     * @throws ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when entities is null
     */
    <S extends T> void save(Iterable<S> entities);

    /**
     * Deletes the entity with the given id.
     *
     * @param id the id
     * @throws NullPointerException when id is null
     */
    void deleteById(K id);

    /**
     * Finds an entity given the id
     *
     * @param id       the id
     * @param callBack the callback
     * @throws NullPointerException when id is null
     */
    void findById(K id, Consumer<Optional<T>> callBack);


    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id       the id
     * @param callBack the callback
     * @throws NullPointerException when id is null
     */
    void existsById(K id, Consumer<Boolean> callBack);

    /**
     * Returns the number of entities available.
     *
     * @param callback the callback
     */
    void count(Consumer<Long> callback);
}

