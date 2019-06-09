/*
 *
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
 *
 */

package org.jnosql.diana;


import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * This element represents a required order to be used in a query, it's has two attributes:
 * -- The name - the field's name to be sorted
 * -- The type - the way to be sorted
 *
 * @see Sort#of(String, SortType)
 * @see SortType
 */
public final class Sort {

    private final String name;

    private final SortType type;

    private Sort(String name, SortType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Creates a wew Sort instance to be used in a NoSQL query.
     *
     * @param name - the field name be used in a sort process
     * @param type - the way to be sorted
     * @return a sort instance
     * @throws NullPointerException when there are null parameters
     */
    public static Sort of(String name, SortType type) {
        requireNonNull(name, "name is required");
        requireNonNull(type, "type is required");
        return new Sort(name, type);
    }

    /**
     * Creates a new Sort of the type {@link SortType#ASC}
     * @param name the field name be used in a sort process
     * @return a sort instance
     * @throws NullPointerException when name is null
     */
    public static Sort asc(String name) {
        return new Sort(requireNonNull(name, "name is required"), SortType.ASC);
    }

    /**
     * Creates a new Sort of the type {@link SortType#DESC}
     * @param name the field name be used in a sort process
     * @return a sort instance
     * @throws NullPointerException when name is null
     */
    public static Sort desc(String name) {
        return new Sort(requireNonNull(name, "name is required"), SortType.DESC);
    }

    public String getName() {
        return name;
    }

    public SortType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sort sort = (Sort) o;
        return Objects.equals(name, sort.name) &&
                type == sort.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "Sort{" + "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    /**
     * The way to be sorted.
     *
     * @see Sort
     */
    public enum SortType {
        /**
         * The ascending way
         */
        ASC,
        /**
         * The descending way
         */
        DESC
    }
}
