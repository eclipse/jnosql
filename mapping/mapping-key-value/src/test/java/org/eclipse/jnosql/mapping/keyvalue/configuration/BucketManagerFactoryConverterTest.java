/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.keyvalue.configuration;

import jakarta.nosql.Settings;
import jakarta.nosql.keyvalue.BucketManagerFactory;
import org.eclipse.jnosql.mapping.keyvalue.configuration.KeyValueConfigurationMock.BucketManagerFactoryMock;
import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.mapping.configuration.ConfigurationException;
import org.eclipse.microprofile.config.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
class BucketManagerFactoryConverterTest {

    @Inject
    private Config config;

    @Test
    public void shouldReturnErrorWhenThereIsNoProvider() {
        final String prefix = UUID.randomUUID().toString();
        System.setProperty(prefix, prefix);
        System.setProperty(prefix + ".settings.key", "value");
        System.setProperty(prefix + ".settings.key2", "value2");
        Assertions.assertThrows(NoSuchElementException.class, () -> config.getValue(prefix, BucketManagerFactory.class) );

        System.clearProperty(prefix);
        System.clearProperty(prefix + ".settings.key");
        System.clearProperty(prefix + ".settings.key2");
    }


    @Test
    public void shouldReturnErrorWhenThereIsInvalidProvider() {
        final String prefix = UUID.randomUUID().toString();
        System.setProperty(prefix, prefix);
        System.setProperty(prefix + ".settings.key", "value");
        System.setProperty(prefix + ".settings.key2", "value2");
        System.setProperty(prefix + ".provider", "java.lang.String");
        Assertions.assertThrows(ConfigurationException.class, () -> config.getValue(prefix, BucketManagerFactory.class) );

        System.clearProperty(prefix);
        System.clearProperty(prefix + ".settings.key");
        System.clearProperty(prefix + ".settings.key2");
        System.clearProperty(prefix + ".provider");
    }

    @Test
    public void shouldReturnManagerFactory() {
        final String prefix = UUID.randomUUID().toString();
        System.setProperty(prefix, prefix);
        System.setProperty(prefix + ".settings.key", "value");
        System.setProperty(prefix + ".settings.key2", "value2");
        System.setProperty(prefix + ".provider", KeyValueConfigurationMock.class.getName());
        final BucketManagerFactory managerFactory = config.getValue(prefix, BucketManagerFactory.class);

        final BucketManagerFactoryMock factoryMock = BucketManagerFactoryMock.class.cast(managerFactory);
        final Settings settings = factoryMock.getSettings();

        assertEquals(2, settings.size());
        assertEquals(settings.get("key").get(), "value");
        assertEquals(settings.get("key2").get(), "value2");

        assertNotNull(managerFactory);
        System.clearProperty(prefix);
        System.clearProperty(prefix + ".settings.key");
        System.clearProperty(prefix + ".settings.key2");
        System.clearProperty(prefix + ".provider");
    }
}