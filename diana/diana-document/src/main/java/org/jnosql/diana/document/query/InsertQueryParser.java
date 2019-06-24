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
package org.jnosql.diana.document.query;

import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentObserverParser;
import jakarta.nosql.document.DocumentPreparedStatement;
import jakarta.nosql.document.DocumentPreparedStatementAsync;
import jakarta.nosql.query.Condition;
import jakarta.nosql.query.InsertQuery;
import jakarta.nosql.query.InsertQuery.InsertQueryProvider;
import jakarta.nosql.query.JSONQueryValue;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

final class InsertQueryParser extends ConditionQueryParser {

    private final InsertQueryProvider insertQueryProvider;

    InsertQueryParser() {
        this.insertQueryProvider = ServiceLoaderProvider.get(InsertQueryProvider.class);
    }

    List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {

        InsertQuery insertQuery = insertQueryProvider.apply(query);

        String collection = insertQuery.getEntity();
        Params params = Params.newParams();

        DocumentEntity entity = getEntity(insertQuery, collection, params, observer);

        Optional<Duration> ttl = insertQuery.getTtl();
        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        if (ttl.isPresent()) {
            return singletonList(collectionManager.insert(entity, ttl.get()));
        } else {
            return singletonList(collectionManager.insert(entity));
        }
    }

    void queryAsync(String query, DocumentCollectionManagerAsync collectionManager,
                    Consumer<List<DocumentEntity>> callBack, DocumentObserverParser observer) {
        InsertQuery insertQuery = insertQueryProvider.apply(query);

        String collection = observer.fireEntity(insertQuery.getEntity());
        Params params = Params.newParams();

        DocumentEntity entity = getEntity(insertQuery, collection, params, observer);

        Optional<Duration> ttl = insertQuery.getTtl();
        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        if (ttl.isPresent()) {
            collectionManager.insert(entity, ttl.get(), c -> callBack.accept(singletonList(c)));
        } else {
            collectionManager.insert(entity, c -> callBack.accept(singletonList(c)));
        }
    }

    DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {
        InsertQuery insertQuery = insertQueryProvider.apply(query);

        String collection = observer.fireEntity(insertQuery.getEntity());
        Params params = Params.newParams();

        Optional<Duration> ttl = insertQuery.getTtl();
        DocumentEntity entity = getEntity(insertQuery, collection, params, observer);

        return DefaultDocumentPreparedStatement.insert(entity, params, query, ttl.orElse(null), collectionManager);

    }

    DocumentPreparedStatementAsync prepareAsync(String query, DocumentCollectionManagerAsync collectionManager, DocumentObserverParser observer) {
        Params params = Params.newParams();

        InsertQuery insertQuery = insertQueryProvider.apply(query);
        String collection = observer.fireEntity(insertQuery.getEntity());
        Optional<Duration> ttl = insertQuery.getTtl();
        DocumentEntity entity = getEntity(insertQuery, collection, params, observer);

        return DefaultDocumentPreparedStatementAsync.insert(entity, params, query, ttl.orElse(null), collectionManager);
    }

    private DocumentEntity getEntity(InsertQuery insertQuery, String collection, Params params, DocumentObserverParser observer) {
        return getEntity(new InsertQueryConditionSupplier(insertQuery), collection, params, observer);
    }


    private class InsertQueryConditionSupplier implements ConditionQuerySupplier {
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
