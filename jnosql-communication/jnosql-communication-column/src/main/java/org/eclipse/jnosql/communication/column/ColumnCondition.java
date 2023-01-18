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
import org.eclipse.jnosql.communication.TypeReference;
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

    /**
     * Creates a new {@link ColumnCondition} using the {@link Condition#OR}
     *
     * @param condition the condition to be aggregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
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


    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#EQUALS}, it means a select will scanning to a
     * column family that has the same name and equals value informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition eq(Column column) {
         return new ColumnCondition(column, Condition.EQUALS);
    }

    /**
     * an alias method to {@link ColumnCondition#eq(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when either name or value is null
     */
    public static ColumnCondition eq(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return eq(Column.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#GREATER_THAN}, it means a select will scanning to a
     * column family that has the same name and the value  greater than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition gt(Column column) {
         return new ColumnCondition(column, Condition.GREATER_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#gt(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when either name or value is null
     */
    public static ColumnCondition gt(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return gt(Column.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#GREATER_EQUALS_THAN},
     * it means a select will scanning to a column family that has the same name and the value
     * greater or equals than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition gte(Column column) {
         return new ColumnCondition(column, Condition.GREATER_EQUALS_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#gte(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when either name or value is null
     */
    public static ColumnCondition gte(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return gte(Column.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LESSER_THAN}, it means a select will scanning to a
     * column family that has the same name and the value  lesser than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition lt(Column column) {
        return new ColumnCondition(column, Condition.LESSER_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#lt(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when either name or value is null
     */
    public static ColumnCondition lt(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return lt(Column.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LESSER_EQUALS_THAN},
     * it means a select will scanning to a column family that has the same name and the value
     * lesser or equals than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition lte(Column column) {
        return new ColumnCondition(column, Condition.LESSER_EQUALS_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#lte(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when either name or value is null
     */
    public static ColumnCondition lte(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return lte(Column.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#IN}, it means a select will scanning to a
     * column family that has the same name and the value is within informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#IN}
     * @throws NullPointerException     when column is null
     * @throws IllegalArgumentException when the {@link Column#get()} in not an iterable implementation
     */
    public static ColumnCondition in(Column column) {
        Objects.requireNonNull(column, "column is required");
        checkInClause(column.value());
        return new ColumnCondition(column, Condition.IN);
    }

    /**
     * an alias method to {@link ColumnCondition#in(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#IN}
     * @throws NullPointerException when either name or value is null
     */
    public static ColumnCondition in(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return in(Column.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LIKE}, it means a select will scanning to a
     * column family that has the same name and the value  is like than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition like(Column column) {
        return new ColumnCondition(column, Condition.LIKE);
    }

    /**
     * an alias method to {@link ColumnCondition#like(Column)} where it will create a {@link Column}
     * instance first and then apply te condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when either name or value is null
     */
    public static ColumnCondition like(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return like(Column.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#BETWEEN},
     * it means a select will scanning to a column family that is between two values informed
     * on a column name.
     * The column must have a {@link Column#get()} an {@link Iterable} implementation
     * with just two elements.
     *
     * @param column a column instance
     * @return The between condition
     * @throws NullPointerException     when column is null
     * @throws IllegalArgumentException When the column neither has an Iterable instance or two elements on
     *                                  an Iterable.
     */
    public static ColumnCondition between(Column column) {
        Objects.requireNonNull(column, "column is required");
        checkBetweenClause(column.get());
        return new ColumnCondition(column, Condition.BETWEEN);
    }

    /**
     * an alias method to {@link ColumnCondition#between(Column)} (Column) where it will create a {@link Column}
     * instance first and then apply te condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link ColumnCondition} with {@link Condition#BETWEEN}
     * @throws NullPointerException when either name or value is null
     */
    public static ColumnCondition between(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return between(Column.of(name, value));
    }

    /**
     * Returns a predicate that is the negation of the supplied predicate.
     * This is accomplished by returning result of the calling target.negate().
     *
     * @param condition the condition
     * @return a condition that negates the results of the supplied predicate
     * @throws NullPointerException when condition is null
     */
    public static ColumnCondition not(ColumnCondition condition) {
        Objects.requireNonNull(condition, "condition is required");
        return condition.negate();
    }

    /**
     * Returns a new {@link ColumnCondition} aggregating ,as "AND", all the conditions as just one condition.
     * The {@link Column} will storage the {@link Condition#getNameField()} as key and the value gonna be
     * the {@link java.util.List} of all conditions, in other words.
     * <p>Given:</p>
     * {@code
     * Column age = Column.of("age", 26);
     * Column name = Column.of("name", "otavio");
     * ColumnCondition condition = ColumnCondition.eq(name).and(ColumnCondition.gte(age));
     * }
     * The {@link ColumnCondition#column()} will have "_AND" as key and the list of condition as value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link ColumnCondition} instance
     * @throws NullPointerException when the conditions is null
     */
    public static ColumnCondition and(ColumnCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(Condition.AND.getNameField(), asList(conditions));
        return ColumnCondition.of(column, Condition.AND);
    }

    /**
     * Returns a new {@link ColumnCondition} aggregating ,as "OR", all the conditions as just one condition.
     * The {@link Column} will storage the {@link Condition#getNameField()} as key and the value gonna be
     * the {@link java.util.List} of all conditions, in other words.
     * <p>Given:</p>
     * {@code
     * Column age = Column.of("age", 26);
     * Column name = Column.of("name", "otavio");
     * ColumnCondition condition = ColumnCondition.eq(name).or(ColumnCondition.gte(age));
     * }
     * The {@link ColumnCondition#column()} will have "_OR" as key and the list of condition as value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link ColumnCondition} instance
     * @throws NullPointerException when the condition is null
     */
    public static ColumnCondition or(ColumnCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(Condition.OR.getNameField(), asList(conditions));
        return ColumnCondition.of(column, Condition.OR);
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
