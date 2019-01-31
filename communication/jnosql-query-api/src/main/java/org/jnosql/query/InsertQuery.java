/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.jnosql.query;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Inserting data for an entity is done using an <b>INSERT</b> statement.
 */
public interface InsertQuery extends Query {

    /**
     * The entity name
     * @return the entity
     */
    String getEntity();

    /**
     * This duration set a time for data in an entity to expire. It defines the time to live of an object in a database.
     * @return the TTL otherwise {@link Optional#empty()}
     */
    Optional<Duration> getTtl();

    /**
     * The list of changes as conditions. Each condition will use the equals operator, {@link Operator#EQUALS},
     *  e.g., name = "any name"
     * @return the conditions
     */
    List<Condition> getConditions();
}
