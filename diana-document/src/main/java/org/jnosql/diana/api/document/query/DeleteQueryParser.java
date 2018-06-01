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
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentPreparedStatement;
import org.jnosql.query.DeleteQuery;
import org.jnosql.query.DeleteQuerySupplier;
import org.jnosql.query.QueryException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

final class DeleteQueryParser {

    private final DeleteQuerySupplier selectQuerySupplier;

    DeleteQueryParser() {
        this.selectQuerySupplier = DeleteQuerySupplier.getSupplier();
    }

    List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager) {

        DocumentDeleteQuery documentQuery = getQuery(query);
        collectionManager.delete(documentQuery);
        return Collections.emptyList();
    }

    void queryAsync(String query, DocumentCollectionManagerAsync collectionManager, Consumer<List<DocumentEntity>> callBack) {
        DocumentDeleteQuery documentQuery = getQuery(query);
        collectionManager.delete(documentQuery, v -> callBack.accept(Collections.emptyList()));
    }

    DocumentPreparedStatement prepare(String query, DocumentCollectionManager collectionManager) {
        DeleteQuery deleteQuery = selectQuerySupplier.apply(query);

        String collection = deleteQuery.getEntity();
        List<String> documents = new ArrayList<>(deleteQuery.getFields());
        DocumentCondition condition = null;
        Params params = new Params();

        if (deleteQuery.getWhere().isPresent()) {
            condition = deleteQuery.getWhere().map(c -> Conditions.getCondition(c, params)).get();
        }

        DocumentDeleteQuery documentQuery = new DefaultDocumentDeleteQuery(collection, condition, documents);
        return DefaultDocumentPreparedStatement.delete(documentQuery, params, query, collectionManager);
    }

    private DocumentDeleteQuery getQuery(String query) {
        DeleteQuery deleteQuery = selectQuerySupplier.apply(query);

        String collection = deleteQuery.getEntity();
        List<String> documents = new ArrayList<>(deleteQuery.getFields());
        DocumentCondition condition = null;
        Params params = new Params();

        if (deleteQuery.getWhere().isPresent()) {
            condition = deleteQuery.getWhere().map(c -> Conditions.getCondition(c, params)).get();
        }

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultDocumentDeleteQuery(collection, condition, documents);
    }


}
