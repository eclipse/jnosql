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
package org.eclipse.jnosql.artemis.column;

import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnEntity;
import org.eclipse.jnosql.artemis.model.Actor;
import org.eclipse.jnosql.artemis.model.Address;
import org.eclipse.jnosql.artemis.model.AppointmentBook;
import org.eclipse.jnosql.artemis.model.Contact;
import org.eclipse.jnosql.artemis.model.ContactType;
import org.eclipse.jnosql.artemis.model.Director;
import org.eclipse.jnosql.artemis.model.Download;
import org.eclipse.jnosql.artemis.model.Job;
import org.eclipse.jnosql.artemis.model.Money;
import org.eclipse.jnosql.artemis.model.Movie;
import org.eclipse.jnosql.artemis.model.Person;
import org.eclipse.jnosql.artemis.model.Vendor;
import org.eclipse.jnosql.artemis.model.Worker;
import org.eclipse.jnosql.artemis.model.ZipCode;
import org.eclipse.jnosql.artemis.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CDIExtension
public class DefaultColumnEntityConverterTest {


    @Inject
    private DefaultColumnEntityConverter converter;

    private Column[] columns;

    private Actor actor = Actor.actorBuilder().withAge()
            .withId()
            .withName()
            .withPhones(asList("234", "2342"))
            .withMovieCharacter(Collections.singletonMap("JavaZone", "Jedi"))
            .withMovieRating(Collections.singletonMap("JavaZone", 10))
            .build();

    @BeforeEach
    public void init() {

        columns = new Column[]{Column.of("_id", 12L),
                Column.of("age", 10), Column.of("name", "Otavio"),
                Column.of("phones", asList("234", "2342"))
                , Column.of("movieCharacter", Collections.singletonMap("JavaZone", "Jedi"))
                , Column.of("movieRating", Collections.singletonMap("JavaZone", 10))};
    }

    @Test
    public void shouldConvertEntityFromColumnEntity() {

        Person person = Person.builder().withAge()
                .withId(12)
                .withName("Otavio")
                .withPhones(asList("234", "2342")).build();

        ColumnEntity entity = converter.toColumn(person);
        assertEquals("Person", entity.getName());
        assertEquals(4, entity.size());
        assertThat(entity.getColumns(), containsInAnyOrder(Column.of("_id", 12L),
                Column.of("age", 10), Column.of("name", "Otavio"), Column.of("phones", Arrays.asList("234", "2342"))));

    }

    @Test
    public void shouldConvertColumnEntityFromEntity() {

        ColumnEntity entity = converter.toColumn(actor);
        assertEquals("Actor", entity.getName());
        assertEquals(6, entity.size());

        assertThat(entity.getColumns(), containsInAnyOrder(columns));
    }

    @Test
    public void shouldConvertColumnEntityToEntity() {
        ColumnEntity entity = ColumnEntity.of("Actor");
        Stream.of(columns).forEach(entity::add);

        Actor actor = converter.toEntity(Actor.class, entity);
        assertNotNull(actor);
        assertEquals(10, actor.getAge());
        assertEquals(12L, actor.getId());
        assertEquals(asList("234", "2342"), actor.getPhones());
        assertEquals(Collections.singletonMap("JavaZone", "Jedi"), actor.getMovieCharacter());
        assertEquals(Collections.singletonMap("JavaZone", 10), actor.getMovieRating());
    }

    @Test
    public void shouldConvertColumnEntityToEntity2() {
        ColumnEntity entity = ColumnEntity.of("Actor");
        Stream.of(columns).forEach(entity::add);

        Actor actor = converter.toEntity(entity);
        assertNotNull(actor);
        assertEquals(10, actor.getAge());
        assertEquals(12L, actor.getId());
        assertEquals(asList("234", "2342"), actor.getPhones());
        assertEquals(Collections.singletonMap("JavaZone", "Jedi"), actor.getMovieCharacter());
        assertEquals(Collections.singletonMap("JavaZone", 10), actor.getMovieRating());
    }

    @Test
    public void shouldConvertColumnEntityToExistEntity() {
        ColumnEntity entity = ColumnEntity.of("Actor");
        Stream.of(columns).forEach(entity::add);
        Actor actor = Actor.actorBuilder().build();
        Actor result = converter.toEntity(actor, entity);

        assertSame(actor, result);
        assertEquals(10, actor.getAge());
        assertEquals(12L, actor.getId());
        assertEquals(asList("234", "2342"), actor.getPhones());
        assertEquals(Collections.singletonMap("JavaZone", "Jedi"), actor.getMovieCharacter());
        assertEquals(Collections.singletonMap("JavaZone", 10), actor.getMovieRating());
    }

    @Test
    public void shouldReturnErrorWhenToEntityIsNull() {
        ColumnEntity entity = ColumnEntity.of("Actor");
        Stream.of(columns).forEach(entity::add);
        Actor actor = Actor.actorBuilder().build();

        assertThrows(NullPointerException.class, () -> converter.toEntity(null, entity));

        assertThrows(NullPointerException.class, () -> converter.toEntity(actor, null));
    }


    @Test
    public void shouldConvertEntityToColumnEntity2() {

        Movie movie = new Movie("Matrix", 2012, Collections.singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(asList("234", "2342")).withMovie(movie).build();

        ColumnEntity entity = converter.toColumn(director);
        assertEquals(5, entity.size());

        assertEquals(getValue(entity.find("name")), director.getName());
        assertEquals(getValue(entity.find("age")), director.getAge());
        assertEquals(getValue(entity.find("_id")), director.getId());
        assertEquals(getValue(entity.find("phones")), director.getPhones());


        Column subColumn = entity.find("movie").get();
        List<Column> columns = subColumn.get(new TypeReference<List<Column>>() {
        });

        assertEquals(3, columns.size());
        assertEquals("movie", subColumn.getName());
        assertEquals(movie.getTitle(), columns.stream().filter(c -> "title".equals(c.getName())).findFirst().get().get());
        assertEquals(movie.getYear(), columns.stream().filter(c -> "year".equals(c.getName())).findFirst().get().get());
        assertEquals(movie.getActors(), columns.stream().filter(c -> "actors".equals(c.getName())).findFirst().get().get());


    }

    @Test
    public void shouldConvertToEmbeddedClassWhenHasSubColumn() {
        Movie movie = new Movie("Matrix", 2012, Collections.singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(asList("234", "2342")).withMovie(movie).build();

        ColumnEntity entity = converter.toColumn(director);
        Director director1 = converter.toEntity(entity);

        assertEquals(movie, director1.getMovie());
        assertEquals(director.getName(), director1.getName());
        assertEquals(director.getAge(), director1.getAge());
        assertEquals(director.getId(), director1.getId());
    }

    @Test
    public void shouldConvertToEmbeddedClassWhenHasSubColumn2() {
        Movie movie = new Movie("Matrix", 2012, singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(asList("234", "2342")).withMovie(movie).build();

        ColumnEntity entity = converter.toColumn(director);
        entity.remove("movie");
        entity.add(Column.of("title", "Matrix"));
        entity.add(Column.of("year", 2012));
        entity.add(Column.of("actors", singleton("Actor")));
        Director director1 = converter.toEntity(entity);

        assertEquals(movie, director1.getMovie());
        assertEquals(director.getName(), director1.getName());
        assertEquals(director.getAge(), director1.getAge());
        assertEquals(director.getId(), director1.getId());
    }

    @Test
    public void shouldConvertToEmbeddedClassWhenHasSubColumn3() {
        Movie movie = new Movie("Matrix", 2012, singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(asList("234", "2342")).withMovie(movie).build();

        ColumnEntity entity = converter.toColumn(director);
        entity.remove("movie");
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Matrix");
        map.put("year", 2012);
        map.put("actors", singleton("Actor"));

        entity.add(Column.of("movie", map));
        Director director1 = converter.toEntity(entity);

        assertEquals(movie, director1.getMovie());
        assertEquals(director.getName(), director1.getName());
        assertEquals(director.getAge(), director1.getAge());
        assertEquals(director.getId(), director1.getId());
    }

    @Test
    public void shouldConvertToColumnWhenHaConverter() {
        Worker worker = new Worker();
        Job job = new Job();
        job.setCity("Sao Paulo");
        job.setDescription("Java Developer");
        worker.setName("Bob");
        worker.setSalary(new Money("BRL", BigDecimal.TEN));
        worker.setJob(job);
        ColumnEntity entity = converter.toColumn(worker);
        assertEquals("Worker", entity.getName());
        assertEquals("Bob", entity.find("name").get().get());
        assertEquals("Sao Paulo", entity.find("city").get().get());
        assertEquals("Java Developer", entity.find("description").get().get());
        assertEquals("BRL 10", entity.find("money").get().get());
    }

    @Test
    public void shouldConvertToEntityWhenHasConverter() {
        Worker worker = new Worker();
        Job job = new Job();
        job.setCity("Sao Paulo");
        job.setDescription("Java Developer");
        worker.setName("Bob");
        worker.setSalary(new Money("BRL", BigDecimal.TEN));
        worker.setJob(job);
        ColumnEntity entity = converter.toColumn(worker);
        Worker worker1 = converter.toEntity(entity);
        assertEquals(worker.getSalary(), worker1.getSalary());
        assertEquals(job.getCity(), worker1.getJob().getCity());
        assertEquals(job.getDescription(), worker1.getJob().getDescription());
    }


    @Test
    public void shouldConvertToListEmbeddable() {
        AppointmentBook appointmentBook = new AppointmentBook("ids");
        appointmentBook.add(Contact.builder().withType(ContactType.EMAIL).withName("Ada").withInformation("ada@lovelace.com").build());
        appointmentBook.add(Contact.builder().withType(ContactType.MOBILE).withName("Ada").withInformation("11 1231231 123").build());
        appointmentBook.add(Contact.builder().withType(ContactType.PHONE).withName("Ada").withInformation("12 123 1231 123123").build());

        ColumnEntity entity = converter.toColumn(appointmentBook);
        Column contacts = entity.find("contacts").get();
        assertEquals("ids", appointmentBook.getId());
        List<List<Column>> columns = (List<List<Column>>) contacts.get();

        assertEquals(3L, columns.stream().flatMap(Collection::stream)
                .filter(c -> c.getName().equals("name"))
                .count());
    }

    @Test
    public void shouldConvertFromListEmbeddable() {
        ColumnEntity entity = ColumnEntity.of("AppointmentBook");
        entity.add(Column.of("_id", "ids"));
        List<List<Column>> columns = new ArrayList<>();

        columns.add(asList(Column.of("name", "Ada"), Column.of("type", ContactType.EMAIL),
                Column.of("information", "ada@lovelace.com")));

        columns.add(asList(Column.of("name", "Ada"), Column.of("type", ContactType.MOBILE),
                Column.of("information", "11 1231231 123")));

        columns.add(asList(Column.of("name", "Ada"), Column.of("type", ContactType.PHONE),
                Column.of("information", "phone")));

        entity.add(Column.of("contacts", columns));

        AppointmentBook appointmentBook = converter.toEntity(entity);

        List<Contact> contacts = appointmentBook.getContacts();
        assertEquals("ids", appointmentBook.getId());
        assertEquals("Ada", contacts.stream().map(Contact::getName).distinct().findFirst().get());

    }


    @Test
    public void shouldConvertSubEntity() {
        ZipCode zipcode = new ZipCode();
        zipcode.setZip("12321");
        zipcode.setPlusFour("1234");

        Address address = new Address();
        address.setCity("Salvador");
        address.setState("Bahia");
        address.setStreet("Rua Engenheiro Jose Anasoh");
        address.setZipCode(zipcode);

        ColumnEntity columnEntity = converter.toColumn(address);
        List<Column> columns = columnEntity.getColumns();
        assertEquals("Address", columnEntity.getName());
        assertEquals(4, columns.size());
        List<Column> zip = columnEntity.find("zipCode").map(d -> d.get(new TypeReference<List<Column>>() {
        })).orElse(Collections.emptyList());

        assertEquals("Rua Engenheiro Jose Anasoh", getValue(columnEntity.find("street")));
        assertEquals("Salvador", getValue(columnEntity.find("city")));
        assertEquals("Bahia", getValue(columnEntity.find("state")));
        assertEquals("12321", getValue(zip.stream().filter(d -> d.getName().equals("zip")).findFirst()));
        assertEquals("1234", getValue(zip.stream().filter(d -> d.getName().equals("plusFour")).findFirst()));
    }

    @Test
    public void shouldConvertColumnInSubEntity() {

        ColumnEntity entity = ColumnEntity.of("Address");

        entity.add(Column.of("street", "Rua Engenheiro Jose Anasoh"));
        entity.add(Column.of("city", "Salvador"));
        entity.add(Column.of("state", "Bahia"));
        entity.add(Column.of("zip", "12321"));
        entity.add(Column.of("plusFour", "1234"));

        Address address = converter.toEntity(entity);

        assertEquals("Rua Engenheiro Jose Anasoh", address.getStreet());
        assertEquals("Salvador", address.getCity());
        assertEquals("Bahia", address.getState());
        assertEquals("12321", address.getZipCode().getZip());
        assertEquals("1234", address.getZipCode().getPlusFour());

    }

    @Test
    public void shouldConvertAndDoNotUseUnmodifiableCollection() {
        ColumnEntity entity = ColumnEntity.of("vendors");
        entity.add("name", "name");
        entity.add("prefixes", Arrays.asList("value", "value2"));

        Vendor vendor = converter.toEntity(entity);
        vendor.add("value3");

        Assertions.assertEquals(3, vendor.getPrefixes().size());

    }

    @Test
    public void shouldConvertEntityToDocumentWithArray() {
        byte[] contents = {1, 2, 3, 4, 5, 6};

        ColumnEntity entity = ColumnEntity.of("download");
        entity.add("_id", 1L);
        entity.add("contents", contents);

        Download download = converter.toEntity(entity);
        Assertions.assertEquals(1L, download.getId());
        Assertions.assertEquals(contents, download.getContents());
    }

    @Test
    public void shouldConvertDocumentToEntityWithArray() {
        byte[] contents = {1, 2, 3, 4, 5, 6};

        Download download = new Download();
        download.setId(1L);
        download.setContents(contents);

        ColumnEntity entity = converter.toColumn(download);


        Assertions.assertEquals(1L, entity.find("_id").get().get());
        Assertions.assertEquals(contents, entity.find("contents").get().get());
    }

    private Object getValue(Optional<Column> column) {
        return column.map(Column::getValue).map(Value::get).orElse(null);
    }

}