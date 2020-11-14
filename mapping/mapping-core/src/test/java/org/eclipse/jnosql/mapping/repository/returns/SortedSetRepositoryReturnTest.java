/*
 *  Copyright (c) 2020 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.repository.returns;

import jakarta.nosql.mapping.DynamicQueryException;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import org.eclipse.jnosql.mapping.repository.DynamicReturn;
import org.eclipse.jnosql.mapping.repository.RepositoryReturn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class SortedSetRepositoryReturnTest {

    private final RepositoryReturn repositoryReturn = new SortedSetRepositoryReturn();

    @Mock
    private Page<Person> page;

    @Test
    public void shouldReturnIsCompatible() {
        Assertions.assertTrue(repositoryReturn.isCompatible(Person.class, NavigableSet.class));
        Assertions.assertTrue(repositoryReturn.isCompatible(Person.class, SortedSet.class));
        assertFalse(repositoryReturn.isCompatible(Object.class, Person.class));
        assertFalse(repositoryReturn.isCompatible(Person.class, Object.class));
    }


    @Test
    public void shouldReturnTreeSetPage() {
        Person ada = new Person("Ada");
        DynamicReturn<Person> dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withResult(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withStreamPagination(p -> Stream.of(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        TreeSet<Person> person = (TreeSet<Person>) repositoryReturn.convertPageable(dynamic);
        Assertions.assertNotNull(person);
        assertFalse(person.isEmpty());
        assertEquals(ada, person.stream().findFirst().get());
    }

    @Test
    public void shouldReturnTreeSet() {
        Person ada = new Person("Ada");
        DynamicReturn<Person> dynamic = DynamicReturn.builder()
                .withSingleResult(Optional::empty)
                .withClassSource(Person.class)
                .withResult(() -> Stream.of(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        TreeSet<Person> person = (TreeSet<Person>) repositoryReturn.convert(dynamic);
        Assertions.assertNotNull(person);
        Assertions.assertFalse(person.isEmpty());
        Assertions.assertEquals(ada, person.stream().findFirst().get());
    }

    @Test
    public void shouldReturnErrorOnTreeSetPage() {
        Animal animal = new Animal();
        DynamicReturn<Animal> dynamic = DynamicReturn.builder()
                .withClassSource(Animal.class)
                .withSingleResult(Optional::empty)
                .withResult(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withStreamPagination(p -> Stream.of(animal))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        Assertions.assertThrows(DynamicQueryException.class, () -> repositoryReturn.convertPageable(dynamic));
    }

    @Test
    public void shouldReturnErrorOnTreeSet() {
        Animal animal = new Animal();
        DynamicReturn<Animal> dynamic = DynamicReturn.builder()
                .withClassSource(Animal.class)
                .withSingleResult(Optional::empty)
                .withResult(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withStreamPagination(p -> Stream.of(animal))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        Assertions.assertThrows(DynamicQueryException.class, () -> repositoryReturn.convert(dynamic));
    }

    private static class Animal {

    }

    private static class Person implements Comparable<Person> {

        private String name;

        public Person(String name) {
            this.name = name;
        }

        public Person() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Person person = (Person) o;
            return Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }

        @Override
        public int compareTo(Person o) {
            return name.compareTo(o.name);
        }
    }

}