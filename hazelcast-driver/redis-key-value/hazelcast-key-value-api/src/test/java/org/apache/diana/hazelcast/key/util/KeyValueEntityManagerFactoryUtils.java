package org.apache.diana.hazelcast.key.util;

import org.apache.diana.api.key.KeyValueConfiguration;
import org.apache.diana.api.key.KeyValueEntityManagerFactory;
import org.apache.diana.hazelcast.key.HazelCastKeyValueConfiguration;


public class KeyValueEntityManagerFactoryUtils {

    public static KeyValueEntityManagerFactory get() {
        KeyValueConfiguration configuration = new HazelCastKeyValueConfiguration();
        KeyValueEntityManagerFactory managerFactory = configuration.getManagerFactory();
        return managerFactory;
    }
}
