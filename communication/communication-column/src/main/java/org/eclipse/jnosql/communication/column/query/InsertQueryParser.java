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
package org.eclipse.jnosql.communication.column.query;

import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.column.ColumnPreparedStatement;
import jakarta.nosql.query.Condition;
import jakarta.nosql.query.InsertQuery;
import jakarta.nosql.query.InsertQuery.InsertQueryProvider;
import jakarta.nosql.query.JSONQueryValue;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

final class InsertQueryParser extends ConditionQueryParser {

    private final InsertQueryProvider insertQueryProvider;

    InsertQueryParser() {
        this.insertQueryProvider = ServiceLoaderProvider.get(InsertQueryProvider.class);
    }

    Stream<ColumnEntity> query(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        InsertQuery insertQuery = insertQueryProvider.apply(query);

        String columnFamily = insertQuery.getEntity();
        Params params = Params.newParams();

        ColumnEntity entity = getEntity(insertQuery, columnFamily, params, observer);

        Optional<Duration> ttl = insertQuery.getTtl();
        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        if (ttl.isPresent()) {
            return Stream.of(manager.insert(entity, ttl.get()));
        } else {
            return Stream.of(manager.insert(entity));
        }
    }


    ColumnPreparedStatement prepare(String query, ColumnFamilyManager manager,
                                    ColumnObserverParser observer) {
        InsertQuery insertQuery = insertQueryProvider.apply(query);

        String columnFamily = observer.fireEntity(insertQuery.getEntity());
        Params params = Params.newParams();

        Optional<Duration> ttl = insertQuery.getTtl();
        ColumnEntity entity = getEntity(insertQuery, columnFamily, params, observer);

        return DefaultColumnPreparedStatement.insert(entity, params, query, ttl.orElse(null), manager);

    }

    private ColumnEntity getEntity(InsertQuery insertQuery, String columnFamily, Params params,
                                   ColumnObserverParser observer) {
        return getEntity(new InsertQueryConditionSupplier(insertQuery), columnFamily, params, observer);
    }


    private static final class InsertQueryConditionSupplier implements ConditionQuerySupplier {
        private final InsertQuery query;

        private InsertQueryConditionSupplier(InsertQuery query) {
            this.query = query;
        }

        @Override
        public List<Condition> getConditions() {
            return query.getConditions();
        }

        @Override
        public Optional<JSONQueryValue> getValue() {
            return query.getValue();
        }
    }

}
