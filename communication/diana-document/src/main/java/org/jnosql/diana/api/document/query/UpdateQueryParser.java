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
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.Params;
import org.jnosql.diana.api.QueryException;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentObserverParser;
import org.jnosql.diana.api.document.DocumentPreparedStatement;
import org.jnosql.diana.api.document.DocumentPreparedStatementAsync;
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

    List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {

        UpdateQuery updateQuery = supplier.apply(query);

        Params params = new Params();

        DocumentEntity entity = getEntity(params, updateQuery, observer);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return singletonList(collectionManager.update(entity));
    }

    void queryAsync(String query, DocumentCollectionManagerAsync collectionManager,
                    Consumer<List<DocumentEntity>> callBack, DocumentObserverParser observer) {

        UpdateQuery updateQuery = supplier.apply(query);

        Params params = new Params();

        DocumentEntity entity = getEntity(params, updateQuery, observer);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        collectionManager.update(entity, c -> callBack.accept(singletonList(c)));
    }

    DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager, DocumentObserverParser observer) {

        Params params = new Params();

        UpdateQuery updateQuery = supplier.apply(query);

        DocumentEntity entity = getEntity(params, updateQuery, observer);

        return DefaultDocumentPreparedStatement.update(entity, params, query, collectionManager);
    }

    DocumentPreparedStatementAsync prepareAsync(String query, DocumentCollectionManagerAsync collectionManager, DocumentObserverParser observer) {
        Params params = new Params();
        UpdateQuery updateQuery = supplier.apply(query);

        DocumentEntity entity = getEntity(params, updateQuery, observer);

        return DefaultDocumentPreparedStatementAsync.update(entity, params, query, collectionManager);
    }

    private DocumentEntity getEntity(Params params, UpdateQuery updateQuery, DocumentObserverParser observer) {
        String collection = observer.fireEntity(updateQuery.getEntity());

        DocumentEntity entity = DocumentEntity.of(collection);

        updateQuery.getConditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params, observer, collection))
                .map(DocumentCondition::getDocument)
                .forEach(entity::add);
        return entity;
    }

}
