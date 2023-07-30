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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.Column;
import org.eclipse.jnosql.mapping.Embeddable;
import org.eclipse.jnosql.mapping.metadata.MappingType;
import org.eclipse.jnosql.mapping.test.entities.Actor;
import org.eclipse.jnosql.mapping.test.entities.Address;
import org.eclipse.jnosql.mapping.test.entities.Movie;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.Worker;
import org.eclipse.jnosql.mapping.test.entities.constructor.BookUser;
import org.eclipse.jnosql.mapping.test.entities.constructor.PetOwner;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MappingTypeTest {

    @Test
    public void shouldReturnList() throws NoSuchFieldException {
        Field field = Person.class.getDeclaredField("phones");
        assertEquals(MappingType.COLLECTION, MappingType.of(field));
    }

    @Test
    public void shouldReturnSet() throws NoSuchFieldException {
        Field field = Movie.class.getDeclaredField("actors");
        assertEquals(MappingType.COLLECTION, MappingType.of(field));
    }

    @Test
    public void shouldReturnMap() throws NoSuchFieldException {
        Field field = Actor.class.getDeclaredField("movieCharacter");
        assertEquals(MappingType.MAP, MappingType.of(field));
    }

    @Test
    public void shouldReturnDefault() throws NoSuchFieldException {
        Field field = Person.class.getDeclaredField("name");
        assertEquals(MappingType.DEFAULT, MappingType.of(field));
    }


    @Test
    public void shouldReturnEmbedded() throws NoSuchFieldException {
        Field field = Worker.class.getDeclaredField("job");
        assertEquals(MappingType.EMBEDDED, MappingType.of(field));
    }

    @Test
    public void shouldReturnEntity() throws NoSuchFieldException {
        Field field = Address.class.getDeclaredField("zipCode");
        assertEquals(MappingType.ENTITY, MappingType.of(field));
    }

    @Test
    public void shouldReturnParameterDefault() throws NoSuchMethodException {
        Constructor<BookUser> constructor = (Constructor<BookUser>) BookUser.class.getDeclaredConstructors()[0];
        Parameter id = constructor.getParameters()[0];
        Parameter name = constructor.getParameters()[1];
        assertEquals(MappingType.DEFAULT, MappingType.of(id));
        assertEquals(MappingType.DEFAULT, MappingType.of(name));
    }

    @Test
    public void shouldReturnParameterCollection() {
        Constructor<BookUser> constructor = (Constructor<BookUser>) BookUser.class.getDeclaredConstructors()[0];
        Parameter books = constructor.getParameters()[2];
        assertEquals(MappingType.COLLECTION, MappingType.of(books));
    }

    @Test
    public void shouldReturnParameterEntity() {
        Constructor<PetOwner> constructor = (Constructor<PetOwner>) PetOwner.class.getDeclaredConstructors()[0];
        Parameter animal = constructor.getParameters()[2];
        assertEquals(MappingType.ENTITY, MappingType.of(animal));
    }

    @Test
    public void shouldReturnParameterMap() {
        Constructor<ForClass> constructor = (Constructor<ForClass>) ForClass.class.getDeclaredConstructors()[0];
        Parameter map = constructor.getParameters()[0];
        assertEquals(MappingType.MAP, MappingType.of(map));
    }

    @Test
    public void shouldReturnParameterEmbedded() {
        Constructor<ForClass> constructor = (Constructor<ForClass>) ForClass.class.getDeclaredConstructors()[0];
        Parameter map = constructor.getParameters()[1];
        assertEquals(MappingType.EMBEDDED, MappingType.of(map));
    }


    public static class ForClass {

        @Column("mapAnnotation")
        private Map<String, String> map;


        @Column
        private BarClass barClass;

        public ForClass(@Column("map") Map<String, String> map, @Column("barClass") BarClass barClass) {
            this.map = map;
            this.barClass = barClass;
        }
    }

    @Embeddable
    public static class BarClass {

        @Column("integerAnnotation")
        private Integer integer;
    }
}