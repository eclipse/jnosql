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
package org.eclipse.jnosql.mapping.metadata;

import java.util.Objects;

/**
 * The mapping information about {@link org.eclipse.jnosql.mapping.Inheritance}
 *
 * @param discriminatorValue  the information from the annotation {@link org.eclipse.jnosql.mapping.DiscriminatorValue}
 *                            from entity or the {@link Class#getSimpleName()} of the entity
 * @param discriminatorColumn the information parent from the annotation {@link org.eclipse.jnosql.mapping.DiscriminatorColumn}
 *                            * or the "type"
 * @param parent              the parent class
 * @param entity              the entity class
 */
public record InheritanceMetadata(String discriminatorValue, String discriminatorColumn, Class<?> parent,
                                  Class<?> entity) {

    /**
     * Checks if the parent is equals to the parameter
     *
     * @param parent the parameter
     * @return if the parent is equals or not
     * @throws NullPointerException if the parent is null
     */
    public boolean isParent(Class<?> parent) {
        Objects.requireNonNull(parent, "parent is required");
        return this.parent.equals(parent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InheritanceMetadata that = (InheritanceMetadata) o;
        return Objects.equals(discriminatorValue, that.discriminatorValue)
                && Objects.equals(discriminatorColumn, that.discriminatorColumn)
                && Objects.equals(parent, that.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discriminatorValue, discriminatorColumn, parent);
    }

    @Override
    public String toString() {
        return "InheritanceMetadata{" +
                "discriminatorValue='" + discriminatorValue + '\'' +
                ", discriminatorColumn='" + discriminatorColumn + '\'' +
                ", parent=" + parent +
                '}';
    }
}
