/*
 *  Copyright (c) 2018 Otávio Santana and others
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
 *   Daniel Cunha <dcunha@tomitribe.com>
 */
package org.eclipse.jnosql.artemis.configuration;

import jakarta.nosql.mapping.configuration.ConfigurationException;
import org.eclipse.jnosql.artemis.CDIExtension;
import jakarta.nosql.mapping.ConfigurationReader;
import jakarta.nosql.mapping.ConfigurationSettingsUnit;
import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.Settings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(CDIExtension.class)
public class DefaultXMLConfigurationReaderTest {

    @Inject
    private ConfigurationReader configurationReader;

    @Test
    public void shouldReadDefaultNameXML() {
        ConfigurationUnit annotation = mock(ConfigurationUnit.class);
        when(annotation.fileName()).thenReturn("jnosql.xml");
        when(annotation.name()).thenReturn("name");
        ConfigurationSettingsUnit unit = configurationReader.read(annotation);

        Map<String, Object> settings = new HashMap<>();
        settings.put("key","value");
        settings.put("key2","value2");
        settings.putAll(System.getenv());
        System.getProperties().forEach((k,v) -> settings.put(k.toString(), v));

        assertEquals("name", unit.getName().get());
        assertEquals("that is the description", unit.getDescription().get());
        assertEquals(Settings.of(settings), unit.getSettings());
        assertEquals(DefaultMockConfiguration.class, unit.getProvider().get());
    }

    @Test
    public void shouldReadDefaultAnnotationNameXML() {
        ConfigurationUnit annotation = mock(ConfigurationUnit.class);
        when(annotation.fileName()).thenReturn("jnosql.xml");
        when(annotation.name()).thenReturn("name");
        ConfigurationSettingsUnit unit = configurationReader.read(annotation);

        Map<String, Object> settings = new HashMap<>();
        settings.put("key","value");
        settings.put("key2","value2");
        settings.putAll(System.getenv());
        System.getProperties().forEach((k,v) -> settings.put(k.toString(), v));

        assertEquals("name", unit.getName().get());
        assertEquals("that is the description", unit.getDescription().get());
        assertEquals(Settings.of(settings), unit.getSettings());
        assertTrue(unit.getProvider().isPresent());
    }

    @Test
    public void shouldReturnErrorWhenFileIsInvalid() {
        Assertions.assertThrows(ConfigurationException.class, () -> {
            ConfigurationUnit annotation = mock(ConfigurationUnit.class);
            when(annotation.fileName()).thenReturn("invalid.xml");
            configurationReader.read(annotation);
        });
    }
}