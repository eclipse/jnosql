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
package org.eclipse.jnosql.diana.document.query;


import jakarta.nosql.document.DocumentDeleteQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultDocumentnDeleteQueryTest {


    private DocumentDeleteQuery query;


    @BeforeEach
    public void setUp() {
        query = DocumentDeleteQuery.delete().from("columnFamily").build();
    }

    @Test
    public void shouldNotEditColumns() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            List<String> documents = query.getDocuments();
            assertTrue(documents.isEmpty());
            documents.clear();
        });
    }
}
