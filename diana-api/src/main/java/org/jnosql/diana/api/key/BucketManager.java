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

package org.jnosql.diana.api.key;


import org.jnosql.diana.api.CloseResource;
import org.jnosql.diana.api.TTL;
import org.jnosql.diana.api.Value;

import java.util.Optional;

/**
 * Interface used to interact with the persistence context to {@link KeyValue}
 * The BucketManager API is used to create and remove persistent {@link KeyValue}.
 *
 * @author Otávio Santana
 */
public interface BucketManager extends CloseResource {

    /**
     * Associates the specified value with the specified key and than storage
     *
     * @param key   the key
     * @param value the value
     * @param <K>   the key type
     * @param <V>   the value type
     * @throws NullPointerException when either key or value are null
     */
    <K, V> void put(K key, V value) throws NullPointerException;

    /**
     * Saves the {@link KeyValue}
     *
     * @param keyValue the entity to be save
     * @param <K>      the key type
     * @throws NullPointerException when entity is null
     */
    <K> void put(KeyValue<K> keyValue) throws NullPointerException;

    /**
     * Saves the {@link KeyValue} with time to live
     *
     * @param keyValue the entity to be save
     * @param ttl      the defined time to live
     * @param <K>      the key type
     * @throws NullPointerException          when entity is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    <K> void put(KeyValue<K> keyValue, TTL ttl) throws NullPointerException, UnsupportedOperationException;

    /**
     * Saves the {@link Iterable} of keys
     *
     * @param keyValues keys to be save
     * @param <K>       the key type
     * @throws NullPointerException when the iterable is null
     */
    <K> void put(Iterable<KeyValue<K>> keyValues) throws NullPointerException;

    /**
     * Saves the {@link Iterable} of keys with a defined time to live
     *
     * @param keyValues keys to be save
     * @param ttl       the time to entity expire
     * @param <K>       the key type
     * @throws NullPointerException          when the iterable is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    <K> void put(Iterable<KeyValue<K>> keyValues, TTL ttl) throws NullPointerException, UnsupportedOperationException;

    /**
     * Finds the Value from a key
     *
     * @param key the key
     * @param <K> the key type
     * @return the {@link Optional} when is not found will return a {@link Optional#empty()}
     * @throws NullPointerException when the key is null
     */
    <K> Optional<Value> get(K key) throws NullPointerException;

    /**
     * Finds a list of values from keys
     *
     * @param keys the keys to be used in this query
     * @param <K>  the key type
     * @return the list of result
     * @throws NullPointerException when either the keys or the entities values are null
     */
    <K> Iterable<Value> get(Iterable<K> keys) throws NullPointerException;

    /**
     * Removes an entity from key
     *
     * @param key the key bo be used
     * @param <K> the key type
     * @throws NullPointerException when the key is null
     */
    <K> void remove(K key) throws NullPointerException;

    /**
     * Removes entities from keys
     *
     * @param keys the keys to be used
     * @param <K>  the key type
     * @throws NullPointerException when the key is null
     */
    <K> void remove(Iterable<K> keys) throws NullPointerException;

}
