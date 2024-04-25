package org.eclipse.jnosql.communication.query.data;

import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.QueryValue;

public record DefaultQueryCondition(String name, Condition condition, QueryValue<?> value) implements QueryCondition {

    @Override
    public String toString() {
        return name + " " + condition + " " + value;
    }
}
