package org.apache.diana.redis.key;


import java.util.Set;


import org.apache.diana.api.key.KeyValueEntityManagerFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RedisSetTest {


    private KeyValueEntityManagerFactory keyValueEntityManagerFactory;
    private User userOtavioJava = new User("otaviojava");
    private User felipe = new User("ffrancesquini");
    private Set<User> users;

    @Before
    public void init() {
        keyValueEntityManagerFactory =  RedisTestUtils.get();
        users = keyValueEntityManagerFactory.getSet("social-media", User.class);
    }

    @Test
    public void shouldAddUsers() {
        assertTrue(users.isEmpty());
        users.add(userOtavioJava);
        assertTrue(users.size() == 1);

        users.remove(userOtavioJava);
        assertTrue(users.isEmpty());
    }


    @SuppressWarnings("unused")
    @Test
    public void shouldIterate() {

        users.add(userOtavioJava);
        users.add(userOtavioJava);
        users.add(felipe);
        users.add(userOtavioJava);
        users.add(felipe);
        int count = 0;
        for (User user: users) {
            count++;
        }
        assertTrue(count == 2);
        users.remove(userOtavioJava);
        users.remove(felipe);
        count = 0;
        for (User user: users) {
            count++;
        }
        assertTrue(count == 0);
    }

    @After
    public void dispose() {
        users.clear();
    }
}
