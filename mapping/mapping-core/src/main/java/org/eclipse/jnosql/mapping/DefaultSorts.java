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
package org.eclipse.jnosql.mapping;

import jakarta.nosql.Sort;
import jakarta.nosql.mapping.Sorts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link Sorts}
 */
final class DefaultSorts implements Sorts {

    private final List<Sort> sorts = new ArrayList<>();

    @Override
    public Sorts asc(String name) {
        requireNonNull(name, "name is required");
        sorts.add(Sort.asc(name));
        return this;
    }

    @Override
    public Sorts desc(String name) {
        requireNonNull(name, "name is required");
        sorts.add(Sort.desc(name));
        return this;
    }

    @Override
    public Sorts add(Sort sort) {
        requireNonNull(sort, "sort is required");
        sorts.add(sort);
        return this;
    }

    @Override
    public Sorts remove(Sort sort) {
        requireNonNull(sort, "sort is required");
        sorts.removeIf(sort::equals);
        return this;
    }

    @Override
    public List<Sort> getSorts() {
        return unmodifiableList(sorts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultSorts that = (DefaultSorts) o;
        return Objects.equals(sorts, that.sorts);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sorts);
    }

    @Override
    public String toString() {
        return "DefaultSorts{" +
                "sorts=" + sorts +
                '}';
    }
}
