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
import org.eclipse.jnosql.communication.query.JSONQueryValue;
import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.UpdateQuery;
import org.eclipse.jnosql.communication.query.UpdateQueryProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

final class UpdateQueryParser extends ConditionQueryParser {

    private final UpdateQueryProvider supplier;

    UpdateQueryParser() {
        this.supplier = new UpdateQueryProvider();
    }

    Stream<DocumentEntity> query(String query, DocumentManager collectionManager, DocumentObserverParser observer) {

        UpdateQuery updateQuery = supplier.apply(query);

        Params params = Params.newParams();

        DocumentEntity entity = getEntity(params, updateQuery, observer);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return Stream.of(collectionManager.update(entity));
    }


    DocumentPreparedStatement prepare(String query, DocumentManager collectionManager, DocumentObserverParser observer) {

        Params params = Params.newParams();

        UpdateQuery updateQuery = supplier.apply(query);

        DocumentEntity entity = getEntity(params, updateQuery, observer);
        return DocumentPreparedStatement.update(entity, params, query, collectionManager);
    }

    private DocumentEntity getEntity(Params params, UpdateQuery updateQuery, DocumentObserverParser observer) {
        String collection = observer.fireEntity(updateQuery.entity());
        return getEntity(new UpdateQueryConditioinSupplier(updateQuery), collection, params, observer);
    }

    private static final class UpdateQueryConditioinSupplier implements ConditionQuerySupplier {
        private final UpdateQuery query;

        private UpdateQueryConditioinSupplier(UpdateQuery query) {
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
