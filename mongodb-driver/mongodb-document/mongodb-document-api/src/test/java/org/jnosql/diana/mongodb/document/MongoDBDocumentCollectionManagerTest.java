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

import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.document.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.jnosql.diana.mongodb.document.DocumentConfigurationUtils.getConfiguration;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;


public class MongoDBDocumentCollectionManagerTest {

    public static final String COLLECTION_NAME = "person";
    private DocumentCollectionManager entityManager;

    @Before
    public void setUp() {
        entityManager = getConfiguration().getDocumentEntityManager("database");
    }

    @Test
    public void shouldSave() {
        DocumentCollectionEntity entity = getEntity();
        DocumentCollectionEntity documentEntity = entityManager.save(entity);
        assertTrue(documentEntity.getDocuments().stream().map(Document::getName).anyMatch(s -> s.equals("_id")));
    }

    @Test
    public void shouldUpdateSave() {
        DocumentCollectionEntity entity = getEntity();
        DocumentCollectionEntity documentEntity = entityManager.save(entity);
        Document newField = Documents.of("newField", "10");
        entity.add(newField);
        DocumentCollectionEntity updated = entityManager.update(entity);
        assertEquals(newField, updated.find("newField").get());
    }

    @Test
    public void shouldRemoveEntity() {
        DocumentCollectionEntity documentEntity = entityManager.save(getEntity());
        DocumentQuery query = DocumentQuery.of(COLLECTION_NAME);
        Optional<Document> id = documentEntity.find("_id");
        query.addCondition(DocumentCondition.eq(id.get()));
        entityManager.delete(query);
        assertTrue(entityManager.find(query).isEmpty());
    }

    @Test
    public void shouldFindDocument() {
        DocumentCollectionEntity entity = entityManager.save(getEntity());
        DocumentQuery query = DocumentQuery.of(COLLECTION_NAME);
        Optional<Document> id = entity.find("_id");
        query.addCondition(DocumentCondition.eq(id.get()));
        List<DocumentCollectionEntity> entities = entityManager.find(query);
        assertFalse(entities.isEmpty());
        assertThat(entities, contains(entity));
    }

    @Test
    public void shouldSaveAsync() {
        DocumentCollectionEntity entity = getEntity();
        entityManager.saveAsync(entity);

    }

    @Test
    public void shouldUpdateAsync() {
        DocumentCollectionEntity entity = getEntity();
        DocumentCollectionEntity documentEntity = entityManager.save(entity);
        Document newField = Documents.of("newField", "10");
        entity.add(newField);
        entityManager.updateAsync(entity);
    }

    @Test
    public void shouldRemoveEntityAsync() {
        DocumentCollectionEntity documentEntity = entityManager.save(getEntity());
        DocumentQuery query = DocumentQuery.of(COLLECTION_NAME);
        Optional<Document> id = documentEntity.find("_id");
        query.addCondition(DocumentCondition.eq(id.get()));
        entityManager.deleteAsync(query);

    }

    @Test
    public void shouldSaveSubDocument() {
        DocumentCollectionEntity entity = getEntity();
        entity.add(Document.of("phones", Document.of("mobile", "1231231")));
        DocumentCollectionEntity entitySaved = entityManager.save(entity);
        Document id = entitySaved.find("_id").get();
        DocumentQuery query = DocumentQuery.of(COLLECTION_NAME);
        query.addCondition(DocumentCondition.eq(id));
        DocumentCollectionEntity entityFound = entityManager.find(query).get(0);
        Map<String, String> result = entityFound.find("phones").get().getValue().cast();
        String key = result.keySet().stream().findFirst().get();
        String value = result.get(key);
        assertEquals("mobile", key);
        assertEquals("1231231", value);
    }

    private DocumentCollectionEntity getEntity() {
        DocumentCollectionEntity entity = DocumentCollectionEntity.of(COLLECTION_NAME);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Poliana");
        map.put("city", "Salvador");
        List<Document> documents = Documents.of(map);
        documents.forEach(entity::add);
        return entity;
    }

}