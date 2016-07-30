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
import com.google.gson.reflect.TypeToken;
import org.jnosql.diana.api.Value;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

final class RedisValue implements Value {

    private final Gson gson;

    private final String json;

    private RedisValue(Gson gson, String json) {
        this.gson = gson;
        this.json = json;
    }

    public static Value of(Gson gson, String json) {
        return new RedisValue(gson, json);
    }

    @Override
    public Object get() {
        return json;
    }

    @Override
    public <T> T cast() throws ClassCastException {
        return (T) json;
    }

    @Override
    public <T> T get(Class<T> clazz) throws NullPointerException, UnsupportedOperationException {
        return gson.fromJson(json, clazz);
    }

    @Override
    public <T> List<T> getList(Class<T> clazz) throws NullPointerException, UnsupportedOperationException {
        Type type = new TypeToken<ArrayList<T>>() { }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public <T> Set<T> getSet(Class<T> clazz) throws NullPointerException, UnsupportedOperationException {
        Type type = new TypeToken<HashSet<T>>() { }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public <K, V> Map<K, V> getMap(Class<K> keyClass, Class<V> valueClass) throws NullPointerException,
            UnsupportedOperationException {
        Type type = new TypeToken<Map<K, V>>() { }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedisValue that = (RedisValue) o;
        return Objects.equals(json, that.json);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(json);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RedisValue{");
        sb.append("gson=").append(gson);
        sb.append(", json='").append(json).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
