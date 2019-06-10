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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jnosql.query.ArrayValue;
import org.jnosql.query.GetQuery;
import org.jnosql.query.GetQuerySupplier;
import org.jnosql.query.NumberValue;
import org.jnosql.query.StringValue;
import org.jnosql.query.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetSupplierTest {

    private GetQuerySupplier querySupplier = new AntlrGetQuerySupplier();

    @Test
    public void shouldReturnErrorWhenStringIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> querySupplier.apply(null));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get \"Diana\""})
    public void shouldReturnParserQuery(String query) {
        GetQuery getQuery = querySupplier.apply(query);
        List<Value<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        Value<?> key = keys.get(0);
        assertTrue(key instanceof StringValue);
        assertEquals("Diana", StringValue.class.cast(key).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get 12"})
    public void shouldReturnParserQuery1(String query) {
        GetQuery getQuery = querySupplier.apply(query);
        List<Value<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        Value<?> key = keys.get(0);
        assertTrue(key instanceof NumberValue);
        assertEquals(12L, NumberValue.class.cast(key).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get 12.12"})
    public void shouldReturnParserQuery2(String query) {
        GetQuery getQuery = querySupplier.apply(query);
        List<Value<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        Value<?> key = keys.get(0);
        assertTrue(key instanceof NumberValue);
        assertEquals(12.12, NumberValue.class.cast(key).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get -12"})
    public void shouldReturnParserQuery3(String query) {
        GetQuery getQuery = querySupplier.apply(query);
        List<Value<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        Value<?> key = keys.get(0);
        assertTrue(key instanceof NumberValue);
        assertEquals(-12L, NumberValue.class.cast(key).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get -12.12"})
    public void shouldReturnParserQuery4(String query) {
        GetQuery getQuery = querySupplier.apply(query);
        List<Value<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        Value<?> key = keys.get(0);
        assertTrue(key instanceof NumberValue);
        assertEquals(-12.12, NumberValue.class.cast(key).get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get {1,12}"})
    public void shouldReturnParserQuery5(String query) {
        GetQuery getQuery = querySupplier.apply(query);
        List<Value<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        Value<?> key = keys.get(0);
        assertTrue(key instanceof ArrayValue);
        Value<?>[] values = ArrayValue.class.cast(key).get();
        MatcherAssert.assertThat(Arrays.stream(values).map(Value::get).collect(Collectors.toList()), Matchers.contains(1L, 12L));
    }


}