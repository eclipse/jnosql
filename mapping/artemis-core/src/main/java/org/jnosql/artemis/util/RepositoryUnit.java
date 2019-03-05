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
import org.jnosql.artemis.DatabaseType;

import java.util.Objects;

/**
 * An instance that has a class of repository and a ConfigurationUnit
 */
public final class RepositoryUnit {

    private final Class<?> repository;

    private final ConfigurationUnit unit;

    private RepositoryUnit(Class<?> repository, ConfigurationUnit unit) {
        this.repository = repository;
        this.unit = unit;
    }

    public Class<?> getRepository() {
        return repository;
    }

    public ConfigurationUnit getUnit() {
        return unit;
    }

    /**
     * @return the {@link ConfigurationUnit#database()}
     */
    public String getDatabase() {
        return unit.database();
    }

    /**
     * @return Returns true if the repository unit is of key type
     */
    public boolean isKey() {
        return isType(DatabaseType.KEY_VALUE);
    }

    /**
     * @return Returns true if the repository unit is of Graph type
     */
    public boolean isGraph() {
        return isType(DatabaseType.GRAPH);
    }

    /**
     * @return Returns true if the repository unit is of Document type
     */
    public boolean isDocument() {
        return isType(DatabaseType.DOCUMENT);
    }

    private boolean isType(DatabaseType graph) {
        DatabaseType type = unit.repository();
        return graph.equals(type) || DatabaseType.SHARED.equals(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RepositoryUnit that = (RepositoryUnit) o;
        return Objects.equals(repository, that.repository) &&
                unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(repository, unit);
    }

    @Override
    public String toString() {
        return "RepositoryUnit{" +
                "repository=" + repository +
                ", unit=" + unit +
                '}';
    }

    /**
     * Creates a {@link RepositoryUnit} instance
     *
     * @param repository the repository class
     * @param unit       the database type
     * @return a instance
     * @throws NullPointerException when there is null parameter
     */
    public static RepositoryUnit of(Class<?> repository, ConfigurationUnit unit) {
        Objects.requireNonNull(repository, "repository is required");
        Objects.requireNonNull(unit, "unit is required");
        return new RepositoryUnit(repository, unit);
    }


}
