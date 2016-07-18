package org.apache.diana.api.column;


import org.apache.diana.api.document.Document;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class ColumnEntity implements Serializable {


    private final List<Column> columns = new ArrayList<>();

    private final String columnFamily;

    private ColumnEntity(String columnFamily) {
        this.columnFamily = Objects.requireNonNull(columnFamily, "column family name is required");
    }

    public static ColumnEntity of(String columnFamily, Column... columns) {
        if (columns.length == 0) {
            return new ColumnEntity(columnFamily);
        }
        return of(columnFamily, Arrays.asList(columns));
    }

    public static ColumnEntity of(String columnFamily, List<Column> columns) {
        ColumnEntity columnEntity = new ColumnEntity(columnFamily);
        columnEntity.addAll(columns);
        return columnEntity;
    }

    public void addAll(List<Column> columns) {
        Objects.requireNonNull(columns, "The object column is required");
        this.columns.addAll(columns);
    }

    public Map<String, Object> toMap() {
        return columns.stream().collect(Collectors.toMap(Column::getName, column -> column.getValue().get()));
    }

    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    public void add(Column column) {
        Objects.requireNonNull(column, "Document is required");
        columns.add(column);
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public boolean isEmpty() {
        return columns.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ColumnEntity that = (ColumnEntity) o;
        return Objects.equals(columns, that.columns) &&
                Objects.equals(columnFamily, that.columnFamily);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, columnFamily);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ColumnEntity{");
        sb.append("columns=").append(columns);
        sb.append(", columnFamily='").append(columnFamily).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
