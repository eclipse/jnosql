package org.apache.diana.api.column;


import org.apache.diana.api.Condition;

import java.util.Objects;

class DefaultColumnCondition implements ColumnCondition {

    private final Column column;

    private final Condition condition;

    private DefaultColumnCondition(Column column, Condition condition) {
        this.column = column;
        this.condition = condition;
    }

    public static DefaultColumnCondition of(Column column, Condition condition) {
        return new DefaultColumnCondition(Objects.requireNonNull(column,"Column is required") , condition);
    }

    public Column getColumn() {
        return column;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultColumnCondition that = (DefaultColumnCondition) o;
        return Objects.equals(column, that.column) &&
                condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, condition);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultColumnCondition{");
        sb.append("column=").append(column);
        sb.append(", condition=").append(condition);
        sb.append('}');
        return sb.toString();
    }
}
