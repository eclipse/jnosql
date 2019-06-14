/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.artemis.document;

import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.Sort;
import org.jnosql.diana.document.DocumentCondition;
import jakarta.nosql.document.DocumentQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class DefaultDocumentQueryPagination implements DocumentQueryPagination {
    
    private final DocumentQuery query;

    private final Pagination pagination;

    DefaultDocumentQueryPagination(DocumentQuery query, Pagination pagination) {
        this.query = query;
        this.pagination = pagination;
    }

    @Override
    public long getLimit() {
        return pagination.getLimit();
    }

    @Override
    public long getSkip() {
        return pagination.getSkip();
    }

    @Override
    public String getDocumentCollection() {
        return query.getDocumentCollection();
    }

    @Override
    public Optional<DocumentCondition> getCondition() {
        return query.getCondition();
    }

    @Override
    public List<String> getDocuments() {
        return query.getDocuments();
    }

    @Override
    public List<Sort> getSorts() {
        return query.getSorts();
    }

    @Override
    public DocumentQueryPagination next() {
        return new DefaultDocumentQueryPagination(query, pagination.next());
    }

    @Override
    public Pagination getPagination() {
        return pagination.unmodifiable();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultDocumentQueryPagination that = (DefaultDocumentQueryPagination) o;
        return Objects.equals(query, that.query) &&
                Objects.equals(pagination, that.pagination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, pagination);
    }

    @Override
    public String toString() {
        return "DefaultDocumentQueryPagination{" +
                "query=" + query +
                ", pagination=" + pagination +
                '}';
    }
}
