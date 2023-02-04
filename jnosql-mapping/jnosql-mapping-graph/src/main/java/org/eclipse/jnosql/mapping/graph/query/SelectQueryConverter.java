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

import jakarta.data.repository.Sort;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.query.SelectQuery;
import org.eclipse.jnosql.communication.query.method.SelectMethodProvider;
import org.eclipse.jnosql.mapping.NoSQLPage;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.repository.DynamicReturn;
import org.eclipse.jnosql.mapping.repository.RepositoryObserverParser;
import org.eclipse.jnosql.mapping.repository.SpecialParameters;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.apache.tinkerpop.gremlin.process.traversal.Order.asc;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.desc;

final class SelectQueryConverter extends AbstractQueryConvert implements BiFunction<GraphQueryMethod, Object[], Stream<Vertex>> {

    static final SelectQueryConverter INSTANCE = new SelectQueryConverter();

    private SelectQueryConverter() {
    }

    @Override
    public Stream<Vertex> apply(GraphQueryMethod graphQuery, Object[] params) {

        SelectMethodProvider selectMethodFactory = SelectMethodProvider.INSTANCE;
        SelectQuery query = selectMethodFactory.apply(graphQuery.getMethod(), graphQuery.getEntityName());
        EntityMetadata mapping = graphQuery.getMapping();
        RepositoryObserverParser parser = RepositoryObserverParser.of(mapping);
        GraphTraversal<Vertex, Vertex> traversal = getGraphTraversal(graphQuery, query::where, mapping);
        traversal.hasLabel(mapping.getName());
        query.orderBy().forEach(getSort(traversal, parser));
        updateDynamicParameter(params, traversal, query, parser);
        return traversal.toStream();
    }


    private static Consumer<Sort> getSort(GraphTraversal<Vertex, Vertex> traversal, RepositoryObserverParser parser) {
        return o -> {
            if (o.isAscending()) {
                traversal.order().by(parser.field(o.property()), asc);
            } else {
                traversal.order().by(parser.field(o.property()), desc);
            }
        };
    }


    static void updateDynamicParameter(Object[] args, GraphTraversal<Vertex, Vertex> traversal, EntityMetadata mapping) {
        updateDynamicParameter(args, traversal, null, RepositoryObserverParser.of(mapping));
    }


    private static void updateDynamicParameter(Object[] args, GraphTraversal<Vertex, Vertex> traversal,
                                               SelectQuery query, RepositoryObserverParser parser) {
        SpecialParameters special = DynamicReturn.findSpecialParameters(args);

        if (query != null) {
            if (query.skip() > 0) {
                traversal.skip(query.skip());
            }

            if (query.limit() > 0) {
                traversal.limit((int) query.limit());
            }
        }
        if (special.isEmpty()) {
            return;
        }

        if (special.hasOnlySort()) {
            special.sorts().forEach(getSort(traversal, parser));
            return;
        }

        special.limit().ifPresent(l -> {
            if (l.startAt() > 1) {
                traversal.skip(l.startAt() - 1);
            }
            traversal.limit((int) l.maxResults());
        });
        special.pageable().ifPresent(p -> {
            special.sorts().forEach(getSort(traversal, parser));
            traversal.skip(NoSQLPage.skip(p)).limit(p.size());
        });

    }


}
