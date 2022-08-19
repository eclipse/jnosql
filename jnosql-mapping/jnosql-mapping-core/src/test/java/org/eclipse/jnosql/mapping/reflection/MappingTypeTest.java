/*
 *  Copyright (c) 2022 Otávio Santana and others
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

import jakarta.nosql.tck.entities.Actor;
import jakarta.nosql.tck.entities.Address;
import jakarta.nosql.tck.entities.Movie;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Worker;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

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
    public void shouldReturnEmbedded() throws NoSuchFieldException{
        Field field = Worker.class.getDeclaredField("job");
        assertEquals(MappingType.EMBEDDED, MappingType.of(field));
    }

    @Test
    public void shouldReturnEntity() throws NoSuchFieldException{
        Field field = Address.class.getDeclaredField("zipCode");
        assertEquals(MappingType.ENTITY, MappingType.of(field));
    }

    //parameter default
    //parameter collection
    //parameter  map
    //parameter entity
    //parameter EMBEDDED

    @Test
    public void shouldReturnDefaultParameter(){

    }
    public void shouldReturnDefaultCollection(){}
    public void shouldReturnDefaultMap(){}
    public void shouldReturnDefaultEntity(){}
    public void shouldReturnDefaultEmbedded(){}

}