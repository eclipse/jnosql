package org.apache.diana.api.document;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentCollectionEntity)) {
            return false;
        }
        DocumentCollectionEntity that = (DocumentCollectionEntity) o;
        return Objects.equals(documents, that.getDocuments()) &&
                Objects.equals(name, that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(documents, name);
    }
}
