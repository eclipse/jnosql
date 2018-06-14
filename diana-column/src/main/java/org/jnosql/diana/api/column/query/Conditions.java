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
package org.jnosql.diana.api.column.query;


import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnObserverParser;
import org.jnosql.query.Condition;
import org.jnosql.query.ConditionValue;
import org.jnosql.query.QueryException;
import org.jnosql.query.Where;

import static org.jnosql.diana.api.column.ColumnCondition.and;
import static org.jnosql.diana.api.column.ColumnCondition.between;
import static org.jnosql.diana.api.column.ColumnCondition.eq;
import static org.jnosql.diana.api.column.ColumnCondition.gt;
import static org.jnosql.diana.api.column.ColumnCondition.gte;
import static org.jnosql.diana.api.column.ColumnCondition.in;
import static org.jnosql.diana.api.column.ColumnCondition.like;
import static org.jnosql.diana.api.column.ColumnCondition.lt;
import static org.jnosql.diana.api.column.ColumnCondition.lte;
import static org.jnosql.diana.api.column.ColumnCondition.or;

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
                return eq(Column.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case GREATER_THAN:
                return gt(Column.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case GREATER_EQUALS_THAN:
                return gte(Column.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case LESSER_THAN:
                return lt(Column.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case LESSER_EQUALS_THAN:
                return lte(Column.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case IN:
                return in(Column.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case LIKE:
                return like(Column.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(),
                                parameters)));
            case BETWEEN:
                return between(Column.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(),
                                parameters)));
            case NOT:
                return getCondition(ConditionValue.class.cast(condition.getValue()).get().get(0),
                        parameters, observer,
                        entity).negate();
            case OR:
                return or(ConditionValue.class.cast(condition.getValue())
                        .get()
                        .stream().map(v -> getCondition(v, parameters, observer, entity))
                        .toArray(ColumnCondition[]::new));
            case AND:
                return and(ConditionValue.class.cast(condition.getValue())
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

    private static Object getValue(Condition condition, Params parameters,
                                   ColumnObserverParser observer, String entity) {
        return observer.fireValue(entity, condition.getName(), Values.get(condition.getValue(), parameters));
    }
}
