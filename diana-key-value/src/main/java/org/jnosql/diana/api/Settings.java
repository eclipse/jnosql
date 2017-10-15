/*
 *
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
 *
 */
package org.jnosql.diana.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * The interface represents the settings used in a configuration.
 *
 * @see Settings#of(Map[])
 */
public interface Settings extends Map<String, Object> {


    /**
     * Creates a settings from a map
     *
     * @param settings the setting
     * @return the new {@link Settings} instance
     * @throws NullPointerException when the parameter is null
     */
    static Settings of(Map<String, Object>... settings) throws NullPointerException {
        Map<String, Object> map = new HashMap<>();
        if (Stream.of(settings).anyMatch(Objects::isNull)) {
            throw new NullPointerException("The settings element cannot be null");
        }
        Stream.of(settings).forEach(map::putAll);
        return new DefaultSettings(map);
    }

}
