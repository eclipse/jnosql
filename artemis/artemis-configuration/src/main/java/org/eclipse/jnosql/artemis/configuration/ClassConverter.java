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

import org.eclipse.microprofile.config.spi.Converter;

/**
 * Converter the {@link String} to {@link Class} it gonna use {@link Class#forName(String)}
 */
public class ClassConverter implements Converter<Class<?>> {

    @Override
    public Class<?> convert(String value) {
        try {
            return Class.forName(value);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("An error to convert the value to Class the value: " + value, e);
        }
    }
}
