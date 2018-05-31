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
import org.jnosql.diana.api.document.DocumentPreparedStatement;
import org.jnosql.query.QueryException;
import org.jnosql.query.UpdateQuery;
import org.jnosql.query.UpdateQuerySupplier;

import java.util.Collections;
import java.util.List;

final class UpdateQueryParser {

    private final UpdateQuerySupplier supplier;

    UpdateQueryParser() {
        this.supplier = UpdateQuerySupplier.getSupplier();
    }

    List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager) {

        UpdateQuery updateQuery = supplier.apply(query);

        String collection = updateQuery.getEntity();
        Params params = new Params();

        DocumentEntity entity = DocumentEntity.of(collection);

        updateQuery.getConditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params))
                .map(DocumentCondition::getDocument)
                .forEach(entity::add);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return Collections.singletonList(collectionManager.update(entity));
    }

    DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager) {

        UpdateQuery updateQuery = supplier.apply(query);

        String collection = updateQuery.getEntity();
        Params params = new Params();

        DocumentEntity entity = DocumentEntity.of(collection);

        updateQuery.getConditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params))
                .map(DocumentCondition::getDocument)
                .forEach(entity::add);

        return DefaultDocumentPreparedStatement.update(entity, params, query, collectionManager);
    }
}
