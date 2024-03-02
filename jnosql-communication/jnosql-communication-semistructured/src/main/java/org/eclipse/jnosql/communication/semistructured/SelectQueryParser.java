/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;


import jakarta.data.Direction;
import jakarta.data.Sort;
import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.query.SelectQueryConverter;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class SelectQueryParser implements BiFunction<org.eclipse.jnosql.communication.query.SelectQuery, CommunicationObserverParser, QueryParams> {


    /**
     * The default constructor
     */
    public SelectQueryParser() {
    }

    Stream<CommunicationEntity> query(String query, DatabaseManager manager, CommunicationObserverParser observer) {

        SelectQuery selectQuery = getColumnQuery(query, observer);
        return manager.select(selectQuery);
    }


    CommunicationPreparedStatement prepare(String query, DatabaseManager manager, CommunicationObserverParser observer) {

        Params params = Params.newParams();
        SelectQueryConverter converter = new SelectQueryConverter();
        org.eclipse.jnosql.communication.query.SelectQuery selectQuery = converter.apply(query);

        SelectQuery columnQuery = getColumnQuery(params, selectQuery, observer);
        return CommunicationPreparedStatement.select(columnQuery, params, query, manager);
    }


    @Override
    public QueryParams apply(org.eclipse.jnosql.communication.query.SelectQuery selectQuery, CommunicationObserverParser observer) {
        Objects.requireNonNull(selectQuery, "selectQuery is required");
        Objects.requireNonNull(observer, "observer is required");

        Params params = Params.newParams();
        SelectQuery columnQuery = getColumnQuery(params, selectQuery, observer);
        return new QueryParams(columnQuery, params);
    }


    private SelectQuery getColumnQuery(String query, CommunicationObserverParser observer) {

        SelectQueryConverter converter = new SelectQueryConverter();
        org.eclipse.jnosql.communication.query.SelectQuery selectQuery = converter.apply(query);
        String columnFamily = observer.fireEntity(selectQuery.entity());
        long limit = selectQuery.limit();
        long skip = selectQuery.skip();
        List<String> columns = selectQuery.fields().stream()
                .map(f -> observer.fireField(columnFamily, f))
                .collect(Collectors.toList());
        List<Sort<?>> sorts = selectQuery.orderBy().stream().map(s -> toSort(s, observer, columnFamily))
                .collect(toList());

        Params params = Params.newParams();
        CriteriaCondition condition = selectQuery.where()
                .map(c -> Conditions.getCondition(c, params, observer, columnFamily)).orElse(null);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultSelectQuery(limit, skip, columnFamily, columns, sorts, condition);
    }

    private SelectQuery getColumnQuery(Params params, org.eclipse.jnosql.communication.query.SelectQuery selectQuery, CommunicationObserverParser observer) {

        String columnFamily = observer.fireEntity(selectQuery.entity());
        long limit = selectQuery.limit();
        long skip = selectQuery.skip();
        List<String> columns = selectQuery.fields().stream()
                .map(f -> observer.fireField(columnFamily, f))
                .collect(Collectors.toList());

        List<Sort<?>> sorts = selectQuery.orderBy().stream().map(s -> toSort(s, observer, columnFamily)).collect(toList());
        CriteriaCondition condition = selectQuery.where()
                .map(c -> Conditions.getCondition(c, params, observer, columnFamily))
                .orElse(null);

        return new DefaultSelectQuery(limit, skip, columnFamily, columns, sorts, condition);
    }

    private Sort<?> toSort(Sort<?> sort, CommunicationObserverParser observer, String entity) {
        return Sort.of(observer.fireField(entity, sort.property()),
                sort.isAscending()? Direction.ASC: Direction.DESC, false);
    }


}