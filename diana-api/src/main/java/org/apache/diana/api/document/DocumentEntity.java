package org.apache.diana.api.document;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DocumentEntity implements Serializable {

    private final Document key;

    private final List<Document> documents = new ArrayList<>();

    private final String collection;

    public DocumentEntity(Document key, String collection) {
        this.key = Objects.requireNonNull(key, "key is required");
        this.collection = Objects.requireNonNull(collection, "collection name is required");
    }

    public Document getKey() {
        return key;
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentEntity that = (DocumentEntity) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(documents, that.documents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, documents);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DocumentEntity{");
        sb.append("key=").append(key);
        sb.append(", documents=").append(documents);
        sb.append('}');
        return sb.toString();
    }
}
