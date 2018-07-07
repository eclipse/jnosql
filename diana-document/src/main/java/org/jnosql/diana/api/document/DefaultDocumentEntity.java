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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableSet;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * A default implementation of {@link DocumentEntity}
 */
final class DefaultDocumentEntity implements DocumentEntity {

    private final Map<String, Document> documents = new HashMap<>();

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
        return documents.remove(documentName) != null;
    }

    @Override
    public List<Document> getDocuments() {
        return documents.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    @Override
    public void add(Document document) {
        requireNonNull(document, "Document is required");
        documents.put(document.getName(), document);
    }

    @Override
    public void add(String documentName, Object value) {
        requireNonNull(documentName, "documentName is required");
        requireNonNull(value, "value is required");
        this.add(Document.of(documentName, value));
    }

    @Override
    public void add(String documentName, Value value) {
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
    public Optional<Document> find(String documentName) {
        requireNonNull(documentName, "documentName is required");
        Document document = documents.get(documentName);
        return ofNullable(document);
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
        entity.documents.putAll(new HashMap<>(this.documents));
        return entity;
    }

    @Override
    public void clear() {
        this.documents.clear();
    }

    @Override
    public Set<String> getDocumentNames() {
        return unmodifiableSet(documents.keySet());
    }

    @Override
    public Collection<Value> getValues() {
        return documents.values().stream()
                .map(Document::getValue)
                .collect(toList());
    }

    @Override
    public boolean contains(String documentName) {
        requireNonNull(documentName, "documentName is required");
        return documents.containsKey(documentName);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Document> entry : documents.entrySet()) {
            Document value = entry.getValue();
            map.put(value.getName(), convert(value.get()));
        }
        return Collections.unmodifiableMap(map);
    }

    private Object convert(Object value) {
        if (value instanceof Document) {
            Document column = Document.class.cast(value);
            return Collections.singletonMap(column.getName(), convert(column.get()));
        }
        return value;
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
        return Objects.equals(this.getDocuments().stream().sorted(comparing(Document::getName)).collect(toList()),
                that.getDocuments().stream().sorted(comparing(Document::getName)).collect(toList())) &&
                Objects.equals(name, that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(documents, name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultDocumentEntity{");
        sb.append("documents=").append(toMap());
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
