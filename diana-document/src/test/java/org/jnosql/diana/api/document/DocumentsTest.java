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

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class DocumentsTest {
    @Test
    public void shouldCreateDocument() {
        Document column = Documents.of("name", "Ada");
        assertEquals("name", column.getName());
        assertEquals("Ada", column.get());
    }

    @Test
    public void shouldCreateColumnsFromMap() {
        Map<String, String> map = singletonMap("name", "Ada");
        List<Document> documents = Documents.of(map);
        assertFalse(documents.isEmpty());
        assertThat(documents, contains(Document.of("name", "Ada")));
    }

    @Test
    public void shouldCreateRecursiveMap() {
        List<List<Map<String, String>>> list = new ArrayList<>();
        Map<String, String> map = singletonMap("mobile", "55 1234-4567");
        list.add(singletonList(map));

        List<Document> documents = Documents.of(singletonMap("contact", list));
        assertEquals(1, documents.size());
        Document document = documents.get(0);
        assertEquals("contact", document.getName());
        List<List<Document>> result = (List<List<Document>>) document.get();
        assertEquals(Document.of("mobile", "55 1234-4567"), result.get(0).get(0));

    }
}