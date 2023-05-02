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
 * The result of {@link ColumnDeleteQueryParams} that has {@link ColumnDeleteQuery} and {@link Params}.
 */
public record ColumnDeleteQueryParams(ColumnDeleteQuery query, Params params) {

    /**
     * The {@link ColumnDeleteQuery}
     *
     * @return a {@link ColumnDeleteQuery} instance
     */
    @Override
    public ColumnDeleteQuery query() {
        return query;
    }

    /**
     * The {@link Params}
     *
     * @return a {@link Params} instance
     */
    @Override
    public Params params() {
        return params;
    }


}
