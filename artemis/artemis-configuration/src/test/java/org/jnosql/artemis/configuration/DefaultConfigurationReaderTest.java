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
 *   Daniel Cunha <dcunha@tomitribe.com>
 */
package org.jnosql.artemis.configuration;

import jakarta.nosql.mapping.ConfigurationReader;
import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.mapping.configuration.ConfigurationException;
import org.jnosql.artemis.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(CDIExtension.class)
public class DefaultConfigurationReaderTest {


    @Inject
    private ConfigurationReader configurationReader;


    @Test
    public void shouldReturnNPEWhenConfigurationIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ConfigurationUnit annotation = mock(ConfigurationUnit.class);
            configurationReader.read( null);
        });
    }

    @Test
    public void shouldReturnErrorWhenFileDoesNotExist() {
        Assertions.assertThrows(ConfigurationException.class, () -> {
            ConfigurationUnit annotation = mock(ConfigurationUnit.class);
            when(annotation.fileName()).thenReturn("unknown.json");
            configurationReader.read(annotation);
        });
    }

    @Test
    public void shouldReturnAnErrorWhenTheExtensionDoesNotSupport() {
        Assertions.assertThrows(ConfigurationException.class, () -> {
            ConfigurationUnit annotation = mock(ConfigurationUnit.class);
            when(annotation.fileName()).thenReturn("invalid.invalid");
            configurationReader.read(annotation);
        });
    }

    @Test
    public void shouldReturnAnErrorWhenTheFileIsInvalid() {
        Assertions.assertThrows(ConfigurationException.class, () -> {
            ConfigurationUnit annotation = mock(ConfigurationUnit.class);
            when(annotation.fileName()).thenReturn("invalid");
            configurationReader.read(annotation);
        });
    }


    @Test
    public void shouldReturnErrorWhenUnitNameIsNotFind() {
        Assertions.assertThrows(ConfigurationException.class, () -> {
            ConfigurationUnit annotation = mock(ConfigurationUnit.class);
            when(annotation.fileName()).thenReturn("jnosql.json");
            when(annotation.name()).thenReturn("unknown");
            configurationReader.read(annotation);
        });
    }

    @Test
    public void shouldReturnErrorWhenClassIsNotFound() {
        Assertions.assertThrows(ConfigurationException.class, () -> {
            ConfigurationUnit annotation = mock(ConfigurationUnit.class);
            when(annotation.fileName()).thenReturn("invalid-class.json");
            when(annotation.name()).thenReturn("name-1");
            configurationReader.read(annotation);
        });
    }

    @Test
    public void shouldReturnErrorWhenClassDoesNotMatch() {
        Assertions.assertThrows(ConfigurationException.class, () -> {
            ConfigurationUnit annotation = mock(ConfigurationUnit.class);
            when(annotation.fileName()).thenReturn("invalid-class.json");
            when(annotation.name()).thenReturn("name");
            configurationReader.read(annotation);
        });
    }

    @Test
    public void shouldReturnErrorWhenThereIsNotDefaultConstructor() {
        Assertions.assertThrows(ConfigurationException.class, () -> {
            ConfigurationUnit annotation = mock(ConfigurationUnit.class);
            when(annotation.fileName()).thenReturn("invalid-class.json");
            when(annotation.name()).thenReturn("name-2");
            configurationReader.read(annotation);
        });
    }

    @Test
    public void shouldReturnErrorWhenThereIsAmbiguous() {
        Assertions.assertThrows(ConfigurationException.class, () -> {
            ConfigurationUnit annotation = mock(ConfigurationUnit.class);
            when(annotation.fileName()).thenReturn("jnosql.json");
            configurationReader.read(annotation);
        });
    }
}