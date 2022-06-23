/*
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
 */
package org.eclipse.jnosql.mapping;

import jakarta.nosql.Sort;
import jakarta.nosql.mapping.Sorts;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultSortsTest {

    @Test
    public void shouldCreateNewSortInstance() {
        Sorts sorts = Sorts.sorts();
        assertTrue(sorts.getSorts().isEmpty());
    }

    @Test
    public void shouldAsc() {
        Sorts sorts = Sorts.sorts().asc("name");
        assertThat(sorts.getSorts(), Matchers.contains(Sort.asc("name")));
    }

    @Test
    public void shouldDesc() {
        Sorts sorts = Sorts.sorts().desc("name");
        assertThat(sorts.getSorts(), Matchers.contains(Sort.desc("name")));
    }

    @Test
    public void shouldAdd() {
        Sort sort = Sort.desc("name");
        Sorts sorts = Sorts.sorts().add(sort);
        assertThat(sorts.getSorts(), Matchers.contains(sort));
    }

    @Test
    public void shouldRemove() {
        Sort sort = Sort.desc("name");
        Sorts sorts = Sorts.sorts().add(sort).desc("name");
        sorts.remove(sort);
        assertTrue(sorts.getSorts().isEmpty());
    }

    @Test
    public void shouldReturnErrorWhenUsesNullParameters(){
        assertThrows(NullPointerException.class, () -> Sorts.sorts().asc(null));
        assertThrows(NullPointerException.class, () -> Sorts.sorts().desc(null));
        assertThrows(NullPointerException.class, () -> Sorts.sorts().add(null));
        assertThrows(NullPointerException.class, () -> Sorts.sorts().remove(null));
    }
}