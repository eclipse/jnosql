package org.apache.diana.api.document;


import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;

/**
 * A default implementation of {@link DocumentCollectionEntity}
 */
final class DefaultDocumentCollectionEntity implements DocumentCollectionEntity {

    private final List<Document> documents = new ArrayList<>();

    private final String name;


    DefaultDocumentCollectionEntity(String name) {
        this.name = Objects.requireNonNull(name, "name name is required");
    }


    public String getName() {
        return name;
    }

    public boolean remove(String columnName) {
        return documents.removeIf(document -> document.getName().equals(columnName));
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
    public DocumentCollectionEntity copy() {
        DefaultDocumentCollectionEntity entity = new DefaultDocumentCollectionEntity(this.name);
        entity.documents.addAll(this.getDocuments());
        return entity;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentCollectionEntity)) {
            return false;
        }
        DocumentCollectionEntity that = (DocumentCollectionEntity) o;
        return Objects.equals(documents.stream().sorted(comparing(Document::getName)).collect(Collectors.toList()),
                that.getDocuments().stream().sorted(comparing(Document::getName)).collect(Collectors.toList())) &&
                Objects.equals(name, that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(documents, name);
    }
}
