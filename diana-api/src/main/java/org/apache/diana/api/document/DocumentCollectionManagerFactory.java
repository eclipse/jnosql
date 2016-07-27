package org.apache.diana.api.document;


import org.apache.diana.api.CloseResource;

/**
 * {@link DocumentCollectionManager} factory.
 * When the application has finished using the document collection manager factory, and/or at application shutdown, the application should close the column family manager factory.
 */
public interface DocumentCollectionManagerFactory extends CloseResource {

    /**
     * Creates a {@link DocumentCollectionManager} from database's name
     * @param database a database name
     * @return a new {@link DocumentCollectionManager} instance
     */
    DocumentCollectionManager getDocumentEntityManager(String database);
}
