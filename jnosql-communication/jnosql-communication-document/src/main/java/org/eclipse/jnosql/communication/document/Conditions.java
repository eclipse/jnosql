/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication.document;


import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.query.ConditionQueryValue;
import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.Where;

import static org.eclipse.jnosql.communication.document.DocumentCondition.between;
import static org.eclipse.jnosql.communication.document.DocumentCondition.eq;
import static org.eclipse.jnosql.communication.document.DocumentCondition.gt;
import static org.eclipse.jnosql.communication.document.DocumentCondition.gte;
import static org.eclipse.jnosql.communication.document.DocumentCondition.in;
import static org.eclipse.jnosql.communication.document.DocumentCondition.like;
import static org.eclipse.jnosql.communication.document.DocumentCondition.lt;
import static org.eclipse.jnosql.communication.document.DocumentCondition.lte;
import static org.eclipse.jnosql.communication.document.DocumentCondition.and;
import static org.eclipse.jnosql.communication.document.DocumentCondition.or;


final class Conditions {

    private Conditions() {
    }

    static DocumentCondition getCondition(Where where, Params params, DocumentObserverParser observer, String entity) {
        QueryCondition condition = where.condition();
        return getCondition(condition, params, observer, entity);
    }

    static DocumentCondition getCondition(QueryCondition condition, Params parameters, DocumentObserverParser observer, String entity) {
        return switch (condition.condition()) {
            case EQUALS -> eq(Document.of(getName(condition, observer, entity),
                    Values.get(condition.value(), parameters)));
            case GREATER_THAN -> gt(Document.of(getName(condition, observer, entity),
                    Values.get(condition.value(), parameters)));
            case GREATER_EQUALS_THAN -> gte(Document.of(getName(condition, observer, entity),
                    Values.get(condition.value(), parameters)));
            case LESSER_THAN -> lt(Document.of(getName(condition, observer, entity),
                    Values.get(condition.value(), parameters)));
            case LESSER_EQUALS_THAN -> lte(Document.of(getName(condition, observer, entity),
                    Values.get(condition.value(), parameters)));
            case IN -> in(Document.of(getName(condition, observer, entity),
                    Values.get(condition.value(), parameters)));
            case LIKE -> like(Document.of(getName(condition, observer, entity),
                    Values.get(condition.value(), parameters)));
            case BETWEEN -> between(Document.of(getName(condition, observer, entity),
                    Values.get(condition.value(), parameters)));
            case NOT -> getCondition(ConditionQueryValue.class.cast(condition.value()).get().get(0),
                    parameters, observer,
                    entity).negate();
            case OR -> or(ConditionQueryValue.class.cast(condition.value())
                    .get()
                    .stream().map(v -> getCondition(v, parameters, observer, entity))
                    .toArray(DocumentCondition[]::new));
            case AND -> and(ConditionQueryValue.class.cast(condition.value())
                    .get()
                    .stream().map(v -> getCondition(v, parameters, observer, entity))
                    .toArray(DocumentCondition[]::new));
            default -> throw new QueryException("There is not support the type: " + condition.condition());
        };
    }

    private static String getName(QueryCondition condition,
                                  DocumentObserverParser observer, String entity) {
        return observer.fireField(entity, condition.name());
    }
}
