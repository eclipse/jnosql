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
package org.eclipse.jnosql.communication.document;


import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.query.InsertQuery;
import org.eclipse.jnosql.communication.query.InsertQueryProvider;
import org.eclipse.jnosql.communication.query.JSONQueryValue;
import org.eclipse.jnosql.communication.query.QueryCondition;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

final class InsertQueryParser extends ConditionQueryParser {

    private final InsertQueryProvider insertQueryProvider;

    InsertQueryParser() {
        this.insertQueryProvider = new InsertQueryProvider();
    }

    Stream<DocumentEntity> query(String query, DocumentManager collectionManager, DocumentObserverParser observer) {

        InsertQuery insertQuery = insertQueryProvider.apply(query);

        String collection = insertQuery.entity();
        Params params = Params.newParams();

        DocumentEntity entity = getEntity(insertQuery, collection, params, observer);

        Optional<Duration> ttl = insertQuery.ttl();
        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        if (ttl.isPresent()) {
            return Stream.of(collectionManager.insert(entity, ttl.get()));
        } else {
            return Stream.of(collectionManager.insert(entity));
        }
    }


    DocumentPreparedStatement prepare(String query, DocumentManager collectionManager, DocumentObserverParser observer) {
        InsertQuery insertQuery = insertQueryProvider.apply(query);

        String collection = observer.fireEntity(insertQuery.entity());
        Params params = Params.newParams();

        Optional<Duration> ttl = insertQuery.ttl();
        DocumentEntity entity = getEntity(insertQuery, collection, params, observer);

        return DocumentPreparedStatement.insert(entity, params, query, ttl.orElse(null), collectionManager);

    }

    private DocumentEntity getEntity(InsertQuery insertQuery, String collection, Params params, DocumentObserverParser observer) {
        return getEntity(new InsertQueryConditionSupplier(insertQuery), collection, params, observer);
    }


    private static final class InsertQueryConditionSupplier implements ConditionQuerySupplier {
        private final InsertQuery query;

        private InsertQueryConditionSupplier(InsertQuery query) {
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
