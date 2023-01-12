/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package org.eclipse.jnosql.communication;


import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

/**
 * This element represents a required order to be used in a query, it has two attributes:
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
     * Returns the field name
     *
     * @return the field name
     */
    public String name() {
        return this.name;
    }

    /**
     * The {@link SortType}
     *
     * @return The {@link SortType}
     */
    public SortType type() {
        return this.type;
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
        return Objects.equals(name, sort.name) && type == sort.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "Sort{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    /**
     * Creates a wew Sort instance to be used in a NoSQL query.
     *
     * @param name - the field name be used in a sort process
     * @param type - the way to be sorted
     * @return a sort instance
     * @throws NullPointerException when there are null parameters
     */
    static Sort of(String name, SortType type) {
        requireNonNull(name, "name is required");
        requireNonNull(type, "type is required");
        return new Sort(name, type);
    }

    /**
     * Creates a new Sort of the type {@link SortType#ASC}
     *
     * @param name the field name be used in a sort process
     * @return a sort instance
     * @throws NullPointerException when name is null
     */
    static Sort asc(String name) {
        return of(name, SortType.ASC);
    }

    /**
     * Creates a new Sort of the type {@link SortType#DESC}
     *
     * @param name the field name be used in a sort process
     * @return a sort instance
     * @throws NullPointerException when name is null
     */
    static Sort desc(String name) {
        return of(name, SortType.DESC);
    }


}
