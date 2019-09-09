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
package org.eclipse.jnosql.diana.document.query;

import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.Document;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

abstract class BaseQueryBuilder {


    protected String name;

    protected boolean negate;

    protected boolean and;

    protected DocumentCondition condition;

    protected <T> void eqImpl(T value) {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.eq(Document.of(name, value));
        appendCondition(newCondition);
    }

    protected <T> void gtImpl(T value) {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.gt(Document.of(name, value));
        appendCondition(newCondition);
    }

    protected void likeImpl(String value) {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.like(Document.of(name, value));
        appendCondition(newCondition);
    }

    protected <T> void ltImpl(T value) {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.lt(Document.of(name, value));
        appendCondition(newCondition);
    }

    protected <T> void lteImpl(T value) {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.lte(Document.of(name, value));
        appendCondition(newCondition);
    }

    protected <T> void gteImpl(T value) {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.gte(Document.of(name, value));
        appendCondition(newCondition);
    }

    protected <T> void betweenImpl(T valueA, T valueB) {
        requireNonNull(valueA, "valueA is required");
        requireNonNull(valueB, "valueB is required");
        DocumentCondition newCondition = DocumentCondition.between(Document.of(name, asList(valueA, valueB)));
        appendCondition(newCondition);
    }

    protected <T> void inImpl(Iterable<T> values) {
        requireNonNull(values, "values is required");
        DocumentCondition newCondition = DocumentCondition.in(Document.of(name, values));
        appendCondition(newCondition);
    }


    protected void appendCondition(DocumentCondition newCondition) {
        DocumentCondition documentCondition = getDocumentCondition(newCondition);

        if (nonNull(condition)) {
            if (and) {
                this.condition = condition.and(documentCondition);
            } else {
                this.condition = condition.or(documentCondition);
            }
        } else {
            this.condition = documentCondition;
        }
        this.negate = false;
        this.name = null;
    }

    private DocumentCondition getDocumentCondition(DocumentCondition newCondition) {
        if (negate) {
            return newCondition.negate();
        } else {
            return newCondition;
        }
    }

}
