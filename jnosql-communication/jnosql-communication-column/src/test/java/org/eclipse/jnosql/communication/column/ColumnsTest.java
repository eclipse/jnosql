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

package org.eclipse.jnosql.communication.column;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class ColumnsTest {

    @Test
    public void shouldCreateColumn() {
        Column column = Columns.of("name", "Ada");
        assertEquals("name", column.name());
        assertEquals("Ada", column.get());
    }

    @Test
    public void shouldCreateColumnsFromMap() {
        Map<String, String> map = singletonMap("name", "Ada");
        List<Column> columns = Columns.of(map);
        assertFalse(columns.isEmpty());
        assertThat(columns).contains(Column.of("name", "Ada"));
    }


    @Test
    public void shouldCreateRecursiveMap() {
        List<List<Map<String, String>>> list = new ArrayList<>();
        Map<String, String> map = singletonMap("mobile", "55 1234-4567");
        list.add(singletonList(map));

        List<Column> columns = Columns.of(singletonMap("contact", list));
        assertEquals(1, columns.size());
        Column column = columns.get(0);
        assertEquals("contact", column.name());
        List<List<Column>> result = (List<List<Column>>) column.get();
        assertEquals(Column.of("mobile", "55 1234-4567"), result.get(0).get(0));

    }
}