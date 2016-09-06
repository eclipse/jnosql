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

import com.hazelcast.core.IMap;
import org.jnosql.diana.api.TTL;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.key.BucketManager;
import org.jnosql.diana.api.key.KeyValue;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class HazelCastKeyValueEntityManager implements BucketManager {

    private final IMap map;

    HazelCastKeyValueEntityManager(IMap map) {
        this.map = map;
    }

    @Override
    public <K, V> void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public <K> void put(KeyValue<K> keyValue) throws NullPointerException {
        map.put(keyValue.getKey(), keyValue.getValue().get());
    }

    @Override
    public <K> void put(KeyValue<K> keyValue, TTL ttl) {
        map.put(keyValue.getKey(), keyValue.getValue().get(), ttl.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public <K> void put(Iterable<KeyValue<K>> keyValues) throws NullPointerException {
        StreamSupport.stream(keyValues.spliterator(), false).forEach(this::put);
    }

    @Override
    public <K> void put(Iterable<KeyValue<K>> keyValues, TTL ttl) throws NullPointerException, UnsupportedOperationException {
        StreamSupport.stream(keyValues.spliterator(), false).forEach(kv -> this.put(kv, ttl));
    }

    @Override
    public <K> Optional<Value> get(K key) throws NullPointerException {
        Object value = map.get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(Value.of(value));
    }

    @Override
    public <K> Iterable<Value> get(Iterable<K> keys) throws NullPointerException {
        return StreamSupport.stream(keys.spliterator(), false).map(k -> map.get(k)).filter(Objects::nonNull)
                .map(Value::of).collect(Collectors.toList());
    }

    @Override
    public <K> void remove(K key) {
        map.remove(key);
    }

    @Override
    public <K> void remove(Iterable<K> keys) {
        StreamSupport.stream(keys.spliterator(), false).forEach(this::remove);
    }

    @Override
    public void close() {
    }
}
