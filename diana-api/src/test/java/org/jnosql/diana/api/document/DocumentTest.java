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

import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.Value;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class DocumentTest {


    private static final Value DEFAULT_VALUE = Value.of(12);

    @Test(expected = NullPointerException.class)
    public void shouldReturnNameWhenNameIsNull() {
       Document document = Document.of(null, DEFAULT_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnNameWhenValueIsNull() {
        Document document = Document.of("Name", null);
    }

    @Test
    public void shouldCreateAnDocumentInstance() {
        String name = "name";
        Document document = Document.of(name, DEFAULT_VALUE);
        assertNotNull(document);
        assertEquals(name, document.getName());
        assertEquals(DEFAULT_VALUE, document.getValue());
    }

    @Test
    public void shouldBeEquals() {
        assertEquals(Document.of("name", DEFAULT_VALUE), Document.of("name", DEFAULT_VALUE));
    }

    @Test
    public void shouldReturnGetObject() {
        Value value = Value.of("text");
        Document document = Document.of("name", value);
        assertEquals(value.get(), document.get());
    }

    @Test
    public void shouldReturnGetClass() {
        Value value = Value.of("text");
        Document document = Document.of("name", value);
        assertEquals(value.get(String.class), document.get(String.class));
    }


    @Test
    public void shouldReturnGetType() {
        Value value = Value.of("text");
        Document document = Document.of("name", value);
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>(){};
        assertEquals(value.get(typeReference), document.get(typeReference));
    }
}