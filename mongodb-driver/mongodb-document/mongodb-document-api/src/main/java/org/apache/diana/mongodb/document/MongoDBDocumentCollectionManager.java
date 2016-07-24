package org.apache.diana.mongodb.document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.diana.api.ExecuteAsyncQueryException;
import org.apache.diana.api.document.*;
import org.bson.Document;

import java.util.List;
import java.util.function.Consumer;

class MongoDBDocumentCollectionManager implements DocumentCollectionManager {

    private final MongoDatabase mongoDatabase;


    MongoDBDocumentCollectionManager(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }


    @Override
    public DocumentCollectionEntity save(DocumentCollectionEntity entity) {
        String collectionName = entity.getName();
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        Document document = new Document();

        entity.getDocuments().stream().map(d -> document.put(d.getName(), d.getValue().get()));
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
        return null;
    }

    @Override
    public void updateAsync(DocumentCollectionEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public void updateAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public void delete(DocumentQuery query) {

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
        return null;
    }

    @Override
    public void nativeQueryAsync(String query, Consumer<List<DocumentCollectionEntity>> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public void close() throws Exception {
    }
}
