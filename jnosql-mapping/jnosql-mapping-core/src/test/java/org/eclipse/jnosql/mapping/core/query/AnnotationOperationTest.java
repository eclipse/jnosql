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
package org.eclipse.jnosql.mapping.core.query;

import jakarta.data.repository.DataRepository;
import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.mapping.core.entities.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.List;

import static org.eclipse.jnosql.mapping.core.query.AnnotationOperation.INSERT;
import static org.eclipse.jnosql.mapping.core.query.AnnotationOperation.UPDATE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnnotationOperationTest {

    @Mock
    private AbstractRepository<Person, Long> repository;


    @Test
    void shouldReturnInvalidParameter() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("invalid", Person.class, Person.class);
        Person person = Person.builder().build();
        Assertions.assertThatThrownBy(() -> INSERT.invoke(new AnnotationOperation.Operation(method, new Object[]{person, person}, repository)))
                .isInstanceOf(UnsupportedOperationException.class);
        Assertions.assertThatThrownBy(() -> UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{person, person}, repository)))
                .isInstanceOf(UnsupportedOperationException.class);
    }
    @Test
    void shouldInsertSingleParameter() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("same", Person.class);
        Person person = Person.builder().build();
        Mockito.when(repository.insert(person)).thenReturn(person);
        Object invoked = INSERT.invoke(new AnnotationOperation.Operation(method, new Object[]{person}, repository));
        Mockito.verify(repository).insert(person);
        Assertions.assertThat(person).isEqualTo(invoked);
    }

    @Test
    void shouldInsertIterableParameter() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("iterable", List.class);
        Person person = Person.builder().build();
        Mockito.when(repository.insertAll(List.of(person))).thenReturn(List.of(person));
        Object invoked = INSERT.invoke(new AnnotationOperation.Operation(method, new Object[]{List.of(person)}, repository));
        Mockito.verify(repository).insertAll(List.of(person));
        Assertions.assertThat(List.of(person)).isEqualTo(invoked);
    }


    @Test
    void shouldInsertArrayParameter() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("array", Person[].class);
        Person person = Person.builder().build();
        Mockito.when(repository.insertAll(List.of(person))).thenReturn(List.of(person));
        Object invoked = INSERT.invoke(new AnnotationOperation.Operation(method, new Object[]{new Person[]{person}},
                repository));
        Mockito.verify(repository).insertAll(List.of(person));
        Assertions.assertThat(List.of(person)).isEqualTo(invoked);
    }


    @Test
    void shouldUpdateSingleParameter() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("same", Person.class);
        Person person = Person.builder().build();
        Mockito.when(repository.update(person)).thenReturn(true);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{person}, repository));
        Mockito.verify(repository).update(person);
        Assertions.assertThat(person).isEqualTo(invoked);
    }

    @Test
    void shouldUpdateSingleParameterBoolean() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("sameBoolean", Person.class);
        Person person = Person.builder().build();
        Mockito.when(repository.update(person)).thenReturn(true);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{person}, repository));
        Mockito.verify(repository).update(person);
        Assertions.assertThat(invoked).isEqualTo(true);
    }

    @Test
    void shouldUpdateSingleParameterVoid() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("sameVoid", Person.class);
        Person person = Person.builder().build();
        Mockito.when(repository.update(person)).thenReturn(true);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{person}, repository));
        Mockito.verify(repository).update(person);
        Assertions.assertThat(invoked).isEqualTo(Void.TYPE);
    }

    @Test
    void shouldUpdateSingleParameterInt() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("sameInt", Person.class);
        Person person = Person.builder().build();
        Mockito.when(repository.update(person)).thenReturn(true);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{person}, repository));
        Mockito.verify(repository).update(person);
        Assertions.assertThat(invoked).isEqualTo(1);
    }

    @Test
    void shouldUpdateIterableParameter() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("iterable", List.class);
        Person person = Person.builder().build();
        Mockito.when(repository.updateAll(List.of(person))).thenReturn(1);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{List.of(person)}, repository));
        Mockito.verify(repository).updateAll(List.of(person));
        Assertions.assertThat(List.of(person)).isEqualTo(invoked);
    }

    @Test
    void shouldUpdateIterableParameterVoid() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("iterableVoid", List.class);
        Person person = Person.builder().build();
        Mockito.when(repository.updateAll(List.of(person))).thenReturn(1);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{List.of(person)}, repository));
        Mockito.verify(repository).updateAll(List.of(person));
        Assertions.assertThat(invoked).isEqualTo(Void.TYPE);
    }

    @Test
    void shouldUpdateIterableParameterInt() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("iterableInt", List.class);
        Person person = Person.builder().build();
        Mockito.when(repository.updateAll(List.of(person))).thenReturn(1);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{List.of(person)}, repository));
        Mockito.verify(repository).updateAll(List.of(person));
        Assertions.assertThat(invoked).isEqualTo(1);
    }

    @Test
    void shouldUpdateIterableParameterBoolean() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("iterableBoolean", List.class);
        Person person = Person.builder().build();
        Mockito.when(repository.updateAll(List.of(person))).thenReturn(1);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{List.of(person)}, repository));
        Mockito.verify(repository).updateAll(List.of(person));
        Assertions.assertThat(invoked).isEqualTo(true);
    }

    @Test
    void shouldUpdateArrayParameter() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("array", Person[].class);
        Person person = Person.builder().build();
        Mockito.when(repository.updateAll(List.of(person))).thenReturn(1);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{new Person[]{person}},
                repository));
        Mockito.verify(repository).updateAll(List.of(person));
        Assertions.assertThat(new Person[]{person}).isEqualTo(invoked);
    }

    @Test
    void shouldUpdateArrayParameterBoolean() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("arrayBoolean", Person[].class);
        Person person = Person.builder().build();
        Mockito.when(repository.updateAll(List.of(person))).thenReturn(1);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{new Person[]{person}},
                repository));
        Mockito.verify(repository).updateAll(List.of(person));
        Assertions.assertThat(invoked).isEqualTo(true);
    }

    @Test
    void shouldUpdateArrayParameterInt() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("arrayInt", Person[].class);
        Person person = Person.builder().build();
        Mockito.when(repository.updateAll(List.of(person))).thenReturn(1);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{new Person[]{person}},
                repository));
        Mockito.verify(repository).updateAll(List.of(person));
        Assertions.assertThat(invoked).isEqualTo(1);
    }

    @Test
    void shouldUpdateArrayParameterVoid() throws Throwable {
        Method method = PersonRepository.class.getDeclaredMethod("arrayVoid", Person[].class);
        Person person = Person.builder().build();
        Mockito.when(repository.updateAll(List.of(person))).thenReturn(1);
        Object invoked = UPDATE.invoke(new AnnotationOperation.Operation(method, new Object[]{new Person[]{person}},
                repository));
        Mockito.verify(repository).updateAll(List.of(person));
        Assertions.assertThat(invoked).isEqualTo(Void.TYPE);
    }


    interface PersonRepository extends DataRepository<Person, Long>{

        void invalid(Person person, Person person2);
        Person same(Person person);

        boolean sameBoolean(Person person);

        void sameVoid(Person person);

        int sameInt(Person person);

        Person[] array(Person[] people);

        boolean arrayBoolean(Person[] people);

        void arrayVoid(Person[] people);

        int arrayInt(Person[] people);

        List<Person> iterable(List<Person> people);

        void iterableVoid(List<Person> people);

        boolean iterableBoolean(List<Person> people);

        int iterableInt(List<Person> people);
    }
}