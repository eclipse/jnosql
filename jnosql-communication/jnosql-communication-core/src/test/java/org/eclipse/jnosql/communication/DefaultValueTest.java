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

package org.eclipse.jnosql.communication;


import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DefaultValueTest {


    @Test
    public void shouldReturnErrorWhenElementIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> Value.of(null));

    }

    @Test
    public void shouldReturnSameInstanceInGet() {
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
        assertThat(value.get(new TypeReference<List<String>>() {
        })).contains("10");
        assertThat(value.get(new TypeReference<List<Long>>() {
        })).contains(10L);
    }

    @Test
    public void shouldConvertToStream() {
        Long number = 10L;
        Value value = Value.of(number);

        assertThat(value.get(new TypeReference<Stream<String>>() {
        }).collect(Collectors.toList())).contains("10");
        assertThat(value.get(new TypeReference<Stream<Long>>() {
        }).collect(Collectors.toList())).contains(10L);
    }

    @Test
    public void shouldConvertToList() {
        Value value = Value.of(Arrays.asList(10, 20, 30));
        assertThat(value.get(new TypeReference<List<String>>() {
        })).contains("10", "20", "30");
        assertThat(value.get(new TypeReference<List<BigInteger>>() {
        })).contains(BigInteger.TEN, BigInteger.valueOf(20L), BigInteger.valueOf(30L));
    }

    @Test
    public void shouldConvertToSingletonSet() {
        Long number = 10L;
        Value value = Value.of(number);
        assertThat(value.get(new TypeReference<Set<String>>() {
        })).contains("10");
        assertThat(value.get(new TypeReference<List<Long>>() {
        })).contains(10L);
    }

    @Test
    public void shouldConvertToSet() {
        Value value = Value.of(Arrays.asList(10, 20, 30));
        assertThat(value.get(new TypeReference<Set<String>>() {
        })).contains("10", "20", "30");
        assertThat(value.get(new TypeReference<List<BigInteger>>() {
        })).contains(BigInteger.TEN, BigInteger.valueOf(20L), BigInteger.valueOf(30L));
    }

    @Test
    public void shouldConvertMap() {
        Map<String, Integer> map = Collections.singletonMap("ONE", 1);
        Value value = Value.of(map);

        Map<String, Integer> result = value.get(new TypeReference<>() {
        });
        assertThat(result.keySet()).contains("ONE");
        assertThat(result.values()).contains(1);
    }

    @Test
    public void shouldConvertKeyValueInsideMap() {
        Map<Integer, String> map = Collections.singletonMap(10, "1");
        Value value = Value.of(map);
        Map<String, Integer> result = value.get(new TypeReference<>() {
        });
        assertThat(result.keySet()).contains("10");
        assertThat(result.values()).contains(1);
    }

    @Test
    public void shouldConvertMapIgnoringKeyValue() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            Map<Integer, List<String>> map = Collections.singletonMap(10, Arrays.asList("1", "2", "3"));
            Value value = Value.of(map);
            Map<String, List<String>> result = value.get(new TypeReference<>() {
            });
            assertThat(result.keySet()).contains("10");
            assertThat(result.values()).contains(Arrays.asList("1", "2", "3"));
        });
    }

    @Test
    public void shouldReturnErrorWhenIsInstanceIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> Value.of(12).isInstanceOf(null));
    }

    @Test
    public void shouldIsInstanceOf() {
        Assertions.assertTrue(Value.of("12").isInstanceOf(String.class));
        Assertions.assertFalse(Value.of("12").isInstanceOf(Integer.class));
    }
}