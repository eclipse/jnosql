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
package org.jnosql.diana.document.query;


import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.document.DocumentQuery.DocumentFrom;
import jakarta.nosql.document.DocumentQuery.DocumentLimit;
import jakarta.nosql.document.DocumentQuery.DocumentNameCondition;
import jakarta.nosql.document.DocumentQuery.DocumentNameOrder;
import jakarta.nosql.document.DocumentQuery.DocumentNotCondition;
import jakarta.nosql.document.DocumentQuery.DocumentOrder;
import jakarta.nosql.document.DocumentQuery.DocumentQueryBuild;
import jakarta.nosql.document.DocumentQuery.DocumentSelect;
import jakarta.nosql.document.DocumentQuery.DocumentSkip;
import jakarta.nosql.document.DocumentQuery.DocumentWhere;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of the Select in the document
 */
class DefaultSelectQueryBuilder extends BaseQueryBuilder implements DocumentSelect, DocumentFrom, DocumentLimit,
        DocumentSkip, DocumentOrder, DocumentNotCondition, DocumentNameOrder, DocumentWhere, DocumentQueryBuild {


    private String documentCollection;

    private long skip;

    private long limit;

    private final List<Sort> sorts = new ArrayList<>();

    private final List<String> documents;


    DefaultSelectQueryBuilder(List<String> documents) {
        this.documents = documents;
    }


    @Override
    public DocumentFrom from(String documentCollection) {
        requireNonNull(documentCollection, "documentCollection is required");
        this.documentCollection = documentCollection;
        return this;
    }


    @Override
    public DocumentNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }

    @Override
    public DocumentNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public DocumentNameCondition or(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }

    @Override
    public DocumentSkip skip(long skip) {
        this.skip = skip;
        return this;
    }

    @Override
    public DocumentLimit limit(long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public DocumentOrder orderBy(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public DocumentNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> DocumentWhere eq(T value) {
        eqImpl(value);
        return this;
    }

    @Override
    public DocumentWhere like(String value) {
        likeImpl(value);
        return this;
    }

    @Override
    public <T> DocumentWhere gt(T value) {
        gtImpl(value);
        return this;
    }

    @Override
    public <T> DocumentWhere gte(T value) {
        gteImpl(value);
        return this;
    }

    @Override
    public <T> DocumentWhere lt(T value) {
        ltImpl(value);
        return this;
    }


    @Override
    public <T> DocumentWhere lte(T value) {
        lteImpl(value);
        return this;
    }

    @Override
    public <T> DocumentWhere between(T valueA, T valueB) {
        betweenImpl(valueA, valueB);
        return this;
    }


    @Override
    public <T> DocumentWhere in(Iterable<T> values) {
        inImpl(values);
        return this;
    }


    @Override
    public DocumentNameOrder asc() {
        this.sorts.add(Sort.of(name, SortType.ASC));
        return this;
    }

    @Override
    public DocumentNameOrder desc() {
        this.sorts.add(Sort.of(name, SortType.DESC));
        return this;
    }


    @Override
    public DocumentQuery build() {
        return new DefaultDocumentQuery(limit, skip, documentCollection, documents, sorts, condition);
    }

    @Override
    public List<DocumentEntity> execute(DocumentCollectionManager manager) {
        requireNonNull(manager, "manager is required");
        return manager.select(this.build());
    }

    @Override
    public Optional<DocumentEntity> executeSingle(DocumentCollectionManager manager) {
        requireNonNull(manager, "manager is required");
        return manager.singleResult(this.build());
    }

    @Override
    public void execute(DocumentCollectionManagerAsync manager, Consumer<List<DocumentEntity>> callback) {
        requireNonNull(manager, "manager is required");
        requireNonNull(callback, "callback is required");
        manager.select(this.build(), callback);
    }

    @Override
    public void executeSingle(DocumentCollectionManagerAsync manager, Consumer<Optional<DocumentEntity>> callback) {
        requireNonNull(manager, "manager is required");
        requireNonNull(callback, "callback is required");
        manager.singleResult(this.build(), callback);
    }


}
