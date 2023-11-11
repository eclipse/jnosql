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
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeleteQueryTest {

    private Where where;

    void setUp() {
        Condition condition = Condition.EQUALS;
        QueryCondition queryCondition = new QueryCondition() {
            @Override
            public String name() {
                return "name";
            }

            @Override
            public Condition condition() {
                return condition;
            }

            @Override
            public QueryValue<?> value() {
                return QueryValue.of("Ada");
            }
        };
        where = new Where(queryCondition);
    }
    @Test
    void testDeleteQueryEquality() {
        DeleteQuery query1 = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), new Where(null));
        DeleteQuery query2 = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), Where.of("condition"));
        DeleteQuery query3 = DeleteQuery.of("Entity2", Arrays.asList("field1", "field2"), Where.of("condition"));

        // Test equality
        assertEquals(query1, query2);
        assertNotEquals(query1, query3);
    }

    @Test
    void testDeleteQueryHashcode() {
        DeleteQuery query1 = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), Where.of("condition"));
        DeleteQuery query2 = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), Where.of("condition"));
        DeleteQuery query3 = DeleteQuery.of("Entity2", Arrays.asList("field1", "field2"), Where.of("condition"));

        // Test hashcode
        assertEquals(query1.hashCode(), query2.hashCode());
        assertNotEquals(query1.hashCode(), query3.hashCode());
    }

    @Test
    void testDeleteQueryToString() {
        DeleteQuery query = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), Where.of("condition"));

        // Test toString
        assertEquals("DeleteQuery{entity='Entity1', fields=[field1, field2], where=Where{condition='condition'}}", query.toString());
    }

    @Test
    void testCreateDeleteQueryWithEmptyFields() {
        DeleteQuery query = DeleteQuery.of("Entity1", Where.of("condition"));

        // Test that fields are empty
        assertTrue(query.fields().isEmpty());
    }

    @Test
    void testCreateDeleteQueryWithNullEntity() {
        // Test NullPointerException when entity is null
        assertThrows(NullPointerException.class, () -> DeleteQuery.of(null, Arrays.asList("field1", "field2"), Where.of("condition")));
    }

    @Test
    void testCreateDeleteQueryWithNullFields() {
        // Test NullPointerException when fields is null
        assertThrows(NullPointerException.class, () -> DeleteQuery.of("Entity1", null, Where.of("condition")));
    }

    @Test
    void testCreateDeleteQueryWithNullWhere() {
        // Test creating DeleteQuery with null Where
        DeleteQuery query = DeleteQuery.of("Entity1", Arrays.asList("field1", "field2"), null);

        // Test that where is empty
        assertFalse(query.where().isPresent());
    }
}