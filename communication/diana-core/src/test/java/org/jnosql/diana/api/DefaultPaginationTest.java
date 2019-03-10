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
package org.jnosql.diana.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultPaginationTest {


    @Test
    public void shouldReturnErrorWhenPageIsZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Pagination.page(0));
    }

    @Test
    public void shouldReturnErrorWhenPageIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Pagination.page(-1));
    }

    @Test
    public void shouldReturnErrorWhenSizeIsZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Pagination.page(2).of(0));
    }

    @Test
    public void shouldReturnErrorWhenSizeIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Pagination.page(2).of(-1));
    }


    @Test
    public void shouldCreatePaginationInstance() {
        Pagination pagination = Pagination.page(1).of(2);
        Assertions.assertEquals(1, pagination.getPageNumber());
        Assertions.assertEquals(2L, pagination.getPageSize());
        Assertions.assertEquals(2L, pagination.getLimit());
        Assertions.assertEquals(0L, pagination.getSkip());
    }


    @Test
    public void shouldNext() {
        Pagination pagination = Pagination.page(1).of(2);

        checkPagination(pagination, 1, 0, 2, 2);
        Pagination next = pagination.next();

        assertNotNull(next);
        checkPagination(pagination, 2, 2, 2, 2 );

        next = pagination.next();
        checkPagination(pagination, 3, 4, 2, 2 );

        next = pagination.next();
        checkPagination(pagination, 4, 6, 2, 2 );

        next = pagination.next();
        checkPagination(pagination, 5, 8, 2, 2 );

    }

    private void checkPagination(Pagination pagination, long pageNumber, long skip, long limit, long size) {
        Assertions.assertEquals(pageNumber, pagination.getPageNumber());
        Assertions.assertEquals(skip, pagination.getSkip());
        Assertions.assertEquals(limit, pagination.getLimit());
        Assertions.assertEquals(size, pagination.getPageSize());
    }
}