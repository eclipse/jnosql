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
package org.eclipse.jnosql.communication.keyvalue;

import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class KeyValueEntityTest {

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
        assertEquals("key", entity.key());
        assertEquals("value", entity.value());
    }

    @Test
    public void shouldAliasOnValue() {
        String value = "10";
        KeyValueEntity entity = KeyValueEntity.of("key", value);
        assertEquals(value, entity.value());
        assertEquals(Integer.valueOf(10), entity.value(Integer.class));
        assertThat(singletonList(10)).contains(entity.value(new TypeReference<List<Integer>>() {
        }).get(0));
    }

    @Test
    public void shouldGetValue() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("key", value);
        assertNotNull(entity);
        assertEquals("value", entity.value());
    }


    @Test
    public void shouldGetKeyClass() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("10", value);
        assertNotNull(entity);
        assertEquals(Long.valueOf(10L), entity.key(Long.class));
    }


    @Test
    public void shouldReturnErrorWhenGetKeyClassIsNull() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("10", value);
        assertNotNull(entity);
        Assertions.assertThrows(NullPointerException.class, () -> entity.key((Class<Object>) null));
    }


    @Test
    public void shouldGetKeyValueSupplier() {
        String value = "10";
        KeyValueEntity entity = KeyValueEntity.of(value, value);
        assertEquals(value, entity.value());
        assertEquals(Integer.valueOf(10), entity.key(Integer.class));
        assertThat(singletonList(10)).contains(entity.value(new TypeReference<List<Integer>>() {
        }).get(0));
    }

    @Test
    public void shouldReturnErrorWhenGetKeySupplierIsNull() {
        Value value = Value.of("value");
        KeyValueEntity entity = KeyValueEntity.of("10", value);
        assertNotNull(entity);
        Assertions.assertThrows(NullPointerException.class, () -> entity.key((TypeReference<Object>) null));
    }

}