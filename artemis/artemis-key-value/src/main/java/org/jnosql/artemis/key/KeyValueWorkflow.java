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

import java.util.function.UnaryOperator;

/**
 * This implementation defines the workflow to insert an Entity on {@link KeyValueTemplate}.
 * The default implementation follows:
 *  <p>{@link KeyValueEventPersistManager#firePreEntity(Object)}</p>
 *  <p>{@link KeyValueEventPersistManager#firePreKeyValueEntity(Object)}</p>
 *  <p>{@link KeyValueEntityConverter#toKeyValue(Object)}</p>
 *  <p>{@link KeyValueEventPersistManager#firePreKeyValue(KeyValueEntity)}</p>
 *  <p>Database alteration</p>
 *  <p>{@link KeyValueEventPersistManager#firePostKeyValue(KeyValueEntity)}</p>
 *  <p>{@link KeyValueEventPersistManager#firePostEntity(Object)}</p>
 *  <p>{@link KeyValueEventPersistManager#firePostKeyValueEntity(Object)}</p>
 */
public interface KeyValueWorkflow {

    /**
     * Executes the workflow to do an interaction on a database key-value.
     *
     * @param entity the entity to be saved
     * @param action the alteration to be executed on database
     * @param <T>    the entity type
     * @return after the workflow the the entity response
     * @see KeyValueTemplate#put(Object, java.time.Duration)  {@link KeyValueTemplate#put(Object)}
     * DocumentTemplate#update(Object)
     */
    <T> T flow(T entity, UnaryOperator<KeyValueEntity<?>> action);
}
