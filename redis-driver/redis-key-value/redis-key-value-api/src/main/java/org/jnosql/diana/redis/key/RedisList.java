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
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

class RedisList<T> extends RedisCollection<T> implements List<T> {


    RedisList(Jedis jedis, Class<T> clazz, String keyWithNameSpace) {
        super(jedis, clazz, keyWithNameSpace);
    }

    @Override
    public int size() {
        return jedis.llen(keyWithNameSpace).intValue();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }


    @Override
    public ListIterator<T> listIterator() {
        return toArrayList().listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return toArrayList().listIterator(index);
    }

    @Override
    public Iterator<T> iterator() {
        return toArrayList().iterator();
    }

    @Override
    public boolean add(T e) {
        Objects.requireNonNull(e);
        int index = size();
        if (index == 0) {
            jedis.lpush(keyWithNameSpace, gson.toJson(e));
        } else {
            String previewValue = jedis.lindex(keyWithNameSpace, index - 1);
            jedis.linsert(keyWithNameSpace, LIST_POSITION.AFTER, previewValue,
                    gson.toJson(e));
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> elements) {
        Objects.requireNonNull(elements);
        for (T element : elements) {
            add(index++, element);
        }
        return true;
    }

    @Override
    public void clear() {
        jedis.del(keyWithNameSpace);
    }

    @Override
    public T get(int index) {
        return super.get(index);
    }

    @Override
    public T set(int index, T element) {
        Objects.requireNonNull(element);
        jedis.lset(keyWithNameSpace, index, gson.toJson(element));
        return element;
    }

    @Override
    public void add(int index, T element) {
        Objects.requireNonNull(element);
        String previewValue = jedis.lindex(keyWithNameSpace, index);
        if (StringUtils.isNotBlank(previewValue)) {
            jedis.linsert(keyWithNameSpace, LIST_POSITION.BEFORE, previewValue, gson.toJson(element));
        } else {
            add(element);
        }

    }

    @Override
    public T remove(int index) {
        return super.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return super.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        Objects.requireNonNull(o);
        String value = gson.toJson(o);
        for (int index = size(); index > 0; --index) {
            String findedValue = jedis.lindex(keyWithNameSpace, (long) index);
            if (value.equals(findedValue)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        List<T> subList = new ArrayList<>();
        List<String> elements = jedis.lrange(keyWithNameSpace, fromIndex, toIndex);
        for (String element : elements) {
            subList.add(gson.fromJson(element, clazz));
        }
        return subList;
    }


}
