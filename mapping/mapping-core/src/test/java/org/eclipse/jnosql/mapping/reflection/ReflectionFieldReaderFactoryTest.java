/*
 *  Copyright (c) 2018 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

@CDIExtension
class ReflectionFieldReaderFactoryTest {

    @Inject
    private ReflectionFieldReaderFactory readerFactory;


    @Test
    public void shouldRead() {
        Person person = Person.builder().withId(10L).withAge(10).withName("Ada").withPhones(singletonList("234234324")).build();

        Field[] fields = Person.class.getDeclaredFields();
        Stream.of(fields).forEach(f -> f.setAccessible(true));
        Field id = Stream.of(fields).filter(f -> f.getName().equals("id")).findFirst().get();
        Field name = Stream.of(fields).filter(f -> f.getName().equals("name")).findFirst().get();
        Field age = Stream.of(fields).filter(f -> f.getName().equals("age")).findFirst().get();
        Field phones = Stream.of(fields).filter(f -> f.getName().equals("phones")).findFirst().get();

        Assertions.assertEquals(10L, readerFactory.apply(id).read(person));
        Assertions.assertEquals("Ada", readerFactory.apply(name).read(person));
        Assertions.assertEquals(10, readerFactory.apply(age).read(person));
        Assertions.assertEquals(singletonList("234234324"), readerFactory.apply(phones).read(person));
    }

    @Test
    public void shouldReturnFieldReader() {
        Person person = Person.builder().withId(10L).withAge(10).withName("Ada").withPhones(singletonList("234234324")).build();
        Field[] fields = Person.class.getDeclaredFields();
        Field id = Stream.of(fields).filter(f -> f.getName().equals("id")).findFirst().get();
        FieldReader reader = readerFactory.apply(id);
        Assertions.assertNotNull(reader);

    }
}