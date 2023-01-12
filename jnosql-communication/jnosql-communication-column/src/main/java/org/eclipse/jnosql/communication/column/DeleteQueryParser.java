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
package org.eclipse.jnosql.communication.column;


import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.query.DeleteQuery;
import org.eclipse.jnosql.communication.query.DeleteQueryProvider;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public final class DeleteQueryParser implements BiFunction<DeleteQuery, ColumnObserverParser, ColumnDeleteQueryParams> {

    private final DeleteQueryProvider deleteQueryProvider;

    public DeleteQueryParser() {
        this.deleteQueryProvider = new DeleteQueryProvider();
    }

    Stream<ColumnEntity> query(String query, ColumnManager manager, ColumnObserverParser observer) {

        ColumnDeleteQuery deleteQuery = getQuery(query, observer);
        manager.delete(deleteQuery);
        return Stream.empty();
    }


    ColumnPreparedStatement prepare(String query, ColumnManager manager,
                                    ColumnObserverParser observer) {
        Params params = Params.newParams();
        ColumnDeleteQuery columnDeleteQuery = getQuery(query, params, observer);
        return ColumnPreparedStatement.delete(columnDeleteQuery, params, query, manager);
    }



    @Override
    public ColumnDeleteQueryParams apply(DeleteQuery deleteQuery,
                                         ColumnObserverParser columnObserverParser) {

        requireNonNull(deleteQuery, "deleteQuery is required");
        requireNonNull(columnObserverParser, "columnObserverParser is required");
        Params params = Params.newParams();
        ColumnDeleteQuery query = getQuery(params, columnObserverParser, deleteQuery);
        return new ColumnDeleteQueryParams(query, params);
    }

    private ColumnDeleteQuery getQuery(String query, Params params, ColumnObserverParser observer) {
        DeleteQuery deleteQuery = deleteQueryProvider.apply(query);

        return getQuery(params, observer, deleteQuery);
    }

    private ColumnDeleteQuery getQuery(Params params, ColumnObserverParser observer, DeleteQuery deleteQuery) {
        String columnFamily = observer.fireEntity(deleteQuery.entity());
        List<String> columns = deleteQuery.fields().stream()
                .map(f -> observer.fireField(columnFamily, f))
                .collect(Collectors.toList());
        ColumnCondition condition = null;

        if (deleteQuery.where().isPresent()) {
            condition = deleteQuery.where().map(c -> Conditions.getCondition(c, params, observer, columnFamily)).get();
        }

        return new DefaultColumnDeleteQuery(columnFamily, condition, columns);
    }

    private ColumnDeleteQuery getQuery(String query, ColumnObserverParser observer) {
        DeleteQuery deleteQuery = deleteQueryProvider.apply(query);

        String columnFamily = observer.fireEntity(deleteQuery.entity());
        List<String> columns = deleteQuery.fields().stream()
                .map(f -> observer.fireField(columnFamily, f))
                .collect(Collectors.toList());
        ColumnCondition condition = null;
        Params params = Params.newParams();

        if (deleteQuery.where().isPresent()) {
            condition = deleteQuery.where().map(c -> Conditions.getCondition(c, params, observer, columnFamily)).get();
        }

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultColumnDeleteQuery(columnFamily, condition, columns);
    }
}