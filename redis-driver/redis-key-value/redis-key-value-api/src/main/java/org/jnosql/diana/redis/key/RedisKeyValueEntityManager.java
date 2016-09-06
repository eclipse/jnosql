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

package org.jnosql.diana.redis.key;


import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.jnosql.diana.api.TTL;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.key.BucketManager;
import org.jnosql.diana.api.key.KeyValue;
import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.jnosql.diana.redis.key.RedisUtils.createKeyWithNameSpace;

public class RedisKeyValueEntityManager implements BucketManager {

    private final String nameSpace;

    private final Gson gson;

    private final Jedis jedis;

    RedisKeyValueEntityManager(String nameSpace, Gson gson, Jedis jedis) {
        this.nameSpace = nameSpace;
        this.gson = gson;
        this.jedis = jedis;
    }

    @Override
    public <K, V> void put(K key, V value) throws NullPointerException {
        Objects.requireNonNull(value, "Value is required");
        Objects.requireNonNull(key, "key is required");
        String valideKey = createKeyWithNameSpace(key.toString(), nameSpace);
        jedis.set(valideKey, gson.toJson(value));
    }

    @Override
    public <K> void put(KeyValue<K> keyValue) throws NullPointerException {
        put(keyValue.getKey(), keyValue.getValue().get());
    }

    @Override
    public <K> void put(KeyValue<K> keyValue, TTL ttl) throws NullPointerException, UnsupportedOperationException {
        put(keyValue);
        String valideKey = createKeyWithNameSpace(keyValue.getKey().toString(), nameSpace);
        jedis.expire(valideKey, (int) ttl.toSeconds());
    }

    @Override
    public <K> void put(Iterable<KeyValue<K>> keyValues) throws NullPointerException {
        StreamSupport.stream(keyValues.spliterator(), false).forEach(this::put);
    }

    @Override
    public <K> void put(Iterable<KeyValue<K>> keyValues, TTL ttl) throws NullPointerException, UnsupportedOperationException {
        StreamSupport.stream(keyValues.spliterator(), false).forEach(this::put);
        StreamSupport.stream(keyValues.spliterator(), false).map(KeyValue::getKey)
                .map(k -> createKeyWithNameSpace(k.toString(), nameSpace))
                .forEach(k -> jedis.expire(k, (int) ttl.toSeconds()));
    }

    @Override
    public <K> Optional<Value> get(K key) throws NullPointerException {
        String value = jedis.get(createKeyWithNameSpace(key.toString(), nameSpace));
        if (StringUtils.isNotBlank(value)) {
            return Optional.of(RedisValue.of(gson, value));
        }
        return Optional.empty();
    }

    @Override
    public <K> Iterable<Value> get(Iterable<K> keys) throws NullPointerException {
        return StreamSupport.stream(keys.spliterator(), false)
                .map(k -> jedis.get(createKeyWithNameSpace(k.toString(), nameSpace)))
                .filter(StringUtils::isNotBlank).map(v -> RedisValue.of(gson, v)).collect(toList());
    }

    @Override
    public <K> void remove(K key) {
        jedis.del(createKeyWithNameSpace(key.toString(), nameSpace));
    }

    @Override
    public <K> void remove(Iterable<K> keys) {
        StreamSupport.stream(keys.spliterator(), false).forEach(this::remove);
    }

    @Override
    public void close() {
        jedis.close();
    }
}
