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

import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.VetedConverter;
import org.eclipse.jnosql.mapping.metadata.ClassConverter;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.test.entities.Address;
import org.eclipse.jnosql.mapping.test.entities.AppointmentBook;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.Worker;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionClassConverterJavaFieldParserTest {

    private ClassConverter converter;

    @BeforeEach
    public void setUp() {
        this.converter = new ReflectionClassConverter();
    }

    @Test
    public void shouldReturnErrorWhenParameterIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            EntityMetadata entityMetadata = converter.apply(Person.class);
            entityMetadata.columnField(null);
        });
    }

    @Test
    public void shouldReturnTheNativeName() {
        EntityMetadata entityMetadata = converter.apply(Worker.class);
        String notFound = entityMetadata.columnField("salary");
        assertEquals("money", notFound);

    }

    @Test
    public void shouldReturnTheSameValueWhenTheFieldDoesNotExistInTheEntityMetadata() {
        EntityMetadata entityMetadata = converter.apply(Person.class);
        String notFound = entityMetadata.columnField("notFound");
        assertEquals("notFound", notFound);
    }

    @Test
    public void shouldReadFieldWhenFieldIsSubEntity() {
        EntityMetadata entityMetadata = converter.apply(Address.class);
        String result = entityMetadata.columnField("zipCode.plusFour");
        assertEquals("zipCode.plusFour", result);
    }

    @Test
    public void shouldReturnAllFieldWhenSelectTheSubEntityField() {
        EntityMetadata entityMetadata = converter.apply(Address.class);
        String result = entityMetadata.columnField("zipCode");
        List<String> resultList = Stream.of(result.split(",")).sorted().collect(toList());
        List<String> expected = Stream.of("zipCode.plusFour", "zipCode.zip").sorted().collect(toList());
        assertEquals(expected, resultList);
    }

    @Test
    public void shouldReadFieldWhenFieldIsEmbedded() {
        EntityMetadata entityMetadata = converter.apply(Worker.class);
        String result = entityMetadata.columnField("job.city");
        assertEquals("city", result);
    }

    @Test
    public void shouldReturnAllFieldWhenSelectTheEmbeddedField() {
        EntityMetadata entityMetadata = converter.apply(Worker.class);
        String result = entityMetadata.columnField("job");
        List<String> resultList = Stream.of(result.split(",")).sorted().collect(toList());
        List<String> expected = Stream.of("description", "city").sorted().collect(toList());
        assertEquals(expected, resultList);
    }


    @Test
    public void shouldReturnEmbeddedFieldInCollection() {
        EntityMetadata entityMetadata = converter.apply(AppointmentBook.class);
        String result = entityMetadata.columnField("contacts.name");
        assertEquals("contacts.contact_name", result);
    }

}
