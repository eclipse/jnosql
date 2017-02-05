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


import org.jnosql.diana.api.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Defualt implementation of {@link ColumnQuery}
 */
class DefaultColumnQuery implements ColumnQuery {

    private final String columnFamily;

    private ColumnCondition condition;

    private final List<Sort> sorts = new ArrayList<>();

    private final List<String> columns = new ArrayList<>();

    private long limit = -1L;

    private long start;

    private DefaultColumnQuery(String columnFamily) {
        this.columnFamily = requireNonNull(columnFamily, "column family is required");
    }

    /**
     * Creates a {@link ColumnQuery}
     *
     * @param columnFamily - the name of column family to do a query
     * @return a {@link ColumnQuery} instance
     */
    static DefaultColumnQuery of(String columnFamily) {
        return new DefaultColumnQuery(columnFamily);
    }

    /**
     * Appends a new condition in the query using {@link ColumnCondition#and(ColumnCondition)}
     *
     * @param condition a condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    public ColumnQuery and(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        if (Objects.isNull(this.condition)) {
            this.condition = condition;
        } else {
            this.condition = this.condition.and(condition);
        }
        return this;
    }


    /**
     * Appends a new condition in the query using {@link ColumnCondition#or(ColumnCondition)}
     *
     * @param condition a condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    public ColumnQuery or(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        if (Objects.isNull(this.condition)) {
            this.condition = condition;
        } else {
            this.condition = this.condition.and(condition);
        }
        return this;
    }

    /**
     * Add the order how the result will returned
     *
     * @param sort the order way
     * @return the same instance with a sort added
     */
    public ColumnQuery addSort(Sort sort) throws NullPointerException {
        this.sorts.add(requireNonNull(sort, "Sort is required"));
        return this;
    }

    /**
     * Add column to be either retrieve or deleted, if empty will either returns
     * all elements in a find query or delete all elements in a column family entity.
     *
     * @param column the column name
     * @return the same instance with a column added
     */
    public ColumnQuery addColumn(String column) throws NullPointerException {
        this.columns.add(requireNonNull(column, "column is required"));
        return this;
    }

    /**
     * The column family name
     *
     * @return the column family name
     */
    public String getColumnFamily() {
        return columnFamily;
    }

    /**
     * The conditions that contains in this {@link ColumnQuery}
     *
     * @return the conditions
     */
    public Optional<ColumnCondition> getCondition() {
        return Optional.ofNullable(condition);
    }

    public List<String> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    /**
     * The sorts that contains in this {@link ColumnQuery}
     *
     * @return the sorts
     */
    public List<Sort> getSorts() {
        return Collections.unmodifiableList(sorts);
    }

    /**
     * Returns the max number of row in a query
     *
     * @return the limit to be used in a query
     */
    public long getLimit() {
        return limit;
    }

    /**
     * Sets the max number of row in a query, if negative the value will ignored
     *
     * @param limit the new limit to query
     */
    public void setLimit(long limit) {
        this.limit = limit;
    }

    /**
     * Gets when the result starts
     *
     * @return the start
     */
    public long getStart() {
        return start;
    }

    /**
     * Setter to start a query
     *
     * @param start the starts
     */
    public void setStart(long start) {
        this.start = start;
    }

    @Override
    public ColumnDeleteQuery toDeleteQuery() {
        ColumnDeleteQuery columnDeleteQuery = ColumnDeleteQuery.of(columnFamily, condition);
        if (!columns.isEmpty()) {
            columnDeleteQuery.addAll(columns);
        }
        return columnDeleteQuery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ColumnQuery that = (ColumnQuery) o;
        return Objects.equals(columnFamily, that.getColumnFamily()) &&
                Objects.equals(condition, that.getCondition()) &&
                Objects.equals(sorts, that.getSorts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnFamily, condition, sorts);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultColumnQuery{");
        sb.append("columnFamily='").append(columnFamily).append('\'');
        sb.append(", condition=").append(condition);
        sb.append(", sorts=").append(sorts);
        sb.append('}');
        return sb.toString();
    }
}
