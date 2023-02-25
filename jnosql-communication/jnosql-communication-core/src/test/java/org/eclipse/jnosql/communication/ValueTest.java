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
 *   Maximillian Arruda
 *
 */
package org.eclipse.jnosql.communication;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class ValueTest {

    abstract Value getValueOf(Supplier<?> supplier);

    @Test
    void shouldReturnSameInstanceInGet() {
        AtomicInteger number = new AtomicInteger(5_000);
        Value value = getValueOf(() -> number);
        assertEquals(number, value.get());
    }

    @Test
    void shouldConvertType() {
        AtomicInteger number = new AtomicInteger(5_000);
        Value value = getValueOf(() -> number);
        assertEquals(Integer.valueOf(5_000), value.get(Integer.class));
        assertEquals("5000", value.get(String.class));
    }

    @Test
    void shouldConvertToSingletonList() {
        long number = 10L;
        Value value = getValueOf(() -> number);
        assertThat(value.get(new TypeReference<List<String>>() {
        })).contains("10");
        assertThat(value.get(new TypeReference<List<Long>>() {
        })).contains(10L);
    }

    @Test
    void shouldConvertToStream() {
        long number = 10L;
        Value value = getValueOf(() -> number);

        assertThat(value.get(new TypeReference<Stream<String>>() {
        }).collect(Collectors.toList())).contains("10");
        assertThat(value.get(new TypeReference<Stream<Long>>() {
        }).collect(Collectors.toList())).contains(10L);
    }

    @Test
    void shouldConvertToList() {
        Value value = getValueOf(() -> Arrays.asList(10, 20, 30));
        assertThat(value.get(new TypeReference<List<String>>() {
        })).contains("10", "20", "30");
        assertThat(value.get(new TypeReference<List<BigInteger>>() {
        })).contains(BigInteger.TEN, BigInteger.valueOf(20L), BigInteger.valueOf(30L));
    }

    @Test
    void shouldConvertToSingletonSet() {
        long number = 10L;
        Value value = getValueOf(() -> number);
        assertThat(value.get(new TypeReference<Set<String>>() {
        })).contains("10");
        assertThat(value.get(new TypeReference<List<Long>>() {
        })).contains(10L);
    }

    @Test
    void shouldConvertToSet() {
        Value value = getValueOf(() -> Arrays.asList(10, 20, 30));
        assertThat(value.get(new TypeReference<Set<String>>() {
        })).contains("10", "20", "30");
        assertThat(value.get(new TypeReference<List<BigInteger>>() {
        })).contains(BigInteger.TEN, BigInteger.valueOf(20L), BigInteger.valueOf(30L));
    }

    @Test
    void shouldConvertMap() {
        Map<String, Integer> map = Collections.singletonMap("ONE", 1);
        Value value = getValueOf(() -> map);

        Map<String, Integer> result = value.get(new TypeReference<>() {
        });
        assertThat(result.keySet()).contains("ONE");
        assertThat(result.values()).contains(1);
    }

    @Test
    void shouldConvertKeyValueInsideMap() {
        Map<Integer, String> map = Collections.singletonMap(10, "1");
        Value value = getValueOf(() -> map);
        Map<String, Integer> result = value.get(new TypeReference<>() {
        });
        assertThat(result.keySet()).contains("10");
        assertThat(result.values()).contains(1);
    }

    @Test
    void shouldConvertMapIgnoringKeyValue() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            Map<Integer, List<String>> map = Collections.singletonMap(10, Arrays.asList("1", "2", "3"));
            Value value = getValueOf(() -> map);
            Map<String, List<String>> result = value.get(new TypeReference<>() {
            });
            assertThat(result.keySet()).contains("10");
            assertThat(result.values()).contains(Arrays.asList("1", "2", "3"));
        });
    }

    @Test
    void shouldReturnErrorWhenIsInstanceIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> getValueOf(() -> 12).isInstanceOf(null));
    }

    @Test
    void shouldIsInstanceOf() {
        Value value = getValueOf(() -> "12");
        Assertions.assertTrue(value.isInstanceOf(String.class));
        Assertions.assertFalse(value.isInstanceOf(Integer.class));
    }

}
