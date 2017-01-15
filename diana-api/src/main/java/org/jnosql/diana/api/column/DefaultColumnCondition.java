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

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.jnosql.diana.api.Condition.AND;
import static org.jnosql.diana.api.Condition.NOT;
import static org.jnosql.diana.api.Condition.OR;

/**
 * The default implementation of {@link ColumnCondition}
 */
class DefaultColumnCondition implements ColumnCondition {

    private final Column column;

    private final Condition condition;

    private DefaultColumnCondition(Column column, Condition condition) {
        this.column = column;
        this.condition = condition;
    }

    public static DefaultColumnCondition of(Column column, Condition condition) {
        return new DefaultColumnCondition(requireNonNull(column, "Column is required"), condition);
    }

    static DefaultColumnCondition and(ColumnCondition... conditions) throws NullPointerException {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(AND.getNameField(), conditions);
        return DefaultColumnCondition.of(column, AND);
    }


    static DefaultColumnCondition or(ColumnCondition... conditions) throws NullPointerException {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(OR.getNameField(), conditions);
        return DefaultColumnCondition.of(column, OR);
    }

    public Column getColumn() {
        return column;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public ColumnCondition and(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "Conditions is required");
        return ColumnCondition.and(this, condition);
    }

    @Override
    public ColumnCondition negate() {
        Column column = Column.of(NOT.getNameField(), this);
        return new DefaultColumnCondition(column, NOT);
    }

    @Override
    public ColumnCondition or(ColumnCondition condition) {
        requireNonNull(condition, "Condition is required");
        return ColumnCondition.or(this, condition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultColumnCondition that = (DefaultColumnCondition) o;
        return Objects.equals(column, that.column) &&
                condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, condition);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultColumnCondition{");
        sb.append("column=").append(column);
        sb.append(", condition=").append(condition);
        sb.append('}');
        return sb.toString();
    }
}
