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
import org.jnosql.diana.api.column.ColumnEntity;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsync;
import org.jnosql.diana.api.column.ColumnObserverParser;
import org.jnosql.diana.api.column.ColumnPreparedStatement;
import org.jnosql.diana.api.column.ColumnPreparedStatementAsync;
import org.jnosql.query.InsertQuery;
import org.jnosql.query.InsertQuerySupplier;
import org.jnosql.query.QueryException;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

final class InsertQueryParser {

    private final InsertQuerySupplier supplier;

    InsertQueryParser() {
        this.supplier = InsertQuerySupplier.getSupplier();
    }

    List<ColumnEntity> query(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        InsertQuery insertQuery = supplier.apply(query);

        String columnFamily = insertQuery.getEntity();
        ColumnParams params = new ColumnParams();

        ColumnEntity entity = getEntity(insertQuery, columnFamily, params, observer);

        Optional<Duration> ttl = insertQuery.getTtl();
        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        if (ttl.isPresent()) {
            return singletonList(manager.insert(entity, ttl.get()));
        } else {
            return singletonList(manager.insert(entity));
        }
    }

    void queryAsync(String query, ColumnFamilyManagerAsync manager,
                    Consumer<List<ColumnEntity>> callBack,
                    ColumnObserverParser observer) {

        InsertQuery insertQuery = supplier.apply(query);

        String columnFamily = observer.fireEntity(insertQuery.getEntity());

        ColumnParams params = new ColumnParams();

        ColumnEntity entity = getEntity(insertQuery, columnFamily, params, observer);

        Optional<Duration> ttl = insertQuery.getTtl();
        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        if (ttl.isPresent()) {
            manager.insert(entity, ttl.get(), c -> callBack.accept(singletonList(c)));
        } else {
            manager.insert(entity, c -> callBack.accept(singletonList(c)));
        }
    }

    ColumnPreparedStatement prepare(String query, ColumnFamilyManager manager,
                                    ColumnObserverParser observer) {
        InsertQuery insertQuery = supplier.apply(query);

        String columnFamily = observer.fireEntity(insertQuery.getEntity());
        ColumnParams params = new ColumnParams();

        Optional<Duration> ttl = insertQuery.getTtl();
        ColumnEntity entity = getEntity(insertQuery, columnFamily, params, observer);

        return DefaultColumnPreparedStatement.insert(entity, params, query, ttl.orElse(null), manager);

    }

    ColumnPreparedStatementAsync prepareAsync(String query, ColumnFamilyManagerAsync manager,
                                              ColumnObserverParser observer) {
        ColumnParams params = new ColumnParams();

        InsertQuery insertQuery = supplier.apply(query);
        String columnFamily = observer.fireEntity(insertQuery.getEntity());
        Optional<Duration> ttl = insertQuery.getTtl();
        ColumnEntity entity = getEntity(insertQuery, columnFamily, params, observer);

        return DefaultColumnPreparedStatementAsync.insert(entity, params, query, ttl.orElse(null), manager);
    }

    private ColumnEntity getEntity(InsertQuery insertQuery, String columnFamily, ColumnParams params,
                                   ColumnObserverParser observer) {
        ColumnEntity entity = ColumnEntity.of(columnFamily);

        insertQuery.getConditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params, observer, columnFamily))
                .map(ColumnCondition::getColumn)
                .forEach(entity::add);
        return entity;
    }



}
