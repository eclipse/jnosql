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


import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.TypeReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.jnosql.diana.api.Condition.AND;
import static org.jnosql.diana.api.Condition.NOT;
import static org.jnosql.diana.api.Condition.OR;
import static org.jnosql.diana.api.Condition.SUBQUERY;

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
        Column column = Column.of(AND.getNameField(), asList(conditions));
        return DefaultColumnCondition.of(column, AND);
    }


    static DefaultColumnCondition or(ColumnCondition... conditions) throws NullPointerException {
        requireNonNull(conditions, "condition is required");
        Column column = Column.of(OR.getNameField(), asList(conditions));
        return DefaultColumnCondition.of(column, OR);
    }

    static DefaultColumnCondition subquery(ColumnQuery query) throws NullPointerException {
        requireNonNull(query, "query is required");
        Column column = Column.of(SUBQUERY.getNameField(), query);
        return DefaultColumnCondition.of(column, SUBQUERY);
    }

    static DefaultColumnCondition between(Column column) {
        Objects.requireNonNull(column, "column is required");
        Object value = column.get();
        checkIterableClause(value);
        return new DefaultColumnCondition(column, Condition.BETWEEN);
    }

    private static void checkIterableClause(Object value) {
        if (Iterable.class.isInstance(value)) {
            int count = 0;
            Iterator iterator = Iterable.class.cast(value).iterator();
            while (iterator.hasNext()) {
                iterator.next();
                count++;
                if (count > 2) {
                    throw new IllegalArgumentException("On Columncondition#between you must use an iterable" +
                            " with two elements");
                }
            }
            if (count != 2) {
                throw new IllegalArgumentException("On Columncondition#between you must use an iterable" +
                        " with two elements");
            }

        } else {
            throw new IllegalArgumentException("On Columncondition#between you must use an iterable" +
                    " with two elements istead of class: " + value.getClass().getName());
        }
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
        if (AND.equals(this.condition)) {
            Column column = getConditions(condition, AND);
            return new DefaultColumnCondition(column, AND);
        }
        return DefaultColumnCondition.and(this, condition);
    }

    @Override
    public ColumnCondition negate() {
        if (NOT.equals(this.condition)) {
            return this.column.get(ColumnCondition.class);
        } else {
            Column column = Column.of(NOT.getNameField(), this);
            return new DefaultColumnCondition(column, NOT);
        }

    }

    @Override
    public ColumnCondition or(ColumnCondition condition) {
        requireNonNull(condition, "Condition is required");
        if (OR.equals(this.condition)) {
            Column column = getConditions(condition, OR);
            return new DefaultColumnCondition(column, OR);
        }
        return DefaultColumnCondition.or(this, condition);
    }

    private Column getConditions(ColumnCondition columnCondition, Condition condition) {
        List<ColumnCondition> conditions = new ArrayList<>();
        conditions.addAll(column.get(new TypeReference<List<ColumnCondition>>() {
        }));
        conditions.add(columnCondition);
        return Column.of(condition.getNameField(), conditions);
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
