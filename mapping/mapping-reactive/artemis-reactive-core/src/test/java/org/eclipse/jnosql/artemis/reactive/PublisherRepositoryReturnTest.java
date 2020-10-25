/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.artemis.reactive;

import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import org.eclipse.jnosql.artemis.repository.DynamicReturn;
import org.eclipse.jnosql.artemis.repository.RepositoryReturn;
import org.eclipse.microprofile.reactive.streams.operators.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PublisherRepositoryReturnTest {

    private final RepositoryReturn repositoryReturn = new PublisherRepositoryReturn();

    @Mock
    private Page<Person> page;

    @Test
    public void shouldReturnIsCompatible() {
        Assertions.assertTrue(repositoryReturn.isCompatible(Person.class, Publisher.class));
        assertFalse(repositoryReturn.isCompatible(Object.class, Person.class));
        assertFalse(repositoryReturn.isCompatible(Person.class, Object.class));
    }

    @Test
    public void shouldReturnList() {
        Person ada = new Person("Ada");
        DynamicReturn<Person> dynamic = DynamicReturn.builder()
                .withSingleResult(Optional::empty)
                .withClassSource(Person.class)
                .withResult(() -> Stream.of(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        final CompletionSubscriber<Person, Optional<Person>> subscriber = ReactiveStreams.<Person>builder().findFirst().build();
        Publisher<Person> publisher = (Publisher<Person>) repositoryReturn.convert(dynamic);
        publisher.subscribe(subscriber);
        final CompletionStage<Optional<Person>> completion = subscriber.getCompletion();
        AtomicReference<Person> person = new AtomicReference<>();
        completion.thenAccept(o -> person.set(o.get()));
        assertNotNull(person);

        assertEquals(ada, person.get());
    }

    @Test
    public void shouldReturnListPage() {
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

        final CompletionSubscriber<Person, Optional<Person>> subscriber = ReactiveStreams.<Person>builder().findFirst().build();
        Publisher<Person> publisher= (Publisher<Person>) repositoryReturn.convertPageable(dynamic);
        publisher.subscribe(subscriber);
        final CompletionStage<Optional<Person>> completion = subscriber.getCompletion();
        AtomicReference<Person> person = new AtomicReference<>();
        completion.thenAccept(o -> person.set(o.get()));
        assertNotNull(person);
        assertEquals(ada, person.get());
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