/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */

package org.eclipse.jnosql.communication.document;

import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DocumentEntityTest {

    @Test
    void shouldReturnErrorWhenNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DocumentEntity.of(null));
    }

    @Test
    void shouldReturnErrorWhenDocumentsIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DocumentEntity.of("entity", null));
    }

    @Test
    void shouldReturnOneDocument() {
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
    void shouldDoCopy() {
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(Document.of("name", "name")));
        DocumentEntity copy = entity.copy();
        assertNotSame(entity, copy);
        assertEquals(entity, copy);

    }

    @Test
    void shouldFindDocument() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        Optional<Document> name = entity.find("name");
        Optional<Document> notfound = entity.find("not_found");
        assertTrue(name.isPresent());
        assertFalse(notfound.isPresent());
        assertEquals(document, name.get());
    }

    @Test
    void shouldReturnErrorWhenFindDocumentIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Document document = Document.of("name", "name");
            DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
            entity.find(null);
        });
    }

    @Test
    void shouldRemoveDocumentByName() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        assertTrue(entity.remove("name"));
        assertTrue(entity.isEmpty());
    }

    @Test
    void shouldConvertToMap() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        Map<String, Object> result = entity.toMap();
        assertFalse(result.isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(result.size()));
        assertEquals(document.name(), result.keySet().stream().findAny().get());

    }

    @Test
    void shouldConvertSubColumnToMap() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(Document.of("sub", document)));
        Map<String, Object> result = entity.toMap();
        assertFalse(result.isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(result.size()));
        Map<String, Object> map = (Map<String, Object>) result.get("sub");
        assertEquals("name", map.get("name"));
    }

    @Test
    void shouldConvertSubDocumentListToMap() {
        DocumentEntity entity = DocumentEntity.of("entity");
        entity.add(Document.of("_id", "id"));
        List<Document> documents = asList(Document.of("name", "Ada"), Document.of("type", "type"),
                Document.of("information", "ada@lovelace.com"));

        entity.add(Document.of("contacts", documents));
        Map<String, Object> result = entity.toMap();
        assertEquals("id", result.get("_id"));
        List<Map<String, Object>> contacts = (List<Map<String, Object>>) result.get("contacts");
        assertEquals(3, contacts.size());
        assertThat(contacts).contains(singletonMap("name", "Ada"), singletonMap("type", "type"),
                singletonMap("information", "ada@lovelace.com"));

    }

    @Test
    void shouldConvertSubDocumentListToMap2() {
        DocumentEntity entity = DocumentEntity.of("entity");
        entity.add(Document.of("_id", "id"));
        List<List<Document>> documents = new ArrayList<>();
        documents.add(asList(Document.of("name", "Ada"), Document.of("type", "type"),
                Document.of("information", "ada@lovelace.com")));

        entity.add(Document.of("contacts", documents));
        Map<String, Object> result = entity.toMap();
        assertEquals("id", result.get("_id"));
        List<List<Map<String, Object>>> contacts = (List<List<Map<String, Object>>>) result.get("contacts");
        assertEquals(1, contacts.size());
        List<Map<String, Object>> maps = contacts.get(0);
        assertEquals(3, maps.size());
        assertThat(maps).contains(singletonMap("name", "Ada"), singletonMap("type", "type"),
                singletonMap("information", "ada@lovelace.com"));

    }

    @Test
    void shouldShouldCreateANewInstance() {
        String name = "name";
        DocumentEntity entity = new DocumentEntity(name);
        assertEquals(name, entity.name());
    }

    @Test
    void shouldCreateAnEmptyEntity() {
        DocumentEntity entity = new DocumentEntity("name");
        assertTrue(entity.isEmpty());
    }

    @Test
    void shouldReturnAnErrorWhenAddANullDocument() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = new DocumentEntity("name");
            entity.add(null);
        });
    }

    @Test
    void shouldAddANewDocument() {
        DocumentEntity entity = new DocumentEntity("name");
        entity.add(Document.of("document", 12));
        assertFalse(entity.isEmpty());
        assertEquals(1, entity.size());
    }

    @Test
    void shouldReturnErrorWhenAddAnNullIterable() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = new DocumentEntity("name");
            entity.addAll(null);
        });
    }

    @Test
    void shouldAddAllDocuments() {
        DocumentEntity entity = new DocumentEntity("name");
        entity.addAll(asList(Document.of("name", 12), Document.of("value", "value")));
        assertFalse(entity.isEmpty());
        assertEquals(2, entity.size());
    }


    @Test
    void shouldNotFindDocument() {
        DocumentEntity entity = new DocumentEntity("name");
        Optional<Document> document = entity.find("name");
        assertFalse(document.isPresent());
    }

    @Test
    void shouldFindValue() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        Optional<String> name = entity.find("name", String.class);
        Assertions.assertNotNull(name);
        Assertions.assertTrue(name.isPresent());
        Assertions.assertEquals("name", name.orElse(""));
    }

    @Test
    void shouldNotFindValue() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        Optional<String> name = entity.find("not_found", String.class);
        Assertions.assertNotNull(name);
        Assertions.assertFalse(name.isPresent());
    }

    @Test
    void shouldFindTypeSupplier() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        List<String> names = entity.find("name", new TypeReference<List<String>>() {})
                .orElse(Collections.emptyList());
        Assertions.assertNotNull(names);
        assertFalse(names.isEmpty());
        assertThat(names).contains("name");
    }

    @Test
    void shouldNotFindTypeSupplier() {
        Document document = Document.of("name", "name");
        DocumentEntity entity = DocumentEntity.of("entity", singletonList(document));
        List<String> names = entity.find("not_find", new TypeReference<List<String>>() {})
                .orElse(Collections.emptyList());
        Assertions.assertNotNull(names);
        Assertions.assertTrue(names.isEmpty());
    }

    @Test
    void shouldRemoveByName() {
        DocumentEntity entity = new DocumentEntity("name");
        entity.add(Document.of("value", 32D));
        assertTrue(entity.remove("value"));
        assertTrue(entity.isEmpty());
    }

    @Test
    void shouldNotRemoveByName() {
        DocumentEntity entity = new DocumentEntity("name");
        entity.add(Document.of("value", 32D));

        assertFalse(entity.remove("value1"));
        assertFalse(entity.isEmpty());
    }

    @Test
    void shouldReturnErrorWhenRemoveByNameIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = new DocumentEntity("name");
            entity.remove(null);
        });
    }


    @Test
    void shouldAddDocumentAsNameAndObject() {
        DocumentEntity entity = new DocumentEntity("documentCollection");
        entity.add("name", 10);
        assertEquals(1, entity.size());
        Optional<Document> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    void shouldAddDocumentAsNameAndValue() {
        DocumentEntity entity = new DocumentEntity("documentCollection");
        entity.add("name", Value.of(10));
        assertEquals(1, entity.size());
        Optional<Document> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    void shouldReturnErrorWhenAddDocumentsObjectWhenHasNullObject() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = new DocumentEntity("documentCollection");
            entity.add("name", null);
        });
    }

    @Test
    void shouldReturnErrorWhenAddDocumentsObjectWhenHasNullDocumentName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = new DocumentEntity("documentCollection");
            entity.add(null, 10);
        });
    }

    @Test
    void shouldReturnErrorWhenAddDocumentsValueWhenHasNullDocumentName() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            DocumentEntity entity = new DocumentEntity("documentCollection");
            entity.add(null, Value.of(12));
        });
    }


    @Test
    void shouldAvoidDuplicatedDocument() {
        DocumentEntity entity = new DocumentEntity("documentCollection");
        entity.add("name", 10);
        entity.add("name", 13);
        assertEquals(1, entity.size());
        Optional<Document> document = entity.find("name");
        assertEquals(Document.of("name", 13), document.get());
    }

    @Test
    void shouldAvoidDuplicatedDocumentWhenAddList() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name", 13));
        DocumentEntity entity = new DocumentEntity("documentCollection");
        entity.addAll(documents);
        assertEquals(1, entity.size());
        assertEquals(1, DocumentEntity.of("documentCollection", documents).size());
    }

    @Test
    void shouldReturnsTheDocumentNames() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name2", 11),
                Document.of("name3", 12), Document.of("name4", 13),
                Document.of("name5", 14), Document.of("name5", 16));

        DocumentEntity collection = DocumentEntity.of("documentCollection", documents);
        assertThat(collection.getDocumentNames()).contains("name", "name2", "name3", "name4", "name5");

    }

    @Test
    void shouldReturnsTheDocumentValues() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name2", 11),
                Document.of("name3", 12), Document.of("name4", 13),
                Document.of("name5", 14), Document.of("name5", 16));

        DocumentEntity collection = DocumentEntity.of("documentCollection", documents);
        assertThat(collection.getValues()).contains(Value.of(10), Value.of(11), Value.of(12),
                Value.of(13), Value.of(16));
    }

    @Test
    void shouldReturnTrueWhenContainsElement() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name2", 11),
                Document.of("name3", 12), Document.of("name4", 13),
                Document.of("name5", 14), Document.of("name5", 16));

        DocumentEntity collection = DocumentEntity.of("documentCollection", documents);

        assertTrue(collection.contains("name"));
        assertTrue(collection.contains("name2"));
        assertTrue(collection.contains("name3"));
        assertTrue(collection.contains("name4"));
        assertTrue(collection.contains("name5"));
    }

    @Test
    void shouldReturnFalseWhenDoesNotContainElement() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name2", 11),
                Document.of("name3", 12), Document.of("name4", 13),
                Document.of("name5", 14), Document.of("name5", 16));

        DocumentEntity collection = DocumentEntity.of("documentCollection", documents);

        assertFalse(collection.contains("name6"));
        assertFalse(collection.contains("name7"));
        assertFalse(collection.contains("name8"));
        assertFalse(collection.contains("name9"));
        assertFalse(collection.contains("name10"));
    }

    @Test
    void shouldRemoveAllElementsWhenUseClearMethod() {
        List<Document> documents = asList(Document.of("name", 10), Document.of("name2", 11),
                Document.of("name3", 12), Document.of("name4", 13),
                Document.of("name5", 14), Document.of("name5", 16));

        DocumentEntity collection = DocumentEntity.of("documentCollection", documents);

        assertFalse(collection.isEmpty());
        collection.clear();
        assertTrue(collection.isEmpty());
    }

    @Test
    void shouldHashCode(){
        List<Document> documents = List.of(Document.of("name", 10));
        var collection = DocumentEntity.of("documentCollection", documents);
        var collection2 = DocumentEntity.of("documentCollection", documents);
        assertThat(collection.hashCode()).isEqualTo(collection2.hashCode());
    }

    @Test
    void shouldEquals(){
        List<Document> documents = List.of(Document.of("name", 10));
        var collection = DocumentEntity.of("documentCollection", documents);
        var collection2 = DocumentEntity.of("documentCollection", documents);
        assertThat(collection).isEqualTo(collection2);
    }

    @Test
    void shouldToString(){
        List<Document> documents = List.of(Document.of("name", 10));
        var collection = DocumentEntity.of("documentCollection", documents);
        assertThat(collection.toString()).isEqualTo("DefaultDocumentEntity{documents={name=DefaultDocument" +
                "[name=name, value=DefaultValue[value=10]]}, name='documentCollection'}");
    }

}