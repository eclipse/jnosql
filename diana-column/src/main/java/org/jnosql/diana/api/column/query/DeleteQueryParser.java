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
package org.jnosql.diana.api.column.query;

import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.ColumnEntity;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsync;
import org.jnosql.diana.api.column.ColumnObserverParser;
import org.jnosql.diana.api.column.ColumnPreparedStatement;
import org.jnosql.diana.api.column.ColumnPreparedStatementAsync;
import org.jnosql.query.DeleteQuery;
import org.jnosql.query.DeleteQuerySupplier;
import org.jnosql.query.QueryException;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

final class DeleteQueryParser implements DeleteQueryConverter{

    private final DeleteQuerySupplier selectQuerySupplier;
    private final CacheQuery<ColumnDeleteQuery> cache;

    DeleteQueryParser() {
        this.selectQuerySupplier = DeleteQuerySupplier.getSupplier();
        cache = new CacheQuery<>(this::getQuery);
    }

    List<ColumnEntity> query(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        ColumnDeleteQuery columnDeleteQuery = cache.get(query, observer);
        manager.delete(columnDeleteQuery);
        return Collections.emptyList();
    }

    void queryAsync(String query, ColumnFamilyManagerAsync manager,
                    Consumer<List<ColumnEntity>> callBack, ColumnObserverParser observer) {

        ColumnDeleteQuery columnDeleteQuery = cache.get(query, observer);
        manager.delete(columnDeleteQuery, v -> callBack.accept(Collections.emptyList()));
    }

    ColumnPreparedStatement prepare(String query, ColumnFamilyManager manager,
                                    ColumnObserverParser observer) {
        ColumnParams params = new ColumnParams();
        ColumnDeleteQuery columnDeleteQuery = getQuery(query, params, observer);
        return DefaultColumnPreparedStatement.delete(columnDeleteQuery, params, query, manager);
    }


    ColumnPreparedStatementAsync prepareAsync(String query, ColumnFamilyManagerAsync manager,
                                              ColumnObserverParser observer) {
        ColumnParams params = new ColumnParams();
        ColumnDeleteQuery columnDeleteQuery = getQuery(query, params, observer);
        return DefaultColumnPreparedStatementAsync.delete(columnDeleteQuery, params, query, manager);

    }

    @Override
    public ColumnDeleteQueryParams apply(DeleteQuery deleteQuery,
                                         ColumnObserverParser columnObserverParser) {

        requireNonNull(deleteQuery, "deleteQuery is required");
        requireNonNull(columnObserverParser, "columnObserverParser is required");
        ColumnParams params = new ColumnParams();
        ColumnDeleteQuery query = getQuery(params, columnObserverParser, deleteQuery);
        return new DefaultColumnDeleteQueryParams(query, params);
    }

    private ColumnDeleteQuery getQuery(String query, ColumnParams params, ColumnObserverParser observer) {
        DeleteQuery deleteQuery = selectQuerySupplier.apply(query);

        return getQuery(params, observer, deleteQuery);
    }

    private ColumnDeleteQuery getQuery(ColumnParams params, ColumnObserverParser observer, DeleteQuery deleteQuery) {
        String columnFamily = observer.fireEntity(deleteQuery.getEntity());
        List<String> columns = deleteQuery.getFields().stream()
                .map(f -> observer.fireField(columnFamily, f))
                .collect(Collectors.toList());
        ColumnCondition condition = null;

        if (deleteQuery.getWhere().isPresent()) {
            condition = deleteQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, columnFamily)).get();
        }

        return new DefaultColumnDeleteQuery(columnFamily, condition, columns);
    }

    private ColumnDeleteQuery getQuery(String query, ColumnObserverParser observer) {
        DeleteQuery deleteQuery = selectQuerySupplier.apply(query);

        String columnFamily = observer.fireEntity(deleteQuery.getEntity());
        List<String> columns = deleteQuery.getFields().stream()
                .map(f -> observer.fireField(columnFamily, f))
                .collect(Collectors.toList());
        ColumnCondition condition = null;
        ColumnParams params = new ColumnParams();

        if (deleteQuery.getWhere().isPresent()) {
            condition = deleteQuery.getWhere().map(c -> Conditions.getCondition(c, params, observer, columnFamily)).get();
        }

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultColumnDeleteQuery(columnFamily, condition, columns);
    }
}