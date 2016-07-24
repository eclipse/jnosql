package org.apache.diana.api.document;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface DocumentCollectionEntity extends Serializable {

    static DocumentCollectionEntity of(String name) throws NullPointerException {
        return new DefaultDocumentCollectionEntity(name);
    }

    static DocumentCollectionEntity of(String name, List<Document> documents) throws NullPointerException {
        DefaultDocumentCollectionEntity entity = new DefaultDocumentCollectionEntity(name);
        entity.addAll(documents);
        return entity;
    }

    String getName();

    boolean remove(String columnName);

    List<Document> getDocuments();

    void add(Document document);

    void addAll(Iterable<Document> documents);

    Optional<Document> find(String name);

    DocumentCollectionEntity copy();

}
