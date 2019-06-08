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
package org.jnosql.artemis.column;


import org.jnosql.diana.api.column.ColumnQuery;

/**
 * When a column query is executed this event if fired
 */
public interface ColumnQueryExecute {

    /**
     * The ColumnQuery before executed
     *
     * @return the {@link ColumnQuery}
     */
    ColumnQuery getQuery();


    /**
     * Returns a ColumnQueryExecute instance
     *
     * @param query the query
     * @return a ColumnQueryExecute instance
     */
    static ColumnQueryExecute of(ColumnQuery query) {
        return new DefaultColumnQueryExecute(query);
    }
}
