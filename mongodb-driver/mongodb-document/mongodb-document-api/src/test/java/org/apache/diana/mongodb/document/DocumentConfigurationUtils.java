package org.apache.diana.mongodb.document;


import org.apache.diana.api.document.DocumentCollectionManagerFactory;
import org.apache.diana.api.document.DocumentConfiguration;

public final class DocumentConfigurationUtils {

    private DocumentConfigurationUtils() {}

    public static DocumentCollectionManagerFactory getConfiguration() {
        DocumentConfiguration configuration = new MongoDBDocumentConfiguration();
        DocumentCollectionManagerFactory managerFactory = configuration.getManagerFactory();
        return managerFactory;
    }
}
