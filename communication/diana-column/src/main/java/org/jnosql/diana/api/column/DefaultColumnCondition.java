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

package org.jnosql.diana.api.column;


import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.jnosql.diana.api.Condition.AND;
import static org.jnosql.diana.api.Condition.BETWEEN;
import static org.jnosql.diana.api.Condition.IN;
import static org.jnosql.diana.api.Condition.NOT;
import static org.jnosql.diana.api.Condition.OR;

/**
 * The default implementation of {@link ColumnCondition}
 */
final class DefaultColumnCondition implements ColumnCondition {

    private final Column column;

    private final Condition condition;

    private DefaultColumnCondition(Column column, Condition condition) {
        this.column = column;
        this.condition = condition;
    }

    public static DefaultColumnCondition of(Column column, Condition condition) {
        return new DefaultColumnCondition(requireNonNull(column, "Column is required"), condition);
    }

    static DefaultColumnCondition and(ColumnCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(AND.getNameField(), asList(conditions));
        return DefaultColumnCondition.of(column, AND);
    }


    static DefaultColumnCondition or(ColumnCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(OR.getNameField(), asList(conditions));
        return DefaultColumnCondition.of(column, OR);
    }


    static DefaultColumnCondition between(Column column) {
        Objects.requireNonNull(column, "column is required");
        checkBetweenClause(column.get());
        return new DefaultColumnCondition(column, BETWEEN);
    }

    static DefaultColumnCondition in(Column column) {
        Objects.requireNonNull(column, "column is required");
        checkInClause(column.getValue());
        return new DefaultColumnCondition(column, IN);
    }

    private static void checkInClause(Value value) {
        if (!value.isInstanceOf(Iterable.class)) {
            throw new IllegalArgumentException("On Columncondition#in you must use an iterable" +
                    " instead of class: " + value.getClass().getName());
        }
    }

    private static void checkBetweenClause(Object value) {
        if (Iterable.class.isInstance(value)) {

            long count = (int) StreamSupport.stream(Iterable.class.cast(value).spliterator(), false).count();
            if (count > 2) {
                throw new IllegalArgumentException("On Columncondition#between you must use an iterable" +
                        " with two elements");
            }
            if (count != 2) {
                throw new IllegalArgumentException("On Columncondition#between you must use an iterable" +
                        " with two elements");
            }

        } else {
            throw new IllegalArgumentException("On Columncondition#between you must use an iterable" +
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
        requireNonNull(condition, "Conditions is required");
        if (AND.equals(this.condition)) {
            Column newColumn = getConditions(condition, AND);
            return new DefaultColumnCondition(newColumn, AND);
        }
        return DefaultColumnCondition.and(this, condition);
    }

    @Override
    public ColumnCondition negate() {
        if (NOT.equals(this.condition)) {
            return this.column.get(ColumnCondition.class);
        } else {
            Column newColumn = Column.of(NOT.getNameField(), this);
            return new DefaultColumnCondition(newColumn, NOT);
        }

    }

    @Override
    public ColumnCondition or(ColumnCondition condition) {
        requireNonNull(condition, "Condition is required");
        if (OR.equals(this.condition)) {
            Column newColumn = getConditions(condition, OR);
            return new DefaultColumnCondition(newColumn, OR);
        }
        return DefaultColumnCondition.or(this, condition);
    }

    private Column getConditions(ColumnCondition columnCondition, Condition condition) {
        List<ColumnCondition> conditions = new ArrayList<>(column.get(new TypeReference<List<ColumnCondition>>() {
        }));
        conditions.add(columnCondition);
        return Column.of(condition.getNameField(), conditions);
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
