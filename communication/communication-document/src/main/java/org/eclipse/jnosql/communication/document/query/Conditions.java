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
package org.eclipse.jnosql.communication.document.query;

import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentObserverParser;
import jakarta.nosql.query.Condition;
import jakarta.nosql.query.ConditionQueryValue;
import jakarta.nosql.query.Where;

import static jakarta.nosql.document.DocumentCondition.and;
import static jakarta.nosql.document.DocumentCondition.between;
import static jakarta.nosql.document.DocumentCondition.eq;
import static jakarta.nosql.document.DocumentCondition.gt;
import static jakarta.nosql.document.DocumentCondition.gte;
import static jakarta.nosql.document.DocumentCondition.in;
import static jakarta.nosql.document.DocumentCondition.like;
import static jakarta.nosql.document.DocumentCondition.lt;
import static jakarta.nosql.document.DocumentCondition.lte;
import static jakarta.nosql.document.DocumentCondition.or;

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
                        Values.get(condition.getValue(), parameters)));
            case GREATER_THAN:
                return gt(Document.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case GREATER_EQUALS_THAN:
                return gte(Document.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case LESSER_THAN:
                return lt(Document.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case LESSER_EQUALS_THAN:
                return lte(Document.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case IN:
                return in(Document.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case LIKE:
                return like(Document.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case BETWEEN:
                return between(Document.of(getName(condition, observer, entity),
                        Values.get(condition.getValue(), parameters)));
            case NOT:
                return getCondition(ConditionQueryValue.class.cast(condition.getValue()).get().get(0),
                        parameters, observer,
                        entity).negate();
            case OR:
                return or(ConditionQueryValue.class.cast(condition.getValue())
                        .get()
                        .stream().map(v -> getCondition(v, parameters, observer, entity))
                        .toArray(DocumentCondition[]::new));
            case AND:
                return and(ConditionQueryValue.class.cast(condition.getValue())
                        .get()
                        .stream().map(v -> getCondition(v, parameters, observer, entity))
                        .toArray(DocumentCondition[]::new));
            default:
                throw new QueryException("There is not support the type: " + condition.getOperator());


        }
    }

    private static String getName(Condition condition,
                                  DocumentObserverParser observer, String entity) {
        return observer.fireField(entity, condition.getName());
    }
}
