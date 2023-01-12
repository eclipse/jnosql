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



import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

/**
 * It is the state of column queries, with a condition and a value, as a {@link Column},
 * where both combined define a predicate.
 *
 * @see ColumnManager#select(ColumnQuery)
 * @see Condition
 */
public final class ColumnCondition {

    private final Column column;

    private final Condition condition;

    private final boolean readOnly;

    private ColumnCondition(Column column, Condition condition) {
        this.column = column;
        this.condition = condition;
        this.readOnly = false;
    }

    private ColumnCondition(Column column, Condition condition, boolean readOnly) {
        this.column = column;
        this.condition = condition;
        this.readOnly = readOnly;
    }

    /**
     * Gets the column to be used in the select
     *
     * @return a column instance
     */
    public Column column() {
        return column;
    }

    /**
     * Gets the conditions to be used in the select
     *
     * @return a Condition instance
     * @see Condition
     */
    public Condition condition() {
        return condition;
    }

    /**
     * Creates a new {@link ColumnCondition} using the {@link Condition#AND}
     *
     * @param condition the condition to be aggregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    public ColumnCondition and(ColumnCondition condition) {
        validateReadOnly();
        requireNonNull(condition, "Conditions is required");
        if (Condition.AND.equals(this.condition)) {
            Column newColumn = getConditions(condition, Condition.AND);
            return new ColumnCondition(newColumn, Condition.AND);
        }
        return ColumnCondition.and(this, condition);
    }

    /**
     * Creates a new {@link ColumnCondition} negating the current one
     *
     * @return the negated condition
     * @see Condition#NOT
     */
    public ColumnCondition negate() {
        validateReadOnly();
        if (Condition.NOT.equals(this.condition)) {
            return this.column.get(ColumnCondition.class);
        } else {
            Column newColumn = Column.of(Condition.NOT.getNameField(), this);
            return new ColumnCondition(newColumn, Condition.NOT);
        }

    }

    @Override
    public ColumnCondition or(ColumnCondition condition) {
        validateReadOnly();
        requireNonNull(condition, "Condition is required");
        if (Condition.OR.equals(this.condition)) {
            Column newColumn = getConditions(condition, Condition.OR);
            return new ColumnCondition(newColumn, Condition.OR);
        }
        return ColumnCondition.or(this, condition);
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
        return Objects.equals(column, that.column()) &&
                condition == that.condition();
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

    public static ColumnCondition readOnly(ColumnCondition condition) {
        requireNonNull(condition, "condition is required");
        return new ColumnCondition(condition.column(), condition.condition(), true);
    }
    static ColumnCondition of(Column column, Condition condition) {
        return new ColumnCondition(requireNonNull(column, "Column is required"), condition);
    }

    static ColumnCondition and(ColumnCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(Condition.AND.getNameField(), asList(conditions));
        return ColumnCondition.of(column, Condition.AND);
    }


    static ColumnCondition or(ColumnCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(Condition.OR.getNameField(), asList(conditions));
        return ColumnCondition.of(column, Condition.OR);
    }


    static ColumnCondition between(Column column) {
        Objects.requireNonNull(column, "column is required");
        checkBetweenClause(column.get());
        return new ColumnCondition(column, Condition.BETWEEN);
    }

    static ColumnCondition in(Column column) {
        Objects.requireNonNull(column, "column is required");
        checkInClause(column.getValue());
        return new ColumnCondition(column, Condition.IN);
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
}
