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

import jakarta.nosql.column.ColumnDeleteQuery.ColumnDelete;
import jakarta.nosql.column.ColumnDeleteQuery.ColumnDeleteProvider;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public final class DefaultColumnDeleteSupplier implements ColumnDeleteProvider {

    @Override
    public ColumnDelete apply(String[] columns) {
        if(Stream.of(columns).anyMatch(Objects::isNull)) {
            throw new NullPointerException("There is null column");
        }
        return new DefaultDeleteQueryBuilder(asList(columns));
    }

    @Override
    public ColumnDelete get() {
        return new DefaultDeleteQueryBuilder(emptyList());
    }
}
