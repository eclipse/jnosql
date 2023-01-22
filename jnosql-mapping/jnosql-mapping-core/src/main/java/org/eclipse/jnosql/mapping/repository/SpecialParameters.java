/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.repository;

import jakarta.data.repository.Pageable;
import jakarta.data.repository.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The repository features has support for specific types like Pageable and Sort,
 * to apply pagination and sorting to your queries dynamically.
 */
public final class SpecialParameters {

    private final Pageable pageable;

    private final List<Sort> sorts;

    private SpecialParameters(Pageable pageable, List<Sort> sorts) {
        this.pageable = pageable;
        this.sorts = sorts;
    }

    /**
     * Returns the pageable as optional.
     * @return a {@link Pageable} or {@link Optional#empty()} when there is not Pageable instance
     */
    public Optional<Pageable> getPageable() {
        return Optional.ofNullable(pageable);
    }

    /**
     * Returns the sorts including {@link Pageable#sorts()} appended
     * @return the sorts as list
     */
    public List<Sort> getSorts() {
        return sorts;
    }

    /**
     * Returns true when {@link SpecialParameters#getPageable()} is empty and
     * {@link SpecialParameters#isSortEmpty()} is true
     * @return when there is no sort and Pageable
     */
    public boolean isEmpty() {
        return this.sorts.isEmpty() && pageable == null;
    }

    /**
     * Return true when there is no sorts
     * @return the sort
     */
    public boolean isSortEmpty() {
        return this.sorts.isEmpty();
    }

    static SpecialParameters of(Object[] parameters) {
        return new SpecialParameters(null, Collections.emptyList());
    }


}
