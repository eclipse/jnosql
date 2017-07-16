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


import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnQuery;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

class DefaultSelectQueryBuilder implements ColumnSelect, ColumnFrom, ColumnWhere, ColumnLimit, ColumnStart, ColumnOrder {

    private String columnFamily;

    private ColumnCondition condition;

    private long start;

    private long limit;

    private final List<Sort> sorts = new ArrayList<>();

    private final List<String> columns;

    DefaultSelectQueryBuilder(List<String> columns) {
        this.columns = columns;
    }


    @Override
    public ColumnFrom from(String columnFamily) throws NullPointerException {
        requireNonNull(columnFamily, "columnFamily is required");
        return this;
    }

    @Override
    public ColumnWhere where(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = condition;
        return this;
    }

    @Override
    public ColumnStart start(long start) {
        this.start = start;
        return this;
    }

    @Override
    public ColumnLimit limit(long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public ColumnOrder orderBy(Sort sort) throws NullPointerException {
        requireNonNull(sort, "sort is required");
        this.sorts.add(sort);
        return this;
    }

    @Override
    public ColumnWhere and(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition.and(condition);
        return this;
    }

    @Override
    public ColumnWhere or(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition.or(condition);
        return this;
    }


    @Override
    public ColumnQuery build() {
        return null;
    }

}
