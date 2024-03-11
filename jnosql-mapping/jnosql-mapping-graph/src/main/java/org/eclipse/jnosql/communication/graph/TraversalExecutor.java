/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication.graph;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.ValueUtil;
import org.eclipse.jnosql.communication.semistructured.CriteriaCondition;
import org.eclipse.jnosql.communication.semistructured.Element;

import java.util.List;

final class TraversalExecutor {

    private TraversalExecutor() {
    }

    static GraphTraversal<Vertex, Vertex> getPredicate(CriteriaCondition condition) {
        Condition operator = condition.condition();
        Element element = condition.element();
        String name = element.name();
        var value = ValueUtil.convert(element.value());

        switch (operator) {
            case EQUALS -> {
                return __.has(name, P.eq(value));
            }
            case GREATER_THAN -> {
                return __.has(name, P.gt(value));
            }
            case GREATER_EQUALS_THAN -> {
                return __.has(name, P.gte(value));
            }
            case LESSER_THAN -> {
                return __.has(name, P.lt(value));
            }
            case LESSER_EQUALS_THAN -> {
                return __.has(name, P.lte(value));
            }
            case BETWEEN -> {
                List<Object> values = ValueUtil.convertToList(element.value());
                if(values.size() == 2) {
                    return __.has(name, P.between(values.get(0), values.get(1)));
                }
               throw new IllegalStateException("The between condition requires two parameters");
            }
            case IN -> {
                return __.has(name, P.within(ValueUtil.convertToList(element.value())));
            }
            case NOT -> {
                var notCondition = element.value().get(CriteriaCondition.class);
                return __.not(getPredicate(notCondition));
            }
            case AND -> {
                return condition.element().value().get(new TypeReference<List<CriteriaCondition>>() {
                        }).stream().map(TraversalExecutor::getPredicate)
                        .reduce(GraphTraversal::and)
                        .orElseThrow(() -> new UnsupportedOperationException("There is an inconsistency at the AND operator"));
            }
            case OR -> {
                return condition.element().value().get(new TypeReference<List<CriteriaCondition>>() {
                        }).stream().map(TraversalExecutor::getPredicate)
                        .reduce(GraphTraversal::or)
                        .orElseThrow(() -> new UnsupportedOperationException("There is an inconsistency at the OR operator"));
            }
            default ->
                    throw new UnsupportedOperationException("There is not support to the type " + operator + " in graph");
        }
    }
}
