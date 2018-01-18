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
package org.jnosql.diana.api.column;


import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        Column column = Column.of("name", value);
        assertEquals(value.get(), column.get());
    }

    @Test
    public void shouldReturnGetClass() {
        Value value = Value.of("text");
        Column column = Column.of("name", value);
        assertEquals(value.get(String.class), column.get(String.class));
    }


    @Test
    public void shouldReturnGetType() {
        Value value = Value.of("text");
        Column column = Column.of("name", value);
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>(){};
        assertEquals(value.get(typeReference), column.get(typeReference));
    }
}
