package org.apache.diana.api.document;


public interface DocumentCollectionManagerFactory extends AutoCloseable {

    DocumentCollectionManager getDocumentEntityManager(String database);
}
