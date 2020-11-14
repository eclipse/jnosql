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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.tck.entities.Actor;
import jakarta.nosql.tck.entities.Address;
import jakarta.nosql.tck.entities.Movie;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Worker;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FieldTypeUtilTest {

    @Test
    public void shouldReturnList() throws NoSuchFieldException {
        Field field = Person.class.getDeclaredField("phones");
        assertEquals(FieldType.COLLECTION, FieldTypeUtil.of(field));
    }

    @Test
    public void shouldReturnSet() throws NoSuchFieldException {
        Field field = Movie.class.getDeclaredField("actors");
        assertEquals(FieldType.COLLECTION, FieldTypeUtil.of(field));
    }

    @Test
    public void shouldReturnMap() throws NoSuchFieldException {
        Field field = Actor.class.getDeclaredField("movieCharacter");
        assertEquals(FieldType.MAP, FieldTypeUtil.of(field));
    }

    @Test
    public void shouldReturnDefault() throws NoSuchFieldException {
        Field field = Person.class.getDeclaredField("name");
        assertEquals(FieldType.DEFAULT, FieldTypeUtil.of(field));
    }


    @Test
    public void shouldReturnEmbedded() throws NoSuchFieldException{
        Field field = Worker.class.getDeclaredField("job");
        assertEquals(FieldType.EMBEDDED, FieldTypeUtil.of(field));
    }

    @Test
    public void shouldReturnSubEntity() throws NoSuchFieldException{
        Field field = Address.class.getDeclaredField("zipCode");
        assertEquals(FieldType.SUB_ENTITY, FieldTypeUtil.of(field));
    }

}