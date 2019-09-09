/*
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
 */
package org.eclipse.jnosql.artemis.graph.query;

import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.query.Condition;
import jakarta.nosql.query.ConditionQueryValue;
import jakarta.nosql.query.Operator;
import jakarta.nosql.query.Where;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Optional;
import java.util.function.Supplier;

abstract class AbstractQueryConvert {


    protected GraphTraversal<Vertex, Vertex> getPredicate(GraphQueryMethod graphQuery, Condition condition,
                                                        ClassMapping mapping) {
        Operator operator = condition.getOperator();
        String name = condition.getName();
        String nativeName = mapping.getColumnField(name);
        switch (operator) {
            case EQUALS:
                return __.has(nativeName, P.eq(graphQuery.getValue(name)));
            case GREATER_THAN:
                return __.has(nativeName, P.gt(graphQuery.getValue(name)));
            case GREATER_EQUALS_THAN:
                return __.has(nativeName, P.gte(graphQuery.getValue(name)));
            case LESSER_THAN:
                return __.has(nativeName, P.lt(graphQuery.getValue(name)));
            case LESSER_EQUALS_THAN:
                return __.has(nativeName, P.lte(graphQuery.getValue(name)));
            case BETWEEN:
                return __.has(nativeName, P.between(graphQuery.getValue(name), graphQuery.getValue(name)));
            case IN:
                return __.has(nativeName, P.within(graphQuery.getInValue(name)));
            case NOT:
                Condition notCondition = ((ConditionQueryValue) condition.getValue()).get().get(0);
                return __.not(getPredicate(graphQuery, notCondition, mapping));
            case AND:
                return ((ConditionQueryValue) condition.getValue()).get().stream()
                        .map(c -> getPredicate(graphQuery, c, mapping)).reduce(GraphTraversal::and)
                        .orElseThrow(() -> new UnsupportedOperationException("There is an inconsistency at the AND operator"));
            case OR:
                return ((ConditionQueryValue) condition.getValue()).get().stream()
                        .map(c -> getPredicate(graphQuery, c, mapping)).reduce(GraphTraversal::or)
                        .orElseThrow(() -> new UnsupportedOperationException("There is an inconsistency at the OR operator"));
            default:
                throw new UnsupportedOperationException("There is not support to the type " + operator + " in graph");


        }
    }

    protected GraphTraversal<Vertex, Vertex> getGraphTraversal(GraphQueryMethod graphQuery,
                                                               Supplier<Optional<Where>> whereSupplier,
                                                               ClassMapping mapping) {

        GraphTraversal<Vertex, Vertex> traversal = graphQuery.getTraversal();
        Optional<Where> whereOptional = whereSupplier.get();

        if (whereOptional.isPresent()) {
            Where where = whereOptional.get();

            Condition condition = where.getCondition();
            traversal.filter(getPredicate(graphQuery, condition, mapping));
        }
        return traversal;
    }
}
