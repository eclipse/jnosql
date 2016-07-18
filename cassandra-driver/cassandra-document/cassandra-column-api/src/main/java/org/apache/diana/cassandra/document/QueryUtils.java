package org.apache.diana.cassandra.document;


import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.apache.diana.api.column.Column;
import org.apache.diana.api.column.ColumnEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

final class QueryUtils {

    private QueryUtils() {
    }


    public static Insert insert(ColumnEntity columnEntity, String keyspace) {
        Insert insert = QueryBuilder.insertInto(keyspace, columnEntity.getColumnFamily());
        columnEntity.getColumns().forEach(d -> insert.value(d.getName(), d.getValue().get()));
        return insert;
    }

    public static Select.Where select(ColumnEntity columnEntity, String keyspace) {
        List<Column> columns = columnEntity.getColumns();
        if(columns.size() == 1) {
            Column column = columns.get(0);
            return QueryBuilder.select().all().from(keyspace, columnEntity.getColumnFamily()).where(QueryBuilder.eq(column.getName(), column.getValue().get()));
        }
        List<String> names = columns.stream().sorted(Comparator.comparing(Column::getName)).map(Column::getName).collect(Collectors.toList());
        List<Object> values = columns.stream().sorted(Comparator.comparing(Column::getName)).map(column -> column.getValue().get()).collect(Collectors.toList());
        return QueryBuilder.select().all().from(keyspace, columnEntity.getColumnFamily()).where(QueryBuilder.eq(names, values));
    }

    public static Delete.Where delete(ColumnEntity columnEntity, String keyspace) {
        List<Column> columns = columnEntity.getColumns();
        if(columns.size() == 1) {
            Column column = columns.get(0);
            return QueryBuilder.delete().all().from(keyspace, columnEntity.getColumnFamily()).where(QueryBuilder.eq(column.getName(), column.getValue().get()));
        }
        List<String> names = columns.stream().sorted(Comparator.comparing(Column::getName)).map(Column::getName).collect(Collectors.toList());
        List<Object> values = columns.stream().sorted(Comparator.comparing(Column::getName)).map(column -> column.getValue().get()).collect(Collectors.toList());
        return QueryBuilder.delete().all().from(keyspace, columnEntity.getColumnFamily()).where(QueryBuilder.eq(names, values));
    }

}
