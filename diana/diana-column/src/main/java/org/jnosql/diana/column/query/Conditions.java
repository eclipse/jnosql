/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */
package org.jnosql.diana.column.query;


import org.jnosql.diana.Params;
import org.jnosql.diana.QueryException;
import org.jnosql.diana.column.Column;
import org.jnosql.diana.column.ColumnCondition;
import org.jnosql.diana.column.ColumnObserverParser;
import jakarta.nosql.query.Condition;
import jakarta.nosql.query.ConditionValue;
import jakarta.nosql.query.Where;

final class Conditions {

    private Conditions() {
    }

    static ColumnCondition getCondition(Where where, Params params, ColumnObserverParser observer, String entity) {
        Condition condition = where.getCondition();
        return getCondition(condition, params, observer, entity);
    }

    static ColumnCondition getCondition(Condition condition, Params parameters, ColumnObserverParser observer, String entity) {
        switch (condition.getOperator()) {
            case EQUALS:
                return ColumnCondition.eq(Column.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case GREATER_THAN:
                return ColumnCondition.gt(Column.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case GREATER_EQUALS_THAN:
                return ColumnCondition.gte(Column.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case LESSER_THAN:
                return ColumnCondition.lt(Column.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case LESSER_EQUALS_THAN:
                return ColumnCondition.lte(Column.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case IN:
                return ColumnCondition.in(Column.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case LIKE:
                return ColumnCondition.like(Column.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(),
                                parameters)));
            case BETWEEN:
                return ColumnCondition.between(Column.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(),
                                parameters)));
            case NOT:
                return getCondition(ConditionValue.class.cast(condition.getValue()).get().get(0),
                        parameters, observer,
                        entity).negate();
            case OR:
                return ColumnCondition.or(ConditionValue.class.cast(condition.getValue())
                        .get()
                        .stream().map(v -> getCondition(v, parameters, observer, entity))
                        .toArray(ColumnCondition[]::new));
            case AND:
                return ColumnCondition.and(ConditionValue.class.cast(condition.getValue())
                        .get()
                        .stream().map(v -> getCondition(v, parameters, observer, entity))
                        .toArray(ColumnCondition[]::new));
            default:
                throw new QueryException("There is not support the type: " + condition.getOperator());


        }
    }

    private static String getName(Condition condition, ColumnObserverParser observer, String entity) {
        return observer.fireField(entity, condition.getName());
    }

}
