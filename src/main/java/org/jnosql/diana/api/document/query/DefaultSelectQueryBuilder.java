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
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentQuery;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

class DefaultSelectQueryBuilder implements DocumentSelect, DocumentFrom, DocumentWhere, DocumentLimit, DocumentStart, DocumentOrder {


    private String documentCollection;

    private DocumentCondition condition;

    private long start;

    private long limit;

    private final List<Sort> sorts = new ArrayList<>();

    private final List<String> documents;

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
    public DocumentQuery build() {
        //return new DefaultDocumentQuery(limit, start, documentCollection, documents, sorts, condition);
        return null;
    }
}
