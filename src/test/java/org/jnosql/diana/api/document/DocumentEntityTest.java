/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
    public void shouldShouldCreateANewInstance() {
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
    public void shouldDoNotRemoveDocument() {
        Document document = Document.of("value", 32D);
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.add(Document.of("value", 31D));
        assertFalse(entity.remove(document));
        assertFalse(entity.isEmpty());
    }

    @Test
    public void shouldAddDocumentAsNameAndObject(){
        DocumentEntity entity = new DefaultDocumentEntity("documentCollection");
        entity.add("name", 10);
        assertEquals(1, entity.size());
        Optional<Document> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test
    public void shouldAddDocumentAsNameAndValue(){
        DocumentEntity entity = new DefaultDocumentEntity("documentCollection");
        entity.add("name", Value.of(10));
        assertEquals(1, entity.size());
        Optional<Document> name = entity.find("name");
        assertTrue(name.isPresent());
        assertEquals(10, name.get().get());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddDocumentasObjectWhenHasNullObject() {
        DocumentEntity entity = new DefaultDocumentEntity("documentCollection");
        entity.add("name", null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddDocumentasObjectWhenHasNullDocumentName() {
        DocumentEntity entity = new DefaultDocumentEntity("documentCollection");
        entity.add(null, 10);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddDocumentasValueWhenHasNullDocumentName() {
        DocumentEntity entity = new DefaultDocumentEntity("documentCollection");
        entity.add(null, Value.of(12));
    }
git 
    //should avoid duplication names (list, a document, both element ways
    //should add using key value, should return NPE when is null
    //should add using key, object, should return NPE when there is any null
    //should returns getKeys
    //should returns getValues
    //should returns true when contains
    //should returns false when does not contains
    //should remove all elements in the document entity


}