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

package org.jnosql.diana.cassandra.column;


import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Ordering;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnFamilyEntity;
import org.jnosql.diana.api.column.ColumnQuery;

import java.util.List;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static com.datastax.driver.core.querybuilder.QueryBuilder.asc;
import static com.datastax.driver.core.querybuilder.QueryBuilder.desc;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static org.jnosql.diana.api.Sort.SortType.ASC;

final class QueryUtils {

    private static final Function<Sort, Ordering> SORT_ORDERING_FUNCTION = sort -> {
        if (ASC.equals(sort.getType())) {
            return asc(sort.getName());
        } else {
            return desc(sort.getName());
        }
    };

    private QueryUtils() {
    }


    public static Insert insert(ColumnFamilyEntity entity, String keyspace) {
        Insert insert = insertInto(keyspace, entity.getName());
        entity.getColumns().forEach(d -> insert.value(d.getName(), d.getValue().get()));
        return insert;
    }

    public static BuiltStatement add(ColumnQuery query, String keySpace) {
        String columnFamily = query.getColumnFamily();
        List<ColumnCondition> conditions = query.getConditions();
        if (conditions.isEmpty()) {
            return QueryBuilder.select().all().from(keySpace, columnFamily);
        }
        Select.Where where = QueryBuilder.select().all().from(keySpace, columnFamily).where();
        if (query.getLimit() > 0) {
            where.limit((int) query.getLimit());
        }
        if (!query.getSorts().isEmpty()) {
            where.orderBy(query.getSorts().stream().map(SORT_ORDERING_FUNCTION).toArray(Ordering[]::new));
        }
        conditions.stream().map(condition -> add(condition)).forEach(where::and);
        return where;
    }

    public static BuiltStatement delete(ColumnQuery query, String keySpace) {
        String columnFamily = query.getColumnFamily();
        List<ColumnCondition> conditions = query.getConditions();

        if (conditions.isEmpty()) {
            return QueryBuilder.delete().all().from(keySpace, query.getColumnFamily());
        }
        Delete.Where where = QueryBuilder.delete().all().from(keySpace, query.getColumnFamily()).where();
        conditions.stream().map(condition -> add(condition)).forEach(where::and);
        return where;
    }

    private static Clause add(ColumnCondition columnCondition) {
        Column column = columnCondition.getColumn();
        Condition condition = columnCondition.getCondition();
        Object value = column.getValue().get();
        switch (condition) {
            case EQUALS:
                return QueryBuilder.eq(column.getName(), value);
            case GREATER_THAN:
                return QueryBuilder.gt(column.getName(), value);
            case GREATER_EQUALS_THAN:
                return QueryBuilder.gte(column.getName(), value);
            case LESSER_THAN:
                return QueryBuilder.lt(column.getName(), value);
            case LESSER_EQUALS_THAN:
                return QueryBuilder.lte(column.getName(), value);
            case IN:
                return QueryBuilder.in(column.getName(), getIinValue(value));
            case LIKE:
                return QueryBuilder.like(column.getName(), value);
            case AND:
            case OR:
            default:
                throw new UnsupportedOperationException("The columnCondition " + condition +
                        " is not supported in cassandra column driver");
        }
    }

    private static Object[] getIinValue(Object value) {
        if (Iterable.class.isInstance(value)) {
            Iterable values = Iterable.class.cast(value);
            return StreamSupport.stream(values.spliterator(), false).toArray(Object[]::new);
        }
        return new Object[]{value};
    }


}
