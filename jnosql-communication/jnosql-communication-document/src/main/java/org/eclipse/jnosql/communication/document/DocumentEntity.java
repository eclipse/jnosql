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



import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.Value;

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
 * The communication level entity. It is the API entity between the database and the Jakarta NoSQL communication level.
 * It represents a document collection.
 * Each DocumentEntity has a name and one or more {@link Document}.
 *
 * @see Document
 * @see DocumentEntity#documents()
 * @see DocumentEntity#name()
 */
public class DocumentEntity {

    private final Map<String, Document> documents = new HashMap<>();

    private final String name;

    DocumentEntity(String name) {
        this.name = name;
    }

    /**
     * The collection name to {@link DocumentEntity}
     *
     * @return collection name
     */
    public String name() {
        return name;
    }

    /**
     * Remove a Document whose name is informed in parameter.
     *
     * @param documentName a document name
     * @return if a column was removed or not
     * @throws NullPointerException when documentName is null
     */
    public boolean remove(String documentName) {
        requireNonNull(documentName, "documentName is required");
        return documents.remove(documentName) != null;
    }

    /**
     * List of all documents
     *
     * @return all documents
     */
    public List<Document> documents() {
        return documents.values()
                .stream()
                .collect(collectingAndThen(toList(),
                        Collections::unmodifiableList));
    }

    /**
     * add a document within {@link DocumentEntity}
     *
     * @param document a document to be included
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when document is null
     */
    public void add(Document document) {
        requireNonNull(document, "Document is required");
        this.documents.put(document.name(), document);
    }

    /**
     * add a document within {@link DocumentEntity}
     *
     * @param documentName a name of the document
     * @param value        the information of the document
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    public void add(String documentName, Object value) {
        requireNonNull(documentName, "documentName is required");
        requireNonNull(value, "value is required");
        this.documents.put(documentName, Document.of(documentName, Value.of(value)));
    }

    /**
     * add a document within {@link DocumentEntity}
     *
     * @param documentName a name of the document
     * @param value        the information of the document
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    public void add(String documentName, Value value) {
        requireNonNull(documentName, "documentName is required");
        requireNonNull(value, "value is required");
        this.documents.put(documentName, Document.of(documentName, value));
    }

    /**
     * add all documents within {@link DocumentEntity}
     *
     * @param documents documents to be included
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when document is null
     */
    public void addAll(Iterable<Document> documents) {
        requireNonNull(documents, "documents are required");
        documents.forEach(this::add);
    }

    /**
     * Find document from document name
     *
     * @param documentName a name of a document
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when documentName is null
     */
    public Optional<Document> find(String documentName) {
        requireNonNull(documentName, "documentName is required");
        Document document = documents.get(documentName);
        return ofNullable(document);
    }

    /**
     * Find a document and converts to specific type from {@link Class}.
     * It is an alias to {@link Value#get(Class)}
     *
     * @param documentName a name of a document
     * @param type         the type to convert the value
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when there are null parameters
     */
    public <T> Optional<T> find(String documentName, Class<T> type) {
        Objects.requireNonNull(documentName, "documentName is required");
        Objects.requireNonNull(type, "type is required");
        return ofNullable(documents.get(documentName))
                .map(d -> d.get(type));
    }

    /**
     * Find a document and converts to specific type from {@link TypeSupplier}.
     * It is an alias to {@link Value#get(TypeSupplier)}
     *
     * @param documentName a name of a document
     * @param type         the type to convert the value
     * @return a new instance converted to informed class
     * @throws NullPointerException when there are null parameters
     */
    public <T> Optional<T> find(String documentName, TypeSupplier<T> type) {
        Objects.requireNonNull(documentName, "documentName is required");
        Objects.requireNonNull(type, "type is required");
        return ofNullable(documents.get(documentName)).map(d -> d.get(type));
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return documents.size();
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements
     */
    public boolean isEmpty() {
        return documents.isEmpty();
    }

    /**
     * make copy of itself
     *
     * @return an instance copy
     */
    public DocumentEntity copy() {
        DocumentEntity entity = new DocumentEntity(this.name);
        entity.documents.putAll(new HashMap<>(this.documents));
        return entity;
    }

    /**
     * Removes all Documents
     */
    public void clear() {
        this.documents.clear();
    }

    /**
     * Returns a Set view of the names of document contained in Document Entity
     *
     * @return the keys
     */
    public Set<String> getDocumentNames() {
        return unmodifiableSet(documents.keySet());
    }

    /**
     * Returns a Collection view of the values contained in this DocumentEntity.
     *
     * @return the collection of values
     */
    public Collection<Value> getValues() {
        return documents
                .values()
                .stream()
                .map(Document::value)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * Returns true if this DocumentEntity contains a document whose the name is informed
     *
     * @param documentName the document name
     * @return true if find a document and otherwise false
     */
    public boolean contains(String documentName) {
        requireNonNull(documentName, "documentName is required");
        return documents.containsKey(documentName);
    }

    /**
     * Converts the columns to a Map where:
     * the key is the name the column
     * The value is the {@link Value#get()} of the map
     *
     * @return a map instance
     */
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
            return singletonMap(column.name(), convert(column.get()));
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
        if (!(o instanceof DocumentEntity that)) {
            return false;
        }
        return Objects.equals(this.documents().stream().sorted(comparing(Document::name)).collect(toList()),
                that.documents().stream().sorted(comparing(Document::name)).collect(toList())) &&
                Objects.equals(name, that.name());
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

    /**
     * Creates a {@link DocumentEntity} instance
     *
     * @param name the name of the collection
     * @return a {@link DocumentEntity} instance
     * @throws NullPointerException when name is null
     */
    public static DocumentEntity of(String name) {
        return new DocumentEntity(requireNonNull(name, "name is required"));
    }

    /**
     * Creates a {@link DocumentEntity} instance
     *
     * @param name      the collection name
     * @param documents the initial document inside {@link DocumentEntity}
     * @return a {@link DocumentEntity} instance
     * @throws NullPointerException when either name or documents are null
     */
    public static DocumentEntity of(String name, List<Document> documents) {
        requireNonNull(documents, "documents is required");
        DocumentEntity entity = new DocumentEntity(requireNonNull(name, "name is required"));
        entity.addAll(documents);
        return entity;
    }
}
