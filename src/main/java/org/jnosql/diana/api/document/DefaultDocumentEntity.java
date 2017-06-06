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


import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;

/**
 * A default implementation of {@link DocumentEntity}
 */
final class DefaultDocumentEntity implements DocumentEntity {

    private final List<Document> documents = new ArrayList<>();

    private final String name;


    DefaultDocumentEntity(String name) {
        this.name = Objects.requireNonNull(name, "name name is required");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean remove(String name) {
        Objects.requireNonNull(name, "name is required");
        return documents.removeIf(document -> document.getName().equals(name));
    }

    @Override
    public boolean remove(Document document) throws NullPointerException {
        Objects.requireNonNull(document, "doument is required");
        return documents.remove(document);
    }

    public List<Document> getDocuments() {
        return unmodifiableList(documents);
    }

    public void add(Document document) {
        Objects.requireNonNull(document, "Document is required");
        documents.add(document);
    }

    @Override
    public void addAll(Iterable<Document> documents) {
        Objects.requireNonNull(documents, "documents are required");
        documents.forEach(this.documents::add);
    }

    @Override
    public Optional<Document> find(String name) {
        Objects.requireNonNull(name, "name is required");
        return documents.stream().filter(document -> document.getName().equals(name)).findFirst();
    }

    @Override
    public int size() {
        return documents.size();
    }

    @Override
    public boolean isEmpty() {
        return documents.isEmpty();
    }

    @Override
    public DocumentEntity copy() {
        DefaultDocumentEntity entity = new DefaultDocumentEntity(this.name);
        entity.documents.addAll(this.getDocuments());
        return entity;
    }

    @Override
    public Map<String, Object> toMap() {
        return documents.stream().collect(Collectors.toMap(Document::getName, document -> document.getValue().get()));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentEntity)) {
            return false;
        }
        DocumentEntity that = (DocumentEntity) o;
        return Objects.equals(documents.stream().sorted(comparing(Document::getName)).collect(Collectors.toList()),
                that.getDocuments().stream().sorted(comparing(Document::getName)).collect(Collectors.toList())) &&
                Objects.equals(name, that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(documents, name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultDocumentEntity{");
        sb.append("documents=").append(documents);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
