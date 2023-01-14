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

import org.eclipse.jnosql.communication.column.ColumnCondition;
import org.eclipse.jnosql.communication.column.ColumnDeleteQuery;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class MappingColumnDeleteQuery implements ColumnDeleteQuery {

    private final String columnFamily;

    private final ColumnCondition condition;

    MappingColumnDeleteQuery(String columnFamily, ColumnCondition condition) {
        this.columnFamily = columnFamily;
        this.condition = condition;
    }


    @Override
    public String columnFamily() {
        return columnFamily;
    }

    @Override
    public Optional<ColumnCondition> condition() {
        return Optional.ofNullable(condition);
    }

    @Override
    public List<String> columns() {
        return Collections.emptyList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnDeleteQuery)) {
            return false;
        }
        ColumnDeleteQuery that = (ColumnDeleteQuery) o;
        return Objects.equals(columnFamily, that.columnFamily())
                && Objects.equals(condition, that.condition().orElse(null))
                && Objects.equals(Collections.emptyList(), that.columns());
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnFamily, condition, Collections.emptyList());
    }
}
