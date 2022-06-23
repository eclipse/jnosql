/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.column.query;

import jakarta.nosql.Params;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.column.ColumnQueryParams;

import java.util.Objects;

final class DefaultColumnQueryParams implements ColumnQueryParams {

    private final ColumnQuery query;

    private final Params params;

    public DefaultColumnQueryParams(ColumnQuery query, Params params) {
        this.query = query;
        this.params = params;
    }

    @Override
    public ColumnQuery getQuery() {
        return query;
    }

    @Override
    public Params getParams() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultColumnQueryParams that = (DefaultColumnQueryParams) o;
        return Objects.equals(query, that.query) &&
                Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, params);
    }
}
