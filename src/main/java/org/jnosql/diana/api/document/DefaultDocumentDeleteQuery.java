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


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link DocumentDeleteQuery}
 */
class DefaultDocumentDeleteQuery implements DocumentDeleteQuery {

    private final String collection;

    private DocumentCondition condition;

    private final List<String> documents = new ArrayList<>();

    private DefaultDocumentDeleteQuery(String collection, DocumentCondition condition) {
        this.collection = collection;
        this.condition = condition;
    }

    DefaultDocumentDeleteQuery(String collection) {
        this.collection = collection;
    }

    public String getCollection() {
        return collection;
    }

    public Optional<DocumentCondition> getCondition() {
        return Optional.ofNullable(condition);
    }

    @Override
    public List<String> getDocuments() {
        return unmodifiableList(documents);
    }

    @Override
    public void add(String document) throws NullPointerException {
        this.documents.add(requireNonNull(document, "document null is required"));
    }

    @Override
    public void addAll(Iterable<String> documents) throws NullPointerException {
        requireNonNull(documents, "documents is required");
        documents.forEach(this::add);
    }

    @Override
    public DocumentDeleteQuery and(DocumentCondition condition) throws NullPointerException {
        Objects.requireNonNull(condition, "condition is required");
        if (Objects.isNull(this.condition)) {
            this.condition = condition;
        } else {
            this.condition = this.condition.and(condition);
        }

        return this;
    }

    @Override
    public DocumentDeleteQuery or(DocumentCondition condition) throws NullPointerException {
        Objects.requireNonNull(condition, "condition is required");
        if (Objects.isNull(this.condition)) {
            this.condition = condition;
        } else {
            this.condition = this.condition.or(condition);
        }

        return this;
    }

    @Override
    public DocumentDeleteQuery with(DocumentCondition condition) throws NullPointerException {
        Objects.requireNonNull(condition, "Condition is required");
        this.condition = condition;
        return this;
    }

    @Override
    public void remove(String document) throws NullPointerException {
        requireNonNull(document, "document is required");
        this.documents.remove(document);
    }

    @Override
    public void removeAll(Iterable<String> documents) throws NullPointerException {
        requireNonNull(documents, "documents is required");
        documents.forEach(this::remove);
    }

    static DefaultDocumentDeleteQuery of(String collection) throws NullPointerException {
        requireNonNull(collection, "collection is required");
        return new DefaultDocumentDeleteQuery(collection);
    }

    static DefaultDocumentDeleteQuery of(String collection, DocumentCondition condition) throws NullPointerException {
        requireNonNull(collection, "collection is required");
        requireNonNull(condition, "condition is required");
        return new DefaultDocumentDeleteQuery(collection, condition);
    }
}
