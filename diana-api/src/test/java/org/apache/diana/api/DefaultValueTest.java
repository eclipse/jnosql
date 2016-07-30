/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.diana.api;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class DefaultValueTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenElementIsNull() {
        Value.of(null);
    }

    @Test
    public void shouldReturnSameInstanteInGet() {
        AtomicInteger number = new AtomicInteger(5_000);
        Value value = Value.of(number);
        assertEquals(number, value.get());
    }

    @Test
    public void shouldConvertType() {
        AtomicInteger number = new AtomicInteger(5_000);
        Value value = Value.of(number);
        assertEquals(Integer.valueOf(5_000), value.get(Integer.class));
        assertEquals("5000", value.get(String.class));
    }

    @Test
    public void shouldConvertToSingletonList() {
        Long number = 10L;
        Value value = Value.of(number);
        assertThat(value.getList(String.class), containsInAnyOrder("10"));
        assertThat(value.getList(Long.class), containsInAnyOrder(10L));
    }

    @Test
    public void shouldConvertToStream() {
        Long number = 10L;
        Value value = Value.of(number);

        assertThat(value.getStream(String.class).collect(Collectors.toList()), containsInAnyOrder("10"));
        assertThat(value.getStream(Long.class).collect(Collectors.toList()), containsInAnyOrder(10L));
    }

    @Test
    public void shouldConvertToList() {
        Value value = Value.of(Arrays.asList(10, 20, 30));
        assertThat(value.getList(String.class), containsInAnyOrder("10", "20", "30"));
        assertThat(value.getList(BigInteger.class), containsInAnyOrder(BigInteger.TEN, BigInteger.valueOf(20L), BigInteger.valueOf(30L)));
    }

    @Test
    public void shouldConvertToSingletonSet() {
        Long number = 10L;
        Value value = Value.of(number);
        assertThat(value.getSet(String.class), containsInAnyOrder("10"));
        assertThat(value.getSet(Long.class), containsInAnyOrder(10L));
    }

    @Test
    public void shouldConvertToSet() {
        Value value = Value.of(Arrays.asList(10, 20, 30));
        assertThat(value.getSet(String.class), containsInAnyOrder("10", "20", "30"));
        assertThat(value.getSet(BigInteger.class), containsInAnyOrder(BigInteger.TEN, BigInteger.valueOf(20L), BigInteger.valueOf(30L)));
    }

    @Test
    public void shouldConvertMap() {
        Map<String, Integer> map = Collections.singletonMap("ONE", 1);
        Value value = Value.of(map);
        Map<String, Integer> result = value.getMap(String.class, Integer.class);
        assertThat(result.keySet(), containsInAnyOrder("ONE"));
        assertThat(result.values(), containsInAnyOrder(1));
    }

    @Test
    public void shouldConvertKeyValueInsideMap() {
        Map<Integer, String> map = Collections.singletonMap(10, "1");
        Value value = Value.of(map);
        Map<String, Integer> result = value.getMap(String.class, Integer.class);
        assertThat(result.keySet(), containsInAnyOrder("10"));
        assertThat(result.values(), containsInAnyOrder(1));
    }

    @Test
    public void shouldConvertMapIgnoringKeyValue() {
        Map<Integer, List<String>> map = Collections.singletonMap(10, Arrays.asList("1", "2", "3"));
        Value value = Value.of(map);
        Map<String, List> result = value.getMap(String.class, List.class);
        assertThat(result.keySet(), containsInAnyOrder("10"));
        assertThat(result.values(), containsInAnyOrder(Arrays.asList("1", "2", "3")));
    }

    @Test
    public void shouldCast() {
        Map<Integer, List<String>> map = Collections.singletonMap(10, Arrays.asList("1", "2", "3"));
        Value value = Value.of(map);
        Map<String, List<String>> result = value.cast();
        assertEquals(map, result);
    }

}