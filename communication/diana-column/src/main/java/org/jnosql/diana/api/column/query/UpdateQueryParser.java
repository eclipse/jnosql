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

import org.jnosql.diana.api.Params;
import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnEntity;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsync;
import org.jnosql.diana.api.column.ColumnObserverParser;
import org.jnosql.diana.api.column.ColumnPreparedStatement;
import org.jnosql.diana.api.column.ColumnPreparedStatementAsync;
import org.jnosql.diana.api.QueryException;
import org.jnosql.query.UpdateQuery;
import org.jnosql.query.UpdateQuerySupplier;

import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

final class UpdateQueryParser {

    private final UpdateQuerySupplier supplier;

    UpdateQueryParser() {
        this.supplier = UpdateQuerySupplier.getSupplier();
    }

    List<ColumnEntity> query(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        UpdateQuery updateQuery = supplier.apply(query);

        Params params = new Params();

        ColumnEntity entity = getEntity(params, updateQuery, observer);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return singletonList(manager.update(entity));
    }

    void queryAsync(String query, ColumnFamilyManagerAsync manager,
                    Consumer<List<ColumnEntity>> callBack, ColumnObserverParser observer) {

        UpdateQuery updateQuery = supplier.apply(query);

        Params params = new Params();

        ColumnEntity entity = getEntity(params, updateQuery, observer);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        manager.update(entity, c -> callBack.accept(singletonList(c)));
    }

    ColumnPreparedStatement prepare(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        Params params = new Params();

        UpdateQuery updateQuery = supplier.apply(query);

        ColumnEntity entity = getEntity(params, updateQuery, observer);

        return DefaultColumnPreparedStatement.update(entity, params, query, manager);
    }

    ColumnPreparedStatementAsync prepareAsync(String query, ColumnFamilyManagerAsync manager,
                                              ColumnObserverParser observer) {
        Params params = new Params();
        UpdateQuery updateQuery = supplier.apply(query);

        ColumnEntity entity = getEntity(params, updateQuery, observer);

        return DefaultColumnPreparedStatementAsync.update(entity, params, query, manager);
    }

    private ColumnEntity getEntity(Params params, UpdateQuery updateQuery, ColumnObserverParser observer) {
        String columnFamily = observer.fireEntity(updateQuery.getEntity());

        ColumnEntity entity = ColumnEntity.of(columnFamily);

        updateQuery.getConditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params, observer, columnFamily))
                .map(ColumnCondition::getColumn)
                .forEach(entity::add);
        return entity;
    }

}