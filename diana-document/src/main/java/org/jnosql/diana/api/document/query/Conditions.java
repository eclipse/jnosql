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
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentObserverParser;
import org.jnosql.query.Condition;
import org.jnosql.query.ConditionValue;
import org.jnosql.query.QueryException;
import org.jnosql.query.Where;

import static org.jnosql.diana.api.document.DocumentCondition.and;
import static org.jnosql.diana.api.document.DocumentCondition.between;
import static org.jnosql.diana.api.document.DocumentCondition.eq;
import static org.jnosql.diana.api.document.DocumentCondition.gt;
import static org.jnosql.diana.api.document.DocumentCondition.gte;
import static org.jnosql.diana.api.document.DocumentCondition.in;
import static org.jnosql.diana.api.document.DocumentCondition.like;
import static org.jnosql.diana.api.document.DocumentCondition.lt;
import static org.jnosql.diana.api.document.DocumentCondition.lte;
import static org.jnosql.diana.api.document.DocumentCondition.or;

final class Conditions {

    private Conditions() {
    }

    static DocumentCondition getCondition(Where where, Params params, DocumentObserverParser observer, String entity) {
        Condition condition = where.getCondition();
        return getCondition(condition, params, observer, entity);
    }

    static DocumentCondition getCondition(Condition condition, Params parameters, DocumentObserverParser observer, String entity) {
        switch (condition.getOperator()) {
            case EQUALS:
                return eq(Document.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case GREATER_THAN:
                return gt(Document.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case GREATER_EQUALS_THAN:
                return gte(Document.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case LESSER_THAN:
                return lt(Document.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case LESSER_EQUALS_THAN:
                return lte(Document.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case IN:
                return in(Document.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case LIKE:
                return like(Document.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case BETWEEN:
                return between(Document.of(getName(condition, observer, entity),
                        getValue(condition, parameters, observer, entity)));
            case NOT:
                return getCondition(ConditionValue.class.cast(condition.getValue()).get().get(0),
                        parameters, observer,
                        entity).negate();
            case OR:
                return or(ConditionValue.class.cast(condition.getValue())
                        .get()
                        .stream().map(v -> getCondition(v, parameters, observer, entity))
                        .toArray(DocumentCondition[]::new));
            case AND:
                return and(ConditionValue.class.cast(condition.getValue())
                        .get()
                        .stream().map(v -> getCondition(v, parameters, observer, entity))
                        .toArray(DocumentCondition[]::new));
            default:
                throw new QueryException("There is not support the type: " + condition.getOperator());


        }
    }

    private static Object getValue(Condition condition, Params parameters,
                                   DocumentObserverParser observer, String entity) {
        return observer.fireValue(entity, condition.getName(), Values.get(condition.getValue(), parameters));
    }

    private static String getName(Condition condition,
                                  DocumentObserverParser observer, String entity) {
        return observer.fireField(entity, condition.getName());
    }
}
