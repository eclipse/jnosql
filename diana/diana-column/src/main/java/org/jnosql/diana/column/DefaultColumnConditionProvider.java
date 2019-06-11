package org.jnosql.diana.column;

import jakarta.nosql.Condition;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnCondition.ColumnConditionProvider;

import java.util.Objects;

/**
 * The default implementation of {@link ColumnConditionProvider}
 */
public class DefaultColumnConditionProvider implements ColumnConditionProvider {

    @Override
    public ColumnCondition between(Column column) {
        Objects.requireNonNull(column, "column is required");
        return DefaultColumnCondition.between(column);
    }

    @Override
    public ColumnCondition and(ColumnCondition... conditions) {
        return DefaultColumnCondition.and(conditions);
    }

    @Override
    public ColumnCondition or(ColumnCondition... conditions) {
        return DefaultColumnCondition.or(conditions);
    }

    @Override
    public ColumnCondition in(Column column) {
        return DefaultColumnCondition.in(column);
    }

    @Override
    public ColumnCondition apply(Column column, Condition condition) {
        return DefaultColumnCondition.of(column, condition);
    }
}
