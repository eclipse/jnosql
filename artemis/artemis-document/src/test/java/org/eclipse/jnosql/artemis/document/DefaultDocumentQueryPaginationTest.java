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
package org.eclipse.jnosql.artemis.document;

import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.document.DocumentQueryPagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static jakarta.nosql.document.DocumentQuery.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultDocumentQueryPaginationTest {


    @Test
    public void shouldReturnNPEWhenQueryIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                DocumentQueryPagination.of(null, Pagination.page(1).size(2)));
    }

    @Test
    public void shouldReturnNPEWhenPaginationIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                DocumentQueryPagination.of(select().from("column").build(), null));
    }

    @Test
    public void shouldCreateDocumentQueryPagination() {
        DocumentQuery query = select().from("column").build();
        Pagination pagination = Pagination.page(1).size(2);
        DocumentQueryPagination queryPagination = DocumentQueryPagination.of(query, pagination);

        assertNotNull(queryPagination);

        isQueryEquals(query, pagination, queryPagination);
    }

    @Test
    public void shouldOverrideSkipLimit() {

        DocumentQuery query = select().from("column").build();
        Pagination pagination = Pagination.page(1).size(2);
        DocumentQueryPagination queryPagination = DocumentQueryPagination.of(query, pagination);

        assertNotNull(queryPagination);
        assertEquals(pagination.getLimit(), queryPagination.getLimit());
        assertEquals(pagination.getSkip(), queryPagination.getSkip());

    }

    @Test
    public void shouldNext() {
        DocumentQuery query = select().from("column").where("name").eq("Ada").build();
        Pagination pagination = Pagination.page(1).size(2);
        Pagination secondPage = pagination.next();

        DocumentQueryPagination queryPagination = DocumentQueryPagination.of(query, pagination);

        assertNotNull(queryPagination);
        assertEquals(pagination.getLimit(), queryPagination.getLimit());
        assertEquals(pagination.getSkip(), queryPagination.getSkip());

        isQueryEquals(query, pagination, queryPagination);

        DocumentQueryPagination next = queryPagination.next();

        isQueryEquals(query, secondPage, next);
    }


    private void isQueryEquals(DocumentQuery query, Pagination pagination, DocumentQueryPagination queryPagination) {
        assertEquals(query.getDocumentCollection(), queryPagination.getDocumentCollection());
        assertEquals(query.getDocuments(), queryPagination.getDocuments());
        assertEquals(pagination, queryPagination.getPagination());

        assertEquals(query.getSorts(), queryPagination.getSorts());
        assertEquals(query.getCondition().orElse(null), queryPagination.getCondition().orElse(null));
    }
}