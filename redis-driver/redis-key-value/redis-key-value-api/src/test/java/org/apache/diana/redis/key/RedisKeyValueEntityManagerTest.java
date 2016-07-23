package org.apache.diana.redis.key;


import org.apache.diana.api.Value;
import org.apache.diana.api.key.KeyValue;
import org.apache.diana.api.key.KeyValueEntityManager;
import org.apache.diana.api.key.KeyValueEntityManagerFactory;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class RedisKeyValueEntityManagerTest {

    private KeyValueEntityManager keyValueEntityManager;

    private KeyValueEntityManagerFactory keyValueEntityManagerFactory;

    private User userOtavio = new User("otavio");
    private KeyValue keyValueOtavio = KeyValue.of("otavio", Value.of(userOtavio));

    private User userSoro = new User("soro");
    private KeyValue keyValueSoro = KeyValue.of("soro", Value.of(userSoro));

    @Before
    public void init() {
        keyValueEntityManagerFactory = RedisTestUtils.get();
        keyValueEntityManager = keyValueEntityManagerFactory.getKeyValueEntityManager("users-entity");
    }


    @Test
    public void shouldPutValue() {
        keyValueEntityManager.put("otavio", userOtavio);
        Optional<User> otavio = keyValueEntityManager.get("otavio", User.class);
        assertTrue(otavio.isPresent());
        assertEquals(userOtavio, otavio.get());
    }

    @Test
    public void shouldPutKeyValue() {
        keyValueEntityManager.put(keyValueOtavio);
        Optional<User> otavio = keyValueEntityManager.get("otavio", User.class);
        assertTrue(otavio.isPresent());
        assertEquals(userOtavio, otavio.get());
    }

    @Test
    public void shouldPutIterableKeyValue() {


        keyValueEntityManager.put(asList(keyValueSoro, keyValueOtavio));
        Optional<User> otavio = keyValueEntityManager.get("otavio", User.class);
        assertTrue(otavio.isPresent());
        assertEquals(userOtavio, otavio.get());

        Optional<User> soro = keyValueEntityManager.get("soro", User.class);
        assertTrue(soro.isPresent());
        assertEquals(userSoro, soro.get());
    }

    @Test
    public void shouldMultiGet() {
        User user = new User("otavio");
        KeyValue keyValue = KeyValue.of("otavio", Value.of(user));
        keyValueEntityManager.put(keyValue);
        assertNotNull(keyValueEntityManager.get("otavio", User.class));


    }

    @Test
    public void shouldRemoveKey() {

        keyValueEntityManager.put(keyValueOtavio);
        assertTrue(keyValueEntityManager.get("otavio", User.class).isPresent());
        keyValueEntityManager.remove("otavio");
        assertFalse(keyValueEntityManager.get("otavio", User.class).isPresent());
    }

    @Test
    public void shouldRemoveMultiKey() {

        keyValueEntityManager.put(asList(keyValueSoro, keyValueOtavio));
        List<String> keys = asList("otavio", "soro");
        assertThat(keyValueEntityManager.get(keys, User.class), containsInAnyOrder(userOtavio, userSoro));
        keyValueEntityManager.remove(keys);
        Iterable<User> users = keyValueEntityManager.get(keys, User.class);
        assertEquals(0L, StreamSupport.stream(users.spliterator(), false).count());
    }
}
