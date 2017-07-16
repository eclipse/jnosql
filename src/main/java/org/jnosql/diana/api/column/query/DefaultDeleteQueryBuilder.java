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
package org.jnosql.diana.api.column.query;


import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnDeleteQuery;

import java.util.List;

import static java.util.Objects.requireNonNull;

class DefaultDeleteQueryBuilder implements ColumnDelete, ColumnDeleteFrom, ColumnDeleteWhere{

    private String columnFamily;

    private ColumnCondition condition;

    private final List<String> columns;

    DefaultDeleteQueryBuilder(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public ColumnDeleteFrom from(String columnFamily) throws NullPointerException {
        requireNonNull(columnFamily, "columnFamily is required");
        this.columnFamily = columnFamily;
        return this;
    }

    @Override
    public ColumnDeleteWhere where(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = condition;
        return this;
    }

    @Override
    public ColumnDeleteWhere and(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = this.condition.and(condition);
        return this;
    }

    @Override
    public ColumnDeleteWhere or(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = this.condition.or(condition);
        return this;
    }


    @Override
    public ColumnDeleteQuery build() {
        return null;
    }
}

