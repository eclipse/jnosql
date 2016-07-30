/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.hazelcast.key;

import org.jnosql.diana.api.key.BucketManagerFactory;
import org.jnosql.diana.hazelcast.key.model.Species;
import org.jnosql.diana.hazelcast.key.util.KeyValueEntityManagerFactoryUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;


public class MapTest {

    private BucketManagerFactory entityManagerFactory;

    private Species mammals = new Species("lion", "cow", "dog");
    private Species fishes = new Species("redfish", "glassfish");
    private Species amphibians = new Species("crododile", "frog");

    @Before
    public void init() {
        entityManagerFactory = KeyValueEntityManagerFactoryUtils.get();
    }

    @Test
    public void shouldPutAndGetMap() {
        Map<String, Species> vertebrates = entityManagerFactory.getMap("vertebrates", String.class, Species.class);
        assertTrue(vertebrates.isEmpty());

        vertebrates.put("mammals", mammals);
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
