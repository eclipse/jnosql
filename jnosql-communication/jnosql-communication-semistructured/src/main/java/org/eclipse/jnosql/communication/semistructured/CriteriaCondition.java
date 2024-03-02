/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;


import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;


/**
 * Represents the state of column queries, including a condition and a value, as an {@link Element},
 * where both together define a predicate.
 *
 * @see DatabaseManager#select(SelectQuery)
 * @see Condition
 */
public final class CriteriaCondition {

    private final Element element;
    private final Condition condition;
    private final boolean readOnly;

    private CriteriaCondition(Element element, Condition condition) {
        this.element = element;
        this.condition = condition;
        this.readOnly = false;
    }

    private CriteriaCondition(Element element, Condition condition, boolean readOnly) {
        this.element = element;
        this.condition = condition;
        this.readOnly = readOnly;
    }

    /**
     * Retrieves the column to be used in the select.
     *
     * @return a column instance
     */
    public Element element() {
        return element;
    }

    /**
     * Retrieves the condition to be used in the select.
     *
     * @return a Condition instance
     * @see Condition
     */
    public Condition condition() {
        return condition;
    }

    /**
     * Creates a new {@link CriteriaCondition} using the {@link Condition#AND}.
     *
     * @param condition the condition to be aggregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    public CriteriaCondition and(CriteriaCondition condition) {
        validateReadOnly();
        Objects.requireNonNull(condition, "Conditions are required");
        if (Condition.AND.equals(this.condition)) {
            Element newElement = getConditions(condition, Condition.AND);
            return new CriteriaCondition(newElement, Condition.AND);
        }
        return CriteriaCondition.and(this, condition);
    }

    /**
     * Creates a new {@link CriteriaCondition} negating the current one.
     *
     * @return the negated condition
     * @see Condition#NOT
     */
    public CriteriaCondition negate() {
        validateReadOnly();
        if (Condition.NOT.equals(this.condition)) {
            return this.element.get(CriteriaCondition.class);
        } else {
            Element newElement = Element.of(Condition.NOT.getNameField(), this);
            return new CriteriaCondition(newElement, Condition.NOT);
        }

    }

    /**
     * Creates a new {@link CriteriaCondition} using the {@link Condition#OR}.
     *
     * @param condition the condition to be aggregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    public CriteriaCondition or(CriteriaCondition condition) {
        validateReadOnly();
        Objects.requireNonNull(condition, "Condition is required");
        if (Condition.OR.equals(this.condition)) {
            Element newElement = getConditions(condition, Condition.OR);
            return new CriteriaCondition(newElement, Condition.OR);
        }
        return CriteriaCondition.or(this, condition);
    }

    private Element getConditions(CriteriaCondition criteriaCondition, Condition condition) {
        List<CriteriaCondition> conditions = new ArrayList<>(element.get(new TypeReference<List<CriteriaCondition>>() {
        }));
        conditions.add(criteriaCondition);
        return Element.of(condition.getNameField(), conditions);
    }

    private void validateReadOnly() {
        if (readOnly) {
            throw new IllegalStateException("You cannot change the status after building the query");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CriteriaCondition that = (CriteriaCondition) o;
        return readOnly == that.readOnly && Objects.equals(element, that.element) && condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, condition, readOnly);
    }

    @Override
    public String toString() {
        return "ColumnCondition{" +
                "column=" + element +
                ", condition=" + condition +
                '}';
    }

    public static CriteriaCondition readOnly(CriteriaCondition condition) {
        Objects.requireNonNull(condition, "Condition is required");
        return new CriteriaCondition(condition.element(), condition.condition(), true);
    }

    static CriteriaCondition of(Element element, Condition condition) {
        return new CriteriaCondition(Objects.requireNonNull(element, "Column is required"), condition);
    }


    /**
     * Creates a {@link CriteriaCondition} with a {@link Condition#EQUALS}, indicating that a select will scan a
     * column family with the same name and equal value as the one provided in this column.
     *
     * @param element a column instance
     * @return a {@link CriteriaCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when the column is null
     */
    public static CriteriaCondition eq(Element element) {
        return new CriteriaCondition(element, Condition.EQUALS);
    }

    /**
     * An alias method to {@link CriteriaCondition#eq(Element)}, which first creates an {@link Element}
     * instance and then applies the condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link CriteriaCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when either the name or the value is null
     */
    public static CriteriaCondition eq(String name, Object value) {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(value, "Value is required");
        return eq(Element.of(name, value));
    }

    /**
     * Creates a {@link CriteriaCondition} with a {@link Condition#GREATER_THAN}, indicating that a select will scan a
     * column family with the same name and the value greater than the one provided in this column.
     *
     * @param element a column instance
     * @return a {@link CriteriaCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when the column is null
     */
    public static CriteriaCondition gt(Element element) {
        return new CriteriaCondition(element, Condition.GREATER_THAN);
    }

    /**
     * An alias method to {@link CriteriaCondition#gt(Element)}, which first creates an {@link Element}
     * instance and then applies the condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link CriteriaCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when either the name or the value is null
     */
    public static CriteriaCondition gt(String name, Object value) {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(value, "Value is required");
        return gt(Element.of(name, value));
    }

    /**
     * Creates a {@link CriteriaCondition} with a {@link Condition#GREATER_EQUALS_THAN},
     * indicating that a select will scan a column family with the same name and the value
     * greater or equal to the one provided in this column.
     *
     * @param element a column instance
     * @return a {@link CriteriaCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when the column is null
     */
    public static CriteriaCondition gte(Element element) {
        return new CriteriaCondition(element, Condition.GREATER_EQUALS_THAN);
    }

    /**
     * An alias method to {@link CriteriaCondition#gte(Element)}, which first creates an {@link Element}
     * instance and then applies the condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link CriteriaCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when either the name or the value is null
     */
    public static CriteriaCondition gte(String name, Object value) {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(value, "Value is required");
        return gte(Element.of(name, value));
    }

    /**
     * Creates a {@link CriteriaCondition} with a {@link Condition#LESSER_THAN}, indicating that a select will scan a
     * column family with the same name and the value lesser than the one provided in this column.
     *
     * @param element a column instance
     * @return a {@link CriteriaCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when the column is null
     */
    public static CriteriaCondition lt(Element element) {
        return new CriteriaCondition(element, Condition.LESSER_THAN);
    }

    /**
     * An alias method to {@link CriteriaCondition#lt(Element)}, which first creates an {@link Element}
     * instance and then applies the condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link CriteriaCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when either the name or the value is null
     */
    public static CriteriaCondition lt(String name, Object value) {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(value, "Value is required");
        return lt(Element.of(name, value));
    }

    /**
     * Creates a {@link CriteriaCondition} with a {@link Condition#LESSER_EQUALS_THAN},
     * indicating that a select will scan a column family with the same name and the value
     * lesser or equal to the one provided in this column.
     *
     * @param element a column instance
     * @return a {@link CriteriaCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when the column is null
     */
    public static CriteriaCondition lte(Element element) {
        return new CriteriaCondition(element, Condition.LESSER_EQUALS_THAN);
    }

    /**
     * An alias method to {@link CriteriaCondition#lte(Element)}, which first creates an {@link Element}
     * instance and then applies the condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link CriteriaCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when either the name or the value is null
     */
    public static CriteriaCondition lte(String name, Object value) {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(value, "Value is required");
        return lte(Element.of(name, value));
    }

    /**
     * Creates a {@link CriteriaCondition} with a {@link Condition#IN}, indicating that a select will scan a
     * column family with the same name and the value within the provided collection.
     *
     * @param element a column instance
     * @return a {@link CriteriaCondition} with {@link Condition#IN}
     * @throws NullPointerException     when the column is null
     * @throws IllegalArgumentException when the {@link Element#get()} is not an iterable implementation
     */
    public static CriteriaCondition in(Element element) {
        Objects.requireNonNull(element, "Column is required");
        checkInClause(element.value());
        return new CriteriaCondition(element, Condition.IN);
    }

    /**
     * An alias method to {@link CriteriaCondition#in(Element)}, which first creates an {@link Element}
     * instance and then applies the condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link CriteriaCondition} with {@link Condition#IN}
     * @throws NullPointerException when either the name or the value is null
     */
    public static CriteriaCondition in(String name, Object value) {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(value, "Value is required");
        return in(Element.of(name, value));
    }

    /**
     * Creates a {@link CriteriaCondition} with a {@link Condition#LIKE}, indicating that a select will scan a
     * column family with the same name and the value is like the one provided in this column.
     *
     * @param element a column instance
     * @return a {@link CriteriaCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when the column is null
     */
    public static CriteriaCondition like(Element element) {
        return new CriteriaCondition(element, Condition.LIKE);
    }

    /**
     * An alias method to {@link CriteriaCondition#like(Element)}, which first creates an {@link Element}
     * instance and then applies the condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link CriteriaCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when either the name or the value is null
     */
    public static CriteriaCondition like(String name, Object value) {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(value, "Value is required");
        return like(Element.of(name, value));
    }

    /**
     * Creates a {@link CriteriaCondition} with a {@link Condition#BETWEEN}, indicating that a select will scan a
     * column family with the same name and the value is between the two provided values.
     * The column must have an {@link Element#get()} an {@link Iterable} implementation
     * with exactly two elements.
     *
     * @param element a column instance
     * @return The between condition
     * @throws NullPointerException     when the column is null
     * @throws IllegalArgumentException When the column neither has an Iterable instance nor two elements on
     *                                  an Iterable.
     */
    public static CriteriaCondition between(Element element) {
        Objects.requireNonNull(element, "Column is required");
        checkBetweenClause(element.get());
        return new CriteriaCondition(element, Condition.BETWEEN);
    }

    /**
     * An alias method to {@link CriteriaCondition#between(Element)}, which first creates an {@link Element}
     * instance and then applies the condition.
     *
     * @param name  the name of the column
     * @param value the column information
     * @return a {@link CriteriaCondition} with {@link Condition#BETWEEN}
     * @throws NullPointerException when either the name or the value is null
     */
    public static CriteriaCondition between(String name, Object value) {
        Objects.requireNonNull(name, "Name is required");
        Objects.requireNonNull(value, "Value is required");
        return between(Element.of(name, value));
    }

    /**
     * Returns a condition that is the negation of the supplied condition.
     * This is accomplished by returning the result of calling {@code target.negate()}.
     *
     * @param condition the condition
     * @return a condition that negates the results of the supplied condition
     * @throws NullPointerException when the condition is null
     */
    public static CriteriaCondition not(CriteriaCondition condition) {
        Objects.requireNonNull(condition, "Condition is required");
        return condition.negate();
    }

    /**
     * Returns a new {@link CriteriaCondition} aggregating all the conditions as just one condition with "AND".
     * The {@link Element} will store the {@link Condition#getNameField()} as the key and the value will be
     * the {@link java.util.List} of all conditions.
     * <p>Example:</p>
     * <pre>{@code
     * Element age = Element.of("age", 26);
     * Element name = Element.of("name", "otavio");
     * CriteriaCondition condition = CriteriaCondition.eq(name).and(CriteriaCondition.gte(age));
     * }</pre>
     * The {@link CriteriaCondition#element()} will have "_AND" as the key and the list of conditions as the value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link CriteriaCondition} instance
     * @throws NullPointerException when the conditions are null
     */
    public static CriteriaCondition and(CriteriaCondition... conditions) {
        Objects.requireNonNull(conditions, "Condition is required");
        Element element = Element.of(Condition.AND.getNameField(), Arrays.asList(conditions));
        return CriteriaCondition.of(element, Condition.AND);
    }

    /**
     * Returns a new {@link CriteriaCondition} aggregating all the conditions as just one condition with "OR".
     * The {@link Element} will store the {@link Condition#getNameField()} as the key and the value will be
     * the {@link java.util.List} of all conditions.
     * <p>Example:</p>
     * <pre>{@code
     * Element age = Element.of("age", 26);
     * Element name = Element.of("name", "otavio");
     * CriteriaCondition condition = CriteriaCondition.eq(name).or(CriteriaCondition.gte(age));
     * }</pre>
     * The {@link CriteriaCondition#element()} will have "_OR" as the key and the list of conditions as the value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link CriteriaCondition} instance
     * @throws NullPointerException when the condition is null
     */
    public static CriteriaCondition or(CriteriaCondition... conditions) {
        Objects.requireNonNull(conditions, "Condition is required");
        Element element = Element.of(Condition.OR.getNameField(), Arrays.asList(conditions));
        return CriteriaCondition.of(element, Condition.OR);
    }

    private static void checkInClause(Value value) {
        if (!value.isInstanceOf(Iterable.class)) {
            throw new IllegalArgumentException("On CriteriaCondition#in, you must use an iterable" +
                    " instead of class: " + value.getClass().getName());
        }
    }

    private static void checkBetweenClause(Object value) {
        if (Iterable.class.isInstance(value)) {

            long count = StreamSupport.stream(Iterable.class.cast(value).spliterator(), false).count();
            if (count != 2) {
                throw new IllegalArgumentException("On CriteriaCondition#between, you must use an iterable" +
                        " with two elements");
            }

        } else {
            throw new IllegalArgumentException("On CriteriaCondition#between, you must use an iterable" +
                    " with two elements instead of class: " + value.getClass().getName());
        }
    }
}
