/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.data.repository.By;
import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.mapping.core.entities.Person;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RepositoryReflectionUtilsTest {

    @Test
    void shouldGetParams(){
        Method method = PersonRepository.class.getDeclaredMethods()[0];
        Map<String, Object> params = RepositoryReflectionUtils.INSTANCE.getParams(method, new Object[]{"Ada"});
        assertThat(params)
                .hasSize(1)
                .containsEntry("name", "Ada");

    }

    @Test
    void shouldQuery(){
        Method method = PersonRepository.class.getDeclaredMethods()[0];
        String query = RepositoryReflectionUtils.INSTANCE.getQuery(method);
        assertEquals("select * from Person where name = @name", query);
    }

    @Test
    void shouldBy(){
        Method method = PersonRepository.class.getDeclaredMethods()[0];
        Map<String, Object> params = RepositoryReflectionUtils.INSTANCE.getBy(method, new Object[]{"Ada"});
        assertThat(params)
                .hasSize(1)
                .containsEntry("name", "Ada");
    }

    interface PersonRepository extends BasicRepository<Person, String> {

        @Query("select * from Person where name = @name")
        List<Person> query(@Param("name") @By("name")  String name);
    }
}