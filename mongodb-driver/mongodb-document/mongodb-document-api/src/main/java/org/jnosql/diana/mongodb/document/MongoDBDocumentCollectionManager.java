/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.mongodb.document;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import java.util.List;
import java.util.function.Consumer;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jnosql.diana.api.ExecuteAsyncQueryException;
import org.jnosql.diana.api.TTL;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.WriterField;
import org.jnosql.diana.api.document.DocumentCollectionEntity;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentQuery;
import org.jnosql.diana.api.document.Documents;
import org.jnosql.diana.api.document.PreparedStatement;
import org.jnosql.diana.api.writer.WriterFieldDecorator;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class MongoDBDocumentCollectionManager implements DocumentCollectionManager {

    private final MongoDatabase mongoDatabase;

    private final com.mongodb.async.client.MongoDatabase asyncMongoDatabase;

    private final WriterField writerField = WriterFieldDecorator.getInstance();


    MongoDBDocumentCollectionManager(MongoDatabase mongoDatabase,
                                     com.mongodb.async.client.MongoDatabase asyncMongoDatabase) {
        this.mongoDatabase = mongoDatabase;
        this.asyncMongoDatabase = asyncMongoDatabase;
    }


    @Override
    public DocumentCollectionEntity save(DocumentCollectionEntity entity) {
        String collectionName = entity.getName();
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        Document document = getDocument(entity);
        collection.insertOne(document);
        boolean hasNotId = entity.getDocuments().stream()
                .map(document1 -> document1.getName()).noneMatch(k -> k.equals("_id"));
        if (hasNotId) {
            entity.add(Documents.of("_id", document.get("_id")));
        }
        return entity;
    }


    @Override
    public void saveAsync(DocumentCollectionEntity entity) {
        saveAsync(entity, (aVoid, throwable) -> {
        });
    }

    @Override
    public DocumentCollectionEntity save(DocumentCollectionEntity entity, TTL ttl) {
        throw new UnsupportedOperationException("MongoDB does not support save with TTL");
    }

    @Override
    public void saveAsync(DocumentCollectionEntity entity, TTL ttl) {
        throw new UnsupportedOperationException("MongoDB does not support saveAsync with TTL");
    }

    @Override
    public void saveAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack)
            throws ExecuteAsyncQueryException, UnsupportedOperationException {
        saveAsync(entity, (aVoid, throwable) -> callBack.accept(entity));
    }

    @Override
    public void saveAsync(DocumentCollectionEntity entity, TTL ttl, Consumer<DocumentCollectionEntity> callBack) {
        throw new UnsupportedOperationException("MongoDB does not support saveAsync with TTL");
    }


    @Override
    public DocumentCollectionEntity update(DocumentCollectionEntity entity) {
        DocumentCollectionEntity copy = entity.copy();
        String collectionName = entity.getName();
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        Document id = copy.find("_id")
                .map(d -> new Document(d.getName(), d.getValue().get()))
                .orElseThrow(() -> new UnsupportedOperationException("To update this DocumentCollectionEntity " +
                        "the field `id` is required"));
        copy.remove("_id");
        collection.findOneAndReplace(id, getDocument(entity));
        return entity;
    }

    @Override
    public void updateAsync(DocumentCollectionEntity entity)
            throws ExecuteAsyncQueryException, UnsupportedOperationException {
        updateAsync(entity, (d, throwable) -> {
        });
    }

    @Override
    public void updateAsync(DocumentCollectionEntity entity, Consumer<DocumentCollectionEntity> callBack)
            throws ExecuteAsyncQueryException, UnsupportedOperationException {
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
    public void deleteAsync(DocumentQuery query) {
        deleteAsync(query, (deleteResult, throwable) -> {
        });
    }

    @Override
    public void deleteAsync(DocumentQuery query, Consumer<Void> callBack)
            throws ExecuteAsyncQueryException, UnsupportedOperationException {
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
        return stream(documents.spliterator(), false).map(Documents::of)
                .map(ds -> DocumentCollectionEntity.of(collectionName, ds)).collect(toList());

    }

    @Override
    public void findAsync(DocumentQuery query, Consumer<List<DocumentCollectionEntity>> callBack)
            throws ExecuteAsyncQueryException, UnsupportedOperationException {

    }

    @Override
    public List<DocumentCollectionEntity> nativeQuery(String query) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("There is not support to native query yet");
    }

    @Override
    public void nativeQueryAsync(String query, Consumer<List<DocumentCollectionEntity>> callBack)
            throws ExecuteAsyncQueryException, UnsupportedOperationException {
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
        entity.getDocuments().stream().forEach(d -> document.append(d.getName(), convert(d.getValue())));
        return document;
    }

    private Object convert(Value value) {
        Object val = value.get();
        if (val instanceof org.jnosql.diana.api.document.Document) {
            org.jnosql.diana.api.document.Document subDocument = (org.jnosql.diana.api.document.Document) val;
            Object converted = convert(subDocument.getValue());
            return new Document(subDocument.getName(), converted);
        }
        if (writerField.isCompatible(val.getClass())) {
            return writerField.write(val);
        }
        return val;
    }

    private void saveAsync(DocumentCollectionEntity entity, SingleResultCallback<Void> callBack) {
        String collectionName = entity.getName();
        com.mongodb.async.client.MongoCollection<Document> collectionAsync =
                asyncMongoDatabase.getCollection(collectionName);
        Document document = getDocument(entity);
        collectionAsync.insertOne(document, callBack);
    }

    private void updateAsync(DocumentCollectionEntity entity, SingleResultCallback<Document> callBack) {
        String collectionName = entity.getName();
        com.mongodb.async.client.MongoCollection<Document> asyncCollection =
                asyncMongoDatabase.getCollection(collectionName);
        Document id = entity.find("_id").map(d -> new Document(d.getName(), d.getValue().get()))
                .orElseThrow(() -> new UnsupportedOperationException("To update this DocumentCollectionEntity " +
                        "the field `id` is required"));

        asyncCollection.findOneAndReplace(id, getDocument(entity), callBack);
    }

    private void deleteAsync(DocumentQuery query, SingleResultCallback<DeleteResult> callBack) {
        String collectionName = query.getCollection();
        com.mongodb.async.client.MongoCollection<Document> asyncCollection =
                asyncMongoDatabase.getCollection(collectionName);
        List<Bson> collect = query.getConditions().stream().map(DocumentQueryConversor::convert).collect(toList());
        asyncCollection.deleteMany(Filters.and(collect), callBack);
    }

}
