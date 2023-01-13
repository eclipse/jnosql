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

import org.eclipse.jnosql.mapping.test.entities.Address;
import org.eclipse.jnosql.mapping.test.entities.AppointmentBook;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.Worker;
import org.eclipse.jnosql.mapping.test.jupiter.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CDIExtension
public class ClassConverterJavaFieldParserTest {

    @Inject
    private ClassConverter classConverter;

    @Test
    public void shouldReturnErrorWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            EntityMetadata entityMetadata = classConverter.create(Person.class);
            entityMetadata.getColumnField(null);
        });
    }

    @Test
    public void shouldReturnTheNativeName() {
        EntityMetadata entityMetadata = classConverter.create(Worker.class);
        String notFound = entityMetadata.getColumnField("salary");
        assertEquals("money", notFound);

    }

    @Test
    public void shouldReturnTheSameValueWhenTheFieldDoesNotExistInTheEntityMetadata() {
        EntityMetadata entityMetadata = classConverter.create(Person.class);
        String notFound = entityMetadata.getColumnField("notFound");
        assertEquals("notFound", notFound);
    }

    @Test
    public void shouldReadFieldWhenFieldIsSubEntity() {
        EntityMetadata entityMetadata = classConverter.create(Address.class);
        String result = entityMetadata.getColumnField("zipCode.plusFour");
        assertEquals("zipCode.plusFour", result);
    }

    @Test
    public void shouldReturnAllFieldWhenSelectTheSubEntityField() {
        EntityMetadata entityMetadata = classConverter.create(Address.class);
        String result = entityMetadata.getColumnField("zipCode");
        List<String> resultList = Stream.of(result.split(",")).sorted().collect(toList());
        List<String> expected = Stream.of("zipCode.plusFour", "zipCode.zip").sorted().collect(toList());
        assertEquals(expected, resultList);
    }

    @Test
    public void shouldReadFieldWhenFieldIsEmbedded() {
        EntityMetadata entityMetadata = classConverter.create(Worker.class);
        String result = entityMetadata.getColumnField("job.city");
        assertEquals("city", result);
    }

    @Test
    public void shouldReturnAllFieldWhenSelectTheEmbeddedField() {
        EntityMetadata entityMetadata = classConverter.create(Worker.class);
        String result = entityMetadata.getColumnField("job");
        List<String> resultList = Stream.of(result.split(",")).sorted().collect(toList());
        List<String> expected = Stream.of("description", "city").sorted().collect(toList());
        assertEquals(expected, resultList);
    }


    @Test
    public void shouldReturnEmbeddedFieldInCollection() {
        EntityMetadata entityMetadata = classConverter.create(AppointmentBook.class);
        String result = entityMetadata.getColumnField("contacts.name");
        assertEquals("contacts.contact_name", result);
    }

}
