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
package org.eclipse.jnosql.communication.column.query;

import jakarta.nosql.column.ColumnQuery.ColumnQueryBuilder;
import jakarta.nosql.column.ColumnQuery.ColumnQueryBuilderProvider;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link ColumnQueryBuilderProvider}
 */
public final class DefaultColumnQueryBuilderProvider implements ColumnQueryBuilderProvider {

    @Override
    public ColumnQueryBuilder apply(String[] columns) {
        Stream.of(columns).forEach(d -> requireNonNull(d, "there is null column in the query"));
        ColumnQueryBuilder builder = new DefaultColumnQueryBuilder();
        Stream.of(columns).forEach(builder::select);
        return builder;
    }

    @Override
    public ColumnQueryBuilder get() {
        return new DefaultColumnQueryBuilder();
    }
}
