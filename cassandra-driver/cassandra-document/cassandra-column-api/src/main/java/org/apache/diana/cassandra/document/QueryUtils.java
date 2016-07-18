package org.apache.diana.cassandra.document;


import com.datastax.driver.core.querybuilder.*;
import org.apache.diana.api.Condition;
import org.apache.diana.api.column.Column;
import org.apache.diana.api.column.ColumnCondition;
import org.apache.diana.api.column.ColumnFamily;
import org.apache.diana.api.column.ColumnQuery;

import java.util.List;

import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;

final class QueryUtils {

    private QueryUtils() {
    }


    public static Insert insert(ColumnFamily columnFamily, String keyspace) {
        Insert insert = insertInto(keyspace, columnFamily.getName());
        columnFamily.getColumns().forEach(d -> insert.value(d.getName(), d.getValue().get()));
        return insert;
    }

    public static BuiltStatement add(ColumnQuery query, String keySpace) {
        String columnFamily = query.getColumnFamily();
        List<ColumnCondition> conditions = query.getConditions();
        if (conditions.isEmpty()) {
            return QueryBuilder.select().all().from(keySpace, columnFamily);
        }
        Select.Where where = QueryBuilder.select().all().from(keySpace, columnFamily).where();
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
        switch (condition) {
            case EQUALS:
                return QueryBuilder.eq(column.getName(), column.getValue().get());
            case GREATER_THAN:
                return QueryBuilder.gt(column.getName(), column.getValue().get());
            case GREATER_EQUALS_THAN:
                return QueryBuilder.gte(column.getName(), column.getValue().get());
            case LESSER_THAN:
                return QueryBuilder.lt(column.getName(), column.getValue().get());
            case LESSER_EQUALS_THAN:
                return QueryBuilder.lte(column.getName(), column.getValue().get());
            case IN:
                return QueryBuilder.in(column.getName(), column.getValue().get());
            case LIKE:
                return QueryBuilder.like(column.getName(), column.getValue().get());
            case AND:
            case OR:
            default:
                throw new UnsupportedOperationException("The columnCondition " + condition + " is not supported in cassandra document driver");

        }
    }


}
