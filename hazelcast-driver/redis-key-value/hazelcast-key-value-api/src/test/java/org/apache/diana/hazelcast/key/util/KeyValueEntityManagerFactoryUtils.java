package org.apache.diana.hazelcast.key.util;

import org.apache.diana.api.key.BucketManagerFactory;
import org.apache.diana.api.key.KeyValueConfiguration;
import org.apache.diana.hazelcast.key.HazelCastKeyValueConfiguration;


public class KeyValueEntityManagerFactoryUtils {

    public static BucketManagerFactory get() {
        KeyValueConfiguration configuration = new HazelCastKeyValueConfiguration();
        BucketManagerFactory managerFactory = configuration.getManagerFactory();
        return managerFactory;
    }
}
