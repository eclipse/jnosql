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
package org.jnosql.artemis.util;

import org.jnosql.artemis.ConfigurationUnit;

import java.util.Objects;

/**
 * An instance that has a class of repository and a ConfigurationUnit
 */
public final class ConfigurationUnitRepository {

    private final Class<?> repository;

    private final ConfigurationUnit unit;

    private ConfigurationUnitRepository(Class<?> repository, ConfigurationUnit unit) {
        this.repository = repository;
        this.unit = unit;
    }

    public Class<?> getRepository() {
        return repository;
    }

    public ConfigurationUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigurationUnitRepository that = (ConfigurationUnitRepository) o;
        return Objects.equals(repository, that.repository) &&
                unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(repository, unit);
    }

    @Override
    public String toString() {
        return "ConfigurationUnitRepository{" +
                "repository=" + repository +
                ", unit=" + unit +
                '}';
    }

    /**
     * Creates a {@link ConfigurationUnitRepository} instance
     *
     * @param repository the repository class
     * @param unit       the database type
     * @return a instance
     * @throws NullPointerException when there is null parameter
     */
    public static ConfigurationUnitRepository of(Class<?> repository, ConfigurationUnit unit) {
        Objects.requireNonNull(repository, "repository is required");
        Objects.requireNonNull(unit, "unit is required");
        return new ConfigurationUnitRepository(repository, unit);
    }

}
