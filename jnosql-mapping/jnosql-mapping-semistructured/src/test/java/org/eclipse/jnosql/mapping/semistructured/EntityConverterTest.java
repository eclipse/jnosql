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
package org.eclipse.jnosql.mapping.semistructured;

import jakarta.inject.Inject;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;
import org.eclipse.jnosql.communication.semistructured.Element;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.semistructured.entities.Actor;
import org.eclipse.jnosql.mapping.semistructured.entities.Address;
import org.eclipse.jnosql.mapping.semistructured.entities.AppointmentBook;
import org.eclipse.jnosql.mapping.semistructured.entities.Citizen;
import org.eclipse.jnosql.mapping.semistructured.entities.Contact;
import org.eclipse.jnosql.mapping.semistructured.entities.ContactType;
import org.eclipse.jnosql.mapping.semistructured.entities.Director;
import org.eclipse.jnosql.mapping.semistructured.entities.Download;
import org.eclipse.jnosql.mapping.semistructured.entities.Job;
import org.eclipse.jnosql.mapping.semistructured.entities.MainStepType;
import org.eclipse.jnosql.mapping.semistructured.entities.Money;
import org.eclipse.jnosql.mapping.semistructured.entities.Movie;
import org.eclipse.jnosql.mapping.semistructured.entities.Person;
import org.eclipse.jnosql.mapping.semistructured.entities.SocialMediaContact;
import org.eclipse.jnosql.mapping.semistructured.entities.Transition;
import org.eclipse.jnosql.mapping.semistructured.entities.Vendor;
import org.eclipse.jnosql.mapping.semistructured.entities.Wine;
import org.eclipse.jnosql.mapping.semistructured.entities.WineFactory;
import org.eclipse.jnosql.mapping.semistructured.entities.Worker;
import org.eclipse.jnosql.mapping.semistructured.entities.WorkflowStep;
import org.eclipse.jnosql.mapping.semistructured.entities.ZipCode;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.mapping.semistructured.entities.StepTransitionReason.REPEAT;
import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddPackages(value = {Converters.class, EntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions(EntityMetadataExtension.class)
class EntityConverterTest {

    @Inject
    private DefaultEntityConverter converter;

    private Element[] columns;

    private final Actor actor = Actor.actorBuilder().withAge()
            .withId()
            .withName()
            .withPhones(asList("234", "2342"))
            .withMovieCharacter(Collections.singletonMap("JavaZone", "Jedi"))
            .withMovieRating(Collections.singletonMap("JavaZone", 10))
            .build();

    @BeforeEach
    void init() {

        columns = new Element[]{Element.of("_id", 12L),
                Element.of("age", 10), Element.of("name", "Otavio"),
                Element.of("phones", asList("234", "2342"))
                , Element.of("movieCharacter", Collections.singletonMap("JavaZone", "Jedi"))
                , Element.of("movieRating", Collections.singletonMap("JavaZone", 10))};
    }

    @Test
    void shouldConvertEntityFromColumnEntity() {

        Person person = Person.builder().withAge()
                .withId(12)
                .withName("Otavio")
                .withPhones(asList("234", "2342")).build();

        CommunicationEntity entity = converter.toCommunication(person);
        assertEquals("Person", entity.name());
        assertEquals(4, entity.size());
        assertThat(entity.elements()).contains(Element.of("_id", 12L),
                Element.of("age", 10), Element.of("name", "Otavio"),
                Element.of("phones", Arrays.asList("234", "2342")));

    }

    @Test
    void shouldConvertColumnEntityFromEntity() {

        CommunicationEntity entity = converter.toCommunication(actor);
        assertEquals("Actor", entity.name());
        assertEquals(6, entity.size());

        assertThat(entity.elements()).contains(columns);
    }

    @Test
    void shouldConvertColumnEntityToEntity() {
        CommunicationEntity entity = CommunicationEntity.of("Actor");
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
    void shouldConvertColumnEntityToEntity2() {
        CommunicationEntity entity = CommunicationEntity.of("Actor");
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
    void shouldConvertColumnEntityToExistEntity() {
        CommunicationEntity entity = CommunicationEntity.of("Actor");
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
    void shouldReturnErrorWhenToEntityIsNull() {
        CommunicationEntity entity = CommunicationEntity.of("Actor");
        Stream.of(columns).forEach(entity::add);
        Actor actor = Actor.actorBuilder().build();

        assertThrows(NullPointerException.class, () -> converter.toEntity(null, entity));

        assertThrows(NullPointerException.class, () -> converter.toEntity(actor, null));
    }


    @Test
    void shouldConvertEntityToColumnEntity2() {

        Movie movie = new Movie("Matrix", 2012, Collections.singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(asList("234", "2342")).withMovie(movie).build();

        CommunicationEntity entity = converter.toCommunication(director);
        assertEquals(5, entity.size());

        assertEquals(getValue(entity.find("name")), director.getName());
        assertEquals(getValue(entity.find("age")), director.getAge());
        assertEquals(getValue(entity.find("_id")), director.getId());
        assertEquals(getValue(entity.find("phones")), director.getPhones());


        Element subColumn = entity.find("movie").get();
        List<Element> columns = subColumn.get(new TypeReference<>() {
        });

        assertEquals(3, columns.size());
        assertEquals("movie", subColumn.name());
        assertEquals(movie.getTitle(), columns.stream().filter(c -> "title".equals(c.name())).findFirst().get().get());
        assertEquals(movie.getYear(), columns.stream().filter(c -> "year".equals(c.name())).findFirst().get().get());
        assertEquals(movie.getActors(), columns.stream().filter(c -> "actors".equals(c.name())).findFirst().get().get());


    }

    @Test
    void shouldConvertToEmbeddedClassWhenHasSubColumn() {
        Movie movie = new Movie("Matrix", 2012, Collections.singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(asList("234", "2342")).withMovie(movie).build();

        CommunicationEntity entity = converter.toCommunication(director);
        Director director1 = converter.toEntity(entity);

        assertEquals(movie, director1.getMovie());
        assertEquals(director.getName(), director1.getName());
        assertEquals(director.getAge(), director1.getAge());
        assertEquals(director.getId(), director1.getId());
    }

    @Test
    void shouldConvertToEmbeddedClassWhenHasSubColumn2() {
        Movie movie = new Movie("Matrix", 2012, singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(asList("234", "2342")).withMovie(movie).build();

        CommunicationEntity entity = converter.toCommunication(director);
        entity.remove("movie");
        entity.add(Element.of("movie", Arrays.asList(Element.of("title", "Matrix"),
                Element.of("year", 2012), Element.of("actors", singleton("Actor")))));
        Director director1 = converter.toEntity(entity);

        assertEquals(movie, director1.getMovie());
        assertEquals(director.getName(), director1.getName());
        assertEquals(director.getAge(), director1.getAge());
        assertEquals(director.getId(), director1.getId());
    }

    @Test
    void shouldConvertToEmbeddedClassWhenHasSubColumn3() {
        Movie movie = new Movie("Matrix", 2012, singleton("Actor"));
        Director director = Director.builderDirector().withAge(12)
                .withId(12)
                .withName("Otavio")
                .withPhones(asList("234", "2342")).withMovie(movie).build();

        CommunicationEntity entity = converter.toCommunication(director);
        entity.remove("movie");
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Matrix");
        map.put("year", 2012);
        map.put("actors", singleton("Actor"));

        entity.add(Element.of("movie", map));
        Director director1 = converter.toEntity(entity);

        assertEquals(movie, director1.getMovie());
        assertEquals(director.getName(), director1.getName());
        assertEquals(director.getAge(), director1.getAge());
        assertEquals(director.getId(), director1.getId());
    }

    @Test
    void shouldConvertToColumnWhenHaConverter() {
        Worker worker = new Worker();
        Job job = new Job();
        job.setCity("Sao Paulo");
        job.setDescription("Java Developer");
        worker.setName("Bob");
        worker.setSalary(new Money("BRL", BigDecimal.TEN));
        worker.setJob(job);
        CommunicationEntity entity = converter.toCommunication(worker);
        assertEquals("Worker", entity.name());
        assertEquals("Bob", entity.find("name").get().get());
        assertEquals("Sao Paulo", entity.find("city").get().get());
        assertEquals("Java Developer", entity.find("description").get().get());
        assertEquals("BRL 10", entity.find("money").get().get());
    }

    @Test
    void shouldConvertToEntityWhenHasConverter() {
        Worker worker = new Worker();
        Job job = new Job();
        job.setCity("Sao Paulo");
        job.setDescription("Java Developer");
        worker.setName("Bob");
        worker.setSalary(new Money("BRL", BigDecimal.TEN));
        worker.setJob(job);
        CommunicationEntity entity = converter.toCommunication(worker);
        Worker worker1 = converter.toEntity(entity);
        assertEquals(worker.getSalary(), worker1.getSalary());
        assertEquals(job.getCity(), worker1.getJob().getCity());
        assertEquals(job.getDescription(), worker1.getJob().getDescription());
    }

    @Test
    void shouldConvertEmbeddableLazily() {
        CommunicationEntity entity = CommunicationEntity.of("Worker");
        entity.add("name", "Otavio");
        entity.add("money", "BRL 10");

        Worker worker = converter.toEntity(entity);
        assertEquals("Otavio", worker.getName());
        assertEquals(new Money("BRL", BigDecimal.TEN), worker.getSalary());
        Assertions.assertNull(worker.getJob());

    }


    @Test
    void shouldConvertToListEmbeddable() {
        AppointmentBook appointmentBook = new AppointmentBook("ids");
        appointmentBook.add(Contact.builder().withType(ContactType.EMAIL)
                .withName("Ada").withInformation("ada@lovelace.com").build());
        appointmentBook.add(Contact.builder().withType(ContactType.MOBILE)
                .withName("Ada").withInformation("11 1231231 123").build());
        appointmentBook.add(Contact.builder().withType(ContactType.PHONE)
                .withName("Ada").withInformation("12 123 1231 123123").build());

        CommunicationEntity entity = converter.toCommunication(appointmentBook);
        Element contacts = entity.find("contacts").get();
        assertEquals("ids", appointmentBook.getId());
        List<List<Element>> columns = (List<List<Element>>) contacts.get();

        assertEquals(3L, columns.stream().flatMap(Collection::stream)
                .filter(c -> c.name().equals("contact_name"))
                .count());
    }

    @Test
    void shouldConvertFromListEmbeddable() {
        CommunicationEntity entity = CommunicationEntity.of("AppointmentBook");
        entity.add(Element.of("_id", "ids"));
        List<List<Element>> columns = new ArrayList<>();

        columns.add(asList(Element.of("contact_name", "Ada"), Element.of("type", ContactType.EMAIL),
                Element.of("information", "ada@lovelace.com")));

        columns.add(asList(Element.of("contact_name", "Ada"), Element.of("type", ContactType.MOBILE),
                Element.of("information", "11 1231231 123")));

        columns.add(asList(Element.of("contact_name", "Ada"), Element.of("type", ContactType.PHONE),
                Element.of("information", "phone")));

        entity.add(Element.of("contacts", columns));

        AppointmentBook appointmentBook = converter.toEntity(entity);

        List<Contact> contacts = appointmentBook.getContacts();
        assertEquals("ids", appointmentBook.getId());
        assertEquals("Ada", contacts.stream().map(Contact::getName).distinct().findFirst().get());

    }


    @Test
    void shouldConvertSubEntity() {
        ZipCode zipcode = new ZipCode();
        zipcode.setZip("12321");
        zipcode.setPlusFour("1234");

        Address address = new Address();
        address.setCity("Salvador");
        address.setState("Bahia");
        address.setStreet("Rua Engenheiro Jose Anasoh");
        address.setZipCode(zipcode);

        CommunicationEntity columnEntity = converter.toCommunication(address);
        List<Element> columns = columnEntity.elements();
        assertEquals("Address", columnEntity.name());
        assertEquals(4, columns.size());
        List<Element> zip = columnEntity.find("zipCode").map(d -> d.get(new TypeReference<List<Element>>() {
        })).orElse(Collections.emptyList());

        assertEquals("Rua Engenheiro Jose Anasoh", getValue(columnEntity.find("street")));
        assertEquals("Salvador", getValue(columnEntity.find("city")));
        assertEquals("Bahia", getValue(columnEntity.find("state")));
        assertEquals("12321", getValue(zip.stream().filter(d -> d.name().equals("zip")).findFirst()));
        assertEquals("1234", getValue(zip.stream().filter(d -> d.name().equals("plusFour")).findFirst()));
    }

    @Test
    void shouldConvertColumnInSubEntity() {

        CommunicationEntity entity = CommunicationEntity.of("Address");

        entity.add(Element.of("street", "Rua Engenheiro Jose Anasoh"));
        entity.add(Element.of("city", "Salvador"));
        entity.add(Element.of("state", "Bahia"));
        entity.add(Element.of("zipCode", Arrays.asList(
                Element.of("zip", "12321"),
                Element.of("plusFour", "1234"))));
        Address address = converter.toEntity(entity);

        assertEquals("Rua Engenheiro Jose Anasoh", address.getStreet());
        assertEquals("Salvador", address.getCity());
        assertEquals("Bahia", address.getState());
        assertEquals("12321", address.getZipCode().getZip());
        assertEquals("1234", address.getZipCode().getPlusFour());

    }

    @Test
    void shouldReturnNullWhenThereIsNotSubEntity() {
        CommunicationEntity entity = CommunicationEntity.of("Address");

        entity.add(Element.of("street", "Rua Engenheiro Jose Anasoh"));
        entity.add(Element.of("city", "Salvador"));
        entity.add(Element.of("state", "Bahia"));
        entity.add(Element.of("zip", "12321"));
        entity.add(Element.of("plusFour", "1234"));

        Address address = converter.toEntity(entity);

        assertEquals("Rua Engenheiro Jose Anasoh", address.getStreet());
        assertEquals("Salvador", address.getCity());
        assertEquals("Bahia", address.getState());
        assertNull(address.getZipCode());
    }

    @Test
    void shouldConvertAndDoNotUseUnmodifiableCollection() {
        CommunicationEntity entity = CommunicationEntity.of("vendors");
        entity.add("name", "name");
        entity.add("prefixes", Arrays.asList("value", "value2"));

        Vendor vendor = converter.toEntity(entity);
        vendor.add("value3");

        Assertions.assertEquals(3, vendor.getPrefixes().size());

    }

    @Test
    void shouldConvertEntityToDocumentWithArray() {
        byte[] contents = {1, 2, 3, 4, 5, 6};

        CommunicationEntity entity = CommunicationEntity.of("download");
        entity.add("_id", 1L);
        entity.add("contents", contents);

        Download download = converter.toEntity(entity);
        Assertions.assertEquals(1L, download.getId());
        Assertions.assertArrayEquals(contents, download.getContents());
    }

    @Test
    void shouldConvertDocumentToEntityWithArray() {
        byte[] contents = {1, 2, 3, 4, 5, 6};

        Download download = new Download();
        download.setId(1L);
        download.setContents(contents);

        CommunicationEntity entity = converter.toCommunication(download);

        Assertions.assertEquals(1L, entity.find("_id").get().get());
        final byte[] bytes = entity.find("contents").map(v -> v.get(byte[].class)).orElse(new byte[0]);
        Assertions.assertArrayEquals(contents, bytes);
    }

    @Test
    void shouldCreateUserScope() {
        CommunicationEntity entity = CommunicationEntity.of("UserScope");
        entity.add("_id", "userName");
        entity.add("scope", "scope");
        entity.add("properties", Collections.singletonList(Element.of("halo", "weld")));

        UserScope user = converter.toEntity(entity);
        Assertions.assertNotNull(user);
        Assertions.assertEquals("userName",user.getUserName());
        Assertions.assertEquals("scope",user.getScope());
        Assertions.assertEquals(Collections.singletonMap("halo", "weld"),user.getProperties());

    }

    @Test
    void shouldCreateUserScope2() {
        CommunicationEntity entity = CommunicationEntity.of("UserScope");
        entity.add("_id", "userName");
        entity.add("scope", "scope");
        entity.add("properties", Element.of("halo", "weld"));

        UserScope user = converter.toEntity(entity);
        Assertions.assertNotNull(user);
        Assertions.assertEquals("userName",user.getUserName());
        Assertions.assertEquals("scope",user.getScope());
        Assertions.assertEquals(Collections.singletonMap("halo", "weld"),user.getProperties());

    }

    @Test
    void shouldCreateLazilyEntity() {
        CommunicationEntity entity = CommunicationEntity.of("Citizen");
        entity.add("id", "10");
        entity.add("name", "Salvador");

        Citizen citizen = converter.toEntity(entity);
        Assertions.assertNotNull(citizen);
        Assertions.assertNull(citizen.getCity());
    }


    @Test
    void shouldReturnNullValuePresent() {
        Person person = Person.builder().build();

        CommunicationEntity entity = converter.toCommunication(person);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(entity.find("name")).isPresent();
            soft.assertThat(entity.find("age")).isPresent();
            soft.assertThat(entity.find("phones")).isPresent();
            soft.assertThat(entity.find("ignore")).isNotPresent();

            soft.assertThat(entity.find("name", String.class)).isNotPresent();
            soft.assertThat(entity.find("phones", String.class)).isNotPresent();
        });
    }

    @Test
    void shouldConvertWorkflow(){
        var workflowStep = WorkflowStep.builder()
                .id("id")
                .key("key")
                .workflowSchemaKey("workflowSchemaKey")
                .stepName("stepName")
                .mainStepType(MainStepType.MAIN)
                .stepNo(1)
                .componentConfigurationKey("componentConfigurationKey")
                .relationTypeKey("relationTypeKey")
                .availableTransitions(List.of(new Transition("TEST_WORKFLOW_STEP_KEY", REPEAT,
                        null, List.of("ADMIN"))))
                .build();

        var document = this.converter.toCommunication(workflowStep);
        WorkflowStep result = this.converter.toEntity(document);
        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(result).isNotNull();
            soft.assertThat(result.id()).isEqualTo("id");
            soft.assertThat(result.key()).isEqualTo("key");
            soft.assertThat(result.workflowSchemaKey()).isEqualTo("workflowSchemaKey");
            soft.assertThat(result.stepName()).isEqualTo("stepName");
            soft.assertThat(result.mainStepType()).isEqualTo(MainStepType.MAIN);
            soft.assertThat(result.stepNo()).isEqualTo(1L);
            soft.assertThat(result.componentConfigurationKey()).isEqualTo("componentConfigurationKey");
            soft.assertThat(result.relationTypeKey()).isEqualTo("relationTypeKey");
            soft.assertThat(result.availableTransitions()).hasSize(1);
            soft.assertThat(result.availableTransitions().get(0).targetWorkflowStepKey()).isEqualTo("TEST_WORKFLOW_STEP_KEY");
            soft.assertThat(result.availableTransitions().get(0).stepTransitionReason()).isEqualTo(REPEAT);
            soft.assertThat(result.availableTransitions().get(0).mailTemplateKey()).isNull();
            soft.assertThat(result.availableTransitions().get(0).restrictedRoleGroups()).hasSize(1);
            soft.assertThat(result.availableTransitions().get(0).restrictedRoleGroups().get(0)).isEqualTo("ADMIN");
        });

    }

    @Test
    void shouldUpdateEmbeddable2() {
        var workflowStep = WorkflowStep.builder()
                .id("id")
                .key("key")
                .workflowSchemaKey("workflowSchemaKey")
                .stepName("stepName")
                .mainStepType(MainStepType.MAIN)
                .stepNo(null)
                .componentConfigurationKey("componentConfigurationKey")
                .relationTypeKey("relationTypeKey")
                .availableTransitions(null)
                .build();
        var document = this.converter.toCommunication(workflowStep);
        WorkflowStep result = this.converter.toEntity(document);
        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(result).isNotNull();
            soft.assertThat(result.id()).isEqualTo("id");
            soft.assertThat(result.key()).isEqualTo("key");
            soft.assertThat(result.workflowSchemaKey()).isEqualTo("workflowSchemaKey");
            soft.assertThat(result.stepName()).isEqualTo("stepName");
            soft.assertThat(result.mainStepType()).isEqualTo(MainStepType.MAIN);
            soft.assertThat(result.stepNo()).isNull();
            soft.assertThat(result.componentConfigurationKey()).isEqualTo("componentConfigurationKey");
            soft.assertThat(result.relationTypeKey()).isEqualTo("relationTypeKey");
            soft.assertThat(result.availableTransitions()).isNull();

        });

    }

    @Test
    void shouldIgnoreWhenNull(){
        CommunicationEntity entity = CommunicationEntity.of("SocialMediaContact");
        entity.add("_id", "id");
        entity.add("name", "Twitter");
        entity.add("users", null);

        SocialMediaContact socialMediaContact = converter.toEntity(entity);

        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(socialMediaContact).isNotNull();
            soft.assertThat(socialMediaContact.getId()).isEqualTo("id");
            soft.assertThat(socialMediaContact.getName()).isEqualTo("Twitter");
            soft.assertThat(socialMediaContact.getUsers()).isNull();
        });
    }

    @Test
    void shouldConvertGroupEmbeddable(){
        CommunicationEntity entity = CommunicationEntity.of("Wine");
        entity.add("_id", "id");
        entity.add("name", "Vin Blanc");
        entity.add("factory", List.of(Element.of("name", "Napa Valley Factory"),
                Element.of("location", "Napa Valley")));

        Wine wine = converter.toEntity(entity);

        SoftAssertions.assertSoftly(soft ->{
            WineFactory factory = wine.getFactory();
            soft.assertThat(wine).isNotNull();
            soft.assertThat(wine.getId()).isEqualTo("id");
            soft.assertThat(wine.getName()).isEqualTo("Vin Blanc");
            soft.assertThat(factory).isNotNull();
            soft.assertThat(factory.getName()).isEqualTo("Napa Valley Factory");
            soft.assertThat(factory.getLocation()).isEqualTo("Napa Valley");
        });
    }

    @Test
    void shouldConvertGroupEmbeddableToCommunication(){

        Wine wine = Wine.of("id", "Vin Blanc", WineFactory.of("Napa Valley Factory", "Napa Valley"));


        var communication = converter.toCommunication(wine);

        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(communication).isNotNull();
            soft.assertThat(communication.name()).isEqualTo("Wine");
            soft.assertThat(communication.find("_id").orElseThrow().get()).isEqualTo("id");
            soft.assertThat(communication.find("name").orElseThrow().get()).isEqualTo("Vin Blanc");
            communication.find("factory").ifPresent(e -> {
                List<Element> elements = e.get(new TypeReference<>(){});
                soft.assertThat(elements).hasSize(2);
                soft.assertThat(elements.stream().filter(c -> "name".equals(c.name())).findFirst().orElseThrow().get())
                        .isEqualTo("Napa Valley Factory");
                soft.assertThat(elements.stream().filter(c -> "location".equals(c.name())).findFirst().orElseThrow().get())
                        .isEqualTo("Napa Valley");
            });

        });
    }


    private Object getValue(Optional<Element> column) {
        return column.map(Element::value).map(Value::get).orElse(null);
    }

}
