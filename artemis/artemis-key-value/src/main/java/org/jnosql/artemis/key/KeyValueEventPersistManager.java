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
package org.jnosql.artemis.key;


import org.jnosql.diana.key.KeyValueEntity;

/**
 * This interface represent the manager of events. When an entity be either saved or updated an event will be fired. This order gonna be:
 * 1) firePreKeyValue
 * 2) firePreKeyValueEntity
 * 3) firePostKeyValue
 * 4) firePostColumn
 * 5) firePostEntity
 * 6) firePostKeyValueEntity
 *
 * @see KeyValueWorkflow
 */
public interface KeyValueEventPersistManager {

    /**
     * Fire an event after the conversion of the entity to communication API model.
     *
     * @param entity the entity
     */
    void firePreKeyValue(KeyValueEntity<?> entity);

    /**
     * Fire an event after the response from communication layer
     *
     * @param entity the entity
     */
    void firePostKeyValue(KeyValueEntity<?> entity);

    /**
     * Fire an event once the method is called
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    <T> void firePreEntity(T entity);

    /**
     * Fire an event after convert the {@link KeyValueEntity},
     * from database response, to Entity.
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    <T> void firePostEntity(T entity);


    /**
     * fire an event after the firePostEntity
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    <T> void firePreKeyValueEntity(T entity);

    /**
     * Fire the last event
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    <T> void firePostKeyValueEntity(T entity);

}

