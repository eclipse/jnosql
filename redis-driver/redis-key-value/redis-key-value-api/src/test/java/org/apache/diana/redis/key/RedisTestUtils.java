package org.apache.diana.redis.key;

import org.apache.diana.api.key.KeyValueConfiguration;
import org.apache.diana.api.key.KeyValueEntityManagerFactory;


public class RedisTestUtils {

    public static KeyValueEntityManagerFactory get() {
        KeyValueConfiguration configuration = new RedisConfiguration();
        KeyValueEntityManagerFactory managerFactory = configuration.getManagerFactory();
        return managerFactory;
    }
}
