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


import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.mapping.column.ColumnEntityPrePersist;

import java.util.Objects;

/**
 * The default implementation to represents {@link ColumnEntityPrePersist}
 */
class DefaultColumnEntityPrePersist implements ColumnEntityPrePersist {

    private final ColumnEntity entity;

    DefaultColumnEntityPrePersist(ColumnEntity entity) {
        this.entity = Objects.requireNonNull(entity, "entity is required");
    }

    @Override
    public ColumnEntity getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultColumnEntityPrePersist)) {
            return false;
        }
        DefaultColumnEntityPrePersist that = (DefaultColumnEntityPrePersist) o;
        return Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity);
    }

    @Override
    public String toString() {
        return "DefaultColumnEntityPrePersist{" + "entity=" + entity +
                '}';
    }
}
