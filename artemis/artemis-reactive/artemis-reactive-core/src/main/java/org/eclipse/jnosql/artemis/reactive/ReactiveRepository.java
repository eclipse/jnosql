/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.artemis.reactive;


import org.reactivestreams.Publisher;

/**
 * An extension of {@link jakarta.nosql.mapping.Repository} that will work with Reactive Stream
 * @param <T>  the bean type
 * @param <K> the K type
 */
public interface ReactiveRepository<T, K> {

    /**
     * Saves entity
     *
     * @param <S>    the entity type
     * @param entity entity to be saved
     * @return the entity saved
     * @throws NullPointerException when document is null
     */
    <S extends T> Publisher<S> save(S entity);

    /**
     * Saves entity
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param <S>      the entity type
     * @param entities entities to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    <S extends T> Publisher<S> save(Iterable<S> entities);

    /**
     * Deletes the entity with the given id.
     *
     * @param id the id
     * @throws NullPointerException when id is null
     */
    Publisher<Void> deleteById(K id);

    /**
     * Deletes the entity with the given ids.
     *
     * @param ids the ids
     * @throws NullPointerException when either ids or same element is null
     */
    Publisher<Void> deleteById(Iterable<K> ids);

    /**
     * Finds an entity given the id
     *
     * @param id the id
     * @return the entity given the K
     * @throws NullPointerException when id is null
     */
    Publisher<T> findById(K id);

    /**
     * Finds the entities given ids
     *
     * @param ids the ids
     * @return the entities from ids
     * @throws NullPointerException when the id is null
     */
    Publisher<T> findById(Iterable<K> ids);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id the id
     * @return if the entity does exist or not
     * @throws NullPointerException when id is null
     */
    Publisher<Boolean> existsById(K id);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    Publisher<Long> count();
}
