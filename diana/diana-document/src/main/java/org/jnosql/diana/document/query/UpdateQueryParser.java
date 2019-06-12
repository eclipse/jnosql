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
import jakarta.nosql.query.JSONQueryValue;
import jakarta.nosql.query.UpdateQuery;
import jakarta.nosql.query.UpdateQuery.UpdateQueryProvider;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

final class UpdateQueryParser extends ConditionQueryParser {

    private final UpdateQueryProvider supplier;

    UpdateQueryParser() {
        this.supplier = ServiceLoaderProvider.get(UpdateQueryProvider.class);
    }

    List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {

        UpdateQuery updateQuery = supplier.apply(query);

        Params params = Params.newParams();

        DocumentEntity entity = getEntity(params, updateQuery, observer);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return singletonList(collectionManager.update(entity));
    }

    void queryAsync(String query, DocumentCollectionManagerAsync collectionManager,
                    Consumer<List<DocumentEntity>> callBack, DocumentObserverParser observer) {

        UpdateQuery updateQuery = supplier.apply(query);

        Params params = Params.newParams();

        DocumentEntity entity = getEntity(params, updateQuery, observer);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        collectionManager.update(entity, c -> callBack.accept(singletonList(c)));
    }

    DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {

        Params params = Params.newParams();

        UpdateQuery updateQuery = supplier.apply(query);

        DocumentEntity entity = getEntity(params, updateQuery, observer);

        return DefaultDocumentPreparedStatement.update(entity, params, query, collectionManager);
    }

    DocumentPreparedStatementAsync prepareAsync(String query, DocumentCollectionManagerAsync collectionManager, DocumentObserverParser observer) {
        Params params = Params.newParams();
        UpdateQuery updateQuery = supplier.apply(query);

        DocumentEntity entity = getEntity(params, updateQuery, observer);

        return DefaultDocumentPreparedStatementAsync.update(entity, params, query, collectionManager);
    }

    private DocumentEntity getEntity(Params params, UpdateQuery updateQuery, DocumentObserverParser observer) {
        String collection = observer.fireEntity(updateQuery.getEntity());
        return getEntity(new UpdasteQueryConditioinSupplier(updateQuery), collection, params, observer);
    }

    private class UpdasteQueryConditioinSupplier implements ConditionQuerySupplier {
        private final UpdateQuery query;

        private UpdasteQueryConditioinSupplier(UpdateQuery query) {
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
