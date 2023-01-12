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
import org.eclipse.jnosql.communication.query.JSONQueryValue;
import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.UpdateQuery;
import org.eclipse.jnosql.communication.query.UpdateQueryProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

final class UpdateQueryParser extends ConditionQueryParser {

    private final UpdateQueryProvider updateQueryProvider;

    UpdateQueryParser() {
        this.updateQueryProvider = new UpdateQueryProvider();
    }

    Stream<ColumnEntity> query(String query, ColumnManager manager, ColumnObserverParser observer) {

        UpdateQuery updateQuery = updateQueryProvider.apply(query);

        Params params = Params.newParams();

        ColumnEntity entity = getEntity(params, updateQuery, observer);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return Stream.of(manager.update(entity));
    }


    ColumnPreparedStatement prepare(String query, ColumnManager manager, ColumnObserverParser observer) {

        Params params = Params.newParams();

        UpdateQuery updateQuery = updateQueryProvider.apply(query);

        ColumnEntity entity = getEntity(params, updateQuery, observer);

        return ColumnPreparedStatement.update(entity, params, query, manager);
    }


    private ColumnEntity getEntity(Params params, UpdateQuery updateQuery, ColumnObserverParser observer) {
        String columnFamily = observer.fireEntity(updateQuery.entity());

        return getEntity(new UpdateQueryConditionSupplier(updateQuery), columnFamily, params, observer);
    }

    private static final class UpdateQueryConditionSupplier implements ConditionQuerySupplier {
        private final UpdateQuery query;

        private UpdateQueryConditionSupplier(UpdateQuery query) {
            this.query = query;
        }


        @Override
        public List<QueryCondition> conditions() {
            return query.conditions();
        }

        @Override
        public Optional<JSONQueryValue> value() {
            return query.value();
        }
    }

}