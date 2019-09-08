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
package org.eclipse.jnosql.artemis.document.spi;

import jakarta.nosql.document.DocumentCollectionManagerAsyncFactory;
import jakarta.nosql.document.DocumentCollectionManagerFactory;
import jakarta.nosql.document.DocumentConfiguration;
import jakarta.nosql.document.DocumentConfigurationAsync;
import jakarta.nosql.mapping.ConfigurationReader;
import jakarta.nosql.mapping.ConfigurationSettingsUnit;
import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.mapping.configuration.ConfigurationException;
import jakarta.nosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.artemis.util.ConfigurationUnitUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 * The class that creates {@link DocumentCollectionManagerFactory} and {@link DocumentCollectionManagerFactory}
 * from the {@link ConfigurationUnit}
 */
@ApplicationScoped
class DocumentConfigurationProducer {

    @Inject
    private Reflections reflections;

    @Inject
    private Instance<ConfigurationReader> configurationReader;

    @ConfigurationUnit
    @Produces
    public DocumentCollectionManagerFactory get(InjectionPoint injectionPoint) {
        return getDocumentCollection(injectionPoint);
    }

    @ConfigurationUnit
    @Produces
    public DocumentCollectionManagerAsyncFactory getAsync(InjectionPoint injectionPoint) {
        return getDocumentCollectionAsync(injectionPoint);
    }

    private DocumentCollectionManagerAsyncFactory
    getDocumentCollectionAsync(InjectionPoint injectionPoint) {

        ConfigurationUnit annotation = ConfigurationUnitUtils.getConfigurationUnit(injectionPoint);
        return getFactoryAsync(annotation);
    }

     DocumentCollectionManagerAsyncFactory getFactoryAsync(ConfigurationUnit annotation) {
        ConfigurationSettingsUnit unit = configurationReader.get().read(annotation);
        Class<?> configurationClass = unit.getProvider()
                .orElseThrow(() -> new IllegalStateException("The DocumentConfigurationAsync provider is required in the configuration"));

         if (DocumentConfigurationAsync.class.isAssignableFrom(configurationClass)) {
             DocumentConfigurationAsync configuration = (DocumentConfigurationAsync) reflections.newInstance(configurationClass);
             return configuration.get(unit.getSettings());
         }

         throw new ConfigurationException(String.format("The class %s does not match with " +
                         "DocumentConfigurationAsync",
                 configurationClass));
    }

    DocumentCollectionManagerFactory getFactory(ConfigurationUnit annotation) {
        ConfigurationSettingsUnit unit = configurationReader.get().read(annotation);
        Class<?> configurationClass = unit.getProvider()
                .orElseThrow(() -> new IllegalStateException("The DocumentConfiguration provider is required in the configuration"));

        if (DocumentConfiguration.class.isAssignableFrom(configurationClass)) {
            DocumentConfiguration configuration = (DocumentConfiguration) reflections.newInstance(configurationClass);
            return configuration.get(unit.getSettings());
        }

        throw new ConfigurationException(String.format("The class %s does not match with " +
                        "DocumentConfiguration",
                configurationClass));
    }

    private DocumentCollectionManagerFactory getDocumentCollection(InjectionPoint injectionPoint) {

        ConfigurationUnit annotation = ConfigurationUnitUtils.getConfigurationUnit(injectionPoint);
        return getFactory(annotation);
    }
}
