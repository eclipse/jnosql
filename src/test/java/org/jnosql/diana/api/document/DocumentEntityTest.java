/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package org.jnosql.diana.api.document;

import org.jnosql.diana.api.Value;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;


public class DocumentEntityTest {

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenNameIsNull() {
        DocumentEntity.of(null);
    }
    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenDocumentsIsNull() {
        DocumentEntity.of("entity", null);
    }

    @Test
    public void shouldReturnOneDocument() {
        DocumentEntity entity = DocumentEntity.of("entity");
        assertEquals(Integer.valueOf(0), Integer.valueOf(entity.size()));
        assertTrue(entity.isEmpty());

        entity.add(Document.of("name", "name"));
        entity.add(Document.of("name2", Value.of("name2")));
        assertFalse(entity.isEmpty());
        assertEquals(Integer.valueOf(2), Integer.valueOf(entity.size()));
        assertFalse(DocumentEntity.of("entity", singletonList(Document.of("name", "name"))).isEmpty());
    }

    @Test
    public void shouldDoCopy() {
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(Document.of("name", "name")));
        DocumentEntity copy = entity.copy();
        assertFalse(entity == copy);
        assertEquals(entity, copy);

    }

    @Test
    public void shouldFindDocument() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        Optional<Document> name = entity.find("name");
        Optional<Document> notfound = entity.find("not_found");
        assertTrue(name.isPresent());
        assertFalse(notfound.isPresent());
        assertEquals(document, name.get());
    }

    @Test
    public void shouldRemoveDocumentByName() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        assertTrue(entity.remove("name"));
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldConvertToMap() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        Map<String, Object> result = entity.toMap();
        assertFalse(result.isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(result.size()));
        assertEquals(document.getName(), result.keySet().stream().findAny().get());

    }

    @Test
    public void shouldShouldCreateANewInsntace() {
        String name = "name";
        DocumentEntity entity = new DefaultDocumentEntity(name);
        assertEquals(name, entity.getName());
    }

    @Test
    public void shouldCreateAnEmptyEntity() {
        DocumentEntity entity = new DefaultDocumentEntity("name");
        assertTrue(entity.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnAnErrorWhenAddANullDocument() {
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.add(null);
    }

    @Test
    public void shouldAddANewDocument() {
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.add(Document.of("document", 12));
        assertFalse(entity.isEmpty());
        assertEquals(1, entity.size());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddAnNullIterable() {
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.addAll(null);
    }

    @Test
    public void shouldAddAllDocuments() {
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.addAll(Arrays.asList(Document.of("name", 12), Document.of("value", "value")));
        assertFalse(entity.isEmpty());
        assertEquals(2, entity.size());
    }


    @Test
    public void shouldNotFindDocument() {
        DocumentEntity entity = new DefaultDocumentEntity("name");
        Optional<Document> document = entity.find("name");
        assertFalse(document.isPresent());
    }

    @Test
    public void shouldRemoveByName() {
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.add(Document.of("value", 32D));
        assertTrue(entity.remove("value"));
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldNotRemoveByName() {
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.add(Document.of("value", 32D));

        assertFalse(entity.remove("value1"));
        assertFalse(entity.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenRemoveByNameIsNull() {
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.remove((String) null);
    }

    @Test
    public void shouldRemoveDocument() {
        Document document = Document.of("value", 32D);
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.add(document);
        assertTrue(entity.remove(document));
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldDontRemoveDocument() {
        Document document = Document.of("value", 32D);
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.add(Document.of("value", 31D));
        assertFalse(entity.remove(document));
        assertFalse(entity.isEmpty());
    }

}