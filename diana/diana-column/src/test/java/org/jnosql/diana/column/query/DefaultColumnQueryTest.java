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
package org.jnosql.diana.column.query;

import jakarta.nosql.Sort;
import jakarta.nosql.column.ColumnQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static jakarta.nosql.column.ColumnQuery.select;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DefaultColumnQueryTest {

    private ColumnQuery query;


    @BeforeEach
    public void setUp() {
        query = select().from("columnFamily").build();
    }


    @Test
    public void shouldNotRemoveColumns() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            List<String> columns = query.getColumns();
            assertTrue(columns.isEmpty());
            columns.clear();
        });
    }


    @Test
    public void shouldNotRemoveSort() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            List<Sort> sorts = query.getSorts();
            assertTrue(sorts.isEmpty());
            sorts.clear();
        });
    }
}