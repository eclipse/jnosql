package org.apache.diana.redis.key;

import org.apache.diana.api.key.BucketManagerFactory;
import org.apache.diana.api.key.KeyValueConfiguration;


public class RedisTestUtils {

    public static BucketManagerFactory get() {
        KeyValueConfiguration configuration = new RedisConfiguration();
        BucketManagerFactory managerFactory = configuration.getManagerFactory();
        return managerFactory;
    }
}
