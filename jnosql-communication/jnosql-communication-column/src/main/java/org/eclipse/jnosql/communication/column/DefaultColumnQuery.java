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



import jakarta.data.repository.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

/**
 * The default implementation of column query.
 */
record DefaultColumnQuery(long limit, long skip, String name,
                          List<String> columns, List<Sort> sorts, ColumnCondition columnCondition)
        implements ColumnQuery {


    @Override
    public Optional<ColumnCondition> condition() {
        return ofNullable(columnCondition).map(ColumnCondition::readOnly);
    }

    @Override
    public List<String> columns() {
        return unmodifiableList(columns);
    }

    @Override
    public List<Sort> sorts() {
        return unmodifiableList(sorts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnQuery that)) {
            return false;
        }
        return limit == that.limit() &&
                skip == that.skip() &&
                Objects.equals(name, that.name()) &&
                Objects.equals(columns, that.columns()) &&
                Objects.equals(sorts, that.sorts()) &&
                Objects.equals(columnCondition, that.condition().orElse(null));
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, skip, name, columns, sorts, columnCondition);
    }


    static ColumnQuery countBy(ColumnQuery query) {
        return new DefaultColumnQuery(0, 0, query.name(), query.columns(),
                Collections.emptyList(), query.condition().orElse(null));
    }

    static ColumnQuery existsBy(ColumnQuery query) {
        return new DefaultColumnQuery(1, 0, query.name(), query.columns(),
                Collections.emptyList(), query.condition().orElse(null));
    }
}
