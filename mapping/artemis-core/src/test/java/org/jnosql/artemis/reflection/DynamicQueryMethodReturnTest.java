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
package org.jnosql.artemis.reflection;

import org.jnosql.artemis.Param;
import org.jnosql.artemis.PreparedStatement;
import org.jnosql.artemis.Query;
import org.jnosql.artemis.Repository;
import org.jnosql.diana.api.NonUniqueResultException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

class DynamicQueryMethodReturnTest {



    @Test
    public void shouldReturnEmptyOptional() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getOptional");

        Function<String, List<?>> list = q -> Collections.emptyList();
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(Optional.class.isInstance(execute));
        Optional<Person> optional = Optional.class.cast(execute);
        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    public void shouldReturnOptional() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getOptional");

        Function<String, List<?>> list = q -> singletonList(new Person("Ada"));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(Optional.class.isInstance(execute));
        Optional<Person> optional = Optional.class.cast(execute);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(new Person("Ada"), optional.get());
    }

    @Test
    public void shouldReturnOptionalError() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getOptional");

        Function<String, List<?>> list = q -> asList(new Person("Poliana"), new
                Person("Otavio"));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withPrepareConverter(s -> preparedStatement)
                .build();

        Assertions.assertThrows(NonUniqueResultException.class, () -> dynamicReturn.execute());
    }


    @Test
    public void shouldReturnAnInstance() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getInstance");

        Function<String, List<?>> list = q -> singletonList(new Person("Ada"));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(Person.class.isInstance(execute));
        Person person = Person.class.cast(execute);
        Assertions.assertEquals(new Person("Ada"), person);
    }



    @Test
    public void shouldReturnNull() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getInstance");

        Function<String, List<?>> list = q -> Collections.emptyList();
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertNull(execute);
    }

    @Test
    public void shouldReturnList() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getList");

        Function<String, List<?>> list = q -> singletonList(new Person("Ada"));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(List.class.isInstance(execute));
        List<Person> persons = List.class.cast(execute);
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(new Person("Ada"), persons.get(0));
    }


    @Test
    public void shouldReturnIterable() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getIterable");

        Function<String, List<?>> list = q -> singletonList(new Person("Ada"));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(Iterable.class.isInstance(execute));
        Iterable<Person> persons = List.class.cast(execute);
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }

    @Test
    public void shouldReturnCollection() throws NoSuchMethodException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getCollection");

        Function<String, List<?>> list = q -> singletonList(new Person("Ada"));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(Collection.class.isInstance(execute));
        Collection<Person> persons = Collection.class.cast(execute);
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }


    @Test
    public void shouldReturnQueue() throws NoSuchMethodException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getQueue");

        Function<String, List<?>> list = q -> singletonList(new Person("Ada"));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(Queue.class.isInstance(execute));
        Queue<Person> persons = Queue.class.cast(execute);
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }

    @Test
    public void shouldReturnStream() throws NoSuchMethodException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getStream");

        Function<String, List<?>> list = q -> singletonList(new Person("Ada"));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(Stream.class.isInstance(execute));
        Stream<Person> persons = Stream.class.cast(execute);
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }

    @Test
    public void shouldReturnFromPrepareStatment() throws NoSuchMethodException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(preparedStatement.<Person>getResultList())
                .thenReturn(singletonList(new Person("Ada")));

        Method method = getMethod(PersonRepository.class, "query");


        Function<String, List<?>> list = q -> singletonList(new Person("Ada"));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withQueryConverter(list)
                .withArgs(new Object[]{"Ada"})
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(Iterable.class.isInstance(execute));
        Iterable<Person> persons = List.class.cast(execute);
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }

    private Method getMethod(Class<?> repository, String methodName) throws NoSuchMethodException {
        return Stream.of(repository.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .findFirst().get();

    }

    private static class Person {

        private final String name;

        private Person(String name) {
            this.name = name;
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
    }

    private interface PersonRepository extends Repository<Person, String> {

        @Query("query")
        Optional<Person> getOptional();

        @Query("query")
        Person getInstance();

        @Query("query")
        List<Person> getList();

        @Query("query")
        Iterable<Person> getIterable();

        @Query("query")
        Collection<Person> getCollection();

        @Query("query")
        Set<Person> getSet();

        @Query("query")
        Queue<Person> getQueue();

        @Query("query")
        Stream<Person> getStream();

        @Query("query")
        List<Person> query(@Param("name") String name);
    }

}