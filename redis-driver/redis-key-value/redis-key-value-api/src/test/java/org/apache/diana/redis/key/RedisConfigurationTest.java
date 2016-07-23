package org.apache.diana.redis.key;

import org.apache.diana.api.key.KeyValueConfiguration;
import org.apache.diana.api.key.KeyValueEntityManagerFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class RedisConfigurationTest {

    private KeyValueConfiguration configuration;

    @Before
    public void setUp() {
        configuration = new RedisConfiguration();
    }

    @Test
    public void shouldCreateKeyValueFactory() {
        Map<String, String> map = new HashMap<>();
        map.put("redis-master-hoster", "172.17.0.2");
        KeyValueEntityManagerFactory managerFactory = configuration.getManagerFactory(map);
        assertNotNull(managerFactory);
    }

    @Test
    public void shouldCreateKeyValueFactoryFromFile() {
        KeyValueEntityManagerFactory managerFactory = configuration.getManagerFactory();
        assertNotNull(managerFactory);
    }

}