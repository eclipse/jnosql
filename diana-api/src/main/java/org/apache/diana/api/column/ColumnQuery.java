package org.apache.diana.api.column;


import org.apache.diana.api.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class that contains information to do a query to {@link ColumnFamily}
 * @see ColumnFamilyManager#find(ColumnQuery)
 * @see ColumnCondition
 * @see Sort
 */
public class ColumnQuery {

    private final String columnFamily;

    private final List<ColumnCondition> conditions = new ArrayList<>();

    private final List<Sort> sorts = new ArrayList<>();

    private ColumnQuery(String columnFamily) {
        this.columnFamily = Objects.requireNonNull(columnFamily, "column family is required");
    }

    /**
     * Creates a {@link ColumnQuery}
     * @param columnFamily - the name of column family to do a query
     * @return a {@link ColumnQuery} instance
     */
    public static ColumnQuery of(String columnFamily) {
        return new ColumnQuery(columnFamily);
    }

    /**
     * Add a new condition in the query
     * @param condition
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    public ColumnQuery addCondition(ColumnCondition condition) throws NullPointerException{
        this.conditions.add(Objects.requireNonNull(condition, "condition is required"));
        return this;
    }

    /**
     * Add the order how the result will returned
     * @param sort the order way
     * @return the same way with a sort added
     */
    public ColumnQuery addSort(Sort sort) throws NullPointerException {
        this.sorts.add(Objects.requireNonNull(sort, "Sort is required"));
        return this;
    }

    /**
     * The column family name
     * @return the column family name
     */
    public String getColumnFamily() {
        return columnFamily;
    }

    /**
     * The conditions that contains in this {@link ColumnQuery}
     * @return the conditions
     */
    public List<ColumnCondition> getConditions() {
        return Collections.unmodifiableList(conditions);
    }

    /**
     * The sorts that contains in this {@link ColumnQuery}
     * @return the sorts
     */
    public List<Sort> getSorts() {
        return Collections.unmodifiableList(sorts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ColumnQuery that = (ColumnQuery) o;
        return Objects.equals(columnFamily, that.columnFamily) &&
                Objects.equals(conditions, that.conditions) &&
                Objects.equals(sorts, that.sorts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnFamily, conditions, sorts);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ColumnQuery{");
        sb.append("columnFamily='").append(columnFamily).append('\'');
        sb.append(", conditions=").append(conditions);
        sb.append(", sorts=").append(sorts);
        sb.append('}');
        return sb.toString();
    }
}
