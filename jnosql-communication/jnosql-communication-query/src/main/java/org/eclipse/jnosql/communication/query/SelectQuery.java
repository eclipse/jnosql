/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.data.Sort;

import java.util.List;
import java.util.Optional;

/**
 * Represents a select statement for querying a NoSQL database, which retrieves specified fields
 * from one or more entities. This interface defines the structure of a select query that can
 * return a result-set comprising entities that match the specified conditions. Each returned entity
 * will include only the fields specified in the query, or all fields if no specific fields are requested.
 */
public interface SelectQuery extends Query {

    /**
     * Retrieves a list of field names to be retrieved by this query. If the list is empty,
     * the query is understood to retrieve all fields of the entity.
     *
     * @return a list of field names; if empty, indicates all fields should be retrieved
     */
    List<String> fields();

    /**
     * Retrieves the name of the entity on which the query is to be executed.
     *
     * @return the name of the entity as a string
     */
    String entity();

    /**
     * Retrieves the condition associated with this select query. The condition defines
     * the constraints that determine which entities are included in the result-set.
     * If no condition is provided, all entities of the specified type may be retrieved.
     *
     * @return an {@link Optional} describing the {@link Where} condition of the query;
     *         {@link Optional#empty()} if no conditions are specified
     */
    Optional<Where> where();

    /**
     * Retrieves the offset of the first row to return, allowing pagination over the result-set.
     * A negative value or zero implies that no offset is applied.
     *
     * @return the number of entities to skip before starting to return the results
     */
    long skip();

    /**
     * Retrieves the limit on the number of rows to return from the query, useful for capping
     * the result-set for performance or practical usage considerations.
     * A negative value or zero implies no limit.
     *
     * @return the maximum number of results the query should return
     */
    long limit();

    /**
     * Retrieves the list of sorting specifications used to order the result-set.
     * The order can be ascending or descending, based on one or more fields.
     *
     * @return a list of {@link Sort} objects defining the order of results;
     *         never null but may be empty, indicating no specific ordering is requested
     */
    List<Sort<?>> orderBy();

    /**
     * Determines if the query should count the number of entities that match the query
     * criteria without actually retrieving the entities themselves.
     *
     * @return true if the query should only count matching entities, false if it should
     *         retrieve the entities
     */
    boolean isCount();
}
