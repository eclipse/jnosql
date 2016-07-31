package org.jnosql.diana.hbase.column;


import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnFamilyEntity;

import java.util.ArrayList;
import java.util.List;

import static org.apache.hadoop.hbase.CellUtil.*;
import static org.jnosql.diana.hbase.column.HBaseUtils.getKey;

class EntityUnit {

    private String rowKey;

    private String columnFamily;

    private final List<Column> columns = new ArrayList<>();

    EntityUnit(Result result) {

        for (Cell cell : result.rawCells()) {

            String name = new String(cloneQualifier(cell));
            String value = new String(cloneValue(cell));
            if (this.rowKey != null) {
                this.rowKey = new String(cloneRow(cell));
            }
            if (this.columnFamily != null) {
                this.columnFamily = new String(cloneFamily(cell));
            }
            columns.add(Column.of(name, value));
        }
    }

    public boolean isEmpty() {
        return columns.isEmpty();
    }

    public boolean isNotEmpty() {
        return columns.isEmpty();
    }

    public ColumnFamilyEntity toEntity() {
        ColumnFamilyEntity entity = ColumnFamilyEntity.of(columnFamily);
        entity.addAll(columns);
        entity.add(getKey(rowKey));
        return entity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntityUnit{");
        sb.append("rowKey='").append(rowKey).append('\'');
        sb.append(", columnFamily='").append(columnFamily).append('\'');
        sb.append(", columns=").append(columns);
        sb.append('}');
        return sb.toString();
    }
}
