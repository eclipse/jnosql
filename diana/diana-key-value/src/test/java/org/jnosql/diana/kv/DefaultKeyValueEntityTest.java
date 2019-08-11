/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.diana.kv;

import jakarta.nosql.Value;
import jakarta.nosql.kv.KeyValueEntity;
import org.hamcrest.Matchers;
import jakarta.nosql.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DefaultKeyValueEntityTest {


    @Test
    public void shouldReturnErrorWhenKeyIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> KeyValueEntity.of(null, "value"));
    }

    @Test
    public void shouldReturnErrorWhenValueIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> KeyValueEntity.of("key", null));
    }

    @Test
    public void shouldCreateInstance() {
        KeyValueEntity entity = KeyValueEntity.of("key", "value");
        assertNotNull(entity);
        assertEquals("key", entity.getKey());
        assertEquals("value", entity.getValue());
    }

    @Test
    public void shouldAliasOnValue() {
        String value = "10";
        KeyValueEntity entity = KeyValueEntity.of("key", value);
        assertEquals(value, entity.getValue());
        assertEquals(Integer.valueOf(10), entity.getValue(Integer.class));
        assertThat(singletonList(10), Matchers.contains(entity.getValue(new TypeReference<List<Integer>>() {
        }).get(0)));
    }

    @Test
    public void shouldGetValue() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("key", value);
        assertNotNull(entity);
        assertEquals("value", entity.getValue());
    }


    @Test
    public void shouldGetKeyClass() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("10", value);
        assertNotNull(entity);
        assertEquals(Long.valueOf(10L), entity.getKey(Long.class));
    }


    @Test
    public void shouldReturnErrorWhenGetKeyClassIsNull() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("10", value);
        assertNotNull(entity);
        Assertions.assertThrows(NullPointerException.class, () -> entity.getKey((Class<Object>) null));
    }


    @Test
    public void shouldGetKeyValueSupplier() {
        String value = "10";
        KeyValueEntity entity = KeyValueEntity.of(value, value);
        assertEquals(value, entity.getValue());
        assertEquals(Integer.valueOf(10), entity.getKey(Integer.class));
        assertThat(singletonList(10), Matchers.contains(entity.getValue(new TypeReference<List<Integer>>() {
        }).get(0)));
    }

    @Test
    public void shouldReturnErrorWhenGetKeySupplierIsNull() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("10", value);
        assertNotNull(entity);
        Assertions.assertThrows(NullPointerException.class, () -> entity.getKey((TypeReference<Object>) null));
    }

}