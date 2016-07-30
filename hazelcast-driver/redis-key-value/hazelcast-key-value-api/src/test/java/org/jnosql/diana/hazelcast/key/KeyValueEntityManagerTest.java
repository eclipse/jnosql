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

package org.jnosql.diana.hazelcast.key;


import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.key.BucketManager;
import org.jnosql.diana.api.key.BucketManagerFactory;
import org.jnosql.diana.api.key.KeyValue;
import org.jnosql.diana.hazelcast.key.model.User;
import org.jnosql.diana.hazelcast.key.util.KeyValueEntityManagerFactoryUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class KeyValueEntityManagerTest {

    private BucketManager keyValueEntityManager;

    private BucketManagerFactory keyValueEntityManagerFactory;

    private User userOtavio = new User("otavio");
    private KeyValue keyValueOtavio = KeyValue.of("otavio", Value.of(userOtavio));

    private User userSoro = new User("soro");
    private KeyValue keyValueSoro = KeyValue.of("soro", Value.of(userSoro));

    @Before
    public void init() {
        keyValueEntityManagerFactory = KeyValueEntityManagerFactoryUtils.get();
        keyValueEntityManager = keyValueEntityManagerFactory.getBucketManager("users-entity");
    }


    @Test
    public void shouldPutValue() {
        keyValueEntityManager.put("otavio", userOtavio);
        Optional<Value> otavio = keyValueEntityManager.get("otavio");
        assertTrue(otavio.isPresent());
        assertEquals(userOtavio, otavio.get().get(User.class));
    }

    @Test
    public void shouldPutKeyValue() {
        keyValueEntityManager.put(keyValueOtavio);
        Optional<Value> otavio = keyValueEntityManager.get("otavio");
        assertTrue(otavio.isPresent());
        assertEquals(userOtavio, otavio.get().get(User.class));
    }

    @Test
    public void shouldPutIterableKeyValue() {


        keyValueEntityManager.put(asList(keyValueSoro, keyValueOtavio));
        Optional<Value> otavio = keyValueEntityManager.get("otavio");
        assertTrue(otavio.isPresent());
        assertEquals(userOtavio, otavio.get().get(User.class));

        Optional<Value> soro = keyValueEntityManager.get("soro");
        assertTrue(soro.isPresent());
        assertEquals(userSoro, soro.get().get(User.class));
    }

    @Test
    public void shouldMultiGet() {
        User user = new User("otavio");
        KeyValue keyValue = KeyValue.of("otavio", Value.of(user));
        keyValueEntityManager.put(keyValue);
        assertNotNull(keyValueEntityManager.get("otavio"));


    }

    @Test
    public void shouldRemoveKey() {

        keyValueEntityManager.put(keyValueOtavio);
        assertTrue(keyValueEntityManager.get("otavio").isPresent());
        keyValueEntityManager.remove("otavio");
        assertFalse(keyValueEntityManager.get("otavio").isPresent());
    }

    @Test
    public void shouldRemoveMultiKey() {

        keyValueEntityManager.put(asList(keyValueSoro, keyValueOtavio));
        List<String> keys = asList("otavio", "soro");
        Iterable<Value> values = keyValueEntityManager.get(keys);
        assertThat(StreamSupport.stream(values.spliterator(), false).map(value -> value.get(User.class)).collect(Collectors.toList()), containsInAnyOrder(userOtavio, userSoro));
        keyValueEntityManager.remove(keys);
        Iterable<Value> users = values;
        assertEquals(0L, StreamSupport.stream(keyValueEntityManager.get(keys).spliterator(), false).count());
    }
}
