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

import org.jnosql.artemis.Repository;
import org.jnosql.diana.api.NonUniqueResultException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

class DynamicReturnTest {

    @Test
    public void shouldReturnEmptyOptional() throws NoSuchMethodException {

        Method method = getMethod(PersonRepository.class, "getOptional");
        Supplier<List<?>> list = Collections::emptyList;
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Optional);
        Optional<Person> optional = (Optional) execute;
        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    public void shouldReturnOptional() throws NoSuchMethodException {

        Method method = getMethod(PersonRepository.class, "getOptional");
        Supplier<List<?>> list = () -> singletonList(new Person("Ada"));
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Optional);
        Optional<Person> optional = (Optional) execute;
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(new Person("Ada"), optional.get());
    }

    @Test
    public void shouldReturnOptionalError() throws NoSuchMethodException {

        Method method = getMethod(PersonRepository.class, "getOptional");
        Supplier<List<?>> list = () -> Arrays.asList(new Person("Poliana"), new Person("Otavio"));
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();

        Assertions.assertThrows(NonUniqueResultException.class, dynamicReturn::execute);

    }


    @Test
    public void shouldReturnAnInstance() throws NoSuchMethodException {
        Method method = getMethod(PersonRepository.class, "getInstance");
        Supplier<List<?>> list = () -> singletonList(new Person("Ada"));
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Person);
        Person person = (Person) execute;
        Assertions.assertEquals(new Person("Ada"), person);
    }


    @Test
    public void shouldReturnNull() throws NoSuchMethodException {

        Method method = getMethod(PersonRepository.class, "getInstance");
        Supplier<List<?>> list = Collections::emptyList;
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();
        Object execute = dynamicReturn.execute();
        Assertions.assertNull(execute);
    }


    @Test
    public void shouldReturnList() throws NoSuchMethodException {

        Method method = getMethod(PersonRepository.class, "getList");
        Supplier<List<?>> list = () -> singletonList(new Person("Ada"));
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof List);
        List<Person> persons = (List) execute;
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(new Person("Ada"), persons.get(0));
    }

    @Test
    public void shouldReturnIterable() throws NoSuchMethodException {

        Method method = getMethod(PersonRepository.class, "getIterable");
        Supplier<List<?>> list = () -> singletonList(new Person("Ada"));
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Iterable);
        Iterable<Person> persons = (List) execute;
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }



    @Test
    public void shouldReturnCollection() throws NoSuchMethodException {

        Method method = getMethod(PersonRepository.class, "getCollection");
        Supplier<List<?>> list = () -> singletonList(new Person("Ada"));
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Collection);
        Collection<Person> persons = (Collection) execute;
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }

    @Test
    public void shouldReturnSet() throws NoSuchMethodException {

        Method method = getMethod(PersonRepository.class, "getSet");
        Supplier<List<?>> list = () -> singletonList(new Person("Ada"));
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Set);
        Set<Person> persons = (Set) execute;
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }

    @Test
    public void shouldReturnQueue() throws NoSuchMethodException {

        Method method = getMethod(PersonRepository.class, "getQueue");
        Supplier<List<?>> list = () -> singletonList(new Person("Ada"));
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Queue);
        Queue<Person> persons = (Queue) execute;
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(new Person("Ada"), persons.iterator().next());
    }


    @Test
    public void shouldReturnStream() throws NoSuchMethodException {

        Method method = getMethod(PersonRepository.class, "getStream");
        Supplier<List<?>> list = () -> singletonList(new Person("Ada"));
        Supplier<Optional<?>> singlResult = DynamicReturn.toSingleResult(method).apply(list);
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withMethodSource(method).withList(list)
                .withSingleResult(singlResult).build();
        Object execute = dynamicReturn.execute();
        Assertions.assertTrue(execute instanceof Stream);
        Stream<Person> persons = (Stream) execute;
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

        Optional<Person> getOptional();

        Person getInstance();

        List<Person> getList();

        Iterable<Person> getIterable();

        Collection<Person> getCollection();

        Set<Person> getSet();

        Queue<Person> getQueue();

        Stream<Person> getStream();
    }


}