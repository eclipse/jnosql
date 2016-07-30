/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api.column;


import org.jnosql.diana.api.Condition;

import static org.jnosql.diana.api.Condition.EQUALS;
import static org.jnosql.diana.api.Condition.GREATER_EQUALS_THAN;
import static org.jnosql.diana.api.Condition.GREATER_THAN;
import static org.jnosql.diana.api.Condition.IN;
import static org.jnosql.diana.api.Condition.LESSER_EQUALS_THAN;
import static org.jnosql.diana.api.Condition.LESSER_THAN;
import static org.jnosql.diana.api.Condition.LIKE;

/**
 * An unit condition  to run a column family query
 *
 * @author Otávio Santana
 * @see ColumnFamilyManager#find(ColumnQuery)
 */
public interface ColumnCondition {

    /**
     * Gets the column to be used in the query
     *
     * @return a column instance
     */
    Column getColumn();

    /**
     * Gets the conditions to be used in the query
     *
     * @return a Condition instance
     * @see Condition
     */
    Condition getCondition();

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#EQUALS}, it means a query will scanning to a
     * column family that has the same name and equals value informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#EQUALS}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition eq(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, EQUALS);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#GREATER_THAN}, it means a query will scanning to a
     * column family that has the same name and the value  greater than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition gt(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, GREATER_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#GREATER_EQUALS_THAN},
     * it means a query will scanning to a column family that has the same name and the value
     * greater or equals than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#GREATER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition gte(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, GREATER_EQUALS_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LESSER_THAN}, it means a query will scanning to a
     * column family that has the same name and the value  lesser than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition lt(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, LESSER_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LESSER_EQUALS_THAN},
     * it means a query will scanning to a column family that has the same name and the value
     * lesser or equals than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LESSER_EQUALS_THAN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition lte(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, LESSER_EQUALS_THAN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#IN}, it means a query will scanning to a
     * column family that has the same name and the value is within informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#IN}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition in(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, IN);
    }

    /**
     * Creates a {@link ColumnCondition} that has a {@link Condition#LIKE}, it means a query will scanning to a
     * column family that has the same name and the value  is like than informed in this column.
     *
     * @param column a column instance
     * @return a {@link ColumnCondition} with {@link Condition#LIKE}
     * @throws NullPointerException when column is null
     */
    static ColumnCondition like(Column column) throws NullPointerException {
        return DefaultColumnCondition.of(column, LIKE);
    }

}
