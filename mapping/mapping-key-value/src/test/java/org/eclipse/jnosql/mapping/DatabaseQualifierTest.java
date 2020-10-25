/*
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
 */
package org.eclipse.jnosql.mapping;

import org.eclipse.jnosql.mapping.DatabaseQualifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static jakarta.nosql.mapping.DatabaseType.COLUMN;
import static jakarta.nosql.mapping.DatabaseType.DOCUMENT;
import static jakarta.nosql.mapping.DatabaseType.GRAPH;
import static jakarta.nosql.mapping.DatabaseType.KEY_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DatabaseQualifierTest {

    @Test
    public void shouldReturnDefaultColumn() {
        DatabaseQualifier qualifier = DatabaseQualifier.ofColumn();
        assertEquals("", qualifier.provider());
        assertEquals(COLUMN, qualifier.value());
    }

    @Test
    public void shouldReturnColumnProvider() {
        String provider = "provider";
        DatabaseQualifier qualifier = DatabaseQualifier.ofColumn(provider);
        assertEquals(provider, qualifier.provider());
        assertEquals(COLUMN, qualifier.value());
    }

    @Test
    public void shouldReturnErrorWhenColumnNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DatabaseQualifier.ofColumn(null));
    }

    @Test
    public void shouldReturnDefaultDocument() {
        DatabaseQualifier qualifier = DatabaseQualifier.ofDocument();
        assertEquals("", qualifier.provider());
        assertEquals(DOCUMENT, qualifier.value());
    }

    @Test
    public void shouldReturnDocumentProvider() {
        String provider = "provider";
        DatabaseQualifier qualifier = DatabaseQualifier.ofDocument(provider);
        assertEquals(provider, qualifier.provider());
        assertEquals(DOCUMENT, qualifier.value());
    }

    @Test
    public void shouldReturnErrorWhenDocumentNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DatabaseQualifier.ofDocument(null));
    }

    @Test
    public void shouldReturnErrorWhenKeyValueNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DatabaseQualifier.ofKeyValue(null));
    }

    @Test
    public void shouldReturnKeyValueProvider() {
        String provider = "provider";
        DatabaseQualifier qualifier = DatabaseQualifier.ofKeyValue(provider);
        assertEquals(provider, qualifier.provider());
        assertEquals(KEY_VALUE, qualifier.value());
    }

    @Test
    public void shouldReturnDefaultKeyValue() {
        DatabaseQualifier qualifier = DatabaseQualifier.ofKeyValue();
        assertEquals("", qualifier.provider());
        assertEquals(KEY_VALUE, qualifier.value());
    }


    @Test
    public void shouldReturnErrorWhenGraphNull() {
        Assertions.assertThrows(NullPointerException.class, () -> DatabaseQualifier.ofGraph(null));
    }

    @Test
    public void shouldReturnGraphProvider() {
        String provider = "provider";
        DatabaseQualifier qualifier = DatabaseQualifier.ofGraph(provider);
        assertEquals(provider, qualifier.provider());
        assertEquals(GRAPH, qualifier.value());
    }

    @Test
    public void shouldReturnDefaultGraph() {
        DatabaseQualifier qualifier = DatabaseQualifier.ofGraph();
        assertEquals("", qualifier.provider());
        assertEquals(GRAPH, qualifier.value());
    }
}