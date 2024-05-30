/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.semistructured.query;

import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;
import org.eclipse.jnosql.mapping.semistructured.MockProducer;
import org.eclipse.jnosql.mapping.semistructured.SemiStructuredTemplate;
import org.eclipse.jnosql.mapping.semistructured.entities.Person;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;
import java.util.List;

@EnableAutoWeld
@AddPackages(value = {Converters.class, EntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class})
class CustomRepositoryHandlerTest {

    @Inject
    private EntitiesMetadata entitiesMetadata;

    private SemiStructuredTemplate template;

    @Inject
    private Converters converters;

    private People people;

    @BeforeEach
    void setUp() {
        template = Mockito.mock(SemiStructuredTemplate.class);
        CustomRepositoryHandler customRepositoryHandler = new CustomRepositoryHandler(entitiesMetadata, template, People.class, converters);
        people = (People) Proxy.newProxyInstance(People.class.getClassLoader(), new Class[]{People.class},
                customRepositoryHandler);
    }

    @Test
    void shouldInsertEntity() {
        Person person = Person.builder().withAge(26).withName("Ada").build();
        Mockito.when(template.insert(person)).thenReturn(person);
        Person result = people.insert(person);

        Mockito.verify(template).insert(person);
        Mockito.verifyNoMoreInteractions(template);
        Assertions.assertThat(result).isEqualTo(person);
    }

    @Test
    void shouldInsertListEntity() {
        var persons = List.of(Person.builder().withAge(26).withName("Ada").build());
        Mockito.when(template.insert(persons)).thenReturn(persons);
        List<Person> result = people.insert(persons);

        Mockito.verify(template).insert(persons);
        Mockito.verifyNoMoreInteractions(template);
        Assertions.assertThat(result).isEqualTo(persons);
    }

    @Test
    void shouldInsertArrayEntity() {
        Person ada = Person.builder().withAge(26).withName("Ada").build();
        var persons = new Person[]{ada};
        Mockito.when(template.insert(Mockito.any())).thenReturn(List.of(ada));
        Person[] result = people.insert(persons);

        Mockito.verify(template).insert(List.of(ada));
        Mockito.verifyNoMoreInteractions(template);
        Assertions.assertThat(result).isEqualTo(persons);
    }


    @Test
    void shouldUpdateEntity() {
        Person person = Person.builder().withAge(26).withName("Ada").build();
        Mockito.when(template.update(person)).thenReturn(person);
        Person result = people.update(person);

        Mockito.verify(template).update(person);
        Mockito.verifyNoMoreInteractions(template);
        Assertions.assertThat(result).isEqualTo(person);
    }

    @Test
    void shouldUpdateListEntity() {
        var persons = List.of(Person.builder().withAge(26).withName("Ada").build());
        Mockito.when(template.update(persons)).thenReturn(persons);
        List<Person> result = people.update(persons);

        Mockito.verify(template).update(persons);
        Mockito.verifyNoMoreInteractions(template);
        Assertions.assertThat(result).isEqualTo(persons);
    }

    @Test
    void shouldUpdateArrayEntity() {
        Person ada = Person.builder().withAge(26).withName("Ada").build();
        var persons = new Person[]{ada};
        Mockito.when(template.update(Mockito.any())).thenReturn(List.of(ada));
        Person[] result = people.update(persons);

        Mockito.verify(template).update(List.of(ada));
        Mockito.verifyNoMoreInteractions(template);
        Assertions.assertThat(result).isEqualTo(persons);
    }

    @Test
    void shouldDeleteEntity() {
        Person person = Person.builder().withId(1).withAge(26).withName("Ada").build();
        people.delete(person);

        Mockito.verify(template).delete(Person.class, 1L);
        Mockito.verifyNoMoreInteractions(template);
    }

    @Test
    void shouldDeleteListEntity() {
        var persons = List.of(Person.builder().withId(12L).withAge(26).withName("Ada").build());
         people.delete(persons);

        Mockito.verify(template).delete(Person.class, 12L);
        Mockito.verifyNoMoreInteractions(template);
    }

    @Test
    void shouldDeleteArrayEntity() {
        Person ada = Person.builder().withId(2L).withAge(26).withName("Ada").build();
        var persons = new Person[]{ada};
        people.delete(persons);

        Mockito.verify(template).delete(Person.class, 2L);
        Mockito.verifyNoMoreInteractions(template);
    }

}