package org.apache.diana.api.column;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ColumnEntity implements Serializable {



    private final List<Column> columns = new ArrayList<>();

    private final String columnFamily;

    private ColumnEntity(String columnFamily) {
        this.columnFamily = Objects.requireNonNull(columnFamily, "column family name is required");
    }

    public static ColumnEntity of(String columnFamily) {
        return new ColumnEntity(columnFamily);
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
