/*
 *
 *  Copyright (c) 2022 Otávio Santana and others
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
package org.eclipse.jnosql.communication.document.query;

import jakarta.nosql.Sort;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

class DefaultBuilderSelectQueryBuilder implements DocumentQuery.DocumentQueryBuilder {

    private List<String> documents = new ArrayList<>();

    private List<Sort> sorts = new ArrayList<>();

    private String documentCollection;

    private DocumentCondition condition;

    private long skip;

    private long limit;


    @Override
    public DocumentQuery.DocumentQueryBuilder select(String document) {
        Objects.requireNonNull(document, "document is required");
        this.documents.add(document);
        return this;
    }

    @Override
    public DocumentQuery.DocumentQueryBuilder select(String... documents) {
        Consumer<String> validNull = d -> requireNonNull(d, "there is null document in the query");
        Consumer<String> consume = this.documents::add;
        Stream.of(documents).forEach(validNull.andThen(consume));
        return this;
    }

    @Override
    public DocumentQuery.DocumentQueryBuilder sort(Sort sort) {
        Objects.requireNonNull(sort, "sort is required");
        this.sorts.add(sort);
        return this;
    }

    @Override
    public DocumentQuery.DocumentQueryBuilder sort(Sort... sorts) {
        Consumer<Sort> validNull = d -> requireNonNull(d, "there is null document in the query");
        Consumer<Sort> consume = this.sorts::add;
        Stream.of(sorts).forEach(validNull.andThen(consume));
        return this;
    }

    @Override
    public DocumentQuery.DocumentQueryBuilder from(String documentCollection) {
        Objects.requireNonNull(documentCollection, "documentCollection is required");
        this.documentCollection = documentCollection;
        return this;
    }

    @Override
    public DocumentQuery.DocumentQueryBuilder where(DocumentCondition condition) {
        Objects.requireNonNull(condition, "condition is required");
        this.condition = condition;
        return this;
    }

    @Override
    public DocumentQuery.DocumentQueryBuilder skip(long skip) {
        if (skip < 0) {
            throw new IllegalArgumentException("The skip should not be negative, skip: " + skip);
        }
        this.skip = skip;
        return this;
    }

    @Override
    public DocumentQuery.DocumentQueryBuilder limit(long limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("The limit should not be negative, limit: " + limit);
        }
        this.limit = limit;
        return this;
    }

    @Override
    public DocumentQuery build() {
        if (Objects.isNull(documentCollection)) {
            throw new IllegalArgumentException("The document collection is mandatory to build");
        }
        return new DefaultDocumentQuery(limit, skip, documentCollection,
                documents, sorts, condition);
    }

    @Override
    public Stream<DocumentEntity> getResult(DocumentCollectionManager manager) {
        Objects.requireNonNull(manager, "manager is required");
        return manager.select(build());
    }

    @Override
    public Optional<DocumentEntity> getSingleResult(DocumentCollectionManager manager) {
        Objects.requireNonNull(manager, "manager is required");
        return manager.singleResult(build());
    }
}
