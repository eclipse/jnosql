package org.apache.diana.mongodb.document;

import org.apache.diana.api.document.Document;
import org.apache.diana.api.document.DocumentCollectionEntity;
import org.apache.diana.api.document.DocumentCollectionManager;
import org.apache.diana.api.document.Documents;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.diana.mongodb.document.DocumentConfigurationUtils.getConfiguration;
import static org.junit.Assert.assertTrue;


public class MongoDBDocumentCollectionManagerTest {

    private DocumentCollectionManager entityManager;

    @Before
    public void setUp() {
        entityManager = getConfiguration().getDocumentEntityManager("database");
    }

    @Test
    public void shouldSave() {
        DocumentCollectionEntity entity = DocumentCollectionEntity.of("person");
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Poliana");
        map.put("city", "Salvador");
        List<Document> documents = Documents.of(map);
        documents.forEach(entity::add);
        DocumentCollectionEntity documentEntity = entityManager.save(entity);
        assertTrue(documentEntity.getDocuments().stream().map(Document::getName).anyMatch(s -> s.equals("_id")));
    }

}