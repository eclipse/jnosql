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
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.query.InsertQuery;
import org.jnosql.query.InsertQuerySupplier;
import org.jnosql.query.QueryException;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

final class InsertQueryParser {

    private final InsertQuerySupplier supplier;

    public InsertQueryParser() {
        this.supplier = InsertQuerySupplier.getSupplier();
    }

    public List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager) {

        InsertQuery insertQuery = supplier.apply(query);

        String collection = insertQuery.getEntity();
        DocumentCondition condition = null;
        Params params = new Params();

        Optional<Duration> ttl = insertQuery.getTtl();
        DocumentEntity entity = DocumentEntity.of(collection);

        insertQuery.getConditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params))
                .map(DocumentCondition::getDocument)
                .forEach(entity::add);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        if (ttl.isPresent()) {
            return Collections.singletonList(collectionManager.insert(entity, ttl.get()));
        } else {
            return Collections.singletonList(collectionManager.insert(entity));
        }
    }
}
