package org.apache.diana.api.document;


import org.apache.diana.api.Condition;
import org.apache.diana.api.column.Column;

import java.util.Objects;

class DefaultDocumentCondition implements DocumentCondition {

    private final Column column;

    private final Condition condition;

    private DefaultDocumentCondition(Column column, Condition condition) {
        this.column = column;
        this.condition = condition;
    }

    public static DefaultDocumentCondition of(Column column, Condition condition) {
        return new DefaultDocumentCondition(Objects.requireNonNull(column,"Column is required") , condition);
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
        DefaultDocumentCondition that = (DefaultDocumentCondition) o;
        return Objects.equals(column, that.column) &&
                condition == that.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, condition);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultDocumentCondition{");
        sb.append("column=").append(column);
        sb.append(", condition=").append(condition);
        sb.append('}');
        return sb.toString();
    }
}
