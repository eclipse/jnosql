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
package org.eclipse.jnosql.artemis.reflection;

import jakarta.nosql.mapping.DynamicQueryException;
import jakarta.nosql.mapping.Page;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DynamicReturnTypeTest {


    @Test
    public void shouldReturnInstance() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, Person.class);
        assertEquals(DynamicReturnType.INSTANCE, returnType);
    }

    @Test
    public void shouldReturnOptional() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, Optional.class);
        assertEquals(DynamicReturnType.OPTIONAL, returnType);
    }

    @Test
    public void shouldReturnList() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, List.class);
        assertEquals(DynamicReturnType.LIST, returnType);
    }

    @Test
    public void shouldReturnIterable() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, Iterable.class);
        assertEquals(DynamicReturnType.ITERABLE, returnType);
    }

    @Test
    public void shouldReturnCollection() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, Collection.class);
        assertEquals(DynamicReturnType.COLLECTION, returnType);
    }

    @Test
    public void shouldReturnSet() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, Set.class);
        assertEquals(DynamicReturnType.SET, returnType);
    }


    @Test
    public void shouldReturnQueue() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, Queue.class);
        assertEquals(DynamicReturnType.QUEUE, returnType);
    }

    @Test
    public void shouldReturnDeque() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, Deque.class);
        assertEquals(DynamicReturnType.DEQUE, returnType);
    }

    @Test
    public void shouldReturnNavigableSet() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, NavigableSet.class);
        assertEquals(DynamicReturnType.NAVIGABLE_SET, returnType);
    }


    @Test
    public void shouldReturnSortedSet() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, SortedSet.class);
        assertEquals(DynamicReturnType.SORTED_SET, returnType);
    }


    @Test
    public void shouldReturnStream() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, Stream.class);
        assertEquals(DynamicReturnType.STREAM, returnType);
    }

    @Test
    public void shouldReturnPage() {
        DynamicReturnType returnType = DynamicReturnType.of(Person.class, Page.class);
        assertEquals(DynamicReturnType.PAGE, returnType);
    }


    @Test
    public void shouldReturnErrorWhenSortedSetAndEntityIsNotComparable() {
        assertThrows(DynamicQueryException.class, () -> DynamicReturnType.of(Animal.class, SortedSet.class));
    }

    @Test
    public void shouldReturnErrorWhenNavigableSetAndEntityIsNotComparable() {
        assertThrows(DynamicQueryException.class, () -> DynamicReturnType.of(Animal.class, NavigableSet.class));
    }

    private static class Animal{

    }
    private static class Person implements Comparable<Person> {
        private String name;

        @Override
        public int compareTo(Person o) {
            return 0;
        }
    }
}