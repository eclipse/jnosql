/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.jnosql.mapping.repository;

import jakarta.data.repository.Limit;
import jakarta.data.repository.Pageable;
import jakarta.data.repository.Sort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SpecialParametersTest {
//should return pageable
// should return pageable with sort
//should return sorts
//should keep the precedence

    @Test
    public void shouldReturnEmpty() {
        SpecialParameters parameters = SpecialParameters.of(new Object[0]);
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void shouldReturnEmptyNonSpecialParameters() {
        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio"});
        assertTrue(parameters.isEmpty());
    }

    @Test
    public void shouldReturnPageable() {
        Pageable pageable = Pageable.ofPage(10);
        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio", pageable});
        assertFalse(parameters.isEmpty());
        Assertions.assertEquals(pageable, parameters.pageable().orElseThrow());
        assertTrue(parameters.isSortEmpty());
    }

    @Test
    public void shouldReturnPageableWithSort() {
        Pageable pageable = Pageable.ofPage(10).sortBy(Sort.asc("name"),
                Sort.desc("age"));
        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio", pageable});
        assertFalse(parameters.isEmpty());
        Assertions.assertEquals(pageable, parameters.pageable().orElseThrow());
        assertFalse(parameters.isSortEmpty());
        assertThat(parameters.sorts()).hasSize(2)
                .contains(Sort.asc("name"),
                        Sort.desc("age"));
    }

    @Test
    public void shouldReturnSort() {
        Sort sort = Sort.asc("name");
        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio", sort});
        assertFalse(parameters.isEmpty());
        assertTrue(parameters.hasOnlySort());
        assertTrue(parameters.pageable().isEmpty());
        assertFalse(parameters.isSortEmpty());
        assertThat(parameters.sorts()).hasSize(1)
                .contains(Sort.asc("name"));
    }

    @Test
    public void shouldKeepOrder() {
        Sort sort = Sort.asc("name");
        Pageable pageable = Pageable.ofPage(10).sortBy(Sort.asc("name"),
                Sort.desc("age"));

        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio", sort, pageable});
        assertFalse(parameters.isEmpty());
        assertFalse(parameters.hasOnlySort());
        Assertions.assertEquals(pageable, parameters.pageable().orElseThrow());
        assertFalse(parameters.isSortEmpty());
        assertThat(parameters.sorts()).hasSize(3)
                .containsExactly(sort, Sort.asc("name"),
                        Sort.desc("age"));
    }

    @Test
    public void shouldReturnLimit() {
        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio", Limit.of(10)});
        assertFalse(parameters.isEmpty());
        Optional<Limit> limit = parameters.limit();
        assertTrue(limit.isPresent());
        Limit limit1 = limit.orElseThrow();
        assertEquals(1, limit1.startAt());
        assertEquals(10, limit1.maxResults());
    }
}