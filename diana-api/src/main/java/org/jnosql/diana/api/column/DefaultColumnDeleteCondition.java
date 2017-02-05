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


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

/**
 * the default implementation of {@link ColumnDeleteCondition}
 */
class DefaultColumnDeleteCondition implements ColumnDeleteCondition {

    private final String columnFamily;

    private final ColumnCondition condition;

    private final List<String> columns = new ArrayList<>();

    private DefaultColumnDeleteCondition(String columnFamily, ColumnCondition condition) {
        this.columnFamily = columnFamily;
        this.condition = condition;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public ColumnCondition getCondition() {
        return condition;
    }

    @Override
    public List<String> getColumns() {
        return unmodifiableList(columns);
    }

    @Override
    public void add(String column) throws NullPointerException {
        this.columns.add(Objects.requireNonNull(column, "column null is required"));
    }

    static DefaultColumnDeleteCondition of(String columnFamily, ColumnCondition condition) throws NullPointerException {
        Objects.requireNonNull(columnFamily, "columnFamily is required");
        Objects.requireNonNull(condition, "condition is required");
        return new DefaultColumnDeleteCondition(columnFamily, condition);
    }
}
