/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
