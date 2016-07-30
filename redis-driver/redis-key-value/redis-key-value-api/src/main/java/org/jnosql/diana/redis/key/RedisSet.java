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

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class RedisSet<T> extends RedisCollection<T> implements Set<T> {

    RedisSet(Jedis jedis, Class<T> clazz, String keyWithNameSpace) {
        super(jedis, clazz, keyWithNameSpace);
    }

    @Override
    public boolean add(T e) {
        Objects.requireNonNull(e);
        jedis.sadd(keyWithNameSpace, gson.toJson(e));
        return false;
    }

    @Override
    public void clear() {
        jedis.del(keyWithNameSpace);
    }

    @Override
    public int size() {
        return jedis.scard(keyWithNameSpace).intValue();
    }

    @Override
    protected int indexOf(Object o) {
        Objects.requireNonNull(o);
        String find = gson.toJson(o);
        Set<String> values = jedis.smembers(keyWithNameSpace);
        int index = 0;
        for (String value : values) {
            if (value.contains(find)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    protected T remove(int index) {
        T element = toArrayList().get(index);
        if (element == null) {
            return null;
        }
        remove(element);
        return element;
    }

    @Override
    public boolean remove(Object o) {
        if (!clazz.isInstance(o)) {
            throw new ClassCastException("The object required is " + clazz.getName());
        }
        String find = gson.toJson(o);
        Set<String> values = jedis.smembers(keyWithNameSpace);
        for (String value : values) {
            if (value.contains(find)) {
                jedis.srem(keyWithNameSpace, value);
                return true;
            }
        }
        return false;
    }

    @Override
    protected List<T> toArrayList() {
        Set<String> redisValues = jedis.smembers(keyWithNameSpace);
        List<T> list = new ArrayList<>();
        for (String redisValue : redisValues) {
            list.add(gson.fromJson(redisValue, clazz));
        }
        return list;
    }

}
