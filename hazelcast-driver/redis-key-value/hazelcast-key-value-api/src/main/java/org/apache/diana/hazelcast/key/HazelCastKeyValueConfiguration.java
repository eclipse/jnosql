/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.diana.hazelcast.key;


import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.diana.api.key.BucketManagerFactory;
import org.apache.diana.api.key.KeyValueConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class HazelCastKeyValueConfiguration implements KeyValueConfiguration {

    private static final String HAZELCAST_FILE_CONFIGURATION = "diana-hazelcast.properties";

    @Override
    public BucketManagerFactory getManagerFactory(Map<String, String> configurations) {

        List<String> servers = configurations.keySet().stream().filter(s -> s.startsWith("hazelcast-hoster-")).collect(Collectors.toList());

        Config config = new Config(configurations.getOrDefault("hazelcast-instanceName", "hazelcast-instanceName"));
        HazelcastInstance hazelcastInstance = Hazelcast.getOrCreateHazelcastInstance(config);
        return new HazelCastKeyValueEntityManagerFactory(hazelcastInstance);
    }

    @Override
    public BucketManagerFactory getManagerFactory() {
        try {
            Properties properties = new Properties();
            InputStream stream = HazelCastKeyValueConfiguration.class.getClassLoader().getResourceAsStream(HAZELCAST_FILE_CONFIGURATION);
            properties.load(stream);
            Map<String, String> collect = properties.keySet().stream().collect(Collectors.toMap(Object::toString, s -> properties.get(s).toString()));
            return getManagerFactory(collect);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
