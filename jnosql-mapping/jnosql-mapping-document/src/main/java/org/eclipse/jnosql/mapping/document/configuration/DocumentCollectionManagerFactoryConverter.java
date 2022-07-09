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
package org.eclipse.jnosql.mapping.document.configuration;

import jakarta.nosql.Settings;
import jakarta.nosql.document.DocumentCollectionManagerFactory;
import jakarta.nosql.document.DocumentConfiguration;
import org.eclipse.jnosql.mapping.configuration.AbstractConfiguration;
import org.eclipse.jnosql.mapping.configuration.ConfigurationException;
import org.eclipse.jnosql.mapping.configuration.SettingsConverter;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

import javax.enterprise.inject.spi.CDI;

/**
 * Converter the {@link String} to {@link DocumentCollectionManagerFactory} it will use the {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link DocumentConfiguration}
 */
public class DocumentCollectionManagerFactoryConverter extends AbstractConfiguration<DocumentCollectionManagerFactory>
        implements Converter<DocumentCollectionManagerFactory> {

    @Override
    public DocumentCollectionManagerFactory success(String value) {
        final SettingsConverter settingsConverter = CDI.current().select(SettingsConverter.class).get();
        Config config = CDI.current().select(Config.class).get();
        final Settings settings = settingsConverter.convert(value);
        String provider = value + ".provider";
        final Class<?> configurationClass = config.getValue(provider, Class.class);
        if (DocumentConfiguration.class.isAssignableFrom(configurationClass)) {
            final Reflections reflections = CDI.current().select(Reflections.class).get();
            final DocumentConfiguration configuration = (DocumentConfiguration) reflections.newInstance(configurationClass);
            return configuration.get(settings);

        }
        throw new ConfigurationException("The class " + configurationClass + " is not valid to " + DocumentConfiguration.class);
    }
}
