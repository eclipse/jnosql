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
        Assertions.assertThrows(IllegalArgumentException.class,() -> Pagination.page(0));
    }

    @Test
    public void shouldReturnErrorWhenPageIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class,() -> Pagination.page(-1));
    }

    @Test
    public void shouldReturnErrorWhenSizeIsZero() {
        Assertions.assertThrows(IllegalArgumentException.class,() -> Pagination.page(2).of(0));
    }

    @Test
    public void shouldReturnErrorWhenSizeIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class,() -> Pagination.page(2).of(-1));
    }


    @Test
    public void shouldCreatePaginationInstance() {
        Pagination pagination = Pagination.page(1).of(2);
        Assertions.assertEquals(1, pagination.getPageNumber());
        Assertions.assertEquals(2L, pagination.getPageSize());
        Assertions.assertEquals(2L, pagination.getLimit());
        Assertions.assertEquals(0L, pagination.getSkip());

    }
}