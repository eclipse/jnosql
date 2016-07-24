package org.apache.diana.mongodb.document;

import org.apache.diana.api.document.DocumentCollectionManagerFactory;
import org.apache.diana.api.document.DocumentConfiguration;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;


public class MongoDBDocumentConfigurationTest {


    @Test
    public void shouldCreateDocumentCollectionManagerFactoryByMap() {
        Map<String, String> map  = new HashMap<>();
        map.put("mongodb-server-host-1", "172.17.0.2:27017");
        DocumentConfiguration configuration = new MongoDBDocumentConfiguration();
        DocumentCollectionManagerFactory managerFactory = configuration.getManagerFactory(map);
        assertNotNull(managerFactory);
    }

    @Test
    public void shouldCreateDocumentCollectionManagerFactoryByFile() {
        DocumentConfiguration configuration = new MongoDBDocumentConfiguration();
        DocumentCollectionManagerFactory managerFactory = configuration.getManagerFactory();
        assertNotNull(managerFactory);
    }
}