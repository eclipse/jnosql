package org.apache.diana.mongodb.document;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.apache.diana.api.ExecuteAsyncQueryException;
import org.apache.diana.api.document.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

class MongoDBDocumentCollectionManager implements DocumentCollectionManager {

    private final MongoDatabase mongoDatabase;

    private final com.mongodb.async.client.MongoDatabase asyncMongoDatabase;


    MongoDBDocumentCollectionManager(MongoDatabase mongoDatabase, com.mongodb.async.client.MongoDatabase asyncMongoDatabase) {
        this.mongoDatabase = mongoDatabase;
        this.asyncMongoDatabase = asyncMongoDatabase;
    }


    @Override
    public DocumentCollectionEntity save(DocumentCollectionEntity entity) {
        String collectionName = entity.getName();
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        Document document = getDocument(entity);
        collection.insertOne(document);
        boolean hasNotId = entity.getDocuments().stream().map(document1 -> document1.getName()).noneMatch(k -> k.equals("_id"));
        if (hasNotId) {
            entity.add(Documents.of("_id", document.get("_id")));
        }
        return entity;
    }


    @Override
    public void saveAsync(DocumentCollectionEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        saveAsync(entity, (aVoid, throwable) -> {
        });
    }

    @Override
    public void saveAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        saveAsync(entity, (aVoid, throwable) -> callBack.accept(entity));
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
        updateAsync(entity, (d, throwable) -> {
        });
    }

    @Override
    public void updateAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        updateAsync(entity, (d, throwable) -> {
            callBack.accept(DocumentCollectionEntity.of(entity.getName(), Documents.of(d)));
        });
    }


    @Override
    public void delete(DocumentQuery query) {
        String collectionName = query.getCollection();
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        List<Bson> collect = query.getConditions().stream().map(DocumentQueryConversor::convert).collect(toList());
        DeleteResult deleteResult = collection.deleteMany(Filters.and(collect));
        System.out.println(deleteResult);
    }

    @Override
    public void deleteAsync(DocumentQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        deleteAsync(query, (deleteResult, throwable) -> {
        });
    }

    @Override
    public void deleteAsync(DocumentQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        deleteAsync(query, (deleteResult, throwable) -> callBack.accept(null));

    }


    @Override
    public List<DocumentCollectionEntity> find(DocumentQuery query) {
        String collectionName = query.getCollection();
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        List<Bson> collect = query.getConditions().stream().map(DocumentQueryConversor::convert).collect(toList());
        if (collect.isEmpty()) {
            return stream(collection.find().spliterator(), false).map(Documents::of)
                    .map(ds -> DocumentCollectionEntity.of(collectionName, ds)).collect(toList());
        }
        FindIterable<Document> documents = collection.find(Filters.and(collect));
        return stream(documents.spliterator(), false).map(Documents::of).map(ds -> DocumentCollectionEntity.of(collectionName, ds)).collect(toList());

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
    public void close() {

    }

    private Document getDocument(DocumentCollectionEntity entity) {
        Document document = new Document();
        entity.getDocuments().stream().forEach(d -> document.append(d.getName(), d.getValue().get()));
        return document;
    }

    private void saveAsync(DocumentCollectionEntity entity, SingleResultCallback<Void> callBack) {
        String collectionName = entity.getName();
        com.mongodb.async.client.MongoCollection<Document> collectionAsync = asyncMongoDatabase.getCollection(collectionName);
        Document document = getDocument(entity);
        collectionAsync.insertOne(document, callBack);
    }

    private void updateAsync(DocumentCollectionEntity entity, SingleResultCallback<Document> callBack) {
        String collectionName = entity.getName();
        com.mongodb.async.client.MongoCollection<Document> asyncCollection = asyncMongoDatabase.getCollection(collectionName);
        Document id = entity.find("_id").map(d -> new Document(d.getName(), d.getValue().get()))
                .orElseThrow(() -> new UnsupportedOperationException("To update this DocumentCollectionEntity the field `id` is required"));

        asyncCollection.findOneAndReplace(id, getDocument(entity), callBack);
    }

    private void deleteAsync(DocumentQuery query, SingleResultCallback<DeleteResult> callBack) {
        String collectionName = query.getCollection();
        com.mongodb.async.client.MongoCollection<Document> asyncCollection = asyncMongoDatabase.getCollection(collectionName);
        List<Bson> collect = query.getConditions().stream().map(DocumentQueryConversor::convert).collect(toList());
        asyncCollection.deleteMany(Filters.and(collect), callBack);
    }

}
