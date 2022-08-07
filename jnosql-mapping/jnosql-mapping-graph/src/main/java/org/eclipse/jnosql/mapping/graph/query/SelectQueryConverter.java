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
package org.eclipse.jnosql.mapping.graph.query;

import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import jakarta.nosql.mapping.Pagination;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import jakarta.nosql.query.SelectQuery;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.repository.DynamicReturn;
import org.eclipse.jnosql.communication.query.method.SelectMethodProvider;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.apache.tinkerpop.gremlin.process.traversal.Order.asc;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.desc;

final class SelectQueryConverter extends AbstractQueryConvert implements BiFunction<GraphQueryMethod, Object[], Stream<Vertex>> {


    @Override
    public Stream<Vertex> apply(GraphQueryMethod graphQuery, Object[] params) {

        SelectMethodProvider selectMethodFactory = SelectMethodProvider.get();
        SelectQuery query = selectMethodFactory.apply(graphQuery.getMethod(), graphQuery.getEntityName());
        EntityMetadata mapping = graphQuery.getMapping();

        GraphTraversal<Vertex, Vertex> traversal = getGraphTraversal(graphQuery, query::getWhere, mapping);

        query.getOrderBy().forEach(getSort(traversal, mapping));
        setSort(params, traversal);
        setPagination(params, traversal, query);
        traversal.hasLabel(mapping.getName());
        return traversal.toStream();
    }


    private Consumer<Sort> getSort(GraphTraversal<Vertex, Vertex> traversal, EntityMetadata mapping) {
        return o -> {
            if (SortType.ASC.equals(o.getType())) {
                traversal.order().by(mapping.getColumnField(o.getName()), asc);
            } else {
                traversal.order().by(mapping.getColumnField(o.getName()), desc);
            }
        };
    }


    static void setPagination(Object[] args, GraphTraversal<Vertex, Vertex> traversal) {
        setPagination(args, traversal, null);
    }


    private static void setPagination(Object[] args, GraphTraversal<Vertex, Vertex> traversal, SelectQuery query) {
        Pagination pagination = DynamicReturn.findPagination(args);
        if (pagination != null) {
            traversal.skip(pagination.getSkip())
                    .limit(pagination.getLimit());
            return;
        }

        if (query != null) {
            if (query.getSkip() > 0) {
                traversal.skip(query.getSkip());
            }

            if (query.getLimit() > 0) {
                traversal.next((int) query.getLimit());
            }
        }

    }

    static void setSort(Object[] args, GraphTraversal<Vertex, Vertex> traversal) {
        List<Sort> sorts = DynamicReturn.findSorts(args);
        if (!sorts.isEmpty()) {
            for (Sort sort : sorts) {
                traversal.order().by(sort.getName(), sort.getType() == SortType.ASC ? asc : desc);
            }
        }
    }

}
