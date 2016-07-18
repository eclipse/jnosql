package org.apache.diana.api.column;



import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A column family is a NoSQL object that contains columns of related data. It is a tuple (pair) that consists of a key-value pair, where the key is mapped to a value that is a set of columns.
 * In analogy with relational databases, a column family is as a "table", each key-value pair being a "row".
 * Each column is a tuple (triplet) consisting of a column name, a value, and a timestamp. In a relational database table,
 * this data would be grouped together within a table with other non-related data.
 * @author Otávio Santana
 */
public class ColumnFamily implements Serializable {


    private final List<Column> columns = new ArrayList<>();

    private final String name;

    private ColumnFamily(String name) {
        this.name = Objects.requireNonNull(name, "name is required");
    }

    /**
     * Creates a column family instance
     * @param name a name to column family
     * @param columns - columns
     * @return a ColumnFamily instance
     */
    public static ColumnFamily of(String name, Column... columns) {
        if (columns.length == 0) {
            return new ColumnFamily(name);
        }
        return of(name, Arrays.asList(columns));
    }

    /**
     * Creates a column family instance
     * @param name a name to column family
     * @param columns - columns
     * @return a ColumnFamily instance
     */
    public static ColumnFamily of(String name, List<Column> columns) {
        ColumnFamily columnEntity = new ColumnFamily(name);
        columnEntity.addAll(columns);
        return columnEntity;
    }

    /**
     * Appends all of the columns in the column family to the end of this list.
     * @param columns
     * @throws NullPointerException when columns is null
     */
    public void addAll(List<Column> columns) {
        Objects.requireNonNull(columns, "The object column is required");
        this.columns.addAll(columns);
    }

    /**
     * Appends the specified column to the end of this list
     * @param column
     * @throws NullPointerException when column is null
     */
    public void add(Column column) {
        Objects.requireNonNull(column, "Column is required");
        columns.add(column);
    }

    /**
     * Converts the columns to a Map where:
     * the key is the name the column
     * The value is the {@link org.apache.diana.api.Value#get()} of the map
     * @return a map instance
     */
    public Map<String, Object> toMap() {
        return columns.stream().collect(Collectors.toMap(Column::getName, column -> column.getValue().get()));
    }

    /**
     * Returns all columns from this Column Family
     * @return an immutable list of columns
     */
    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    /**
     * Column Family's name
     * @return Column Family's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns true if the number of columns is zero otherwise false.
     * @return true if there isnt elements to {@link ColumnFamily#columns}
     */
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
