package org.apache.diana.mongodb.document;

import com.mongodb.MongoClient;
import org.apache.diana.api.document.DocumentCollectionManager;
import org.apache.diana.api.document.DocumentCollectionManagerFactory;

class MongoDBDocumentCollectionManagerFactory implements DocumentCollectionManagerFactory {

    private final MongoClient mongoClient;

    MongoDBDocumentCollectionManagerFactory(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public DocumentCollectionManager getDocumentEntityManager(String database) {
        return null;
    }

    @Override
    public void close() throws Exception {
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
