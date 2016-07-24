package org.apache.diana.mongodb.document;

import org.apache.diana.api.document.DocumentCollectionManager;
import org.apache.diana.api.document.DocumentCollectionManagerFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class MongoDBDocumentCollectionManagerFactoryTest {

    private DocumentCollectionManagerFactory managerFactory;

    @Before
    public void setUp() {
        managerFactory = DocumentConfigurationUtils.getConfiguration();
    }

    @Test
    public void shouldCreateEntityManager() {
        DocumentCollectionManager database = managerFactory.getDocumentEntityManager("database");
        assertNotNull(database);
    }
}