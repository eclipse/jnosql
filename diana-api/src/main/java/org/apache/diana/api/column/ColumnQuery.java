package org.apache.diana.api.column;


import org.apache.diana.api.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ColumnQuery {

    private final String columnFamily;

    private final List<ColumnCondition> conditions = new ArrayList<>();

    private final List<Sort> sorts = new ArrayList<>();

    private ColumnQuery(String columnFamily) {
        this.columnFamily = Objects.requireNonNull(columnFamily, "column family is required");
    }

    public static ColumnQuery of(String columnFamily) {
        return new ColumnQuery(columnFamily);
    }

    public ColumnQuery addCondition(ColumnCondition condition) {
        this.conditions.add(Objects.requireNonNull(condition, "condition is required"));
        return this;
    }

    public ColumnQuery addSort(Sort sort) {
        this.sorts.add(Objects.requireNonNull(sort, "Sort is required"));
        return this;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public List<ColumnCondition> getConditions() {
        return Collections.unmodifiableList(conditions);
    }

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
