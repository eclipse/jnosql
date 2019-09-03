/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.diana.column.query;

import jakarta.nosql.column.ColumnQuery.ColumnSelect;
import jakarta.nosql.column.ColumnQuery.ColumnSelectProvider;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

public final class DefaultColumnSelectProvider implements ColumnSelectProvider {
    @Override
    public ColumnSelect apply(String[] columns) {
        Stream.of(columns).forEach(d -> requireNonNull(d, "there is null column in the query"));
        return new DefaultSelectQueryBuilder(Arrays.asList(columns));
    }

    @Override
    public ColumnSelect get() {
        return new DefaultSelectQueryBuilder(emptyList());
    }
}
