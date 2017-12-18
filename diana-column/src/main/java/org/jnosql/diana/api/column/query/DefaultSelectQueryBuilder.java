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
import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnQuery;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * The default implementation of the Select in the column
 */
class DefaultSelectQueryBuilder implements ColumnSelect, ColumnFrom, ColumnLimit, ColumnStart,
        ColumnOrder, ColumnWhereName, ColumnNameCondition, ColumnNotCondition {


    private final DefaultColumnWhere columnWhere = new DefaultColumnWhere(this);
    private final DefaultColumnFromOrder columnFromOrder = new DefaultColumnFromOrder(this);

    protected String columnFamily;

    protected ColumnCondition condition;

    protected long start;

    protected long limit;

    protected final List<Sort> sorts = new ArrayList<>();

    protected final List<String> columns;

    protected String name;

    protected boolean negate;

    protected boolean and;

    DefaultSelectQueryBuilder(List<String> columns) {
       this.columns = columns;
    }


    @Override
    public ColumnFrom from(String columnFamily) throws NullPointerException {
        requireNonNull(columnFamily, "columnFamily is required");
        this.columnFamily = columnFamily;
        return this;
    }

    @Override
    public ColumnWhere where(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = condition;
        return columnWhere;
    }

    @Override
    public ColumnWhereName where(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
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
    public ColumnFromOrder orderBy(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
        return columnFromOrder;
    }




    @Override
    public ColumnNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> ColumnWhere eq(T value) throws NullPointerException {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.eq(Column.of(name, value));
        return appendCondition(newCondition);
    }


    @Override
    public ColumnWhere like(String value) throws NullPointerException {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.like(Column.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public ColumnWhere gt(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.gt(Column.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public ColumnWhere gte(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.gte(Column.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public ColumnWhere lt(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.lt(Column.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public ColumnWhere lte(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        ColumnCondition newCondition = ColumnCondition.lte(Column.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public ColumnWhere between(Number valueA, Number valueB) throws NullPointerException {
        requireNonNull(valueA, "valueA is required");
        requireNonNull(valueB, "valueB is required");
        ColumnCondition newCondition = ColumnCondition.between(Column.of(name, asList(valueA, valueB)));
        return appendCondition(newCondition);
    }

    @Override
    public <T> ColumnWhere in(Iterable<T> values) throws NullPointerException {
        requireNonNull(values, "values is required");
        ColumnCondition newCondition = ColumnCondition.in(Column.of(name, values));
        return columnWhere;
    }

    @Override
    public ColumnWhere asc() {
        this.sorts.add(Sort.of(name, Sort.SortType.ASC));
        return columnWhere;
    }

    @Override
    public ColumnWhere desc() {
        this.sorts.add(Sort.of(name, Sort.SortType.ASC));
        return columnWhere;
    }


    @Override
    public ColumnQuery build() {
        return new DefaultColumnQuery(limit, start, columnFamily, columns, sorts, condition);
    }

    private ColumnWhere appendCondition(ColumnCondition newCondition) {
        if (negate) {
            newCondition = newCondition.negate();
        }
        if (nonNull(condition)) {
            if (and) {
                this.condition = condition.and(newCondition);
            } else {
                this.condition = condition.or(newCondition);
            }
        } else {
            this.condition = newCondition;
        }
        this.negate = false;
        this.name = null;
        return columnWhere;
    }

}
