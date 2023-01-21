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
import org.eclipse.jnosql.communication.query.DeleteQuery;
import org.eclipse.jnosql.communication.query.DeleteQueryConverter;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DeleteQueryParser implements BiFunction<DeleteQuery, DocumentObserverParser, DocumentDeleteQueryParams> {

    Stream<DocumentEntity> query(String query, DocumentManager manager, DocumentObserverParser observer) {

        DocumentDeleteQuery documentQuery = getQuery(query, observer);
        manager.delete(documentQuery);
        return Stream.empty();
    }


    DocumentPreparedStatement prepare(String query, DocumentManager collectionManager,
                                      DocumentObserverParser observer) {
        Params params = Params.newParams();
        DocumentDeleteQuery documentQuery = getQuery(query, params, observer);
        return DocumentPreparedStatement.delete(documentQuery, params, query, collectionManager);
    }



    @Override
    public DocumentDeleteQueryParams apply(DeleteQuery deleteQuery, DocumentObserverParser observer) {
        Objects.requireNonNull(deleteQuery, "deleteQuery is required");
        Objects.requireNonNull(observer, "observer is required");
        Params params = Params.newParams();
        DocumentDeleteQuery query = getQuery(params, observer, deleteQuery);
        return new DocumentDeleteQueryParams(query, params);
    }

    private DocumentDeleteQuery getQuery(String query, Params params, DocumentObserverParser observer) {
        DeleteQueryConverter converter = new DeleteQueryConverter();
        DeleteQuery deleteQuery = converter.apply(query);

        return getQuery(params, observer, deleteQuery);
    }

    private DocumentDeleteQuery getQuery(Params params, DocumentObserverParser observer,
                                         DeleteQuery deleteQuery) {
        String collection = observer.fireEntity(deleteQuery.entity());
        List<String> documents = deleteQuery.fields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());
        DocumentCondition condition = deleteQuery.where()
                .map(c -> Conditions.getCondition(c, params, observer, collection)).orElse(null);

        return new DefaultDocumentDeleteQuery(collection, condition, documents);
    }

    private DocumentDeleteQuery getQuery(String query, DocumentObserverParser observer) {
        DeleteQueryConverter converter = new DeleteQueryConverter();
        DeleteQuery deleteQuery = converter.apply(query);

        String collection = observer.fireEntity(deleteQuery.entity());
        List<String> documents = deleteQuery.fields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());
        Params params = Params.newParams();

        DocumentCondition condition = deleteQuery.where()
                .map(c -> Conditions.getCondition(c, params, observer, collection))
                .orElse(null);

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultDocumentDeleteQuery(collection, condition, documents);
    }


}
