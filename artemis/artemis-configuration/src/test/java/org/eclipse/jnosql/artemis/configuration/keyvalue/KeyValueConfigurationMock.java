package org.eclipse.jnosql.artemis.configuration.keyvalue;

import jakarta.nosql.Settings;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.BucketManagerFactory;
import jakarta.nosql.keyvalue.KeyValueConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class KeyValueConfigurationMock implements KeyValueConfiguration {
    @Override
    public BucketManagerFactory get() {
        return  new BucketManagerFactoryMock(Settings.builder().build());
    }

    @Override
    public BucketManagerFactory get(Settings settings) {
        return  new BucketManagerFactoryMock(settings);
    }


    public static class BucketManagerFactoryMock implements BucketManagerFactory  {

     private final Settings settings;

        public BucketManagerFactoryMock(Settings settings) {
            this.settings = settings;
        }

        @Override
        public <T extends BucketManager> T getBucketManager(String bucketName) {
            return null;
        }

        @Override
        public <T> List<T> getList(String bucketName, Class<T> clazz) {
            return null;
        }

        @Override
        public <T> Set<T> getSet(String bucketName, Class<T> clazz) {
            return null;
        }

        @Override
        public <T> Queue<T> getQueue(String bucketName, Class<T> clazz) {
            return null;
        }

        @Override
        public <K, V> Map<K, V> getMap(String bucketName, Class<K> keyValue, Class<V> valueValue) {
            return null;
        }

        @Override
        public void close() {

        }
    }
}
