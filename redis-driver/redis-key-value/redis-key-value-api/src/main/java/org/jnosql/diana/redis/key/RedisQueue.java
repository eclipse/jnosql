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

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;

class RedisQueue<T> extends RedisCollection<T> implements Queue<T> {

    RedisQueue(Jedis jedis, Class<T> clazz, String keyWithNameSpace) {
        super(jedis, clazz, keyWithNameSpace);
    }

    @Override
    public void clear() {
        jedis.del(keyWithNameSpace);
    }

    @Override
    public boolean add(T e) {
        Objects.requireNonNull(e);
        jedis.rpush(keyWithNameSpace, gson.toJson(e));
        return true;
    }

    @Override
    public boolean offer(T e) {
        return add(e);
    }

    @Override
    public T remove() {
        T value = poll();
        if (value == null) {
            throw new NoSuchElementException("No element in Redis Queue");
        }
        return value;
    }

    @Override
    public T poll() {
        String value = jedis.lpop(keyWithNameSpace);
        if (StringUtils.isNoneBlank(value)) {
            return gson.fromJson(value, clazz);
        }
        return null;
    }

    @Override
    public T element() {
        T value = peek();
        if (value == null) {
            throw new NoSuchElementException("No element in Redis Queue");
        }
        return value;
    }

    @Override
    public T peek() {
        int index = size();
        if (index == 0) {
            return null;
        }
        return gson.fromJson(jedis.lindex(keyWithNameSpace, (long) index - 1), clazz);
    }

}
