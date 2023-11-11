/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query;

import org.eclipse.jnosql.communication.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeleteQueryTest {

    private Where where;

    @BeforeEach
    void setUp() {
        QueryCondition condition = new DefaultQueryCondition("name", Condition.EQUALS, BooleanQueryValue.TRUE);
        where = new Where(condition);
    }
    @Test
    void shouldDeleteQueryEquality() {
        DeleteQuery query1 = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), where);
        DeleteQuery query2 = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), where);
        DeleteQuery query3 = DeleteQuery.of("Entity2", Arrays.asList("field1", "field2"), where);

        // Test equality
        assertEquals(query1, query2);
        assertEquals(query1, query1);
        assertNotEquals(query1, query3);
        assertNotEquals(query1, "query3");
    }

    @Test
    void shouldDeleteQueryHashcode() {
        DeleteQuery query1 = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), where);
        DeleteQuery query2 = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), where);
        DeleteQuery query3 = DeleteQuery.of("Entity2", Arrays.asList("field1", "field2"), where);

        assertEquals(query1.hashCode(), query2.hashCode());
        assertNotEquals(query1.hashCode(), query3.hashCode());
    }

    @Test
    void shouldDeleteQueryToString() {
        DeleteQuery query = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), where);
        assertEquals("DeleteQuery{entity='Entity1', fields=[field1, field2], where=where name EQUALS BooleanQueryValue{value=true}}", query.toString());
    }

    @Test
    void shouldCreateDeleteQueryWithEmptyFields() {
        DeleteQuery query = DeleteQuery.of("Entity1", where);

        // Test that fields are empty
        assertTrue(query.fields().isEmpty());
    }

    @Test
    void shouldCreateDeleteQueryWithNullEntity() {
        // Test NullPointerException when entity is null
        assertThrows(NullPointerException.class, () -> DeleteQuery.of(null, Arrays.asList("field1", "field2"), where));
    }

    @Test
    void shouldCreateDeleteQueryWithNullFields() {
        // Test NullPointerException when fields is null
        assertThrows(NullPointerException.class, () -> DeleteQuery.of("Entity1", null, where));
    }

    @Test
    void shouldCreateDeleteQueryWithNullWhere() {
        // Test creating DeleteQuery with null Where
        DeleteQuery query = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), null);

        // Test that where is empty
        assertFalse(query.where().isPresent());
    }
}