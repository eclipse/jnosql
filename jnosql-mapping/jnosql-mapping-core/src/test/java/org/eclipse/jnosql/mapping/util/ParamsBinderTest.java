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
package org.eclipse.jnosql.mapping.util;

import jakarta.nosql.Params;
import jakarta.nosql.Value;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.DynamicQueryException;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@CDIExtension
class ParamsBinderTest {

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private EntityMetadata metadata;

    private ParamsBinder binder;

    @BeforeEach
    public void setUp() {
        this.metadata = entities.get(Person.class);
        this.binder = new ParamsBinder(metadata, converters);
    }

    @Test
    public void shouldReturnNPEWhenThereIsNullParameter() {
        Params params = Params.newParams();
        Assertions.assertThrows(NullPointerException.class, () ->
                binder.bind(params, null, null)
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                binder.bind(null, null, null)
        );
    }

    @Test
    public void shouldReturnErrorWhenParamsIsBiggerThanArgs() {
        Method method = PersonRepository.class.getDeclaredMethods()[0];
        Params params = Params.newParams();
        params.add("name");
        Assertions.assertThrows(DynamicQueryException.class, () ->
                binder.bind(params, new Object[0], method)
        );
    }

    @Test
    public void shouldBindParameterFindById() {
        Method method = PersonRepository.class.getDeclaredMethods()[0];
        Params params = Params.newParams();
        Value value = params.add("name");
        binder.bind(params, new Object[]{"otavio"}, method);

        Object param = value.get();
        Assertions.assertNotNull(param);
        Assertions.assertEquals("otavio", param);
    }

    @Test
    public void shouldBindParameterFindByUnderLineParameter() {
        Method method = PersonRepository.class.getDeclaredMethods()[0];
        Params params = Params.newParams();
        Value value = params.add("name_1212");
        binder.bind(params, new Object[]{"otavio"}, method);

        Object param = value.get();
        Assertions.assertNotNull(param);
        Assertions.assertEquals("otavio", param);
    }

    @Test
    public void shouldBindParameterInSingleParameter() {
        Method method = PersonRepository.class.getDeclaredMethods()[1];
        Params params = Params.newParams();
        Value value = params.add("name_1212");
        binder.bind(params, new Object[]{"otavio"}, method);

        Object param = value.get();
        Assertions.assertNotNull(param);
        Assertions.assertEquals("otavio", param);
    }

    @Test
    public void shouldBindParameterInIterableParameter() {
        Method method = PersonRepository.class.getDeclaredMethods()[1];
        Params params = Params.newParams();
        Value value = params.add("name_1212");
        binder.bind(params, new Object[]{Arrays.asList("otavio", "poliana")}, method);

        Object param = value.get();
        Assertions.assertNotNull(param);
        Assertions.assertTrue(param instanceof Iterable);
        Assertions.assertEquals(Arrays.asList("otavio", "poliana"), param);
    }

    @Test
    public void shouldConvertParamBinder() {
        Method method = PersonRepository.class.getDeclaredMethods()[2];
        Params params = Params.newParams();
        Value value = params.add("age_1212");
        binder.bind(params, new Object[]{1L}, method);

        Object param = value.get();
        Assertions.assertNotNull(param);
        Assertions.assertEquals(1, param);
    }

    @Test
    public void shouldConvertIterable() {
        Method method = PersonRepository.class.getDeclaredMethods()[1];
        Params params = Params.newParams();
        Value value = params.add("age");
        binder.bind(params, new Object[]{Arrays.asList(1L, 2L)}, method);

        Object param = value.get();
        Assertions.assertNotNull(param);
        Assertions.assertTrue(param instanceof Iterable);
        Assertions.assertEquals(Arrays.asList(1, 2), param);
    }


    interface PersonRepository extends Repository<Person, Long> {

        Optional<Person> findByName(String name);

        Optional<Person> findByNameIn(String name);

        Optional<Person> findByNameIn(List<String> names);

        List<Person> findByAgeIn(Long age);

        List<Person> findByAgeIn(Iterable<Long> age);
    }

}