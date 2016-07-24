package org.apache.diana.redis.key;

import org.apache.diana.api.key.BucketManager;
import org.apache.diana.api.key.BucketManagerFactory;
import org.apache.diana.api.key.KeyValueConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertNotNull;


public class RedisKeyValueEntityManagerFactoryTest {

    public static final String BUCKET_NAME = "bucketName";
    private BucketManagerFactory managerFactory;

    @Before
    public void setUp() {
        KeyValueConfiguration configuration = new RedisConfiguration();
        managerFactory = configuration.getManagerFactory();
    }

    @Test
    public void shouldCreateKeyValueEntityManager(){
        BucketManager keyValueEntityManager = managerFactory.getBucketManager(BUCKET_NAME);
        assertNotNull(keyValueEntityManager);
    }

    @Test
    public void shouldCreateMap(){
        Map<String, String> map = managerFactory.getMap(BUCKET_NAME, String.class, String.class);
        assertNotNull(map);
    }

    @Test
    public void shouldCreateSet(){
        Set<String> set = managerFactory.getSet(BUCKET_NAME, String.class);
        assertNotNull(set);
    }

    @Test
    public void shouldCreateList(){
        List<String> list = managerFactory.getList(BUCKET_NAME, String.class);
        assertNotNull(list);
    }

    @Test
    public void shouldCreateQueue(){
        Queue<String> queue = managerFactory.getQueue(BUCKET_NAME, String.class);
        assertNotNull(queue);
    }

}