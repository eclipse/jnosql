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
import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;
import org.eclipse.jnosql.communication.semistructured.Element;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.semistructured.entities.Animal;
import org.eclipse.jnosql.mapping.semistructured.entities.Book;
import org.eclipse.jnosql.mapping.semistructured.entities.BookRelease;
import org.eclipse.jnosql.mapping.semistructured.entities.Money;
import org.eclipse.jnosql.mapping.semistructured.entities.SocialMediaContact;
import org.eclipse.jnosql.mapping.semistructured.entities.Wine;
import org.eclipse.jnosql.mapping.semistructured.entities.WineFactory;
import org.eclipse.jnosql.mapping.semistructured.entities.constructor.Beer;
import org.eclipse.jnosql.mapping.semistructured.entities.constructor.BeerFactory;
import org.eclipse.jnosql.mapping.semistructured.entities.constructor.BookUser;
import org.eclipse.jnosql.mapping.semistructured.entities.constructor.Computer;
import org.eclipse.jnosql.mapping.semistructured.entities.constructor.PetOwner;
import org.eclipse.jnosql.mapping.semistructured.entities.constructor.SocialMediaFollowers;
import org.eclipse.jnosql.mapping.semistructured.entities.constructor.SocialMediaFollowersRecord;
import org.eclipse.jnosql.mapping.semistructured.entities.constructor.SuperHero;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
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
@AddPackages(value = {Converters.class, EntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class})
class EntityConverterConstructorTest {

    @Inject
    private EntityConverter converter;

    @Test
    void shouldConverterEntityComputer() {
        CommunicationEntity communication = CommunicationEntity.of("Computer");
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
    void shouldConvertComputerToCommunication() {
        Computer computer = new Computer(10L, "Dell", 2020, "Dell 2020",
                Money.parse("USD 20"));
        CommunicationEntity communication = this.converter.toCommunication(computer);
        assertNotNull(communication);

        assertEquals(computer.getId(), communication.find("_id", Long.class).get());
        assertEquals(computer.getName(), communication.find("name", String.class).get());
        assertEquals(computer.getAge(), communication.find("age", int.class).get());
        assertEquals(computer.getModel(), communication.find("model", String.class).get());
        assertEquals(computer.getPrice().toString(), communication.find("price", String.class).get());
    }

    @Test
    void shouldConvertPetOwner() {
        CommunicationEntity communication = CommunicationEntity.of("PetOwner");
        communication.add("_id", 10L);
        communication.add("name", "Otavio");
        communication.add("animal", Arrays.asList(Element.of("_id", 23)
                , Element.of("name", "Ada")));

        PetOwner petOwner = this.converter.toEntity(communication);
        assertNotNull(petOwner);
        assertEquals(10L, petOwner.getId());
        assertEquals("Otavio", petOwner.getName());
        Animal animal = petOwner.getAnimal();
        assertEquals(23L, animal.getId());
        assertEquals("Ada", animal.getName());
    }

    @Test
    void shouldConvertPetOwnerCommunication() {
        Animal ada = new Animal("Ada");
        PetOwner petOwner = new PetOwner(10L, "Poliana", ada);
        CommunicationEntity communication = this.converter.toCommunication(petOwner);
        assertNotNull(communication);
        assertEquals(10L, communication.find("_id", Long.class).get());
        assertEquals("Poliana", communication.find("name", String.class).get());
        List<Element> columns = communication.find("animal", new TypeReference<List<Element>>() {})
                .get();
        assertThat(columns).contains(Element.of("name", "Ada"));
    }

    @Test
    void shouldConvertBookUser() {
        CommunicationEntity communication = CommunicationEntity.of("BookUser");
        communication.add("_id", "otaviojava");
        communication.add("native_name", "Otavio Santana");
        List<List<Element>> columns = new ArrayList<>();
        columns.add(Arrays.asList(Element.of("_id", 10), Element.of("name", "Effective Java")));
        columns.add(Arrays.asList(Element.of("_id", 12), Element.of("name", "Clean Code")));
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
    void shouldConverterFieldsOnEntityComputer() {
        CommunicationEntity communication = CommunicationEntity.of("Computer");
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
    void shouldConverterEntityBookRelease() {
        CommunicationEntity communication = CommunicationEntity.of("BookRelease");
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
    void shouldConverterEntityBookReleaseOnStringYear() {
        CommunicationEntity communication = CommunicationEntity.of("BookRelease");
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

    @Test
    void shouldConvertHero() {
        CommunicationEntity communication = CommunicationEntity.of("SuperHero");
        communication.add("_id", "10L");
        communication.add("name", "Otavio");
        communication.add("powers", List.of("speed", "strength"));

        SuperHero hero = this.converter.toEntity(communication);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(hero.id()).isEqualTo("10L");
            softly.assertThat(hero.name()).isEqualTo("Otavio");
            softly.assertThat(hero.powers()).contains("speed", "strength");
        });
    }

    @Test
    void shouldIgnoreWhenNullAtConstructor(){
        CommunicationEntity entity = CommunicationEntity.of("SocialMediaFollowers");
        entity.add("_id", "id");
        entity.add("followers", null);

        SocialMediaFollowers socialMediaContact = converter.toEntity(entity);

        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(socialMediaContact).isNotNull();
            soft.assertThat(socialMediaContact.getId()).isEqualTo("id");
        });
    }

    @Test
    void shouldIgnoreWhenNullAtRecord(){
        CommunicationEntity entity = CommunicationEntity.of("SocialMediaFollowersRecord");
        entity.add("_id", "id");
        entity.add("followers", null);

        SocialMediaFollowersRecord socialMediaContact = converter.toEntity(entity);

        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(socialMediaContact).isNotNull();
            soft.assertThat(socialMediaContact.id()).isEqualTo("id");
        });
    }

    @Test
    void shouldConvertGroupEmbeddable(){
        CommunicationEntity entity = CommunicationEntity.of("Beer");
        entity.add("_id", "id");
        entity.add("name", "Vin Blanc");
        entity.add("factory", List.of(Element.of("name", "Napa Valley Factory"),
                Element.of("location", "Napa Valley")));

        Beer beer = converter.toEntity(entity);

        SoftAssertions.assertSoftly(soft ->{
            var factory = beer.factory();
            soft.assertThat(beer).isNotNull();
            soft.assertThat(beer.id()).isEqualTo("id");
            soft.assertThat(beer.name()).isEqualTo("Vin Blanc");
            soft.assertThat(factory).isNotNull();
            soft.assertThat(factory.name()).isEqualTo("Napa Valley Factory");
            soft.assertThat(factory.location()).isEqualTo("Napa Valley");
        });
    }

    @Test
    void shouldConvertGroupEmbeddableToCommunication(){

        var wine = Beer.of("id", "Vin Blanc", BeerFactory.of("Napa Valley Factory", "Napa Valley"));


        var communication = converter.toCommunication(wine);

        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(communication).isNotNull();
            soft.assertThat(communication.name()).isEqualTo("Beer");
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

}
