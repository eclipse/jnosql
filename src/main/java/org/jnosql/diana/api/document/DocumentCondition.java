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

package org.jnosql.diana.api.document;


import org.jnosql.diana.api.Condition;

import static org.jnosql.diana.api.Condition.EQUALS;
import static org.jnosql.diana.api.Condition.GREATER_EQUALS_THAN;
import static org.jnosql.diana.api.Condition.GREATER_THAN;
import static org.jnosql.diana.api.Condition.IN;
import static org.jnosql.diana.api.Condition.LESSER_EQUALS_THAN;
import static org.jnosql.diana.api.Condition.LESSER_THAN;
import static org.jnosql.diana.api.Condition.LIKE;

/**
 * An unit condition  to run a document collection select
 *
 * @see DocumentCollectionManager#select(DocumentQuery)
 */
public interface DocumentCondition {

    /**
     * Gets the document to be used in the select
     *
     * @return a document instance
     */
    Document getDocument();

    /**
     * Gets the conditions to be used in the select
     *
     * @return a Condition instance
     * @see Condition
     */
    Condition getCondition();

    /**
     * Creates a new {@link DocumentCondition} using the {@link Condition#AND}
     *
     * @param condition the condition to be agregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    DocumentCondition and(DocumentCondition condition) throws NullPointerException;

    /**
     * Creates a new {@link DocumentCondition} negating the current one
     *
     * @return the negated condition
     * @see Condition#NOT
     */
    DocumentCondition negate();

    /**
     * Creates a new {@link DocumentCondition} using the {@link Condition#OR}
     *
     * @param condition the condition to be agregated
     * @return the conditions joined as AND
     * @throws NullPointerException when the condition is null
     */
    DocumentCondition or(DocumentCondition condition) throws NullPointerException;

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#EQUALS}, it means a select will scanning to a
     * document collection that has the same name and equals value informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition eq(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, EQUALS);
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
    static DocumentCondition gt(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, GREATER_THAN);
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
    static DocumentCondition gte(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, GREATER_EQUALS_THAN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LESSER_THAN}, it means a select will scanning to a
     * document collection that has the same name and the value  lesser than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition lt(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, LESSER_THAN);
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
    static DocumentCondition lte(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, LESSER_EQUALS_THAN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#IN}, it means a select will scanning to a
     * document collection that has the same name and the value is within informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#IN}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition in(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, IN);
    }

    /**
     * Creates a {@link DocumentCondition} that has a {@link Condition#LIKE}, it means a select will scanning to a
     * document collection that has the same name and the value  is like than informed in this document.
     *
     * @param document a column instance
     * @return a {@link DocumentCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when column is null
     */
    static DocumentCondition like(Document document) throws NullPointerException {
        return DefaultDocumentCondition.of(document, LIKE);
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
    static DocumentCondition between(Document document) throws NullPointerException, IllegalArgumentException {
        return DefaultDocumentCondition.between(document);
    }

    /**
     * Returns a new {@link DocumentCondition} aggregating ,as ¨AND", all the conditions as just one condition.
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
     * The {@link DocumentCondition#getDocument()} will have "_AND" as key and the list of condition as value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link DocumentCondition} instance
     * @throws NullPointerException when the conditions is null
     */
    static DocumentCondition and(DocumentCondition... conditions) throws NullPointerException {
        return DefaultDocumentCondition.and(conditions);
    }

    /**
     * Returns a new {@link DocumentCondition} aggregating ,as ¨OR", all the conditions as just one condition.
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
     * The {@link DocumentCondition#getDocument()} will have "_OR" as key and the list of condition as value.
     *
     * @param conditions the conditions to be aggregated
     * @return the new {@link DocumentCondition} instance
     * @throws NullPointerException when the condition is null
     */
    static DocumentCondition or(DocumentCondition... conditions) throws NullPointerException {
        return DefaultDocumentCondition.or(conditions);
    }

    /**
     * Returns a new {@link DocumentCondition} aggregating ,as ¨SUBQUERY", all the conditions as just one condition.
     * The {@link Document} will storage the {@link Condition#getNameField()} as key and the value gonna be
     * the {@link DocumentQuery} of all conditions, in other words.
     * <p>Given:</p>
     * <pre>
     * {@code
     * condition.subquery(condition2);
     * }
     * </pre>
     * The {@link DocumentCondition#getDocument()} will have "_SUBQUERY" as key and the select as value.
     *
     * @param query the conditions to be aggregated
     * @return the new {@link DocumentCondition} instance
     * @throws NullPointerException when the condition is null
     */
    static DocumentCondition subquery(DocumentQuery query) throws NullPointerException {
        return DefaultDocumentCondition.subquery(query);
    }

}
