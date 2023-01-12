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
import org.eclipse.jnosql.communication.Sort;
import org.eclipse.jnosql.communication.query.SelectQuery;
import org.eclipse.jnosql.communication.query.SelectQueryProvider;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class SelectQueryParser implements BiFunction<SelectQuery, DocumentObserverParser, DocumentQueryParams> {

    private final SelectQueryProvider selectQueryProvider;

    public SelectQueryParser() {
        this.selectQueryProvider = new SelectQueryProvider();
    }

    Stream<DocumentEntity> query(String query, DocumentManager manager, DocumentObserverParser observer) {
        DocumentQuery documentQuery = getDocumentQuery(query, observer);
        return manager.select(documentQuery);
    }


    DocumentPreparedStatement prepare(String query, DocumentManager collectionManager, DocumentObserverParser observer) {

        Params params = Params.newParams();

        SelectQuery selectQuery = selectQueryProvider.apply(query);

        DocumentQuery documentQuery = getDocumentQuery(params, selectQuery, observer);
        return DocumentPreparedStatement.select(documentQuery, params, query, collectionManager);
    }


    @Override
    public DocumentQueryParams apply(SelectQuery selectQuery, DocumentObserverParser observer) {
        Objects.requireNonNull(selectQuery, "selectQuery is required");
        Objects.requireNonNull(observer, "observer is required");
        Params params = Params.newParams();
        DocumentQuery columnQuery = getDocumentQuery(params, selectQuery, observer);
        return new DocumentQueryParams(columnQuery, params);
    }

    private DocumentQuery getDocumentQuery(String query, DocumentObserverParser observer) {

        SelectQuery selectQuery = selectQueryProvider.apply(query);
        String collection = observer.fireEntity(selectQuery.entity());
        long limit = selectQuery.limit();
        long skip = selectQuery.skip();
        List<String> documents = selectQuery.fields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());
        List<Sort> sorts = selectQuery.orderBy().stream().map(s -> toSort(s, observer, collection))
                .collect(toList());
        DocumentCondition condition = null;
        Params params = Params.newParams();
        if (selectQuery.where().isPresent()) {
            condition = selectQuery.where().map(c -> Conditions.getCondition(c, params, observer, collection)).get();
        }

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }
        return new DefaultDocumentQuery(limit, skip, collection, documents, sorts, condition);
    }

    private DocumentQuery getDocumentQuery(Params params, SelectQuery selectQuery, DocumentObserverParser observer) {

        String collection = observer.fireEntity(selectQuery.entity());
        long limit = selectQuery.limit();
        long skip = selectQuery.skip();
        List<String> documents = selectQuery.fields().stream()
                .map(f -> observer.fireField(collection, f))
                .collect(Collectors.toList());

        List<Sort> sorts = selectQuery.orderBy().stream().map(s -> toSort(s, observer, collection)).collect(toList());
        DocumentCondition condition = null;
        if (selectQuery.where().isPresent()) {
            condition = selectQuery.where().map(c -> Conditions.getCondition(c, params, observer, collection)).get();
        }

        return new DefaultDocumentQuery(limit, skip, collection, documents, sorts, condition);
    }

    private Sort toSort(Sort sort, DocumentObserverParser observer, String entity) {
        return Sort.of(observer.fireField(entity, sort.name()), sort.type());
    }


}
