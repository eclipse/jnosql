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
package org.eclipse.jnosql.artemis.configuration;

import org.eclipse.microprofile.config.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.UUID;

@ExtendWith(CDIExtension.class)
class ClassConverterTest {

    @Inject
    private Config config;


    @Test
    public void shouldLoadClass() {
        String key = UUID.randomUUID().toString();
        System.setProperty(key, "java.lang.String");

        final Class<?> value = config.getValue(key, Class.class);
        Assertions.assertNotNull(value);
        Assertions.assertEquals(String.class, value);
        System.clearProperty(key);
    }

    @Test
    public void shouldReturnErrorWhenLoadClass() {
        String key = UUID.randomUUID().toString();
        System.setProperty(key, "java.lang.Wrong");

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                config.getValue(key, Class.class));
        System.clearProperty(key);
    }
}