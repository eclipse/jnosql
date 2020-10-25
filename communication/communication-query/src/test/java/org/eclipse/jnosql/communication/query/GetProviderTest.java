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

package org.eclipse.jnosql.communication.query;

import jakarta.nosql.query.ArrayQueryValue;
import jakarta.nosql.query.GetQuery;
import jakarta.nosql.query.GetQuery.GetQueryProvider;
import jakarta.nosql.query.NumberQueryValue;
import jakarta.nosql.query.QueryValue;
import jakarta.nosql.query.StringQueryValue;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetProviderTest {

    private GetQueryProvider queryProvider = new AntlrGetQueryProvider();

    @Test
    public void shouldReturnErrorWhenStringIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> queryProvider.apply(null));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get \"Diana\""})
    public void shouldReturnParserQuery(String query) {
        GetQuery getQuery = queryProvider.apply(query);
        List<QueryValue<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        QueryValue<?> key = keys.get(0);
        assertTrue(key instanceof StringQueryValue);
        assertEquals("Diana", StringQueryValue.class.cast(key).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get 12"})
    public void shouldReturnParserQuery1(String query) {
        GetQuery getQuery = queryProvider.apply(query);
        List<QueryValue<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        QueryValue<?> key = keys.get(0);
        assertTrue(key instanceof NumberQueryValue);
        assertEquals(12L, NumberQueryValue.class.cast(key).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get 12.12"})
    public void shouldReturnParserQuery2(String query) {
        GetQuery getQuery = queryProvider.apply(query);
        List<QueryValue<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        QueryValue<?> key = keys.get(0);
        assertTrue(key instanceof NumberQueryValue);
        assertEquals(12.12, NumberQueryValue.class.cast(key).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get -12"})
    public void shouldReturnParserQuery3(String query) {
        GetQuery getQuery = queryProvider.apply(query);
        List<QueryValue<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        QueryValue<?> key = keys.get(0);
        assertTrue(key instanceof NumberQueryValue);
        assertEquals(-12L, NumberQueryValue.class.cast(key).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get -12.12"})
    public void shouldReturnParserQuery4(String query) {
        GetQuery getQuery = queryProvider.apply(query);
        List<QueryValue<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        QueryValue<?> key = keys.get(0);
        assertTrue(key instanceof NumberQueryValue);
        assertEquals(-12.12, NumberQueryValue.class.cast(key).get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get {1,12}"})
    public void shouldReturnParserQuery5(String query) {
        GetQuery getQuery = queryProvider.apply(query);
        List<QueryValue<?>> keys = getQuery.getKeys();
        assertEquals(1, keys.size());
        QueryValue<?> key = keys.get(0);
        assertTrue(key instanceof ArrayQueryValue);
        QueryValue<?>[] values = ArrayQueryValue.class.cast(key).get();
        MatcherAssert.assertThat(Arrays.stream(values).map(QueryValue::get)
                .collect(Collectors.toList()), Matchers.contains(1L, 12L));
    }


}