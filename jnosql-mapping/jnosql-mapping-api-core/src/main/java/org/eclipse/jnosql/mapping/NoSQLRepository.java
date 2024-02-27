/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Delete;

/**
 * Interface for NoSQL repositories, providing additional operations beyond basic {@link BasicRepository}.
 *
 * @param <T> the type of the entity managed by this repository
 * @param <K> the type of the entity's primary key
 */
public interface NoSQLRepository<T, K> extends BasicRepository<T, K> {

    /**
     * Deletes all persistent entities of the primary entity type that are managed by the repository.
     *
     * @throws UnsupportedOperationException for Key-Value and Wide-Column databases that are not capable of the {@code deleteAll} operation.
     */
    @Delete
    void deleteAll();

    /**
     * Retrieves the total number of persistent entities of the specified type in the database.
     *
     * @return the total number of entities.
     * @throws UnsupportedOperationException for Key-Value and Wide-Column databases that are not capable of the {@code count} operation.
     */
    long countBy();
}
