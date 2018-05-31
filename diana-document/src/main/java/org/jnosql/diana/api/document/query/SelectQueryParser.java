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

import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentQuery;
import org.jnosql.query.SelectQuery;
import org.jnosql.query.SelectQuerySupplier;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.jnosql.diana.api.Sort.SortType.ASC;
import static org.jnosql.diana.api.Sort.SortType.DESC;

final class SelectQueryParser {

    private final SelectQuerySupplier selectQuerySupplier;

    public SelectQueryParser() {
        this.selectQuerySupplier = SelectQuerySupplier.getSupplier();
    }

    public List<DocumentEntity> query(String query, DocumentCollectionManager collectionManager) {
        SelectQuery selectQuery = selectQuerySupplier.apply(query);

        String collection = selectQuery.getEntity();
        long limit = selectQuery.getLimit();
        long skip = selectQuery.getSkip();
        List<String> documents = new ArrayList<>(selectQuery.getFields());
        List<Sort> sorts = selectQuery.getOrderBy().stream().map(this::toSort).collect(toList());
        DocumentCondition condition = null;

        if(selectQuery.getWhere().isPresent()) {
            condition = selectQuery.getWhere().map(Conditions::getCondition).get();
        }

        DocumentQuery documentQuery = new DefaultDocumentQuery(limit, skip, collection, documents, sorts, condition);
        return collectionManager.select(documentQuery);
    }

    private Sort toSort(org.jnosql.query.Sort sort) {
        return Sort.of(sort.getName(), sort.getType().equals(org.jnosql.query.Sort.SortType.ASC)? ASC: DESC);
    }
}
