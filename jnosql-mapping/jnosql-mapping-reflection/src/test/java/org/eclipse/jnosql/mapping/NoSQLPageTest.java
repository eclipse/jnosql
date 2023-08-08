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
package org.eclipse.jnosql.mapping;

import jakarta.data.repository.Page;
import jakarta.data.repository.Pageable;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NoSQLPageTest {


    @Test
    public void shouldReturnErrorWhenNull() {
        assertThrows(NullPointerException.class, ()->
                NoSQLPage.of(Collections.emptyList(), null));

        assertThrows(NullPointerException.class, ()->
                NoSQLPage.of(null, Pageable.ofPage(2)));
    }


    @Test
    public void shouldReturnUnsupportedOperation() {
        Page<Person> page = NoSQLPage.of(Collections.singletonList(Person.builder().withName("Otavio").build()),
                Pageable.ofPage(2));

        assertThrows(UnsupportedOperationException.class, page::totalPages);

        assertThrows(UnsupportedOperationException.class, page::totalElements);
    }

    @Test
    public void shouldReturnHasContent() {

        Page<Person> page = NoSQLPage.of(Collections.singletonList(Person.builder().withName("Otavio").build()),
                Pageable.ofPage(2));

        Assertions.assertTrue(page.hasContent());
        page = NoSQLPage.of(Collections.emptyList(),
                Pageable.ofPage(2));
        Assertions.assertFalse(page.hasContent());
    }

    @Test
    public void shouldNumberOfElements() {

        Page<Person> page = NoSQLPage.of(Collections.singletonList(Person.builder().withName("Otavio").build()),
                Pageable.ofPage(2));

        assertEquals(1, page.numberOfElements());
    }

    @Test
    public void shouldIterator() {
        Page<Person> page = NoSQLPage.of(Collections.singletonList(Person.builder().withName("Otavio").build()),
                Pageable.ofPage(2));
        Assertions.assertNotNull(page.iterator());
    }

    @Test
    public void shouldPageable() {
        Page<Person> page = NoSQLPage.of(Collections.singletonList(Person.builder().withName("Otavio").build()),
                Pageable.ofPage(2));
        Pageable pageable = page.pageable();
        Assertions.assertNotNull(pageable);
        assertEquals(Pageable.ofPage(2), pageable);
    }

    @Test
    public void shouldNextPageable() {
        Page<Person> page = NoSQLPage.of(Collections.singletonList(Person.builder().withName("Otavio").build()),
                Pageable.ofPage(2));
        Pageable pageable = page.nextPageable();
        assertEquals(Pageable.ofPage(3), pageable);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenPageableIsNull() {
        assertThrows(NullPointerException.class, () -> NoSQLPage.skip(null));
    }

    @Test
    public void shouldCalculateSkip() {
        long skipValue = NoSQLPage.skip(Pageable.ofPage(2).size(10));
        assertEquals(10, skipValue);
    }

    @Test
    public void shouldCalculateSkipForFirstPage() {
        // Create a pageable with page=1 and size=5
        long skipValue = NoSQLPage.skip(Pageable.ofPage(1).size(5));
        assertEquals(0, skipValue);
    }
}