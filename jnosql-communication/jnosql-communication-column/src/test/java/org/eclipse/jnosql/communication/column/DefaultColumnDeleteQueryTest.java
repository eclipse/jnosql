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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DefaultColumnDeleteQueryTest {

    private ColumnDeleteQuery query;


    @BeforeEach
    public void setUp() {
        query = ColumnDeleteQuery.delete().from("columnFamily").build();
    }

    @Test
    void shouldNotEditColumns() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            List<String> columns = query.columns();
            assertTrue(columns.isEmpty());
            columns.clear();
        });
    }

    @Test
    void shouldHasCode(){
        var query =ColumnDeleteQuery.delete().from("columnFamily").build();
        var query1 =ColumnDeleteQuery.delete().from("columnFamily").build();
        assertThat(query.hashCode()).isEqualTo(query1.hashCode());
    }

    @Test
    void shouldEquals(){
        var query =ColumnDeleteQuery.delete().from("columnFamily").build();
        var query1 =ColumnDeleteQuery.delete().from("columnFamily").build();
        assertThat(query).isEqualTo(query1);
    }
}