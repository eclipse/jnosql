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
package org.jnosql.artemis.column;


import org.jnosql.diana.column.ColumnEntity;

import java.util.Objects;

/**
 * The interface represents the model when the {@link ColumnEntity} be saved that  event will fired.
 */
public interface ColumnEntityPostPersist {

    /**
     * The {@link ColumnEntity}  after be saved
     *
     * @return the {@link ColumnEntity} instance
     */
    ColumnEntity getEntity();

    /**
     * Creates the {@link ColumnEntityPostPersist} instance
     *
     * @param entity the entity
     * @return {@link ColumnEntityPostPersist} instance
     * @throws NullPointerException when the entity is null
     */
    static ColumnEntityPostPersist of(ColumnEntity entity) {
        Objects.requireNonNull(entity, "Entity is required");
        return new DefaultColumnEntityPostPersist(entity);
    }

}
