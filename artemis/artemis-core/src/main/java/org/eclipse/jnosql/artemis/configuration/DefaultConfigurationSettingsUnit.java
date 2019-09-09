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
package org.eclipse.jnosql.artemis.configuration;

import jakarta.nosql.Settings;
import jakarta.nosql.mapping.ConfigurationSettingsUnit;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * The default implementation of {@link ConfigurationSettingsUnit}
 */
class DefaultConfigurationSettingsUnit implements ConfigurationSettingsUnit {

    private final String name;

    private final String description;

    private final Class<?> provider;

    private final Settings settings;

    DefaultConfigurationSettingsUnit(String name, String description, Class<?> provider, Settings settings) {
        this.name = name;
        this.description = description;
        this.provider = provider;
        this.settings = settings;
    }

    @Override
    public Optional<String> getName() {
        return ofNullable(name);
    }

    @Override
    public Optional<String> getDescription() {
        return ofNullable(description);
    }

    @Override
    public <T> Optional<Class<T>> getProvider() {
        return ofNullable((Class<T>) provider);
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultConfigurationSettingsUnit)) {
            return false;
        }
        DefaultConfigurationSettingsUnit that = (DefaultConfigurationSettingsUnit) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return  "DefaultConfigurationSettingsUnit{" + "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", provider=" + provider +
                ", settings=" + settings +
                '}';
    }
}
