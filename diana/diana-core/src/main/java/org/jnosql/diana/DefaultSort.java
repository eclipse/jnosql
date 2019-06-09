/*
 *
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
 *
 */
package org.jnosql.diana;

import jakarta.nosql.Sort;
import jakarta.nosql.SortType;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

final class DefaultSort implements Sort {

    private final String name;
    private final SortType type;

    private DefaultSort(String name, SortType type) {
        this.name = name;
        this.type = type;
    }

    public static Sort of(String name, SortType type) {
        requireNonNull(name, "name is required");
        requireNonNull(type, "type is required");
        return new DefaultSort(name, type);
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
        return Objects.equals(name, sort.getName()) &&
                type == sort.getType();
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
}