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
package org.jnosql.artemis.key.spi;

import jakarta.nosql.key.BucketManagerFactory;
import jakarta.nosql.key.KeyValueConfiguration;
import jakarta.nosql.mapping.ConfigurationReader;
import jakarta.nosql.mapping.ConfigurationSettingsUnit;
import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.mapping.reflection.Reflections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import static org.jnosql.artemis.util.ConfigurationUnitUtils.getConfigurationUnit;

/**
 * The class that creates {@link BucketManagerFactory} from the {@link ConfigurationUnit}
 */
@ApplicationScoped
class KeyValueConfigurationProducer {


    @Inject
    private Reflections reflections;

    @Inject
    private Instance<ConfigurationReader> configurationReader;


    @ConfigurationUnit
    @Produces
    public BucketManagerFactory get(InjectionPoint injectionPoint) {
        return getBucketManagerFactory(injectionPoint);
    }

    BucketManagerFactory getBucketManagerFactory(ConfigurationUnit annotation) {
        ConfigurationSettingsUnit unit = configurationReader.get().read(annotation, KeyValueConfiguration.class);
        Class<KeyValueConfiguration> configurationClass = unit.<KeyValueConfiguration>getProvider()
                .orElseThrow(() -> new IllegalStateException("The KeyValueConfiguration provider is required in the configuration"));

        KeyValueConfiguration configuration = reflections.newInstance(configurationClass);

        return configuration.get(unit.getSettings());
    }

    private BucketManagerFactory getBucketManagerFactory(InjectionPoint injectionPoint) {
        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint);
        return getBucketManagerFactory(annotation);
    }

}
