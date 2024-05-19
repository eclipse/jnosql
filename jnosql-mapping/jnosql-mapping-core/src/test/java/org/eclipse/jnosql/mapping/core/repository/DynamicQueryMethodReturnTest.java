/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.core.repository;

import jakarta.data.exceptions.NonUniqueResultException;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.mapping.PreparedStatement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

class DynamicQueryMethodReturnTest {

    @Test
    void shouldReturnEmptyOptional() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getOptional");

        Mockito.when(preparedStatement.result()).thenReturn(Stream.empty());
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Optional);
        Optional<Person> optional = (Optional) execute;
        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    void shouldReturnOptional() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getOptional");
       Mockito.when(preparedStatement.result()).thenReturn(Stream.of(new Person("Ada")));

        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Optional);
        Optional<Person> optional = (Optional) execute;
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(new Person("Ada"), optional.get());
    }

    @Test
    void shouldReturnOptionalError() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getOptional");

        Mockito.when(preparedStatement.result()).thenReturn(Stream.of(new Person("Poliana"), new
                Person("Otavio")));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();

        Assertions.assertThrows(NonUniqueResultException.class, dynamicReturn::execute);
    }

    @Test
    void shouldReturnAnInstance() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getInstance");

        Mockito.when(preparedStatement.result()).thenReturn(Stream.of(new Person("Ada")));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Person);
        Person person = (Person) execute;
        Assertions.assertEquals(new Person("Ada"), person);
    }

    @Test
    void shouldReturnNull() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getInstance");

        Mockito.when(preparedStatement.result()).thenReturn(Stream.empty());
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertNull(execute);
    }

    @Test
    void shouldReturnList() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getList");

        Mockito.when(preparedStatement.result()).thenReturn(Stream.of(new Person("Ada")));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof List);
        List<Person> persons = (List) execute;
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(new Person("Ada"), persons.get(0));
    }


    @Test
    void shouldReturnIterable() throws NoSuchMethodException {

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getIterable");

        Mockito.when(preparedStatement.result()).thenReturn(Stream.of(new Person("Ada")));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Iterable);
        Iterable<Person> persons = (List) execute;
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }

    @Test
    void shouldReturnCollection() throws NoSuchMethodException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getCollection");

        Mockito.when(preparedStatement.result()).thenReturn(Stream.of(new Person("Ada")));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Collection);
        Collection<Person> persons = (Collection) execute;
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }


    @Test
    void shouldReturnQueue() throws NoSuchMethodException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getQueue");

        Mockito.when(preparedStatement.result()).thenReturn(Stream.of(new Person("Ada")));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Queue);
        Queue<Person> persons = (Queue) execute;
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }

    @Test
    void shouldReturnStream() throws NoSuchMethodException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Method method = getMethod(PersonRepository.class, "getStream");

        Mockito.when(preparedStatement.result()).thenReturn(Stream.of(new Person("Ada")));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();

        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Stream);
        Stream<Person> persons = (Stream) execute;
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }

    @Test
    void shouldReturnFromPrepareStatement() throws NoSuchMethodException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(preparedStatement.<Person>result())
                .thenReturn(Stream.of(new Person("Ada")));

        Method method = getMethod(PersonRepository.class, "query");

        Mockito.when(preparedStatement.result()).thenReturn(Stream.of(new Person("Ada")));
        DynamicQueryMethodReturn dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withArgs(new Object[]{"Ada"})
                .withPrepareConverter(s -> preparedStatement)
                .build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Iterable);
        Iterable<Person> persons = (List) execute;
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }

    @Test
    void shouldReturnLong() throws NoSuchMethodException {
        var preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(preparedStatement.count()).thenReturn(1L);
        Mockito.when(preparedStatement.isCount()).thenReturn(true);
        Method method = getMethod(PersonRepository.class, "count");


        var dynamicReturn = DynamicQueryMethodReturn.builder()
                .withTypeClass(Person.class)
                .withMethod(method)
                .withPrepareConverter(s -> preparedStatement)
                .build();

        Object execute = dynamicReturn.execute();
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(execute).isInstanceOf(Long.class);
            soft.assertThat(execute).isEqualTo(1L);
        });
    }

    private Method getMethod(Class<?> repository, String methodName) throws NoSuchMethodException {
        return Stream.of(repository.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .findFirst().get();

    }

    private record Person(String name) {

    }

    private interface PersonRepository extends CrudRepository<Person, String> {

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

        @Query("select count(this) from Person")
        long count();
    }

}