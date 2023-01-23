/*
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
 */
package org.eclipse.jnosql.mapping.graph.query;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.query.ConditionQueryValue;
import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.QueryValue;
import org.eclipse.jnosql.communication.query.Where;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;

import java.util.Optional;
import java.util.function.Supplier;

abstract class AbstractQueryConvert {


    protected GraphTraversal<Vertex, Vertex> getPredicate(GraphQueryMethod graphQuery, QueryCondition condition,
                                                        EntityMetadata mapping) {
        Condition operator = condition.condition();
        String name = condition.name();
        QueryValue<?> value = condition.value();
        String nativeName = mapping.getColumnField(name);
        switch (operator) {
            case EQUALS:
                return __.has(nativeName, P.eq(graphQuery.getValue(name, value)));
            case GREATER_THAN:
                return __.has(nativeName, P.gt(graphQuery.getValue(name, value)));
            case GREATER_EQUALS_THAN:
                return __.has(nativeName, P.gte(graphQuery.getValue(name, value)));
            case LESSER_THAN:
                return __.has(nativeName, P.lt(graphQuery.getValue(name, value)));
            case LESSER_EQUALS_THAN:
                return __.has(nativeName, P.lte(graphQuery.getValue(name, value)));
            case BETWEEN:
                return __.has(nativeName, P.between(graphQuery.getValue(name, value),
                        graphQuery.getValue(name, value)));
            case IN:
                return __.has(nativeName, P.within(graphQuery.getInValue(name)));
            case NOT:
                QueryCondition notCondition = ((ConditionQueryValue) value).get().get(0);
                return __.not(getPredicate(graphQuery, notCondition, mapping));
            case AND:
                return ((ConditionQueryValue) value).get().stream()
                        .map(c -> getPredicate(graphQuery, c, mapping)).reduce(GraphTraversal::and)
                        .orElseThrow(() -> new UnsupportedOperationException("There is an inconsistency at the AND operator"));
            case OR:
                return ((ConditionQueryValue) value).get().stream()
                        .map(c -> getPredicate(graphQuery, c, mapping)).reduce(GraphTraversal::or)
                        .orElseThrow(() -> new UnsupportedOperationException("There is an inconsistency at the OR operator"));
            default:
                throw new UnsupportedOperationException("There is not support to the type " + operator + " in graph");


        }
    }

    protected GraphTraversal<Vertex, Vertex> getGraphTraversal(GraphQueryMethod graphQuery,
                                                               Supplier<Optional<Where>> whereSupplier,
                                                               EntityMetadata mapping) {

        GraphTraversal<Vertex, Vertex> traversal = graphQuery.getTraversal();
        Optional<Where> whereOptional = whereSupplier.get();

        if (whereOptional.isPresent()) {
            Where where = whereOptional.get();

            QueryCondition condition = where.condition();
            traversal.filter(getPredicate(graphQuery, condition, mapping));
        }
        return traversal;
    }
}
