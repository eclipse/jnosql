/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.jnosql.communication.document;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * A default implementation of {@link DocumentEntity}
 */
final class DefaultDocumentEntity {

    private final Map<String, Document> documents = new HashMap<>();

    private final String name;

    DefaultDocumentEntity(String name) {
        this.name = name;
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
        return documents.values()
                .stream()
                .collect(collectingAndThen(toList(),
                        Collections::unmodifiableList));
    }

    @Override
    public void add(Document document) {
        requireNonNull(document, "Document is required");
        this.documents.put(document.getName(), document);
    }

    @Override
    public void add(String documentName, Object value) {
        requireNonNull(documentName, "documentName is required");
        requireNonNull(value, "value is required");
        this.documents.put(documentName, Document.of(documentName, Value.of(value)));
    }

    @Override
    public void add(String documentName, Value value) {
        requireNonNull(documentName, "documentName is required");
        requireNonNull(value, "value is required");
        this.documents.put(documentName, Document.of(documentName, value));
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
    public <T> Optional<T> find(String documentName, Class<T> type) {
        Objects.requireNonNull(documentName, "documentName is required");
        Objects.requireNonNull(type, "type is required");
        return ofNullable(documents.get(documentName))
                .map(d -> d.get(type));
    }

    @Override
    public <T> Optional<T> find(String documentName, TypeSupplier<T> type) {
        Objects.requireNonNull(documentName, "documentName is required");
        Objects.requireNonNull(type, "type is required");
        return ofNullable(documents.get(documentName)).map(d -> d.get(type));
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
        return documents
                .values()
                .stream()
                .map(Document::getValue)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
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
            Document document = entry.getValue();
            map.put(entry.getKey(), convert(document.get()));
        }
        return Collections.unmodifiableMap(map);
    }

    private Object convert(Object value) {
        if (value instanceof Document) {
            Document column = Document.class.cast(value);
            return singletonMap(column.getName(), convert(column.get()));
        } else if (value instanceof Iterable) {
            List<Object> list = new ArrayList<>();
            Iterable.class.cast(value).forEach(e -> list.add(convert(e)));
            return list;
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
        return "DefaultDocumentEntity{" + "documents=" + documents +
                ", name='" + name + '\'' +
                '}';
    }
}
