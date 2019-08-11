/*
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.jnosql.artemis.kv;

import org.jnosql.artemis.CDIExtension;
import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.Settings;
import jakarta.nosql.kv.BucketManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(CDIExtension.class)
public class KeyValueConfigurationProducerXMLTest {

    @Inject
    @ConfigurationUnit(fileName = "key-value.xml", name = "name")
    private BucketManagerFactory factoryA;

    @Inject
    @ConfigurationUnit(fileName = "key-value.xml", name = "name-2")
    private BucketManagerFactory factoryB;


    @Test
    public void shouldReadBucketManager() {
        factoryA.getBucketManager("database");
        assertTrue(factoryA instanceof KeyValueConfigurationMock.BucketManagerFactoryMock);
        KeyValueConfigurationMock.BucketManagerFactoryMock mock = (KeyValueConfigurationMock.BucketManagerFactoryMock) factoryA;
        Settings settings = mock.getSettings();
        assertEquals("value", settings.get("key").get());
        assertEquals("value2", settings.get("key2").get());
    }

    @Test
    public void shouldReadBucketManagerB() {
        factoryB.getBucketManager("database");
        assertTrue(factoryB instanceof KeyValueConfigurationMock.BucketManagerFactoryMock);
        KeyValueConfigurationMock.BucketManagerFactoryMock mock = (KeyValueConfigurationMock.BucketManagerFactoryMock) factoryB;
        Settings settings = mock.getSettings();
        assertEquals("value", settings.get("key").get());
        assertEquals("value2", settings.get("key2").get());
        assertEquals("value3", settings.get("key3").get());
    }
}