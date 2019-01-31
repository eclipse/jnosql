/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis;

/**
 * Interface for pagination information.
 */
public interface Pagination {

    /**
     * Returns the max number of row in a query
     *
     * @return the limit to be used in a query
     */
    long getLimit();

    /**
     * Gets when the result starts
     *
     * @return the start
     */
    long getSkip();

    /**
     * Creates a default pagination
     *
     * @param start the start
     * @param limit the limit
     * @return the pagination instance
     */
    static Pagination of(long start, long limit) {
        return new DefaultPagination(limit, start);
    }
}
