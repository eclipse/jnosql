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

import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.api.document.DocumentDeleteQuery;

import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation to Delete query
 */
class DefaultDeleteQueryBuilder extends BaseQueryBuilder implements DocumentDelete, DocumentDeleteFrom,
        DocumentDeleteWhere, DocumentDeleteNotCondition {

    private String documentCollection;


    private final List<String> documents;


    DefaultDeleteQueryBuilder(List<String> documents) {
        this.documents = documents;
    }

    @Override
    public DocumentDeleteFrom from(String documentCollection) {
        requireNonNull(documentCollection, "documentCollection is required");
        this.documentCollection = documentCollection;
        return this;
    }


    @Override
    public DocumentDeleteNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public DocumentDeleteNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public DocumentDeleteNameCondition or(String name) {
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
    public <T> DocumentDeleteWhere eq(T value) {
        eqImpl(value);
        return this;
    }

    @Override
    public DocumentDeleteWhere like(String value) {
        likeImpl(value);
        return this;
    }

    @Override
    public <T> DocumentDeleteWhere gt(T value) {
        gtImpl(value);
        return this;
    }

    @Override
    public <T> DocumentDeleteWhere gte(T value) {
        gteImpl(value);
        return this;
    }

    @Override
    public <T> DocumentDeleteWhere lt(T value) {
        ltImpl(value);
        return this;
    }

    @Override
    public <T> DocumentDeleteWhere lte(T value) {
        lteImpl(value);
        return this;
    }

    @Override
    public <T> DocumentDeleteWhere between(T valueA, T valueB) {
        betweenImpl(valueA, valueB);
        return this;
    }

    @Override
    public <T> DocumentDeleteWhere in(Iterable<T> values) {
        inImpl(values);
        return this;
    }


    @Override
    public DocumentDeleteQuery build() {
        return new DefaultDocumentDeleteQuery(documentCollection, condition, documents);
    }

    @Override
    public void execute(DocumentCollectionManager manager) {
        requireNonNull(manager, "manager is required");
        manager.delete(this.build());
    }

    @Override
    public void execute(DocumentCollectionManagerAsync manager) {
        requireNonNull(manager, "manager is required");
        manager.delete(this.build());
    }

    @Override
    public void execute(DocumentCollectionManagerAsync manager, Consumer<Void> callback) {
        requireNonNull(manager, "manager is required");
        requireNonNull(callback, "callback is required");
        manager.delete(this.build(), callback);
    }
}
