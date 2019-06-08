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
package org.jnosql.artemis.reflection;

import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.model.Address;
import org.jnosql.artemis.model.AppointmentBook;
import org.jnosql.artemis.model.Person;
import org.jnosql.artemis.model.Worker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(CDIExtension.class)
public class ClassConverterJavaFieldParserTest {

    @Inject
    private ClassConverter classConverter;

    @Test
    public void shouldReturnErrorWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            ClassMapping classMapping = classConverter.create(Person.class);
            classMapping.getColumnField(null);
        });
    }

    @Test
    public void shouldReturnTheNativeName() {
        ClassMapping classMapping = classConverter.create(Worker.class);
        String notFound = classMapping.getColumnField("salary");
        assertEquals("money", notFound);

    }

    @Test
    public void shouldReturnTheSameValueWhenTheFieldDoesNotExistInTheClassMapping() {
        ClassMapping classMapping = classConverter.create(Person.class);
        String notFound = classMapping.getColumnField("notFound");
        assertEquals("notFound", notFound);
    }

    @Test
    public void shouldReadFieldWhenFieldIsSubEntity() {
        ClassMapping classMapping = classConverter.create(Address.class);
        String result = classMapping.getColumnField("zipcode.plusFour");
        assertEquals("zipcode.plusFour", result);
    }

    @Test
    public void shouldReturnAllFieldWhenSelectTheSubEntityField() {
        ClassMapping classMapping = classConverter.create(Address.class);
        String result = classMapping.getColumnField("zipcode");
        List<String> resultList = Stream.of(result.split(",")).sorted().collect(toList());
        List<String> expected = Stream.of("zipcode.plusFour", "zipcode.zip").sorted().collect(toList());
        assertEquals(expected, resultList);
    }

    @Test
    public void shouldReadFieldWhenFieldIsEmbedded() {
        ClassMapping classMapping = classConverter.create(Worker.class);
        String result = classMapping.getColumnField("job.city");
        assertEquals("city", result);
    }

    @Test
    public void shouldReturnAllFieldWhenSelectTheEmbeddedField() {
        ClassMapping classMapping = classConverter.create(Worker.class);
        String result = classMapping.getColumnField("job");
        List<String> resultList = Stream.of(result.split(",")).sorted().collect(toList());
        List<String> expected = Stream.of("description", "city").sorted().collect(toList());
        assertEquals(expected, resultList);
    }


    @Test
    public void shouldReturnEmbeddedFieldInCollection() {
        ClassMapping classMapping = classConverter.create(AppointmentBook.class);
        String result = classMapping.getColumnField("contacts.name");
        assertEquals("contacts.contact_name", result);
    }

}
