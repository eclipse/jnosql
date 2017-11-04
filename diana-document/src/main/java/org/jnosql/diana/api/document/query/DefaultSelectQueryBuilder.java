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


import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentQuery;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * The default implementation of the Select in the document
 */
class DefaultSelectQueryBuilder implements DocumentSelect, DocumentFrom, DocumentWhere, DocumentLimit,
        DocumentStart, DocumentOrder, DocumentWhereName, DocumentNotCondition {


    private String documentCollection;

    private DocumentCondition condition;

    private long start;

    private long limit;

    private final List<Sort> sorts = new ArrayList<>();

    private final List<String> documents;

    private String name;

    private boolean negate;

    private boolean and;

    DefaultSelectQueryBuilder(List<String> documents) {
        this.documents = documents;
    }


    @Override
    public DocumentFrom from(String documentCollection) throws NullPointerException {
        requireNonNull(documentCollection, "documentCollection is required");
        this.documentCollection = documentCollection;
        return this;
    }

    @Override
    public DocumentWhere where(DocumentCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = condition;
        return this;
    }

    @Override
    public DocumentWhereName where(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }

    @Override
    public DocumentStart start(long start) {
        this.start = start;
        return this;
    }

    @Override
    public DocumentLimit limit(long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public DocumentOrder orderBy(Sort sort) throws NullPointerException {
        requireNonNull(sort, "sort is required");
        this.sorts.add(sort);
        return this;
    }

    @Override
    public DocumentWhere and(DocumentCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = this.condition.and(condition);
        return this;
    }

    @Override
    public DocumentWhere or(DocumentCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.condition = this.condition.or(condition);
        return this;
    }

    @Override
    public DocumentWhereName and(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public DocumentWhereName or(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }


    @Override
    public DocumentNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> DocumentWhere eq(T value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.eq(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentWhere like(String value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.like(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentWhere gt(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.gt(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentWhere gte(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.gte(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentWhere lt(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.lt(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentWhere lte(Number value) throws NullPointerException {
        requireNonNull(value, "value is required");
        DocumentCondition newCondition = DocumentCondition.lte(Document.of(name, value));
        return appendCondition(newCondition);
    }

    @Override
    public DocumentWhere between(Number valueA, Number valueB) throws NullPointerException {
        requireNonNull(valueA, "valueA is required");
        requireNonNull(valueB, "valueB is required");
        DocumentCondition newCondition = DocumentCondition.between(Document.of(name, asList(valueA, valueB)));
        return appendCondition(newCondition);
    }

    @Override
    public <T> DocumentWhere in(Iterable<T> values) throws NullPointerException {
        requireNonNull(values, "values is required");
        DocumentCondition newCondition = DocumentCondition.in(Document.of(name, values));
        return this;
    }

    @Override
    public DocumentQuery build() {
        return new DefaultDocumentQuery(limit, start, documentCollection, documents, sorts, condition);
    }

    private DocumentWhere appendCondition(DocumentCondition newCondition) {
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
