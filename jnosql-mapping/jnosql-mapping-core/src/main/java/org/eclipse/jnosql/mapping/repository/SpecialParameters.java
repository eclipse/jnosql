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

    public Optional<Pageable> getPageable() {
        return Optional.ofNullable(pageable);
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public boolean isEmpty() {
        return this.sorts.isEmpty();
    }
}
