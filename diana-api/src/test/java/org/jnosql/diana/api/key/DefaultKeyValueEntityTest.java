/*
 * Copyright 2017 Eclipse Foundation
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
package org.jnosql.diana.api.key;

import org.hamcrest.Matchers;
import org.jnosql.diana.api.TypeReference;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;


public class DefaultKeyValueEntityTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenKeyIsNull() {
        KeyValueEntity.of(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenValueIsNull() {
        KeyValueEntity.of("key", null);
    }

    @Test
    public void shouldCreateInstance() {
        KeyValueEntity<String> entity = KeyValueEntity.of("key", "value");
        assertNotNull(entity);
        assertEquals("key", entity.getKey());
        assertEquals("value", entity.get());
    }

    @Test
    public void shouldAliasOnValue() {
        String value = "10";
        KeyValueEntity<String> entity = KeyValueEntity.of("key", value);
        assertEquals(value, entity.get());
        assertEquals(Integer.valueOf(10), entity.get(Integer.class));
        assertThat(singletonList(10), Matchers.contains(entity.get(new TypeReference<List<Integer>>() {}).get(0)));
    }
}