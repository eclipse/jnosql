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
package org.eclipse.jnosql.communication.semistructured;


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
 * It is the state of column queries, with a condition and a value, as a {@link Element},
 * where both combined define a predicate.
 *
 * @see DatabaseManager#select(ColumnQuery)
 * @see Condition
 */
public final class ColumnCondition {

    private final Element element;

    private final Condition condition;

    private final boolean readOnly;

    private ColumnCondition(Element element, Condition condition) {
        this.element = element;
        this.condition = condition;
        this.readOnly = false;
    }

    private ColumnCondition(Element element, Condition condition, boolean readOnly) {
        this.element = element;
        this.condition = condition;
        this.readOnly = readOnly;
    }

    /**
     * Gets the column to be used in the select
     *
     * @return a column instance
     */
    public Element column() {
        return element;
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
            Element newElement = getConditions(condition, Condition.AND);
            return new ColumnCondition(newElement, Condition.AND);
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
            return this.element.get(ColumnCondition.class);
        } else {
            Element newElement = Element.of(Condition.NOT.getNameField(), this);
            return new ColumnCondition(newElement, Condition.NOT);
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
            Element newElement = getConditions(condition, Condition.OR);
            return new ColumnCondition(newElement, Condition.OR);
        }
        return ColumnCondition.or(this, condition);
    }

    private Element getConditions(ColumnCondition columnCondition, Condition condition) {
        List<ColumnCondition> conditions = new ArrayList<>(element.get(new TypeReference<List<ColumnCondition>>() {
        }));
        conditions.add(columnCondition);
        return Element.of(condition.getNameField(), conditions);
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ColumnCondition that = (ColumnCondition) o;
        return Objects.equals(element, that.element)
                && condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, condition);
    }

    @Override
    public String toString() {
        return "ColumnCondition{" + "column=" + element +
                ", condition=" + condition +
                '}';
    }

    public static ColumnCondition readOnly(ColumnCondition condition) {
        requireNonNull(condition, "condition is required");
        return new ColumnCondition(condition.column(), condition.condition(), true);
    }

    static ColumnCondition of(Element element, Condition condition) {
        return new ColumnCondition(requireNonNull(element, "Column is required"), condition);
    }


    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#EQUALS}, it means a select will scanning to a
     * column family that has the same name and equals value informed in this column.
     *
     * @param element a column instance
     * @return a {@link ColumnCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition eq(Element element) {
         return new ColumnCondition(element, Condition.EQUALS);
    }

    /**
     * an alias method to {@link ColumnCondition#eq(Element)} where it will create a {@link Element}
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
        return eq(Element.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#GREATER_THAN}, it means a select will scanning to a
     * column family that has the same name and the value  greater than informed in this column.
     *
     * @param element a column instance
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition gt(Element element) {
         return new ColumnCondition(element, Condition.GREATER_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#gt(Element)} where it will create a {@link Element}
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
        return gt(Element.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#GREATER_EQUALS_THAN},
     * it means a select will scanning to a column family that has the same name and the value
     * greater or equals than informed in this column.
     *
     * @param element a column instance
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition gte(Element element) {
         return new ColumnCondition(element, Condition.GREATER_EQUALS_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#gte(Element)} where it will create a {@link Element}
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
        return gte(Element.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LESSER_THAN}, it means a select will scanning to a
     * column family that has the same name and the value  lesser than informed in this column.
     *
     * @param element a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition lt(Element element) {
        return new ColumnCondition(element, Condition.LESSER_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#lt(Element)} where it will create a {@link Element}
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
        return lt(Element.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LESSER_EQUALS_THAN},
     * it means a select will scanning to a column family that has the same name and the value
     * lesser or equals than informed in this column.
     *
     * @param element a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition lte(Element element) {
        return new ColumnCondition(element, Condition.LESSER_EQUALS_THAN);
    }

    /**
     * an alias method to {@link ColumnCondition#lte(Element)} where it will create a {@link Element}
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
        return lte(Element.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#IN}, it means a select will scanning to a
     * column family that has the same name and the value is within informed in this column.
     *
     * @param element a column instance
     * @return a {@link ColumnCondition} with {@link Condition#IN}
     * @throws NullPointerException     when column is null
     * @throws IllegalArgumentException when the {@link Element#get()} in not an iterable implementation
     */
    public static ColumnCondition in(Element element) {
        Objects.requireNonNull(element, "column is required");
        checkInClause(element.value());
        return new ColumnCondition(element, Condition.IN);
    }

    /**
     * an alias method to {@link ColumnCondition#in(Element)} where it will create a {@link Element}
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
        return in(Element.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LIKE}, it means a select will scanning to a
     * column family that has the same name and the value  is like than informed in this column.
     *
     * @param element a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when column is null
     */
    public static ColumnCondition like(Element element) {
        return new ColumnCondition(element, Condition.LIKE);
    }

    /**
     * an alias method to {@link ColumnCondition#like(Element)} where it will create a {@link Element}
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
        return like(Element.of(name, value));
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#BETWEEN},
     * it means a select will scanning to a column family that is between two values informed
     * on a column name.
     * The column must have a {@link Element#get()} an {@link Iterable} implementation
     * with just two elements.
     *
     * @param element a column instance
     * @return The between condition
     * @throws NullPointerException     when column is null
     * @throws IllegalArgumentException When the column neither has an Iterable instance or two elements on
     *                                  an Iterable.
     */
    public static ColumnCondition between(Element element) {
        Objects.requireNonNull(element, "column is required");
        checkBetweenClause(element.get());
        return new ColumnCondition(element, Condition.BETWEEN);
    }

    /**
     * an alias method to {@link ColumnCondition#between(Element)} (Column) where it will create a {@link Element}
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
        return between(Element.of(name, value));
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
     * The {@link Element} will storage the {@link Condition#getNameField()} as key and the value will be
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
     * @throws NullPointerException when the conditions are null
     */
    public static ColumnCondition and(ColumnCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Element element = Element.of(Condition.AND.getNameField(), asList(conditions));
        return ColumnCondition.of(element, Condition.AND);
    }

    /**
     * Returns a new {@link ColumnCondition} aggregating ,as "OR", all the conditions as just one condition.
     * The {@link Element} will storage the {@link Condition#getNameField()} as key and the value gonna be
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
        Element element = Element.of(Condition.OR.getNameField(), asList(conditions));
        return ColumnCondition.of(element, Condition.OR);
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
