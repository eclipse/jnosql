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
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentDeleteQuery;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * The default implementation to Delete query
 */
class DefaultDeleteQueryBuilder implements DocumentDelete, DocumentDeleteFrom,
        DocumentDeleteWhere, DocumentDeleteWhereName, DocumentDeleteNotCondition {

    private String documentCollection;

    private DocumentCondition condition;

    private final List<String> documents;

    private String name;

    private boolean negate;

    private boolean and;

    DefaultDeleteQueryBuilder(List<String> documents) {
        this.documents = documents;
    }

    @Override
    public DocumentDeleteFrom from(String documentCollection) throws NullPointerException {
        requireNonNull(documentCollection, "documentCollection is required");
        this.documentCollection = documentCollection;
        return this;
    }

    @Override
    public DocumentDeleteWhere where(DocumentCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = condition;
        return this;
    }

    @Override
    public DocumentDeleteWhereName where(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }

    @Override
    public DocumentDeleteWhere and(DocumentCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = this.condition.and(condition);
        return this;
    }

    @Override
    public DocumentDeleteWhere or(DocumentCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = this.condition.or(condition);
        return this;
    }

    @Override
    public DocumentDeleteNameCondition and(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public DocumentDeleteNameCondition or(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }



    @Override
    public DocumentDeleteNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> DocumentDeleteWhere eq(T value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.eq(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentDeleteWhere like(String value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.like(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentDeleteWhere gt(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.gt(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentDeleteWhere gte(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.gte(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentDeleteWhere lt(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.lt(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentDeleteWhere lte(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.lte(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentDeleteWhere between(Number valueA, Number valueB) throws NullPointerException {
        requireNonNull(valueA, "valueA is required");
        requireNonNull(valueB, "valueB is required");
        DocumentCondition newCondition = DocumentCondition.between(Document.of(name, asList(valueA, valueB)));
        return appendCondition(newCondition);
    }

    @Override
    public <T> DocumentDeleteWhere in(Iterable<T> values) throws NullPointerException {
        requireNonNull(values, "values is required");
        DocumentCondition newCondition = DocumentCondition.in(Document.of(name, values));
        return this;
    }

    @Override
    public DocumentDeleteQuery build() {
        return new DefaultDocumentDeleteQuery(documentCollection, condition, documents);
    }

    private DocumentDeleteWhere appendCondition(DocumentCondition newCondition) {
        if (negate) {
            newCondition = newCondition.negate();
        }
        if (nonNull(condition)) {
            if (and) {
                this.condition = condition.and(newCondition);
            } else {
                this.condition = condition.or(newCondition);
            }
        } else {
            this.condition = newCondition;
        }
        this.negate = false;
        this.name = null;
        return this;
    }
}
