package org.apache.diana.mongodb.document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.apache.diana.api.document.DocumentCollectionManager;
import org.apache.diana.api.document.DocumentCollectionManagerFactory;

class MongoDBDocumentCollectionManagerFactory implements DocumentCollectionManagerFactory {

    private final MongoClient mongoClient;
    private final com.mongodb.async.client.MongoClient asyncMongoDatabase;

    MongoDBDocumentCollectionManagerFactory(MongoClient mongoClient, com.mongodb.async.client.MongoClient asyncMongoDatabase) {
        this.mongoClient = mongoClient;
        this.asyncMongoDatabase = asyncMongoDatabase;
    }

    @Override
    public DocumentCollectionManager getDocumentEntityManager(String database) {
        return new MongoDBDocumentCollectionManager(mongoClient.getDatabase(database), asyncMongoDatabase.getDatabase(database));
    }

    @Override
    public void close()  {
        mongoClient.close();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MongoDBDocumentCollectionManagerFactory{");
        sb.append("mongoClient=").append(mongoClient);
        sb.append('}');
        return sb.toString();
    }
}
