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



import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

/**
 * The default implementation of {@link ColumnDeleteQuery}
 */
record DefaultColumnDeleteQuery(String name, ColumnCondition columnCondition, List<String> columns)
        implements ColumnDeleteQuery {


    @Override
    public Optional<ColumnCondition> condition() {
        return ofNullable(columnCondition);
    }

    @Override
    public List<String> columns() {
        return unmodifiableList(columns);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnDeleteQuery that)) {
            return false;
        }
        return Objects.equals(name, that.name()) &&
                Objects.equals(columnCondition, that.condition().orElse(null)) &&
                Objects.equals(columns, that.columns());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columnCondition, columns);
    }


}
