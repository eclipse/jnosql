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
package org.eclipse.jnosql.artemis.graph.configuration;

import jakarta.nosql.Settings;
import jakarta.nosql.mapping.reflection.Reflections;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.artemis.configuration.ConfigurationException;
import org.eclipse.jnosql.artemis.configuration.SettingsConverter;
import org.eclipse.jnosql.artemis.graph.GraphConfiguration;
import org.eclipse.jnosql.artemis.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * Converter the {@link String} to {@link Graph}
 */
public class GraphConverter implements Converter<Graph> {

    @Override
    public Graph convert(String value) {
        final SettingsConverter settingsConverter = BeanManagers.getInstance(SettingsConverter.class);
        Config config = BeanManagers.getInstance(Config.class);
        final Settings settings = settingsConverter.convert(value);
        final String provider = value + ".provider";
        final Class<?> configurationClass = config.getValue(provider, Class.class);
        if (GraphConfiguration.class.isAssignableFrom(configurationClass)) {
            final Reflections reflections = BeanManagers.getInstance(Reflections.class);
            final GraphConfiguration configuration = (GraphConfiguration) reflections.newInstance(configurationClass);
            return configuration.apply(settings);

        }
        throw new ConfigurationException("The class " + configurationClass + " is not valid to " + GraphConfiguration.class);
    }
}
