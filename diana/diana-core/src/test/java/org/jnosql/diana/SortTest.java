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
package org.jnosql.diana;

import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SortTest {


    @Test
    public void shouldReturnErrorWhenParameterIsRequired() {
        Assertions.assertThrows(NullPointerException.class, ()-> Sort.of(null, SortType.ASC));
        Assertions.assertThrows(NullPointerException.class, ()-> Sort.of("name", null));
        Assertions.assertThrows(NullPointerException.class, ()-> Sort.asc(null));
        Assertions.assertThrows(NullPointerException.class, ()-> Sort.desc(null));
    }

    @Test
    public void shouldCreateInstance() {
        Sort asc = Sort.of("name", SortType.ASC);
        Sort desc = Sort.of("name", SortType.DESC);

        Assertions.assertEquals("name", asc.getName());
        Assertions.assertEquals("name", desc.getName());

        Assertions.assertEquals(SortType.ASC, asc.getType());
        Assertions.assertEquals(SortType.DESC, desc.getType());
    }

    @Test
    public void shouldCreateInstanceFromAsc() {
        Sort sort = Sort.asc("name");
        Assertions.assertEquals(Sort.of("name", SortType.ASC), sort);
    }

    @Test
    public void shouldCreateInstanceFromDesc() {
        Sort sort = Sort.desc("name");
        Assertions.assertEquals(Sort.of("name", SortType.DESC), sort);
    }

}