/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.column;


import org.eclipse.jnosql.communication.Params;

import java.util.Objects;


/**
 *  The result of {@link ColumnDeleteQueryParams} that has {@link ColumnDeleteQuery} and {@link Params}.
 */
public final class ColumnDeleteQueryParams {

    private final ColumnDeleteQuery query;

    private final Params params;

    public ColumnDeleteQueryParams(ColumnDeleteQuery query, Params params) {
        this.query = query;
        this.params = params;
    }

    /**
     * The {@link ColumnDeleteQuery}
     *
     * @return a {@link ColumnDeleteQuery} instance
     */
    public ColumnDeleteQuery query() {
        return query;
    }

    /**
     * The {@link Params}
     *
     * @return a {@link Params} instance
     */
    public Params params() {
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
        ColumnDeleteQueryParams that = (ColumnDeleteQueryParams) o;
        return Objects.equals(query, that.query) &&
                Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, params);
    }
}
