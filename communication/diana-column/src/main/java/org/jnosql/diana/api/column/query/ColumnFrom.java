/*
 *
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
 *
 */
package org.jnosql.diana.api.column.query;


/**
 * The ColumnFrom Query
 */
public interface ColumnFrom extends ColumnQueryBuild {


    /**
     * Starts a new condition defining the  column name
     *
     * @param name the column name
     * @return a new {@link ColumnNameCondition}
     * @throws NullPointerException when name is null
     */
    ColumnNameCondition where(String name);

    /**
     * Defines the position of the first result to retrieve.
     *
     * @param skip the first result to retrive
     * @return a query with first result defined
     */
    ColumnSkip skip(long skip);


    /**
     * Defines the maximum number of results to retrieve.
     *
     * @param limit the limit
     * @return a query with the limit defined
     */
    ColumnLimit limit(long limit);

    /**
     * Add the order how the result will returned
     *
     * @param name the name to be ordered
     * @return a query with the sort defined
     * @throws NullPointerException when name is null
     */
    ColumnOrder orderBy(String name);


}
