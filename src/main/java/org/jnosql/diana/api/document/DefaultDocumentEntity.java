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


import org.jnosql.diana.api.Value;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

/**
 * A default implementation of {@link DocumentEntity}
 */
final class DefaultDocumentEntity implements DocumentEntity {

    private final List<Document> documents = new ArrayList<>();

    private final String name;


    DefaultDocumentEntity(String name) {
        this.name = requireNonNull(name, "name name is required");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean remove(String documentName) {
        requireNonNull(documentName, "documentName is required");
        return documents.removeIf(document -> document.getName().equals(documentName));
    }

    @Override
    public List<Document> getDocuments() {
        return unmodifiableList(documents);
    }

    @Override
    public void add(Document document) {
        requireNonNull(document, "Document is required");
        this.remove(document.getName());
        documents.add(document);
    }

    @Override
    public void add(String documentName, Object value) throws UnsupportedOperationException, NullPointerException {
        requireNonNull(documentName, "documentName is required");
        requireNonNull(value, "value is required");
        remove(documentName);
        this.add(Document.of(documentName, value));
    }

    @Override
    public void add(String documentName, Value value) throws UnsupportedOperationException, NullPointerException {
        requireNonNull(documentName, "documentName is required");
        requireNonNull(value, "value is required");
        remove(documentName);
        this.add(Document.of(documentName, value));
    }

    @Override
    public void addAll(Iterable<Document> documents) {
        requireNonNull(documents, "documents are required");
        documents.forEach(this::add);
    }

    @Override
    public Optional<Document> find(String name) {
        requireNonNull(name, "name is required");
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
    public void clear() {
        this.documents.clear();
    }

    @Override
    public Set<String> getDocumentNames() {
        return documents.stream().map(Document::getName).collect(toSet());
    }

    @Override
    public Collection<Value> getValues() {
        return documents.stream().map(Document::getValue)
                .collect(toSet());
    }

    @Override
    public boolean contains(String documentName) {
        requireNonNull(documentName, "documentName is required");
        return documents.stream().anyMatch(d -> documentName.equals(d.getName()));
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
