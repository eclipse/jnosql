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
package org.jnosql.artemis.column.spi;

import jakarta.nosql.column.ColumnConfiguration;
import jakarta.nosql.column.ColumnConfigurationAsync;
import jakarta.nosql.column.ColumnFamilyManagerAsyncFactory;
import jakarta.nosql.column.ColumnFamilyManagerFactory;
import jakarta.nosql.column.UnaryColumnConfiguration;
import jakarta.nosql.mapping.ConfigurationReader;
import jakarta.nosql.mapping.ConfigurationSettingsUnit;
import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.mapping.configuration.ConfigurationException;
import jakarta.nosql.mapping.reflection.Reflections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import static org.jnosql.artemis.util.ConfigurationUnitUtils.getConfigurationUnit;

/**
 * The class that creates {@link ColumnFamilyManagerFactory} and {@link ColumnFamilyManagerAsyncFactory}
 * from the {@link ConfigurationUnit}
 */
@ApplicationScoped
class ColumnConfigurationProducer {

    @Inject
    private Reflections reflections;

    @Inject
    private Instance<ConfigurationReader> configurationReader;


    @ConfigurationUnit
    @Produces
    public ColumnFamilyManagerFactory get(InjectionPoint injectionPoint) {
        return gettColumnFamilyManagerFactory(injectionPoint);
    }

    @ConfigurationUnit
    @Produces
    public ColumnFamilyManagerAsyncFactory getAsync(InjectionPoint injectionPoint) {
        return gettColumnFamilyManagerAsyncFactory(injectionPoint);
    }


    private ColumnFamilyManagerAsyncFactory gettColumnFamilyManagerAsyncFactory(InjectionPoint injectionPoint) {

        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint);

        return getFactoryAsync(annotation);
    }

    ColumnFamilyManagerAsyncFactory getFactoryAsync(ConfigurationUnit annotation) {
        ConfigurationSettingsUnit unit = configurationReader.get().read(annotation);
        Class<ColumnConfigurationAsync> configurationClass = unit.<ColumnConfigurationAsync>getProvider()
                .orElseThrow(() -> new IllegalStateException("The ColumnConfiguration provider is required in the configuration"));

        if (ColumnConfigurationAsync.class.isAssignableFrom(configurationClass)) {
            ColumnConfigurationAsync configuration = (ColumnConfigurationAsync) reflections.newInstance(configurationClass);
            return configuration.get(unit.getSettings());
        }
        if (UnaryColumnConfiguration.class.isAssignableFrom(configurationClass)) {
            UnaryColumnConfiguration<?> configuration = (UnaryColumnConfiguration) reflections.newInstance(configurationClass);
            return configuration.get(unit.getSettings());
        }

        throw new ConfigurationException(String.format("The class %s does not match with either " +
                        "ColumnConfigurationAsync or UnaryColumnConfiguration",
                configurationClass));
    }

    ColumnFamilyManagerFactory getFactory(ConfigurationUnit annotation) {
        ConfigurationSettingsUnit unit = configurationReader.get().read(annotation);
        Class<?> configurationClass = unit.getProvider()
                .orElseThrow(() -> new IllegalStateException("The ColumnConfiguration provider is required in the configuration"));

        if (ColumnConfiguration.class.isAssignableFrom(configurationClass)) {
            ColumnConfiguration configuration = (ColumnConfiguration) reflections.newInstance(configurationClass);
            return configuration.get(unit.getSettings());
        }
        if (UnaryColumnConfiguration.class.isAssignableFrom(configurationClass)) {
            UnaryColumnConfiguration configuration = (UnaryColumnConfiguration) reflections.newInstance(configurationClass);
            return configuration.get(unit.getSettings());
        }

        throw new ConfigurationException(String.format("The class %s does not match with either " +
                        "ColumnConfiguration or UnaryColumnConfiguration",
                configurationClass));
    }

    private ColumnFamilyManagerFactory gettColumnFamilyManagerFactory(InjectionPoint injectionPoint) {

        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint);
        return getFactory(annotation);
    }


}
