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
package org.jnosql.artemis.column;

import org.jnosql.artemis.Pagination;
import org.jnosql.diana.api.column.ColumnQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultColumnQueryPaginationTest {


    @Test
    public void shouldReturnNPEWhenQueryIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                ColumnQueryPagination.of(null, Pagination.page(1).size(2)));
    }

    @Test
    public void shouldReturnNPEWhenPaginationIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                ColumnQueryPagination.of(select().from("column").build(), null));
    }

    @Test
    public void shouldCreateColumnQueryPagination() {
        ColumnQuery query = select().from("column").build();
        Pagination pagination = Pagination.page(1).size(2);
        ColumnQueryPagination queryPagination = ColumnQueryPagination.of(query, pagination);

        assertNotNull(queryPagination);

        isQueryEquals(query, pagination, queryPagination);
    }

    @Test
    public void shouldOverrideSkipLimit() {

        ColumnQuery query = select().from("column").build();
        Pagination pagination = Pagination.page(1).size(2);
        ColumnQueryPagination queryPagination = ColumnQueryPagination.of(query, pagination);

        assertNotNull(queryPagination);
        assertEquals(pagination.getLimit(), queryPagination.getLimit());
        assertEquals(pagination.getSkip(), queryPagination.getSkip());

    }

    @Test
    public void shouldNext() {
        ColumnQuery query = select().from("column").where("name").eq("Ada").build();
        Pagination pagination = Pagination.page(1).size(2);
        Pagination secondPage = pagination.next();

        ColumnQueryPagination queryPagination = ColumnQueryPagination.of(query, pagination);

        assertNotNull(queryPagination);
        assertEquals(pagination.getLimit(), queryPagination.getLimit());
        assertEquals(pagination.getSkip(), queryPagination.getSkip());

        isQueryEquals(query, pagination, queryPagination);

        ColumnQueryPagination next = queryPagination.next();

        isQueryEquals(query, secondPage, next);
    }


    private void isQueryEquals(ColumnQuery query, Pagination pagination, ColumnQueryPagination queryPagination) {
        assertEquals(query.getColumnFamily(), queryPagination.getColumnFamily());
        assertEquals(query.getColumns(), queryPagination.getColumns());
        assertEquals(pagination, queryPagination.getPagination());

        assertEquals(query.getSorts(), queryPagination.getSorts());
        assertEquals(query.getCondition().orElse(null), queryPagination.getCondition().orElse(null));
    }

}