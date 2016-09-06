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


import com.hazelcast.core.HazelcastInstance;
import org.jnosql.diana.api.key.BucketManager;
import org.jnosql.diana.api.key.BucketManagerFactory;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class HazelCastKeyValueEntityManagerFactory implements BucketManagerFactory<HazelCastKeyValueEntityManager> {

    private final HazelcastInstance hazelcastInstance;

    HazelCastKeyValueEntityManagerFactory(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public HazelCastKeyValueEntityManager getBucketManager(String bucketName) {
        return new HazelCastKeyValueEntityManager(hazelcastInstance.getMap(bucketName));
    }

    @Override
    public <T> List<T> getList(String bucketName, Class<T> clazz) {
        return hazelcastInstance.getList(bucketName);
    }

    @Override
    public <T> Set<T> getSet(String bucketName, Class<T> clazz) {
        return hazelcastInstance.getSet(bucketName);
    }

    @Override
    public <T> Queue<T> getQueue(String bucketName, Class<T> clazz) {
        return hazelcastInstance.getQueue(bucketName);
    }

    @Override
    public <K, V> Map<K, V> getMap(String bucketName, Class<K> keyValue, Class<V> valueValue) {
        return hazelcastInstance.getMap(bucketName);
    }

    @Override
    public void close() {

    }
}
