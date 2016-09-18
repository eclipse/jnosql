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
package org.jnosql.diana.api.document;

import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;


public class DefaultDocumentEntityTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenNameIsNull() {
        DocumentEntity entity = new DefaultDocumentEntity(null);
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
    public void shouldFindDocument() {
        DocumentEntity entity = new DefaultDocumentEntity("name");
        entity.addAll(Arrays.asList(Document.of("name", 12), Document.of("value", "value")));
        Optional<Document> document = entity.find("name");
        assertTrue(document.isPresent());
        assertEquals(Document.of("name", 12), document.get());
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


}