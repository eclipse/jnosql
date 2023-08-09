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
package org.eclipse.jnosql.mapping.column;

import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.column.Column;
import org.eclipse.jnosql.communication.column.ColumnEntity;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.column.entities.Animal;
import org.eclipse.jnosql.mapping.column.entities.Book;
import org.eclipse.jnosql.mapping.column.entities.BookRelease;
import org.eclipse.jnosql.mapping.column.entities.Money;
import org.eclipse.jnosql.mapping.column.entities.constructor.BookUser;
import org.eclipse.jnosql.mapping.column.entities.constructor.Computer;
import org.eclipse.jnosql.mapping.column.entities.constructor.PetOwner;
import org.eclipse.jnosql.mapping.column.spi.ColumnExtension;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableAutoWeld
@AddPackages(value = {Converters.class, ColumnEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, ColumnExtension.class})
class ColumnEntityConverterConstructorTest {

    @Inject
    private ColumnEntityConverter converter;

    @Test
    public void shouldConverterEntityComputer() {
        ColumnEntity communication = ColumnEntity.of("Computer");
        communication.add("_id", 10L);
        communication.add("name", "Dell");
        communication.add("age", 2020);
        communication.add("model", "Dell 2020");
        communication.add("price", "USD 20");
        Computer computer = this.converter.toEntity(communication);
        assertNotNull(computer);
        assertEquals(10L, computer.getId());
        assertEquals("Dell", computer.getName());
        assertEquals(2020, computer.getAge());
        assertEquals("Dell 2020", computer.getModel());
        assertEquals(Money.parse("USD 20"), computer.getPrice());
    }

    @Test
    public void shouldConvertComputerToCommunication() {
        Computer computer = new Computer(10L, "Dell", 2020, "Dell 2020",
                Money.parse("USD 20"));
        ColumnEntity communication = this.converter.toColumn(computer);
        assertNotNull(communication);

        assertEquals(computer.getId(), communication.find("_id", Long.class).get());
        assertEquals(computer.getName(), communication.find("name", String.class).get());
        assertEquals(computer.getAge(), communication.find("age", int.class).get());
        assertEquals(computer.getModel(), communication.find("model", String.class).get());
        assertEquals(computer.getPrice().toString(), communication.find("price", String.class).get());
    }

    @Test
    public void shouldConvertPetOwner() {
        ColumnEntity communication = ColumnEntity.of("PetOwner");
        communication.add("_id", 10L);
        communication.add("name", "Otavio");
        communication.add("animal", Arrays.asList(Column.of("_id", 23)
                , Column.of("name", "Ada")));

        PetOwner petOwner = this.converter.toEntity(communication);
        assertNotNull(petOwner);
        assertEquals(10L, petOwner.getId());
        assertEquals("Otavio", petOwner.getName());
        Animal animal = petOwner.getAnimal();
        assertEquals(23L, animal.getId());
        assertEquals("Ada", animal.getName());
    }

    @Test
    public void shouldConvertPetOwnerCommunication() {
        Animal ada = new Animal("Ada");
        PetOwner petOwner = new PetOwner(10L, "Poliana", ada);
        ColumnEntity communication = this.converter.toColumn(petOwner);
        assertNotNull(communication);
        assertEquals(10L, communication.find("_id", Long.class).get());
        assertEquals("Poliana", communication.find("name", String.class).get());
        List<Column> columns = communication.find("animal", new TypeReference<List<Column>>() {})
                .get();
        assertThat(columns).contains(Column.of("name", "Ada"));
    }

    @Test
    public void shouldConvertBookUser() {
        ColumnEntity communication = ColumnEntity.of("BookUser");
        communication.add("_id", "otaviojava");
        communication.add("native_name", "Otavio Santana");
        List<List<Column>> columns = new ArrayList<>();
        columns.add(Arrays.asList(Column.of("_id", 10), Column.of("name", "Effective Java")));
        columns.add(Arrays.asList(Column.of("_id", 12), Column.of("name", "Clean Code")));
        communication.add("books", columns);

        BookUser bookUser = this.converter.toEntity(communication);
        assertNotNull(bookUser);
        assertEquals("Otavio Santana", bookUser.getName());
        assertEquals("otaviojava", bookUser.getNickname());
        assertEquals(2, bookUser.getBooks().size());
        List<String> names = bookUser.getBooks().stream().map(Book::getName).toList();
        assertThat(names).contains("Effective Java", "Clean Code");

    }

    @Test
    public void shouldConverterFieldsOnEntityComputer() {
        ColumnEntity communication = ColumnEntity.of("Computer");
        communication.add("_id", "10");
        communication.add("name", "Dell");
        communication.add("age", "2020");
        communication.add("model", "Dell 2020");
        communication.add("price", "USD 20");
        Computer computer = this.converter.toEntity(communication);
        assertNotNull(computer);
        assertEquals(10L, computer.getId());
        assertEquals("Dell", computer.getName());
        assertEquals(2020, computer.getAge());
        assertEquals("Dell 2020", computer.getModel());
        assertEquals(Money.parse("USD 20"), computer.getPrice());
    }

    @Test
    public void shouldConverterEntityBookRelease() {
        ColumnEntity communication = ColumnEntity.of("BookRelease");
        communication.add("isbn", "9780132345286");
        communication.add("title", "Effective Java");
        communication.add("author", "Joshua Bloch");
        communication.add("year", Year.of(2001));
        BookRelease book = this.converter.toEntity(communication);
        assertNotNull(book);
        assertEquals("9780132345286", book.getIsbn());
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals(Year.of(2001), book.getYear());
    }

    @Test
    public void shouldConverterEntityBookReleaseOnStringYear() {
        ColumnEntity communication = ColumnEntity.of("BookRelease");
        communication.add("isbn", "9780132345286");
        communication.add("title", "Effective Java");
        communication.add("author", "Joshua Bloch");
        communication.add("year", "2001");
        BookRelease book = this.converter.toEntity(communication);
        assertNotNull(book);
        assertEquals("9780132345286", book.getIsbn());
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals(Year.of(2001), book.getYear());
    }
}
