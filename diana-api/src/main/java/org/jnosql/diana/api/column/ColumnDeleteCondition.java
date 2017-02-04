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

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A unit that has the columnFamily and condition to delete from conditions
 *
 * @see ColumnDeleteCondition#of(String, ColumnCondition).
 * This instance will be used on:
 * <p>{@link ColumnFamilyManager#delete(ColumnDeleteCondition)}</p>
 * <p>{@link ColumnFamilyManagerAsync#delete(ColumnDeleteCondition)}</p>
 * <p>{@link ColumnFamilyManagerAsync#delete(ColumnDeleteCondition, Consumer)}</p>
 */
public class ColumnDeleteCondition {

    private final String columnFamily;

    private final ColumnCondition condition;

    private ColumnDeleteCondition(String columnFamily, ColumnCondition condition) {
        this.columnFamily = columnFamily;
        this.condition = condition;
    }

    /**
     * getter the columnFamily name
     *
     * @return the columnFamily name
     */
    public String getColumnFamily() {
        return columnFamily;
    }

    /**
     * getter the condition
     *
     * @return the condition
     */
    public ColumnCondition getCondition() {
        return condition;
    }

    /**
     * Creates a instance of column family
     *
     * @param columnFamily the column family name
     * @param condition the condition
     * @return an {@link ColumnDeleteCondition}
     * @throws NullPointerException when either columnFamily
     */
    public ColumnDeleteCondition of(String columnFamily, ColumnCondition condition) throws NullPointerException {
        Objects.requireNonNull(columnFamily, "columnFamily is required");
        Objects.requireNonNull(condition, "condition is required");
        return new ColumnDeleteCondition(columnFamily, condition);
    }
}
