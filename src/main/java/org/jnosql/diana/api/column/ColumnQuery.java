/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package org.jnosql.diana.api.column;


import org.jnosql.diana.api.Sort;

import java.util.List;
import java.util.Optional;

/**
 * Class that contains information to do a query to {@link ColumnEntity}
 *
 * @see ColumnFamilyManager#find(ColumnQuery)
 * @see ColumnCondition
 * @see Sort
 */
public interface ColumnQuery {

    /**
     * Creates a {@link ColumnQuery}
     *
     * @param columnFamily - the name of column family to do a query
     * @return a {@link ColumnQuery} instance
     */
    static ColumnQuery of(String columnFamily) {
        return DefaultColumnQuery.of(columnFamily);
    }

    /**
     * Appends a new condition in the query using {@link ColumnCondition#and(ColumnCondition)}
     *
     * @param condition a condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    ColumnQuery and(ColumnCondition condition);


    /**
     * Appends a new condition in the query using {@link ColumnCondition#or(ColumnCondition)}
     *
     * @param condition a condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    ColumnQuery or(ColumnCondition condition) throws NullPointerException;

    /**
     * Sets the column query
     *
     * @param condition
     * @return this instance
     * @throws NullPointerException when condition is null
     */
    ColumnQuery withCondition(ColumnCondition condition) throws NullPointerException;

    /**
     * Set the position of the first result to retrieve.
     *
     * @param firstResult the firstResult
     * @return this instance
     */
    ColumnQuery withFirstResult(long firstResult);

    /**
     * Set the maximum number of results to retrieve.
     *
     * @param maxResults
     * @return this instance
     */
    ColumnQuery withMaxResults(long maxResults);

    /**
     * @return The maximum number of results the query object was set to retrieve.
     */
    long getMaxResults();

    /**
     * @return The position of the first result the query object was set to retrieve.
     */
    long getFirstResult();


    /**
     * Add the order how the result will returned
     *
     * @param sort the order way
     * @return the same instance with a sort added
     */
    ColumnQuery addSort(Sort sort) throws NullPointerException;

    /**
     * Add column to be either retrieve or deleted, if empty will either returns
     * all elements in a find query or delete all elements in a column family entity.
     *
     * @param column the column name
     * @return the same instance with a column added
     */
    ColumnQuery addColumn(String column) throws NullPointerException;

    /**
     * The column family name
     *
     * @return the column family name
     */
    String getColumnFamily();

    /**
     * The conditions that contains in this {@link ColumnQuery}
     *
     * @return the conditions
     */
    Optional<ColumnCondition> getCondition();

    /**
     * Returns the columns
     *
     * @return the columns
     */
    List<String> getColumns();

    /**
     * The sorts that contains in this {@link ColumnQuery}
     *
     * @return the sorts
     */
    List<Sort> getSorts();


    /**
     * Converts this to {@link ColumnDeleteQuery}
     *
     * @return the {@link ColumnDeleteQuery} instance
     * @throws NullPointerException if {@link ColumnQuery#getCondition()} still null
     */
    ColumnDeleteQuery toDeleteQuery() throws NullPointerException;

}
