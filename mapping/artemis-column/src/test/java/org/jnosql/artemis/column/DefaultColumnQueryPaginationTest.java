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
import org.jnosql.artemis.column.query.ColumnQueryMapperBuilder;
import org.jnosql.diana.api.column.ColumnQuery;
import org.jnosql.diana.api.column.query.ColumnQueryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultColumnQueryPaginationTest {


    @Test
    public void shouldReturnNPEWhenQueryIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                ColumnQueryPagination.of(null, Pagination.page(1).of(2)));
    }

    @Test
    public void shouldReturnNPEWhenPaginationIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                ColumnQueryPagination.of(ColumnQueryBuilder.select().from("column").build(), null));
    }

    @Test
    public void shouldCreateColumnQueryPagination() {
        ColumnQuery query = ColumnQueryBuilder.select().from("column").build();
        Pagination pagination = Pagination.page(1).of(2);
        ColumnQueryPagination queryPagination = ColumnQueryPagination.of(query, pagination);

        Assertions.assertNotNull(queryPagination);

        Assertions.assertEquals(query.getColumnFamily(), queryPagination.getColumnFamily());
        Assertions.assertEquals(query.getColumns(), queryPagination.getColumns());
        Assertions.assertEquals(query.getLimit(), queryPagination.getLimit());
        Assertions.assertEquals(query.getSkip(), queryPagination.getSkip());
        Assertions.assertEquals(query.getSorts(), queryPagination.getSorts());
        Assertions.assertEquals(query.getCondition().orElse(null), queryPagination.getCondition().orElse(null));
    }



}