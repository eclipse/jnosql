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

import org.jnosql.artemis.PreparedStatementAsync;
import org.jnosql.artemis.Query;
import org.jnosql.artemis.RepositoryAsync;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DynamicAsyncQueryMethodReturnTest {




    @Test
    public void shouldRunWithoutCallback() throws NoSuchMethodException {
        Method method = getMethod(PersonRepository.class, "run");
        PreparedStatementAsync preparedStatementAsync = Mockito.mock(PreparedStatementAsync.class);
        AtomicReference<Object> reference = new AtomicReference<>();
        Consumer<List<Object>> listConsumer = reference::set;
        BiConsumer<String, Consumer<List<Object>>> biconsumer = (q,l) -> reference.set(l);

        Function<String, PreparedStatementAsync> converter = s -> preparedStatementAsync;

        DynamicAsyncQueryMethodReturn<Object> nativeQuery = DynamicAsyncQueryMethodReturn.builder()
                .withArgs(new Object[]{})
                .withMethod(method)
                .withAsyncConsumer(biconsumer)
                .withPrepareConverter(converter)
                .build();

        nativeQuery.execute();
        Object result = reference.get();
        assertNotNull(reference);
    }

    @Test
    public void shouldRunCallback() throws NoSuchMethodException {
        Method method = getMethod(PersonRepository.class, "runCallBack");
        PreparedStatementAsync preparedStatementAsync = Mockito.mock(PreparedStatementAsync.class);
        AtomicReference<Object> reference = new AtomicReference<>();
        Consumer<List<Object>> listConsumer = reference::set;
        BiConsumer<String, Consumer<List<Object>>> biconsumer = (q,l) -> reference.set(l);

        Function<String, PreparedStatementAsync> converter = s -> preparedStatementAsync;

        DynamicAsyncQueryMethodReturn<Object> nativeQuery = DynamicAsyncQueryMethodReturn.builder()
                .withArgs(new Object[]{})
                .withMethod(method)
                .withAsyncConsumer(biconsumer)
                .withPrepareConverter(converter)
                .build();

        nativeQuery.execute();
        Object result = reference.get();
        assertNotNull(reference);
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

    private interface PersonRepository extends RepositoryAsync<Person, String> {

        @Query("query")
        List<Person> run();

        @Query("query")
        List<Person> runCallBack(Consumer<Person> callback);


    }

}