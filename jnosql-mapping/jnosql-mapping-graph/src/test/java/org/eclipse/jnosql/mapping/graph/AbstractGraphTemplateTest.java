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
package org.eclipse.jnosql.mapping.graph;

import jakarta.data.exceptions.EmptyResultException;
import jakarta.data.exceptions.NonUniqueResultException;
import org.eclipse.jnosql.mapping.PreparedStatement;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.mapping.IdNotFoundException;
import org.eclipse.jnosql.mapping.graph.entities.Animal;
import org.eclipse.jnosql.mapping.graph.entities.Book;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.eclipse.jnosql.mapping.graph.entities.WrongEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractGraphTemplateTest {

    protected abstract Graph getGraph();

    protected abstract GraphTemplate getGraphTemplate();

    @AfterEach
    void after() {
        getGraph().traversal().V().toList().forEach(Vertex::remove);
        getGraph().traversal().E().toList().forEach(Edge::remove);
    }

    @Test
    void shouldReturnErrorWhenEntityIsNull() {
        assertThrows(NullPointerException.class, () -> getGraphTemplate().insert(null));
    }

    @Test
    void shouldInsertAnEntity() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Person updated = getGraphTemplate().insert(person);

        getGraphTemplate().delete(updated.getId());
    }

    @Test
    void shouldReturnErrorWhenInsertWithTTL() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> getGraphTemplate().insert(person, Duration.ZERO));
    }

    @Test
    void shouldReturnErrorWhenInsertIterableWithTTL() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> getGraphTemplate().insert(Collections.singleton(person), Duration.ZERO));
    }

    @Test
    void shouldInsertEntities() {
        Person otavio = Person.builder().withAge()
                .withName("Otavio").build();

        Person poliana = Person.builder().withAge()
                .withName("Poliana").build();

        final Iterable<Person> people = getGraphTemplate()
                .insert(Arrays.asList(otavio, poliana));

        final boolean allHasId = StreamSupport.stream(people.spliterator(), false)
                .map(Person::getId)
                .allMatch(Objects::nonNull);
        assertTrue(allHasId);
    }

    @Test
    void shouldMergeOnInsert() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Person updated = getGraphTemplate().insert(person);
        assertSame(person, updated);
    }

    @Test
    void shouldGetErrorWhenIdIsNullWhenUpdate() {
        assertThrows(EmptyResultException.class, () -> {
            Person person = Person.builder().withAge()
                    .withName("Otavio").build();
            getGraphTemplate().update(person);
        });
    }

    @Test
    void shouldGetErrorWhenEntityIsNotSavedYet() {
        assertThrows(EmptyResultException.class, () -> {
            Person person = Person.builder().withAge()
                    .withId(10L)
                    .withName("Otavio").build();

            getGraphTemplate().update(person);
        });
    }

    @Test
    void shouldUpdate() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Person updated = getGraphTemplate().insert(person);
        Person newPerson = Person.builder()
                .withAge()
                .withId(updated.getId())
                .withName("Otavio Updated").build();

        Person update = getGraphTemplate().update(newPerson);

        assertEquals(newPerson, update);

        getGraphTemplate().delete(update.getId());
    }

    @Test
    void shouldUpdateEntities() {
        Person otavio = Person.builder().withAge()
                .withName("Otavio").build();

        Person poliana = Person.builder().withAge()
                .withName("Poliana").build();

        final Iterable<Person> insertPeople = getGraphTemplate().insert(Arrays.asList(otavio, poliana));

        final List<Person> newPeople = StreamSupport.stream(insertPeople.spliterator(), false)
                .map(p -> Person.builder().withAge().withId(p.getId()).withName(p.getName() + " updated").build())
                .collect(toList());

        final Iterable<Person> update = getGraphTemplate().update(newPeople);

        final boolean allUpdated = StreamSupport.stream(update.spliterator(), false)
                .map(Person::getName).allMatch(name -> name.contains(" updated"));

        assertTrue(allUpdated);
    }


    @Test
    void shouldMergeOnUpdate() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Person updated = getGraphTemplate().insert(person);
        Person newPerson = Person.builder()
                .withAge()
                .withId(updated.getId())
                .withName("Otavio Updated").build();

        Person update = getGraphTemplate().update(newPerson);

        assertSame(update, newPerson);
    }

    @Test
    void shouldReturnErrorInFindWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> getGraphTemplate().find(null));
    }

    @Test
    void shouldFindAnEntity() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Person updated = getGraphTemplate().insert(person);
        Optional<Person> personFound = getGraphTemplate().find(updated.getId());

        assertTrue(personFound.isPresent());
        assertEquals(updated, personFound.get());

        getGraphTemplate().delete(updated.getId());
    }

    @Test
    void shouldNotFindAnEntity() {
        Optional<Person> personFound = getGraphTemplate().find(0L);
        assertFalse(personFound.isPresent());
    }

    @Test
    void shouldDeleteById() {

        Person person = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        assertTrue(getGraphTemplate().find(person.getId()).isPresent());
        getGraphTemplate().delete(person.getId());
        assertFalse(getGraphTemplate().find(person.getId()).isPresent());
    }


    @Test
    void shouldDeleteAnEntityFromTemplate() {

        Person person = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        assertTrue(getGraphTemplate().find(person.getId()).isPresent());
        getGraphTemplate().delete(Person.class, person.getId());
        assertFalse(getGraphTemplate().find(person.getId()).isPresent());
    }

    @Test
    void shouldNotDeleteAnEntityFromTemplate() {

        Person person = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        assertTrue(getGraphTemplate().find(person.getId()).isPresent());
        getGraphTemplate().delete(Book.class, person.getId());
        assertTrue(getGraphTemplate().find(person.getId()).isPresent());
        getGraphTemplate().delete(Person.class, person.getId());
        assertFalse(getGraphTemplate().find(person.getId()).isPresent());
    }


    @Test
    void shouldDeleteEntitiesById() {

        Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        Person poliana = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Poliana").build());

        assertTrue(getGraphTemplate().find(otavio.getId()).isPresent());
        getGraphTemplate().delete(Arrays.asList(otavio.getId(), poliana.getId()));
        assertFalse(getGraphTemplate().find(otavio.getId()).isPresent());
        assertFalse(getGraphTemplate().find(poliana.getId()).isPresent());
    }

    @Test
    void shouldReturnErrorWhenGetEdgesIdHasNullId() {
        assertThrows(NullPointerException.class, () -> getGraphTemplate().edgesById(null, Direction.BOTH));
    }

    @Test
    void shouldReturnErrorWhenGetEdgesIdHasNullDirection() {
        assertThrows(NullPointerException.class, () -> getGraphTemplate().edgesById(10, null));
    }

    @Test
    void shouldReturnEmptyWhenVertexDoesNotExist() {
        Collection<EdgeEntity> edges = getGraphTemplate().edgesById(10, Direction.BOTH);
        assertTrue(edges.isEmpty());
    }

    @Test
    void shouldReturnEdgesById() {
        Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        Animal dog = getGraphTemplate().insert(new Animal("dog"));
        Book cleanCode = getGraphTemplate().insert(Book.builder().withName("Clean code").build());

        EdgeEntity likes = getGraphTemplate().edge(otavio, "likes", dog);
        EdgeEntity reads = getGraphTemplate().edge(otavio, "reads", cleanCode);

        Collection<EdgeEntity> edgesById = getGraphTemplate().edgesById(otavio.getId(), Direction.BOTH);
        Collection<EdgeEntity> edgesById1 = getGraphTemplate().edgesById(otavio.getId(), Direction.BOTH, "reads");
        Collection<EdgeEntity> edgesById2 = getGraphTemplate().edgesById(otavio.getId(), Direction.BOTH, () -> "likes");
        Collection<EdgeEntity> edgesById3 = getGraphTemplate().edgesById(otavio.getId(), Direction.OUT);
        Collection<EdgeEntity> edgesById4 = getGraphTemplate().edgesById(cleanCode.getId(), Direction.IN);

        assertEquals(edgesById, edgesById3);
        assertThat(edgesById).contains(likes, reads);
        assertThat(edgesById1).contains(reads);
        assertThat(edgesById2).contains(likes);
        assertThat(edgesById4).contains(reads);

    }

    @Test
    void shouldDeleteEdge() {
        Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());
        Animal dog = getGraphTemplate().insert(new Animal("Ada"));

        EdgeEntity likes = getGraphTemplate().edge(otavio, "likes", dog);

        final Optional<EdgeEntity> edge = getGraphTemplate().edge(likes.id());
        Assertions.assertTrue(edge.isPresent());

        getGraphTemplate().deleteEdge(likes.id());
        assertFalse(getGraphTemplate().edge(likes.id()).isPresent());
    }

    @Test
    void shouldDeleteEdges() {
        Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());
        Animal dog = getGraphTemplate().insert(new Animal("Ada"));
        Book cleanCode = getGraphTemplate().insert(Book.builder().withName("Clean code").build());

        EdgeEntity likes = getGraphTemplate().edge(otavio, "likes", dog);
        EdgeEntity reads = getGraphTemplate().edge(otavio, "reads", cleanCode);

        final Optional<EdgeEntity> edge = getGraphTemplate().edge(likes.id());
        Assertions.assertTrue(edge.isPresent());

        getGraphTemplate().deleteEdge(Arrays.asList(likes.id(), reads.id()));
        assertFalse(getGraphTemplate().edge(likes.id()).isPresent());
        assertFalse(getGraphTemplate().edge(reads.id()).isPresent());
    }

    @Test
    void shouldReturnErrorWhenGetEdgesHasNullId() {
        assertThrows(NullPointerException.class, () -> getGraphTemplate().edges(null, Direction.BOTH));
    }

    @Test
    void shouldReturnErrorWhenGetEdgesHasNullId2() {
            Person otavio = Person.builder().withAge().withName("Otavio").build();
        Collection<EdgeEntity> edges = getGraphTemplate().edges(otavio, Direction.BOTH);
        assertThat(edges).isEmpty();
    }

    @Test
    void shouldReturnErrorWhenGetEdgesHasNullDirection() {
        assertThrows(NullPointerException.class, () -> {
            Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                    .withName("Otavio").build());
            getGraphTemplate().edges(otavio, null);
        });
    }

    @Test
    void shouldReturnEmptyWhenEntityDoesNotExist() {
        Person otavio = Person.builder().withAge().withName("Otavio").withId(10L).build();
        Collection<EdgeEntity> edges = getGraphTemplate().edges(otavio, Direction.BOTH);
        assertTrue(edges.isEmpty());
    }


    @Test
    void shouldReturnEdges() {
        Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        Animal dog = getGraphTemplate().insert(new Animal("dog"));
        Book cleanCode = getGraphTemplate().insert(Book.builder().withName("Clean code").build());

        EdgeEntity likes = getGraphTemplate().edge(otavio, "likes", dog);
        EdgeEntity reads = getGraphTemplate().edge(otavio, "reads", cleanCode);

        Collection<EdgeEntity> edgesById = getGraphTemplate().edges(otavio, Direction.BOTH);
        Collection<EdgeEntity> edgesById1 = getGraphTemplate().edges(otavio, Direction.BOTH, "reads");
        Collection<EdgeEntity> edgesById2 = getGraphTemplate().edges(otavio, Direction.BOTH, () -> "likes");
        Collection<EdgeEntity> edgesById3 = getGraphTemplate().edges(otavio, Direction.OUT);
        Collection<EdgeEntity> edgesById4 = getGraphTemplate().edges(cleanCode, Direction.IN);

        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(edgesById).contains(likes, reads);
            soft.assertThat(edgesById1).contains(reads);
            soft.assertThat(edgesById2).contains(likes);
            soft.assertThat(edgesById3).contains(likes, reads);
            soft.assertThat(edgesById4).contains(reads);
        });
    }

    @Test
    void shouldGetTransaction() {
        Transaction transaction = getGraphTemplate().transaction();
        assertNotNull(transaction);
    }

    @Test
    void shouldExecuteQuery() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        getGraphTemplate().insert(person);
        List<Person> people = getGraphTemplate()
                .<Person>gremlin("g.V().hasLabel('Person')")
                .toList();
        assertThat(people.stream().map(Person::getName).collect(toList())).contains("Otavio");
    }

    @Test
    void shouldReturnEmpty() {
        Optional<Person> person = getGraphTemplate().gremlinSingleResult("g.V().hasLabel('Person')");
        assertFalse(person.isPresent());
    }

    @Test
    void shouldReturnOneElement() {
        Person otavio = Person.builder().withAge()
                .withName("Otavio").build();
        getGraphTemplate().insert(otavio);
        Optional<Person> person = getGraphTemplate().gremlinSingleResult("g.V().hasLabel('Person')");
        assertTrue(person.isPresent());
    }

    @Test
    void shouldReturnErrorWhenHasNoneThanOneElement() {

        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        getGraphTemplate().insert(Person.builder().withAge().withName("Poliana").build());
        assertThrows(NonUniqueResultException.class, () -> getGraphTemplate().gremlinSingleResult("g.V().hasLabel('Person')"));
    }

    @Test
    void shouldExecutePrepareStatement() {
        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        PreparedStatement prepare = getGraphTemplate().gremlinPrepare("g.V().hasLabel(@param)");
        prepare.bind("param", "Person");
        List<Person> people = prepare.<Person>result().toList();
        assertThat(people.stream().map(Person::getName).collect(toList())).contains("Otavio");
    }

    @Test
    void shouldExecutePrepareStatementSingleton() {
        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        PreparedStatement prepare = getGraphTemplate().gremlinPrepare("g.V().hasLabel(@param)");
        prepare.bind("param", "Person");
        Optional<Person> otavio = prepare.singleResult();
        assertTrue(otavio.isPresent());
    }

    @Test
    void shouldExecutePrepareStatementSingletonEmpty() {
        PreparedStatement prepare = getGraphTemplate().gremlinPrepare("g.V().hasLabel(@param)");
        prepare.bind("param", "Person");
        Optional<Person> otavio = prepare.singleResult();
        assertFalse(otavio.isPresent());
    }

    @Test
    void shouldExecutePrepareStatementWithErrorWhenThereIsMoreThanOneResult() {
        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        getGraphTemplate().insert(Person.builder().withAge().withName("Poliana").build());
        PreparedStatement prepare = getGraphTemplate().gremlinPrepare("g.V().hasLabel(@param)");
        prepare.bind("param", "Person");
        assertThrows(NonUniqueResultException.class, prepare::singleResult);
    }

    @Test
    void shouldCount() {
        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        getGraphTemplate().insert(Person.builder().withAge().withName("Poliana").build());
        assertEquals(2L, getGraphTemplate().count("Person"));
    }

    @Test
    void shouldCountFromEntity() {
        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        getGraphTemplate().insert(Person.builder().withAge().withName("Poliana").build());
        assertEquals(2L, getGraphTemplate().count(Person.class));
    }


    @Test
    void shouldFindById() {
        final Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        final Optional<Person> person = getGraphTemplate().find(Person.class, otavio.getId());
        assertNotNull(person);
        assertTrue(person.isPresent());
        assertEquals(otavio.getName(), person.map(Person::getName).get());
    }

    @Test
    void shouldFindAll() {
        final Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());
        List<Person> people = getGraphTemplate().findAll(Person.class).toList();

        assertThat(people).hasSize(1)
                .map(Person::getName)
                .contains("Otavio");
    }

    @Test
    void shouldDeleteAll() {
        final Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());
        List<Person> people = getGraphTemplate().findAll(Person.class).toList();

        assertThat(people).hasSize(1)
                .map(Person::getName)
                .contains("Otavio");

        getGraphTemplate().deleteAll(Person.class);
        people = getGraphTemplate().findAll(Person.class).toList();

        assertThat(people).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenFindByIdNotFound() {

        final Optional<Person> person = getGraphTemplate().find(Person.class, -2L);
        assertNotNull(person);
        assertFalse(person.isPresent());
    }

    @Test
    void shouldUpdateNullValues(){
        final Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        assertEquals("Otavio", otavio.getName());
        otavio.setName(null);
        final Person person = getGraphTemplate().update(otavio);
        assertNull(person.getName());

    }
}
