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

import jakarta.data.repository.Pageable;
import jakarta.data.repository.Sort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertTrue(parameters.isEmpty());
    }

    @Test
    public void shouldReturnEmptyNonSpecialParameters() {
        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio"});
        Assertions.assertTrue(parameters.isEmpty());
    }

    @Test
    public void shouldReturnPageable() {
        Pageable pageable = Pageable.ofPage(10);
        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio", pageable});
        Assertions.assertFalse(parameters.isEmpty());
        Assertions.assertEquals(pageable, parameters.getPageable().orElseThrow());
        Assertions.assertTrue(parameters.isSortEmpty());
    }

    @Test
    public void shouldReturnPageableWithSort() {
        Pageable pageable = Pageable.ofPage(10).sortBy(Sort.asc("name"),
                Sort.desc("age"));
        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio", pageable});
        Assertions.assertFalse(parameters.isEmpty());
        Assertions.assertEquals(pageable, parameters.getPageable().orElseThrow());
        Assertions.assertFalse(parameters.isSortEmpty());
        assertThat(parameters.getSorts()).hasSize(2)
                .contains(Sort.asc("name"),
                        Sort.desc("age"));
    }

    @Test
    public void shouldReturnSort() {
        Sort sort = Sort.asc("name");
        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio", sort});
        Assertions.assertFalse(parameters.isEmpty());
        Assertions.assertTrue(parameters.getPageable().isEmpty());
        Assertions.assertFalse(parameters.isSortEmpty());
        assertThat(parameters.getSorts()).hasSize(1)
                .contains(Sort.asc("name"));
    }

    @Test
    public void shouldKeepOrder() {
        Sort sort = Sort.asc("name");
        Pageable pageable = Pageable.ofPage(10).sortBy(Sort.asc("name"),
                Sort.desc("age"));

        SpecialParameters parameters = SpecialParameters.of(new Object[]{10, "Otavio", sort, pageable});
        Assertions.assertFalse(parameters.isEmpty());
        Assertions.assertEquals(pageable, parameters.getPageable().orElseThrow());
        Assertions.assertFalse(parameters.isSortEmpty());
        assertThat(parameters.getSorts()).hasSize(3)
                .containsExactly(sort, Sort.asc("name"),
                        Sort.desc("age"));
    }
}