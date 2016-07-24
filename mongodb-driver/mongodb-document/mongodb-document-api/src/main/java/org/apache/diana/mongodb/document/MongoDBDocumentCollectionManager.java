package org.apache.diana.mongodb.document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.apache.diana.api.ExecuteAsyncQueryException;
import org.apache.diana.api.document.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class MongoDBDocumentCollectionManager implements DocumentCollectionManager {

    private final MongoDatabase mongoDatabase;


    MongoDBDocumentCollectionManager(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }


    @Override
    public DocumentCollectionEntity save(DocumentCollectionEntity entity) {
        String collectionName = entity.getName();
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        Document document = getDocument(entity);
        collection.insertOne(document);
        boolean hasNotId = entity.getDocuments().stream().map(document1 -> document1.getName()).noneMatch(k -> k.equals("_id"));
        if(hasNotId) {
            entity.add(Documents.of("_id", document.get("_id")));
        }
        return entity;
    }



    @Override
    public void saveAsync(DocumentCollectionEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public void saveAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public DocumentCollectionEntity update(DocumentCollectionEntity entity) {
        DocumentCollectionEntity copy = entity.copy();
        String collectionName = entity.getName();
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        Document id = copy.find("_id").map(d -> new Document(d.getName(), d.getValue().get()))
                .orElseThrow(() -> new UnsupportedOperationException("To update this DocumentCollectionEntity the field `id` is required"));
        copy.remove("_id");
        collection.findOneAndReplace(id, getDocument(entity));
        return entity;
    }

    @Override
    public void updateAsync(DocumentCollectionEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public void updateAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public void delete(DocumentQuery query) {
        String collectionName = query.getCollection();
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        List<Bson> collect = query.getConditions().stream().map(DocumentQueryConversor::convert).collect(Collectors.toList());
        collection.deleteMany(Filters.and(collect));
    }

    @Override
    public void deleteAsync(DocumentQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public void deleteAsync(DocumentQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public List<DocumentCollectionEntity> find(DocumentQuery query) {
        return null;
    }

    @Override
    public void findAsync(DocumentQuery query, Consumer<List<DocumentCollectionEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public List<DocumentCollectionEntity> nativeQuery(String query) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to native query yet");
    }

    @Override
    public void nativeQueryAsync(String query, Consumer<List<DocumentCollectionEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to native query yet");
    }

    @Override
    public PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to native query yet");
    }

    @Override
    public void close() throws Exception {
    }

    private Document getDocument(DocumentCollectionEntity entity) {
        Document document = new Document();
        entity.getDocuments().stream().map(d -> document.put(d.getName(), d.getValue().get()));
        return document;
    }
}
