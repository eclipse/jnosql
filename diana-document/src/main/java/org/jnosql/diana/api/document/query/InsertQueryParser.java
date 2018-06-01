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

import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentPreparedStatement;
import org.jnosql.query.InsertQuery;
import org.jnosql.query.InsertQuerySupplier;
import org.jnosql.query.QueryException;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;

final class InsertQueryParser {

    private final InsertQuerySupplier supplier;

    InsertQueryParser() {
        this.supplier = InsertQuerySupplier.getSupplier();
    }

    List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager) {

        InsertQuery insertQuery = supplier.apply(query);

        String collection = insertQuery.getEntity();
        Params params = new Params();

        DocumentEntity entity = getEntity(insertQuery, collection, params);

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

    void queryAsync(String query, DocumentCollectionManagerAsync collectionManager, Consumer<List<DocumentEntity>> callBack) {
        InsertQuery insertQuery = supplier.apply(query);

        String collection = insertQuery.getEntity();
        Params params = new Params();

        DocumentEntity entity = getEntity(insertQuery, collection, params);

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

    DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager) {
        InsertQuery insertQuery = supplier.apply(query);

        String collection = insertQuery.getEntity();
        Params params = new Params();

        Optional<Duration> ttl = insertQuery.getTtl();
        DocumentEntity entity = getEntity(insertQuery, collection, params);

        return DefaultDocumentPreparedStatement.insert(entity, params, query, ttl.orElse(null), collectionManager);

    }

    private DocumentEntity getEntity(InsertQuery insertQuery, String collection, Params params) {
        DocumentEntity entity = DocumentEntity.of(collection);

        insertQuery.getConditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params))
                .map(DocumentCondition::getDocument)
                .forEach(entity::add);
        return entity;
    }


}
