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
package org.eclipse.jnosql.mapping.reactive;

import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import org.eclipse.jnosql.mapping.query.RepositoryType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.lang.reflect.Method;
import java.util.stream.Stream;

class ReactiveRepositoryTypeTest {

    @Test
    public void shouldReturnFindAll() {
        final Method method = Stream.of(MockReactiveRepository.class.getDeclaredMethods())
                .filter(m -> "findAll".equals(m.getName())).findFirst().get();
        final RepositoryType type = ReactiveRepositoryType.of(method);
        Assertions.assertEquals(RepositoryType.FIND_ALL, type);
    }

    @Test
    public void shouldReturnFindById() {
        final Method method = Stream.of(MockReactiveRepository.class.getDeclaredMethods())
                .filter(m -> "findById".equals(m.getName())).findFirst().get();
        final RepositoryType type = ReactiveRepositoryType.of(method);
        Assertions.assertEquals(RepositoryType.FIND_BY, type);
    }

    @Test
    public void shouldReturnDeleteById() {
        final Method method = Stream.of(MockReactiveRepository.class.getDeclaredMethods())
                .filter(m -> "deleteById".equals(m.getName())).findFirst().get();
        final RepositoryType type = ReactiveRepositoryType.of(method);
        Assertions.assertEquals(RepositoryType.DELETE_BY, type);
    }


    @Test
    public void shouldReturnJNoSQLQuery() {
        final Method method = Stream.of(MockReactiveRepository.class.getDeclaredMethods())
                .filter(m -> "query".equals(m.getName())).findFirst().get();
        final RepositoryType type = ReactiveRepositoryType.of(method);
        Assertions.assertEquals(RepositoryType.JNOSQL_QUERY, type);
    }


    @Test
    public void shouldReturnUnknown() {
        final Method method = Stream.of(Repository.class.getDeclaredMethods())
                .filter(m -> "save".equals(m.getName())).findFirst().get();
        final RepositoryType type = ReactiveRepositoryType.of(method);
        Assertions.assertEquals(RepositoryType.UNKNOWN, type);
    }



    interface MockReactiveRepository extends ReactiveRepository<Object, Object> {
        Publisher<Object> findAll();
        Observable<Object> findById(Object id);
        @Query("annotation")
        Publisher<Object> query(Object id);
        Observable<Void> deleteById(Object id);
    }
    interface MockRepository extends Repository<Object, Object> {}
}