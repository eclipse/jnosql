/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.column;



import org.eclipse.jnosql.communication.column.ColumnQuery;

import java.util.Objects;
import java.util.function.Supplier;


/**
 * When a column query is executed this event if fired
 */
public final class ColumnQueryExecute implements Supplier<ColumnQuery> {

    private final ColumnQuery query;

    ColumnQueryExecute(ColumnQuery query) {
        this.query = Objects.requireNonNull(query, "query is required");
    }

    @Override
    public ColumnQuery get() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnQueryExecute)) {
            return false;
        }
        ColumnQueryExecute that = (ColumnQueryExecute) o;
        return Objects.equals(query, that.get());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(query);
    }

    @Override
    public String toString() {
        return  "ColumnQueryExecute{" + "query=" + query +
                '}';
    }
}
