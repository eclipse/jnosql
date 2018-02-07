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
import org.jnosql.diana.api.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.jnosql.diana.api.Condition.AND;
import static org.jnosql.diana.api.Condition.NOT;
import static org.jnosql.diana.api.Condition.OR;

/**
 * The default implementation of {@link DocumentCondition}
 */
final class DefaultDocumentCondition implements DocumentCondition {

    private final Document document;

    private final Condition condition;

    private DefaultDocumentCondition(Document document, Condition condition) {
        this.document = document;
        this.condition = condition;
    }

    public static DefaultDocumentCondition of(Document document, Condition condition) {
        return new DefaultDocumentCondition(Objects.requireNonNull(document, "Document is required"), condition);
    }

    static DocumentCondition between(Document document) {
        Objects.requireNonNull(document, "document is required");
        Object value = document.get();
        checkIterableClause(value);
        return new DefaultDocumentCondition(document, Condition.BETWEEN);
    }

    private static void checkIterableClause(Object value) {
        if (Iterable.class.isInstance(value)) {

            long count = (int) StreamSupport.stream(Iterable.class.cast(value).spliterator(), false).count();

            if (count > 2) {
                throw new IllegalArgumentException("On Documentcondition#between you must use an iterable" +
                        " with two elements");
            }

            if (count != 2) {
                throw new IllegalArgumentException("On Documentcondition#between you must use an iterable" +
                        " with two elements");
            }
        } else {
            throw new IllegalArgumentException("On Documentcondition#between you must use an iterable" +
                    " with two elements instead of class: " + value.getClass().getName());
        }
    }

    static DefaultDocumentCondition and(DocumentCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Document document = Document.of(AND.getNameField(), asList(conditions));
        return DefaultDocumentCondition.of(document, AND);
    }


    static DefaultDocumentCondition or(DocumentCondition... conditions) {
        requireNonNull(conditions, "condition is required");
        Document document = Document.of(OR.getNameField(), asList(conditions));
        return DefaultDocumentCondition.of(document, OR);
    }


    public Document getDocument() {
        return document;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public DocumentCondition and(DocumentCondition condition) {
        requireNonNull(condition, "Conditions is required");
        if (AND.equals(this.condition)) {
            Document column = getConditions(condition, AND);
            return new DefaultDocumentCondition(column, AND);
        }
        return DefaultDocumentCondition.and(this, condition);
    }

    @Override
    public DocumentCondition negate() {
        if (NOT.equals(this.condition)) {
            return this.document.get(DocumentCondition.class);
        } else {
            Document document = Document.of(NOT.getNameField(), this);
            return new DefaultDocumentCondition(document, NOT);
        }
    }

    @Override
    public DocumentCondition or(DocumentCondition condition) {
        requireNonNull(condition, "Condition is required");
        if (OR.equals(this.condition)) {
            Document document = getConditions(condition, OR);
            return new DefaultDocumentCondition(document, OR);
        }
        return DefaultDocumentCondition.or(this, condition);
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
        if (o == null || !DocumentCondition.class.isAssignableFrom(o.getClass())) {
            return false;
        }
        DocumentCondition that = (DocumentCondition) o;
        return Objects.equals(document, that.getDocument()) &&
                condition == that.getCondition();
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, condition);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultDocumentCondition{");
        sb.append("document=").append(document);
        sb.append(", condition=").append(condition);
        sb.append('}');
        return sb.toString();
    }


}
