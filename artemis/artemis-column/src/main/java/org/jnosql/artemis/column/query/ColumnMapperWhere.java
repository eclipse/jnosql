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
package org.jnosql.artemis.column.query;



/**
 * The Column Where whose define the condition in the query.
 */
public interface ColumnMapperWhere  extends ColumnMapperQueryBuild {


    /**
     * Starts a new condition in the select using
     * {@link org.jnosql.diana.api.column.ColumnCondition#and(org.jnosql.diana.api.column.ColumnCondition)}
     *
     * @param name a condition to be added
     * @return the same {@link ColumnMapperNameCondition} with the condition appended
     * @throws NullPointerException when condition is null
     */
    ColumnMapperNameCondition and(String name);

    /**
     * Appends a new condition in the select using
     * {@link org.jnosql.diana.api.column.ColumnCondition#or(org.jnosql.diana.api.column.ColumnCondition)}
     *
     * @param name a condition to be added
     * @return the same {@link ColumnMapperNameCondition} with the condition appended
     * @throws NullPointerException when condition is null
     */
    ColumnMapperNameCondition or(String name);

    /**
     * Defines the position of the first result to retrieve.
     *
     * @param skip the first result to retrive
     * @return a query with first result defined
     */
    ColumnMapperSkip skip(long skip);


    /**
     * Defines the maximum number of results to retrieve.
     *
     * @param limit the limit
     * @return a query with the limit defined
     */
    ColumnMapperLimit limit(long limit);

    /**
     * Add the order how the result will returned
     *
     * @param name the name to order
     * @return a query with the sort defined
     * @throws NullPointerException when name is null
     */
    ColumnMapperOrder orderBy(String name);
}
