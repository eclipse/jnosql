package org.apache.diana.redis.key;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import org.apache.diana.api.key.KeyValueEntityManagerFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class RedisMapTest {

    private KeyValueEntityManagerFactory entityManagerFactory;

    private Species mammals = new Species("lion", "cow", "dog");
    private Species fishes = new Species("redfish", "glassfish");
    private Species amphibians = new Species("crododile", "frog");

    @Before
    public void init() {
        entityManagerFactory = RedisTestUtils.get();
    }

    @Test
    public void shouldPutAndGetMap() {
        Map<String, Species> vertebrates = entityManagerFactory.getMap("vertebrates", String.class, Species.class);
        assertTrue(vertebrates.isEmpty());

        assertNotNull(vertebrates.put("mammals", mammals));
        Species species = vertebrates.get("mammals");
        assertNotNull(species);
        assertEquals(species.getAnimals().get(0), mammals.getAnimals().get(0));
        assertTrue(vertebrates.size() == 1);
    }

    @Test
    public void shouldVerifyExist() {

        Map<String, Species> vertebrates = entityManagerFactory.getMap("vertebrates", String.class, Species.class);
        vertebrates.put("mammals", mammals);
        assertTrue(vertebrates.containsKey("mammals"));
        Assert.assertFalse(vertebrates.containsKey("redfish"));

        assertTrue(vertebrates.containsValue(mammals));
        Assert.assertFalse(vertebrates.containsValue(fishes));
    }

    @Test
    public void shouldShowKeyAndValues() {
        Map<String, Species> vertebratesMap = new HashMap<>();
        vertebratesMap.put("mammals", mammals);
        vertebratesMap.put("fishes", fishes);
        vertebratesMap.put("amphibians", amphibians);
        Map<String, Species> vertebrates = entityManagerFactory.getMap("vertebrates", String.class, Species.class);
        vertebrates.putAll(vertebratesMap);

        Set<String> keys = vertebrates.keySet();
        Collection<Species> collectionSpecies = vertebrates.values();

        assertTrue(keys.size() == 3);
        assertTrue(collectionSpecies.size() == 3);
        assertNotNull(vertebrates.remove("mammals"));
        assertNull(vertebrates.remove("mammals"));
        assertNull(vertebrates.get("mammals"));
        assertTrue(vertebrates.size() == 2);
    }

    @After
    public void dispose() {
        Map<String, Species> vertebrates = entityManagerFactory.getMap("vertebrates", String.class, Species.class);
        vertebrates.clear();
    }

}
