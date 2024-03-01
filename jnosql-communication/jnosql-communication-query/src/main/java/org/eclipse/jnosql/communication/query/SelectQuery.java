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
 * Represents a select statement, which reads one or more fields for one or more entities.
 * It returns a result-set of the entities matching the request, where each entity contains the fields
 * corresponding to the query.
 */
public interface SelectQuery extends Query {
    /**
     * Retrieves the fields to be retrieved in this query. If this list is empty, the query will retrieve the entire entity.
     *
     * @return the list of fields
     */
    List<String> fields();

    /**
     * Retrieves the name of the entity.
     *
     * @return the entity name
     */
    String entity();

    /**
     * Retrieves the condition associated with this select query. If the condition is empty, the query may retrieve the entire entities.
     *
     * @return the {@link Where} entity, otherwise {@link Optional#empty()}
     */
    Optional<Where> where();

    /**
     * Retrieves the statement defining where the query should start.
     *
     * @return the number of entities to skip, otherwise a negative value or zero
     */
    long skip();

    /**
     * Retrieves the statement limiting the number of rows returned by a query.
     *
     * @return the maximum number of results, otherwise a negative value or zero
     */
    long limit();

    /**
     * Retrieves the list of orders used to sort the result-set in ascending or descending order.
     *
     * @return the list of orders
     */
    List<Sort<?>> orderBy();
}
