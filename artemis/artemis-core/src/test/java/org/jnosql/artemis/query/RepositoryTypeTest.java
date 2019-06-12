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
package org.jnosql.artemis.query;

import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.RepositoryAsync;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import static org.jnosql.artemis.query.RepositoryType.DEFAULT;
import static org.jnosql.artemis.query.RepositoryType.DELETE_BY;
import static org.jnosql.artemis.query.RepositoryType.FIND_ALL;
import static org.jnosql.artemis.query.RepositoryType.FIND_BY;
import static org.jnosql.artemis.query.RepositoryType.JNOSQL_QUERY;
import static org.jnosql.artemis.query.RepositoryType.OBJECT_METHOD;
import static org.jnosql.artemis.query.RepositoryType.UNKNOWN;
import static org.jnosql.artemis.query.RepositoryType.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RepositoryTypeTest {


    @Test
    public void shouldReturnDefaultSync() throws NoSuchMethodException {
        assertEquals(DEFAULT, of(getMethod(Repository.class, "save")));
        assertEquals(DEFAULT, of(getMethod(Repository.class, "deleteById")));
        assertEquals(DEFAULT, of(getMethod(Repository.class, "findById")));
        assertEquals(DEFAULT, of(getMethod(Repository.class, "existsById")));
        assertEquals(DEFAULT, of(getMethod(Repository.class, "count")));
    }


    @Test
    public void shouldReturnDefaultASync() throws NoSuchMethodException {
        assertEquals(DEFAULT, of(getMethod(RepositoryAsync.class, "save")));
        assertEquals(DEFAULT, of(getMethod(RepositoryAsync.class, "deleteById")));
        assertEquals(DEFAULT, of(getMethod(RepositoryAsync.class, "findById")));
        assertEquals(DEFAULT, of(getMethod(RepositoryAsync.class, "existsById")));
        assertEquals(DEFAULT, of(getMethod(RepositoryAsync.class, "count")));
    }

    @Test
    public void shouldReturnObjectMethod() throws NoSuchMethodException {
        assertEquals(OBJECT_METHOD, of(getMethod(Object.class, "equals")));
        assertEquals(OBJECT_METHOD, of(getMethod(Object.class, "hashCode")));
    }

    @Test
    public void shouldReturnObjectMethodAsync() throws NoSuchMethodException {
        assertEquals(OBJECT_METHOD, of(getMethod(Object.class, "equals")));
        assertEquals(OBJECT_METHOD, of(getMethod(Object.class, "hashCode")));
    }


    @Test
    public void shouldReturnFindBy() throws NoSuchMethodException {
        assertEquals(FIND_BY, of(getMethod(SyncRepository.class, "findByName")));
    }

    @Test
    public void shouldReturnDeleteBy() throws NoSuchMethodException {
        assertEquals(DELETE_BY, of(getMethod(SyncRepository.class, "deleteByName")));
    }

    @Test
    public void shouldReturnFindAllBy() throws NoSuchMethodException {
        assertEquals(FIND_ALL, of(getMethod(SyncRepository.class, "findAll")));
    }

    @Test
    public void shouldReturnJNoSQLQuery() throws NoSuchMethodException {
        assertEquals(JNOSQL_QUERY, of(getMethod(SyncRepository.class, "query")));
    }

    @Test
    public void shouldReturnUnknown() throws NoSuchMethodException {
        assertEquals(UNKNOWN, of(getMethod(SyncRepository.class, "nope")));
    }

    @Test
    public void shouldReturnFindByAsync() throws NoSuchMethodException {
        assertEquals(FIND_BY, of(getMethod(AsyncSyncRepository.class, "findByName")));
    }

    @Test
    public void shouldReturnDeleteByAsync() throws NoSuchMethodException {
        assertEquals(DELETE_BY, of(getMethod(AsyncSyncRepository.class, "deleteByName")));
    }

    @Test
    public void shouldReturnFindAllByAsync() throws NoSuchMethodException {
        assertEquals(FIND_ALL, of(getMethod(AsyncSyncRepository.class, "findAll")));
    }

    @Test
    public void shouldReturnJNoSQLQueryAsync() throws NoSuchMethodException {
        assertEquals(JNOSQL_QUERY, of(getMethod(AsyncSyncRepository.class, "query")));
    }

    @Test
    public void shouldReturnUnknownAsync() throws NoSuchMethodException {
        assertEquals(UNKNOWN, of(getMethod(AsyncSyncRepository.class, "nope")));
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

    interface AsyncSyncRepository extends RepositoryAsync {

        String findByName(String name);

        String deleteByName(String name);

        List<String> findAll();

        @Query("query")
        String query(String query);

        void nope();
    }
}