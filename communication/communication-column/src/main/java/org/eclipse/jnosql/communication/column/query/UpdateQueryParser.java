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
import jakarta.nosql.query.JSONQueryValue;
import jakarta.nosql.query.UpdateQuery;
import jakarta.nosql.query.UpdateQuery.UpdateQueryProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

final class UpdateQueryParser extends ConditionQueryParser {

    private final UpdateQueryProvider updateQueryProvider;

    UpdateQueryParser() {
        this.updateQueryProvider = ServiceLoaderProvider.get(UpdateQueryProvider.class);
    }

    Stream<ColumnEntity> query(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        UpdateQuery updateQuery = updateQueryProvider.apply(query);

        Params params = Params.newParams();

        ColumnEntity entity = getEntity(params, updateQuery, observer);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return Stream.of(manager.update(entity));
    }


    ColumnPreparedStatement prepare(String query, ColumnFamilyManager manager, ColumnObserverParser observer) {

        Params params = Params.newParams();

        UpdateQuery updateQuery = updateQueryProvider.apply(query);

        ColumnEntity entity = getEntity(params, updateQuery, observer);

        return DefaultColumnPreparedStatement.update(entity, params, query, manager);
    }


    private ColumnEntity getEntity(Params params, UpdateQuery updateQuery, ColumnObserverParser observer) {
        String columnFamily = observer.fireEntity(updateQuery.getEntity());

        return getEntity(new UpdateQueryConditionSupplier(updateQuery), columnFamily, params, observer);
    }

    private static final class UpdateQueryConditionSupplier implements ConditionQuerySupplier {
        private final UpdateQuery query;

        private UpdateQueryConditionSupplier(UpdateQuery query) {
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