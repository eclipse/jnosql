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

package org.eclipse.jnosql.communication.document;


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
 * It is the state of column queries, with a condition and a value, as a {@link Document},
 * where both combined define a predicate.
 *
 * @see DocumentManager#select(DocumentQuery)
 * @see Condition
 */
public final class DocumentCondition {

    private final Document document;

    private final Condition condition;

    private final boolean readOnly;

    private DocumentCondition(Document document, Condition condition) {
        this.document = document;
        this.condition = condition;
        this.readOnly = false;
    }

    private DocumentCondition(Document document, Condition condition, boolean readOnly) {
        this.document = document;
        this.condition = condition;
        this.readOnly = readOnly;
    }

    /**
     * Gets the document to be used in the select
     *
     * @return a document instance
     */
    public Document document() {
        return document;
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
     * Creates a new {@link DocumentCondition} using the {@link Condition#AND}
     *
     * @param condition the condition to be aggregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    public DocumentCondition and(DocumentCondition condition) {
        validateReadOnly();
        requireNonNull(condition, "Conditions is required");
        if (Condition.AND.equals(this.condition)) {
            Document column = getConditions(condition, Condition.AND);
            return new DocumentCondition(column, Condition.AND);
        }
        return DocumentCondition.and(this, condition);
    }

    /**
     * Creates a new {@link DocumentCondition} negating the current one
     *
     * @return the negated condition
     * @see Condition#NOT
     */
    public DocumentCondition negate() {
        validateReadOnly();
        if (Condition.NOT.equals(this.condition)) {
            return this.document.get(DocumentCondition.class);
        } else {
            Document newDocument = Document.of(Condition.NOT.getNameField(), this);
            return new DocumentCondition(newDocument, Condition.NOT);
        }
    }

    /**
     * Creates a new {@link DocumentCondition} using the {@link Condition#OR}
     *
     * @param condition the condition to be aggregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    public DocumentCondition or(DocumentCondition condition) {
        validateReadOnly();
        requireNonNull(condition, "Condition is required");
        if (Condition.OR.equals(this.condition)) {
            Document newDocument = getConditions(condition, Condition.OR);
            return new DocumentCondition(newDocument, Condition.OR);
        }
        return DocumentCondition.or(this, condition);
    }

    private void validateReadOnly() {
        if (readOnly) {
            throw new IllegalStateException("You cannot change the status after building the query");
        }
    }


    private Document getConditions(DocumentCondition columnCondition, Condition condition) {
        List<DocumentCondition> conditions = new ArrayList<>(document.get(new TypeReference<List<DocumentCondition>>() {
        }));
        conditions.add(columnCondition);
        return Document.of(condition.getNameField(), conditions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentCondition that = (DocumentCondition) o;
        return Objects.equals(document, that.document) && condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, condition);
    }

    @Override
    public String toString() {
        return "DocumentCondition{" + "document=" + document +
                ", condition=" + condition +
                '}';
    }

    public static DocumentCondition readOnly(DocumentCondition condition) {
        requireNonNull(condition, "condition is required");
        return new DocumentCondition(condition.document(), condition.condition(), true);
    }

    public static DocumentCondition of(Document document, Condition condition) {
        return new DocumentCondition(Objects.requireNonNull(document, "Document is required"), condition);
    }

    /**
     * Returns a new {@link DocumentCondition} aggregating ,as "AND", all the conditions as just one condition.
     * The {@link Document} will storage the {@link Condition#getNameField()} as key and the value gonna be
     * the {@link java.util.List} of all conditions, in other words.
     * <p>Given:</p>
     * <pre>
     * {@code
     * Document age = Document.of("age", 26);
     * Document name = Document.of("name", "otavio");
     * DocumentCondition condition = DocumentCondition.eq(name).and(DocumentCondition.gte(age));
     * }
     * </pre>
     * The {@link DocumentCondition#document()} will have "_AND" as key and the list of condition as value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link DocumentCondition} instance
     * @throws NullPointerException when the conditions are null
     */
    public static DocumentCondition and(DocumentCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Document document = Document.of(Condition.AND.getNameField(), asList(conditions));
        return DocumentCondition.of(document, Condition.AND);
    }

    /**
     * Returns a new {@link DocumentCondition} aggregating ,as "OR", all the conditions as just one condition.
     * The {@link Document} will storage the {@link Condition#getNameField()} as key and the value gonna be
     * the {@link java.util.List} of all conditions, in other words.
     * <p>Given:</p>
     * <pre>
     * {@code
     * Document age = Document.of("age", 26);
     * Document name = Document.of("name", "otavio");
     * ColumnCondition condition = DocumentCondition.eq(name).or(DocumentCondition.gte(age));
     * }
     * </pre>
     * The {@link DocumentCondition#document()} will have "_OR" as key and the list of condition as value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link DocumentCondition} instance
     * @throws NullPointerException when the condition is null
     */
    public static DocumentCondition or(DocumentCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Document document = Document.of(Condition.OR.getNameField(), asList(conditions));
        return DocumentCondition.of(document, Condition.OR);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#EQUALS}, it means a select will scanning to a
     * document collection that has the same name and equals value informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when column is null
     */
    public static DocumentCondition eq(Document document) {
        Objects.requireNonNull(document, "document is required");
        return new DocumentCondition(document, Condition.EQUALS);
    }

    /**
     * an alias method to {@link DocumentCondition#eq(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     *
     * @param name  the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when either name or value is null
     */
    public static DocumentCondition eq(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return new DocumentCondition(Document.of(name, value), Condition.EQUALS);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#GREATER_THAN},
     * it means a select will scanning to a document collection that has the same name and the value
     * greater than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when column is null
     */
    public static DocumentCondition gt(Document document) {
        Objects.requireNonNull(document, "document is required");
        return new DocumentCondition(document, Condition.GREATER_THAN);
    }

    /**
     * an alias method to {@link DocumentCondition#gt(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     *
     * @param name  the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when either name or value is null
     */
    public static DocumentCondition gt(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return gt(Document.of(name, value));
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#GREATER_EQUALS_THAN},
     * it means a select will scanning to a document collection that has the same name and the value
     * greater or equals than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    public static DocumentCondition gte(Document document) {
        Objects.requireNonNull(document, "document is required");
        return new DocumentCondition(document, Condition.GREATER_EQUALS_THAN);
    }

    /**
     * an alias method to {@link DocumentCondition#gte(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     *
     * @param name  the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when either name or value is null
     */
    public static DocumentCondition gte(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return gte(Document.of(name, value));
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LESSER_THAN}, it means a select will scanning to a
     * document collection that has the same name and the value  lesser than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when column is null
     */
    public static DocumentCondition lt(Document document) {
        Objects.requireNonNull(document, "document is required");
        return new DocumentCondition(document, Condition.LESSER_THAN);
    }

    /**
     * an alias method to {@link DocumentCondition#lt(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     *
     * @param name  the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when either name or value is null
     */
    public static DocumentCondition lt(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return lt(Document.of(name, value));
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LESSER_EQUALS_THAN},
     * it means a select will scanning to a document collection that has the same name and the value
     * lesser or equals than informed in this document.
     *
     * @param document a document instance
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    public static DocumentCondition lte(Document document) {
        Objects.requireNonNull(document, "document is required");
        return new DocumentCondition(document, Condition.LESSER_EQUALS_THAN);
    }

    /**
     * an alias method to {@link DocumentCondition#lte(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     *
     * @param name  the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when either name or value is null
     */
    public static DocumentCondition lte(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return lte(Document.of(name, value));
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#IN}, it means a select will scanning to a
     * document collection that has the same name and the value is within informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#IN}
     * @throws NullPointerException when column is null
     */
    public static DocumentCondition in(Document document) {
        Objects.requireNonNull(document, "document is required");
        Value value = document.value();
        checkInClause(value);
        return new DocumentCondition(document, Condition.IN);
    }

    /**
     * an alias method to {@link DocumentCondition#in(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     *
     * @param name  the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#IN}
     * @throws NullPointerException when either name or value is null
     */
    public static DocumentCondition in(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return in(Document.of(name, value));
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LIKE}, it means a select will scanning to a
     * document collection that has the same name and the value  is like than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when column is null
     */
    public static DocumentCondition like(Document document) {
        Objects.requireNonNull(document, "document is required");
        return new DocumentCondition(document, Condition.LIKE);
    }


    /**
     * an alias method to {@link DocumentCondition#like(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     *
     * @param name  the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when either name or value is null
     */
    public static DocumentCondition like(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return like(Document.of(name, value));
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#BETWEEN},
     * it means a select will scanning to a document collection that is between two values informed
     * on a document name.
     * The document must have a {@link Document#get()} an {@link Iterable} implementation
     * with just two elements.
     *
     * @param document a column instance
     * @return The between condition
     * @throws NullPointerException     when document is null
     * @throws IllegalArgumentException When the document neither has an Iterable instance or two elements on
     *                                  an Iterable.
     */
    public  static DocumentCondition between(Document document) {
        Objects.requireNonNull(document, "document is required");
        checkBetweenClause(document.get());
        return new DocumentCondition(document, Condition.BETWEEN);
    }

    /**
     * Returns a predicate that is the negation of the supplied predicate.
     * This is accomplished by returning result of the calling target.negate().
     *
     * @param condition the condition
     * @return a condition that negates the results of the supplied predicate
     * @throws NullPointerException when condition is null
     */
    public static DocumentCondition not(DocumentCondition condition) {
        Objects.requireNonNull(condition, "condition is required");
        return condition.negate();
    }

    /**
     * an alias method to {@link DocumentCondition#between(Document)} where it will create a {@link Document}
     * instance first and then apply te condition.
     *
     * @param name  the name of the document
     * @param value the document information
     * @return a {@link DocumentCondition} with {@link Condition#BETWEEN}
     * @throws NullPointerException when either name or value is null
     */
    public static DocumentCondition between(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return between(Document.of(name, value));
    }

    private static void checkInClause(Value value) {
        if (!value.isInstanceOf(Iterable.class)) {
            throw new IllegalArgumentException("On DocumentCondition#in you must use an iterable" +
                    " instead of class: " + value.getClass().getName());
        }
    }

    private static void checkBetweenClause(Object value) {
        if (Iterable.class.isInstance(value)) {

            long count = (int) StreamSupport.stream(Iterable.class.cast(value).spliterator(), false).count();

            if (count != 2) {
                throw new IllegalArgumentException("On DocumentCondition#between you must use an iterable" +
                        " with two elements");
            }
        } else {
            throw new IllegalArgumentException("On DocumentCondition#between you must use an iterable" +
                    " with two elements instead of class: " + value.getClass().getName());
        }
    }
}