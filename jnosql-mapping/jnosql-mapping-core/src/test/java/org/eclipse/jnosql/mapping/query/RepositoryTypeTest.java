/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.query;

import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

class RepositoryTypeTest {


    @Test
    public void shouldReturnDefaultSync() throws NoSuchMethodException {
        Assertions.assertEquals(RepositoryType.DEFAULT, RepositoryType.of(getMethod(Repository.class, "save")));
        Assertions.assertEquals(RepositoryType.DEFAULT, RepositoryType.of(getMethod(Repository.class, "deleteById")));
        Assertions.assertEquals(RepositoryType.DEFAULT, RepositoryType.of(getMethod(Repository.class, "findById")));
        Assertions.assertEquals(RepositoryType.DEFAULT, RepositoryType.of(getMethod(Repository.class, "existsById")));
        Assertions.assertEquals(RepositoryType.DEFAULT, RepositoryType.of(getMethod(Repository.class, "count")));
    }


    @Test
    public void shouldReturnObjectMethod() throws NoSuchMethodException {
        Assertions.assertEquals(RepositoryType.OBJECT_METHOD, RepositoryType.of(getMethod(Object.class, "equals")));
        Assertions.assertEquals(RepositoryType.OBJECT_METHOD, RepositoryType.of(getMethod(Object.class, "hashCode")));
    }


    @Test
    public void shouldReturnFindBy() throws NoSuchMethodException {
        Assertions.assertEquals(RepositoryType.FIND_BY, RepositoryType.of(getMethod(SyncRepository.class, "findByName")));
    }

    @Test
    public void shouldReturnDeleteBy() throws NoSuchMethodException {
        Assertions.assertEquals(RepositoryType.DELETE_BY, RepositoryType.of(getMethod(SyncRepository.class, "deleteByName")));
    }

    @Test
    public void shouldReturnFindAllBy() throws NoSuchMethodException {
        Assertions.assertEquals(RepositoryType.FIND_ALL, RepositoryType.of(getMethod(SyncRepository.class, "findAll")));
    }

    @Test
    public void shouldReturnJNoSQLQuery() throws NoSuchMethodException {
        Assertions.assertEquals(RepositoryType.JNOSQL_QUERY, RepositoryType.of(getMethod(SyncRepository.class, "query")));
    }

    @Test
    public void shouldReturnUnknown() throws NoSuchMethodException {
        Assertions.assertEquals(RepositoryType.UNKNOWN, RepositoryType.of(getMethod(SyncRepository.class, "nope")));
    }

    private Method getMethod(Class<?> repository, String methodName) throws NoSuchMethodException {
        return Stream.of(repository.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .findFirst().get();

    }

    interface SyncRepository extends Repository {

        String findByName(String name);

        String deleteByName(String name);

        List<String> findAll();

        @Query("query")
        String query(String query);

        void nope();
    }

}