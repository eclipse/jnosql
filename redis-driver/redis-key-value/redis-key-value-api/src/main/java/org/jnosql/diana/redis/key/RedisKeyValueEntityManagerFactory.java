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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import org.jnosql.diana.api.key.BucketManagerFactory;
import redis.clients.jedis.JedisPool;

public class RedisKeyValueEntityManagerFactory implements BucketManagerFactory<RedisKeyValueEntityManager> {

    private final JedisPool jedisPool;

    RedisKeyValueEntityManagerFactory(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    @Override
    public RedisKeyValueEntityManager getBucketManager(String bucketName) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        return new RedisKeyValueEntityManager(bucketName, new Gson(), jedisPool.getResource());
    }

    @Override
    public <T> List<T> getList(String bucketName, Class<T> clazz) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(clazz, "Class type is required");
        return new RedisList<T>(jedisPool.getResource(), clazz, bucketName);
    }

    @Override
    public <T> Set<T> getSet(String bucketName, Class<T> clazz) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(clazz, "Class type is required");
        return new RedisSet<T>(jedisPool.getResource(), clazz, bucketName);
    }

    @Override
    public <T> Queue<T> getQueue(String bucketName, Class<T> clazz) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(clazz, "Class type is required");
        return new RedisQueue<T>(jedisPool.getResource(), clazz, bucketName);
    }

    @Override
    public <K, V> Map<K, V> getMap(String bucketName, Class<K> keyValue, Class<V> valueValue) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(valueValue, "Class type is required");
        return new RedisMap<>(jedisPool.getResource(), keyValue, valueValue, bucketName);
    }

    @Override
    public void close() {
        jedisPool.close();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RedisKeyValueEntityManagerFactory{");
        sb.append("jedisPool=").append(jedisPool);
        sb.append('}');
        return sb.toString();
    }
}
