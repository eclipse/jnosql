/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.graph.configuration;

import jakarta.nosql.Settings;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.mapping.configuration.AbstractConfiguration;
import org.eclipse.jnosql.mapping.configuration.ConfigurationException;
import org.eclipse.jnosql.mapping.configuration.SettingsConverter;
import org.eclipse.jnosql.mapping.graph.GraphConfiguration;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

import javax.enterprise.inject.spi.CDI;

/**
 * Converter the {@link String} to {@link Graph}
 */
public class GraphConverter extends AbstractConfiguration<Graph> implements Converter<Graph> {

    @Override
    protected Graph success(String value) {
        final SettingsConverter settingsConverter = CDI.current().select(SettingsConverter.class).get();
        Config config = CDI.current().select(Config.class).get();
        final Settings settings = settingsConverter.convert(value);
        final String provider = value + ".provider";
        final Class<?> configurationClass = config.getValue(provider, Class.class);
        if (GraphConfiguration.class.isAssignableFrom(configurationClass)) {
            final Reflections reflections = CDI.current().select(Reflections.class).get();
            final GraphConfiguration configuration = (GraphConfiguration) reflections.newInstance(configurationClass);
            return configuration.apply(settings);
        }
        throw new ConfigurationException("The class " + configurationClass + " is not valid to " + GraphConfiguration.class);
    }
}
