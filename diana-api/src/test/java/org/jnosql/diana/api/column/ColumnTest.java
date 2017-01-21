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
package org.jnosql.diana.api.column;


import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.document.Document;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ColumnTest {



    private static final Value DEFAULT_VALUE = Value.of(12);

    @Test(expected = NullPointerException.class)
    public void shouldReturnNameWhenNameIsNull() {
        Column column = Column.of(null, DEFAULT_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnNameWhenValueIsNull() {
        Column column = Column.of("Name", null);
    }

    @Test
    public void shouldCreateAnDocumentInstance() {
        String name = "name";
        Column column = Column.of(name, DEFAULT_VALUE);
        assertNotNull(column);
        assertEquals(name, column.getName());
        assertEquals(DEFAULT_VALUE, column.getValue());
    }

    @Test
    public void shouldBeEquals() {
        assertEquals(Column.of("name", DEFAULT_VALUE), Column.of("name", DEFAULT_VALUE));
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
