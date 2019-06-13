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

import jakarta.nosql.column.ColumnDeleteQuery;

import java.util.Objects;


/**
 * The default implementation of {@link ColumnDeleteQueryExecute}
 */
class DefaultColumnDeleteQueryExecute implements ColumnDeleteQueryExecute {

    private final ColumnDeleteQuery query;

    public DefaultColumnDeleteQueryExecute(ColumnDeleteQuery query) {
        this.query = Objects.requireNonNull(query, "query is required");
    }

    @Override
    public ColumnDeleteQuery getQuery() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnDeleteQueryExecute)) {
            return false;
        }
        ColumnDeleteQueryExecute that = (ColumnDeleteQueryExecute) o;
        return Objects.equals(query, that.getQuery());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(query);
    }

    @Override
    public String toString() {
        return  "DefaultColumnDeleteQueryExecute{" + "query=" + query +
                '}';
    }
}
