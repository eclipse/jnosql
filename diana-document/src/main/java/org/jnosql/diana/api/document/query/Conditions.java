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
import org.jnosql.query.Condition;
import org.jnosql.query.QueryException;
import org.jnosql.query.Where;

import static org.jnosql.diana.api.document.DocumentCondition.between;
import static org.jnosql.diana.api.document.DocumentCondition.eq;
import static org.jnosql.diana.api.document.DocumentCondition.gt;
import static org.jnosql.diana.api.document.DocumentCondition.gte;
import static org.jnosql.diana.api.document.DocumentCondition.in;
import static org.jnosql.diana.api.document.DocumentCondition.like;
import static org.jnosql.diana.api.document.DocumentCondition.lt;
import static org.jnosql.diana.api.document.DocumentCondition.lte;

final class Conditions {

    private Conditions() {
    }

    public static DocumentCondition getCondition(Where where) {
        Condition condition = where.getCondition();
        switch (condition.getOperator()) {
            case EQUALS:
                return eq(Document.of(condition.getName(), Values.get(condition.getValue())));
            case GREATER_THAN:
                return gt(Document.of(condition.getName(), Values.get(condition.getValue())));
            case GREATER_EQUALS_THAN:
                return gte(Document.of(condition.getName(), Values.get(condition.getValue())));
            case LESSER_THAN:
                return lt(Document.of(condition.getName(), Values.get(condition.getValue())));
            case LESSER_EQUALS_THAN:
                return lte(Document.of(condition.getName(), Values.get(condition.getValue())));
            case IN:
                return in(Document.of(condition.getName(), Values.get(condition.getValue())));
            case LIKE:
                return like(Document.of(condition.getName(), Values.get(condition.getValue())));
            case BETWEEN:
                return between(Document.of(condition.getName(), Values.get(condition.getValue())));
            case AND:
            case OR:
            case NOT:
            default:
                throw new QueryException("There is not support the type: " + condition.getOperator());


        }
    }
}
