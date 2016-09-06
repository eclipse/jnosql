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


import org.jnosql.diana.api.key.BucketManagerFactory;
import org.jnosql.diana.api.key.KeyValueConfiguration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public final class RedisConfiguration implements KeyValueConfiguration<RedisKeyValueEntityManagerFactory> {

    private static final String REDIS_FILE_CONFIGURATION = "diana-redis.properties";

    public RedisKeyValueEntityManagerFactory getManagerFactory(Map<String, String> configurations) {
        JedisPoolConfig poolConfig = getJedisPoolConfig(configurations);
        JedisPool jedisPool = getJedisPool(configurations, poolConfig);

        return new RedisKeyValueEntityManagerFactory(jedisPool);
    }

    private JedisPool getJedisPool(Map<String, String> configurations, JedisPoolConfig poolConfig) {
        String localhost = configurations.getOrDefault("redis-master-hoster", "localhost");
        Integer port = Integer.valueOf(configurations.getOrDefault("redis-master-port", "6379"));
        Integer timeout = Integer.valueOf(configurations.getOrDefault("redis-timeout", "2000"));
        String password = configurations.getOrDefault("redis-password", null);
        Integer database = Integer.valueOf(configurations.getOrDefault("redis-database", "0"));
        String clientName = configurations.getOrDefault("redis-clientName", null);
        return new JedisPool(poolConfig, localhost, port, timeout, password, database, clientName);
    }

    private JedisPoolConfig getJedisPoolConfig(Map<String, String> configurations) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Integer.valueOf(configurations.getOrDefault("redis-configuration-max-total", "1000")));
        poolConfig.setMaxIdle(Integer.valueOf(configurations.getOrDefault("redis-configuration-max-idle", "10")));
        poolConfig.setMinIdle(Integer.valueOf(configurations.getOrDefault("redis-configuration-min-idle", "1")));
        poolConfig.setMaxWaitMillis(Integer.valueOf(configurations
                .getOrDefault("redis-configuration-max--wait-millis", "3000")));
        return poolConfig;
    }


    @Override
    public RedisKeyValueEntityManagerFactory getManagerFactory() {
        try {
            Properties properties = new Properties();
            InputStream stream = RedisConfiguration.class.getClassLoader()
                    .getResourceAsStream(REDIS_FILE_CONFIGURATION);
            properties.load(stream);
            Map<String, String> collect = properties.keySet().stream()
                    .collect(Collectors.toMap(Object::toString, s -> properties.get(s).toString()));
            return getManagerFactory(collect);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
