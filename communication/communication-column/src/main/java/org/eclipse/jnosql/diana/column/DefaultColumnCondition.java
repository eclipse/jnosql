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

package org.eclipse.jnosql.diana.column;


import jakarta.nosql.Condition;
import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link ColumnCondition}
 */
final public class DefaultColumnCondition implements ColumnCondition {

    private final Column column;

    private final Condition condition;

    private final boolean readOnly;

    private DefaultColumnCondition(Column column, Condition condition) {
        this.column = column;
        this.condition = condition;
        this.readOnly = false;
    }

    private DefaultColumnCondition(Column column, Condition condition, boolean readOnly) {
        this.column = column;
        this.condition = condition;
        this.readOnly = readOnly;
    }

    public static DefaultColumnCondition readOnly(ColumnCondition condition) {
        requireNonNull(condition, "condition is required");
        return new DefaultColumnCondition(condition.getColumn(), condition.getCondition(), true);
    }
    static DefaultColumnCondition of(Column column, Condition condition) {
        return new DefaultColumnCondition(requireNonNull(column, "Column is required"), condition);
    }

    static DefaultColumnCondition and(ColumnCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(Condition.AND.getNameField(), asList(conditions));
        return DefaultColumnCondition.of(column, Condition.AND);
    }


    static DefaultColumnCondition or(ColumnCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(Condition.OR.getNameField(), asList(conditions));
        return DefaultColumnCondition.of(column, Condition.OR);
    }


    static DefaultColumnCondition between(Column column) {
        Objects.requireNonNull(column, "column is required");
        checkBetweenClause(column.get());
        return new DefaultColumnCondition(column, Condition.BETWEEN);
    }

    static DefaultColumnCondition in(Column column) {
        Objects.requireNonNull(column, "column is required");
        checkInClause(column.getValue());
        return new DefaultColumnCondition(column, Condition.IN);
    }

    private static void checkInClause(Value value) {
        if (!value.isInstanceOf(Iterable.class)) {
            throw new IllegalArgumentException("On ColumnCondition#in you must use an iterable" +
                    " instead of class: " + value.getClass().getName());
        }
    }

    private static void checkBetweenClause(Object value) {
        if (Iterable.class.isInstance(value)) {

            long count = (int) StreamSupport.stream(Iterable.class.cast(value).spliterator(), false).count();
            if (count != 2) {
                throw new IllegalArgumentException("On ColumnCondition#between you must use an iterable" +
                        " with two elements");
            }

        } else {
            throw new IllegalArgumentException("On ColumnCondition#between you must use an iterable" +
                    " with two elements instead of class: " + value.getClass().getName());
        }
    }

    public Column getColumn() {
        return column;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public ColumnCondition and(ColumnCondition condition) {
        validateReadOnly();
        requireNonNull(condition, "Conditions is required");
        if (Condition.AND.equals(this.condition)) {
            Column newColumn = getConditions(condition, Condition.AND);
            return new DefaultColumnCondition(newColumn, Condition.AND);
        }
        return DefaultColumnCondition.and(this, condition);
    }

    @Override
    public ColumnCondition negate() {
        validateReadOnly();
        if (Condition.NOT.equals(this.condition)) {
            return this.column.get(ColumnCondition.class);
        } else {
            Column newColumn = Column.of(Condition.NOT.getNameField(), this);
            return new DefaultColumnCondition(newColumn, Condition.NOT);
        }

    }

    @Override
    public ColumnCondition or(ColumnCondition condition) {
        validateReadOnly();
        requireNonNull(condition, "Condition is required");
        if (Condition.OR.equals(this.condition)) {
            Column newColumn = getConditions(condition, Condition.OR);
            return new DefaultColumnCondition(newColumn, Condition.OR);
        }
        return DefaultColumnCondition.or(this, condition);
    }

    private Column getConditions(ColumnCondition columnCondition, Condition condition) {
        List<ColumnCondition> conditions = new ArrayList<>(column.get(new TypeReference<List<ColumnCondition>>() {
        }));
        conditions.add(columnCondition);
        return Column.of(condition.getNameField(), conditions);
    }

    private void validateReadOnly() {
        if (readOnly) {
            throw new IllegalStateException("You cannot change the status after building the query");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !ColumnCondition.class.isAssignableFrom(o.getClass())) {
            return false;
        }
        ColumnCondition that = (ColumnCondition) o;
        return Objects.equals(column, that.getColumn()) &&
                condition == that.getCondition();
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, condition);
    }

    @Override
    public String toString() {
        return "DefaultColumnCondition{" + "column=" + column +
                ", condition=" + condition +
                '}';
    }
}
