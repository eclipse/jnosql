package org.apache.diana.api.column;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class ColumnFamily implements Serializable {


    private final List<Column> columns = new ArrayList<>();

    private final String name;

    private ColumnFamily(String name) {
        this.name = Objects.requireNonNull(name, "name is required");
    }

    public static ColumnFamily of(String columnFamily, Column... columns) {
        if (columns.length == 0) {
            return new ColumnFamily(columnFamily);
        }
        return of(columnFamily, Arrays.asList(columns));
    }

    public static ColumnFamily of(String columnFamily, List<Column> columns) {
        ColumnFamily columnEntity = new ColumnFamily(columnFamily);
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
        Objects.requireNonNull(column, "Column is required");
        columns.add(column);
    }

    public String getName() {
        return name;
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
        ColumnFamily that = (ColumnFamily) o;
        return Objects.equals(columns, that.columns) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ColumnFamily{");
        sb.append("columns=").append(columns);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
