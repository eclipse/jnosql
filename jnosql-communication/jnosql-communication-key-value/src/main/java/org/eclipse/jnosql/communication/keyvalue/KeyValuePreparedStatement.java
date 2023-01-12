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
package org.eclipse.jnosql.communication.keyvalue;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Value;

import java.util.Optional;
import java.util.stream.Stream;

public interface KeyValuePreparedStatement {

    /**
     * Binds an argument to a positional parameter.
     *
     * @param name  the parameter name
     * @param value the parameter value
     * @return the same query instance
     * @throws NullPointerException     when there is null parameter
     */
    KeyValuePreparedStatement bind(String name, Object value);

    /**
     * Executes a query and return the result as {@link Stream}
     *
     * @return The result as Stream, if either delete or put it will return an empty stream
     */
    Stream<Value> getResult();

    /**
     * Returns the result as a single element otherwise it will return an {@link Optional#empty()}
     *
     * @return the single result
     * @throws NonUniqueResultException when the result has more than one entity
     */
    Optional<Value> getSingleResult();
}
