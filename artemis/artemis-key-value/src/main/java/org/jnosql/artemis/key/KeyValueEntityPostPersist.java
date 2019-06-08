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


import org.jnosql.diana.api.key.KeyValueEntity;

import java.util.Objects;

public interface KeyValueEntityPostPersist {

    /**
     * The {@link org.jnosql.diana.api.key.KeyValueEntity}  after be saved
     *
     * @return the {@link KeyValueEntity} instance
     */
    KeyValueEntity<?> getEntity();

    /**
     * Creates the {@link KeyValueEntityPostPersist} instance
     *
     * @param <T>    the entity type
     * @param entity the entity
     * @return {@link KeyValueEntityPostPersist} instance
     * @throws NullPointerException when the entity is null
     */
    static <T> KeyValueEntityPostPersist of(KeyValueEntity<T> entity) {
        Objects.requireNonNull(entity, "Entity is required");
        return new DefaultKeyValueEntityPostPersist(entity);
    }
}
