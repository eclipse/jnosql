/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.jnosql.aphrodite.antlr;

import org.jnosql.query.NumberValue;
import org.jnosql.query.ParamValue;
import org.jnosql.query.PutQuery;
import org.jnosql.query.PutQuerySupplier;
import org.jnosql.query.StringValue;
import org.jnosql.query.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PutSupplierTest {

    private PutQuerySupplier supplier = new AntlrPutQuerySupplier();

    @Test
    public void shouldReturnErrorWhenStringIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> supplier.apply(null));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"put {\"Ada\", \"Hunt\"}\n"})
    public void shouldReturnParserQuery(String query) {
        PutQuery putQuery = supplier.apply(query);
        Value<?> key = putQuery.getKey();
        Value<?> value = putQuery.getValue();
        Optional<Duration> ttl = putQuery.getTtl();

        assertTrue(key instanceof StringValue);
        assertEquals("Ada", StringValue.class.cast(key).get());
        assertFalse(ttl.isPresent());

        assertTrue(value instanceof StringValue);
        assertEquals("Hunt", StringValue.class.cast(value).get());

        assertFalse(ttl.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"put {12, \"Hunt\"}\n"})
    public void shouldReturnParserQuery1(String query) {
        PutQuery putQuery = supplier.apply(query);
        Value<?> key = putQuery.getKey();
        Value<?> value = putQuery.getValue();
        Optional<Duration> ttl = putQuery.getTtl();

        assertTrue(key instanceof NumberValue);
        assertEquals(12L, NumberValue.class.cast(key).get());

        assertTrue(value instanceof StringValue);
        assertEquals("Hunt", StringValue.class.cast(value).get());
        assertFalse(ttl.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"put {12, 12.12}\n"})
    public void shouldReturnParserQuery2(String query) {
        PutQuery putQuery = supplier.apply(query);
        Value<?> key = putQuery.getKey();
        Value<?> value = putQuery.getValue();
        Optional<Duration> ttl = putQuery.getTtl();

        assertTrue(key instanceof NumberValue);
        assertEquals(12L, NumberValue.class.cast(key).get());

        assertTrue(value instanceof NumberValue);
        assertEquals(12.12, NumberValue.class.cast(value).get());
        assertFalse(ttl.isPresent());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"put {@name, @value, 10 hour}\n"})
    public void shouldReturnParserQuery3(String query) {
        PutQuery putQuery = supplier.apply(query);
        Value<?> key = putQuery.getKey();
        Value<?> value = putQuery.getValue();
        Optional<Duration> ttl = putQuery.getTtl();

        assertTrue(key instanceof ParamValue);
        assertEquals("name", ParamValue.class.cast(key).get());

        assertTrue(value instanceof ParamValue);
        assertEquals("value", ParamValue.class.cast(value).get());
        assertTrue(ttl.isPresent());
        assertEquals(Duration.ofHours(10L), ttl.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"put {@name, @value, 10 minute}\n"})
    public void shouldReturnParserQuery4(String query) {
        PutQuery putQuery = supplier.apply(query);
        Value<?> key = putQuery.getKey();
        Value<?> value = putQuery.getValue();
        Optional<Duration> ttl = putQuery.getTtl();

        assertTrue(key instanceof ParamValue);
        assertEquals("name", ParamValue.class.cast(key).get());

        assertTrue(value instanceof ParamValue);
        assertEquals("value", ParamValue.class.cast(value).get());
        assertTrue(ttl.isPresent());
        assertEquals(Duration.ofMinutes(10L), ttl.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"put {@name, @value, 10 second}\n"})
    public void shouldReturnParserQuery5(String query) {
        PutQuery putQuery = supplier.apply(query);
        Value<?> key = putQuery.getKey();
        Value<?> value = putQuery.getValue();
        Optional<Duration> ttl = putQuery.getTtl();

        assertTrue(key instanceof ParamValue);
        assertEquals("name", ParamValue.class.cast(key).get());

        assertTrue(value instanceof ParamValue);
        assertEquals("value", ParamValue.class.cast(value).get());
        assertTrue(ttl.isPresent());
        assertEquals(Duration.ofSeconds(10L), ttl.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"put {@name, @value, 10 millisecond}\n"})
    public void shouldReturnParserQuery6(String query) {
        PutQuery putQuery = supplier.apply(query);
        Value<?> key = putQuery.getKey();
        Value<?> value = putQuery.getValue();
        Optional<Duration> ttl = putQuery.getTtl();

        assertTrue(key instanceof ParamValue);
        assertEquals("name", ParamValue.class.cast(key).get());

        assertTrue(value instanceof ParamValue);
        assertEquals("value", ParamValue.class.cast(value).get());
        assertTrue(ttl.isPresent());
        assertEquals(Duration.ofMillis(10L), ttl.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"put {@name, @value, 10 nanosecond}\n"})
    public void shouldReturnParserQuery7(String query) {
        PutQuery putQuery = supplier.apply(query);
        Value<?> key = putQuery.getKey();
        Value<?> value = putQuery.getValue();
        Optional<Duration> ttl = putQuery.getTtl();

        assertTrue(key instanceof ParamValue);
        assertEquals("name", ParamValue.class.cast(key).get());

        assertTrue(value instanceof ParamValue);
        assertEquals("value", ParamValue.class.cast(value).get());
        assertTrue(ttl.isPresent());
        assertEquals(Duration.ofNanos(10L), ttl.get());
    }

}