package org.apache.diana.api.column;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ColumnEntity implements Serializable {

    private final Column key;

    private final List<Column> columns = new ArrayList<>();

    private final String columnFamily;

    private ColumnEntity(Column key, String columnFamily) {
        this.key = Objects.requireNonNull(key, "key is required");
        this.columnFamily = Objects.requireNonNull(columnFamily, "column family name is required");
    }

    public static ColumnEntity of(Column key, String columnFamily) {
        return new ColumnEntity(key, columnFamily);
    }

    public Column getKey() {
        return key;
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
        return Objects.equals(key, that.key) &&
                Objects.equals(columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, columns);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DocumentEntity{");
        sb.append("key=").append(key);
        sb.append(", columns=").append(columns);
        sb.append('}');
        return sb.toString();
    }
}
