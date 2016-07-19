package org.apache.diana.api.document;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DocumentCollection implements Serializable {


    private final List<Document> documents = new ArrayList<>();

    private final String name;

    public DocumentCollection(String name) {
        this.name = Objects.requireNonNull(name, "name name is required");
    }


    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    public void add(Document document) {
        Objects.requireNonNull(document, "Document is required");
        documents.add(document);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentCollection that = (DocumentCollection) o;
        return Objects.equals(documents, that.documents) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documents, name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DocumentCollection{");
        sb.append("documents=").append(documents);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
