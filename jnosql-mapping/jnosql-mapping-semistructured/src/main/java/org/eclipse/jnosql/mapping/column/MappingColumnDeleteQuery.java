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
import java.util.Optional;

record MappingColumnDeleteQuery(String columnFamily, ColumnCondition columnCondition) implements ColumnDeleteQuery {


    @Override
    public String name() {
        return columnFamily;
    }

    @Override
    public Optional<ColumnCondition> condition() {
        return Optional.ofNullable(columnCondition);
    }

    @Override
    public List<String> columns() {
        return Collections.emptyList();
    }
}
