package org.apache.diana.cassandra.document;


import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import org.apache.diana.api.column.Column;
import org.apache.diana.api.column.ColumnFamily;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

final class QueryUtils {

    private QueryUtils() {
    }


    public static Insert insert(ColumnFamily columnFamily, String keyspace) {
        Insert insert = QueryBuilder.insertInto(keyspace, columnFamily.getName());
        columnFamily.getColumns().forEach(d -> insert.value(d.getName(), d.getValue().get()));
        return insert;
    }

    public static Select.Where select(ColumnFamily columnFamily, String keyspace) {
        List<Column> columns = columnFamily.getColumns();
        if(columns.size() == 1) {
            Column column = columns.get(0);
            return QueryBuilder.select().all().from(keyspace, columnFamily.getName()).where(QueryBuilder.eq(column.getName(), column.getValue().get()));
        }
        List<String> names = columns.stream().sorted(Comparator.comparing(Column::getName)).map(Column::getName).collect(Collectors.toList());
        List<Object> values = columns.stream().sorted(Comparator.comparing(Column::getName)).map(column -> column.getValue().get()).collect(Collectors.toList());
        return QueryBuilder.select().all().from(keyspace, columnFamily.getName()).where(QueryBuilder.eq(names, values));
    }

    public static Delete.Where delete(ColumnFamily columnFamily, String keyspace) {
        List<Column> columns = columnFamily.getColumns();
        if(columns.size() == 1) {
            Column column = columns.get(0);
            return QueryBuilder.delete().all().from(keyspace, columnFamily.getName()).where(QueryBuilder.eq(column.getName(), column.getValue().get()));
        }
        List<String> names = columns.stream().sorted(Comparator.comparing(Column::getName)).map(Column::getName).collect(Collectors.toList());
        List<Object> values = columns.stream().sorted(Comparator.comparing(Column::getName)).map(column -> column.getValue().get()).collect(Collectors.toList());
        return QueryBuilder.delete().all().from(keyspace, columnFamily.getName()).where(QueryBuilder.eq(names, values));
    }

}
