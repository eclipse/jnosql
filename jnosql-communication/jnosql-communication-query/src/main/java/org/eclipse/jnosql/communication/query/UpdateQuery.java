/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query;

import java.util.List;
import java.util.Optional;

/**
 * Represents an update operation within a NoSQL database context. This interface defines
 * the structure of an update query that modifies fields of entities that match specified conditions.
 * Update queries allow setting new values for various fields of an entity or entities.
 */
public interface UpdateQuery extends Query {

    /**
     * Retrieves the name of the entity on which the query is to be executed.
     *
     * @return the name of the entity as a string
     */
    String entity();

    /**
     * Retrieves a list of update items that specify which fields to modify and their new values.
     * Each {@link UpdateItem} in the list represents a specific field and the value it should be set to.
     * This method provides the details necessary to construct the update part of the query.
     *
     * @return a list of {@link UpdateItem} objects representing the fields to update and their new values;
     *         never null but can be empty, which indicates that no fields are specified for updating
     */
    List<UpdateItem> set();

    /**
     * Retrieves the condition that specifies which entities should be updated. The condition,
     * defined using a {@link Where} clause, acts as a filter that determines which entities
     * in the database meet the criteria for updating. If no condition is provided, it implies
     * that all entities of the specified type may potentially be updated, depending on the
     * database implementation and the query execution context.
     *
     * @return an {@link Optional} containing the {@link Where} condition if specified; otherwise,
     *         {@link Optional#empty()}, which indicates that no specific conditions have been set,
     *         potentially impacting all entities of the type
     */
    Optional<Where> where();
}
