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

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.mapping.EntityNotFoundException;
import jakarta.nosql.mapping.IdNotFoundException;
import jakarta.nosql.mapping.PreparedStatement;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractGraphTemplateTest {

    protected abstract Graph getGraph();

    protected abstract GraphTemplate getGraphTemplate();

    @AfterEach
    public void after() {
        getGraph().traversal().V().toList().forEach(Vertex::remove);
        getGraph().traversal().E().toList().forEach(Edge::remove);
    }

    @Test
    public void shouldReturnErrorWhenThereIsNotId() {
        assertThrows(IdNotFoundException.class, () -> {
            WrongEntity entity = new WrongEntity("lion");
            getGraphTemplate().insert(entity);
        });
    }

    @Test
    public void shouldReturnErrorWhenEntityIsNull() {
        assertThrows(NullPointerException.class, () -> getGraphTemplate().insert(null));
    }

    @Test
    public void shouldInsertAnEntity() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Person updated = getGraphTemplate().insert(person);

        assertNotNull(updated.getId());
        getGraphTemplate().delete(updated.getId());
    }

    @Test
    public void shouldReturnErrorWhenInsertWithTTL() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> getGraphTemplate().insert(person, Duration.ZERO));
    }

    @Test
    public void shouldReturnErrorWhenInsertIterableWithTTL() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> getGraphTemplate().insert(Collections.singleton(person), Duration.ZERO));
    }

    @Test
    public void shouldInsertEntities() {
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
    public void shouldMergeOnInsert() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Person updated = getGraphTemplate().insert(person);
        assertSame(person, updated);
    }

    @Test
    public void shouldGetErrorWhenIdIsNullWhenUpdate() {
        assertThrows(IllegalStateException.class, () -> {
            Person person = Person.builder().withAge()
                    .withName("Otavio").build();
            getGraphTemplate().update(person);
        });
    }

    @Test
    public void shouldGetErrorWhenEntityIsNotSavedYet() {
        assertThrows(EntityNotFoundException.class, () -> {
            Person person = Person.builder().withAge()
                    .withId(10L)
                    .withName("Otavio").build();

            getGraphTemplate().update(person);
        });
    }

    @Test
    public void shouldUpdate() {
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
    public void shouldUpdateEntities() {
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
    public void shouldMergeOnUpdate() {
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
    public void shouldReturnErrorInFindWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> getGraphTemplate().find(null));
    }

    @Test
    public void shouldFindAnEntity() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        Person updated = getGraphTemplate().insert(person);
        Optional<Person> personFound = getGraphTemplate().find(updated.getId());

        assertTrue(personFound.isPresent());
        assertEquals(updated, personFound.get());

        getGraphTemplate().delete(updated.getId());
    }

    @Test
    public void shouldNotFindAnEntity() {
        Optional<Person> personFound = getGraphTemplate().find(0L);
        assertFalse(personFound.isPresent());
    }

    @Test
    public void shouldDeleteAnEntity() {

        Person person = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        assertTrue(getGraphTemplate().find(person.getId()).isPresent());
        getGraphTemplate().delete(person.getId());
        assertFalse(getGraphTemplate().find(person.getId()).isPresent());
    }

    @Test
    public void shouldDeleteAnEntityFromTemplate() {

        Person person = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        assertTrue(getGraphTemplate().find(person.getId()).isPresent());
        getGraphTemplate().delete(Person.class, person.getId());
        assertFalse(getGraphTemplate().find(person.getId()).isPresent());
    }

    @Test
    public void shouldNotDeleteAnEntityFromTemplate() {

        Person person = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        assertTrue(getGraphTemplate().find(person.getId()).isPresent());
        getGraphTemplate().delete(Book.class, person.getId());
        assertTrue(getGraphTemplate().find(person.getId()).isPresent());
        getGraphTemplate().delete(Person.class, person.getId());
        assertFalse(getGraphTemplate().find(person.getId()).isPresent());
    }


    @Test
    public void shouldDeleteEntities() {

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
    public void shouldReturnErrorWhenGetEdgesIdHasNullId() {
        assertThrows(NullPointerException.class, () -> getGraphTemplate().getEdgesById(null, Direction.BOTH));
    }

    @Test
    public void shouldReturnErrorWhenGetEdgesIdHasNullDirection() {
        assertThrows(NullPointerException.class, () -> getGraphTemplate().getEdgesById(10, null));
    }

    @Test
    public void shouldReturnEmptyWhenVertexDoesNotExist() {
        Collection<EdgeEntity> edges = getGraphTemplate().getEdgesById(10, Direction.BOTH);
        assertTrue(edges.isEmpty());
    }

    @Test
    public void shouldReturnEdgesById() {
        Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        Animal dog = getGraphTemplate().insert(new Animal("dog"));
        Book cleanCode = getGraphTemplate().insert(Book.builder().withName("Clean code").build());

        EdgeEntity likes = getGraphTemplate().edge(otavio, "likes", dog);
        EdgeEntity reads = getGraphTemplate().edge(otavio, "reads", cleanCode);

        Collection<EdgeEntity> edgesById = getGraphTemplate().getEdgesById(otavio.getId(), Direction.BOTH);
        Collection<EdgeEntity> edgesById1 = getGraphTemplate().getEdgesById(otavio.getId(), Direction.BOTH, "reads");
        Collection<EdgeEntity> edgesById2 = getGraphTemplate().getEdgesById(otavio.getId(), Direction.BOTH, () -> "likes");
        Collection<EdgeEntity> edgesById3 = getGraphTemplate().getEdgesById(otavio.getId(), Direction.OUT);
        Collection<EdgeEntity> edgesById4 = getGraphTemplate().getEdgesById(cleanCode.getId(), Direction.IN);

        assertEquals(edgesById, edgesById3);
        assertThat(edgesById).contains(likes, reads);
        assertThat(edgesById1).contains(reads);
        assertThat(edgesById2).contains(likes);
        assertThat(edgesById4).contains(reads);

    }

    @Test
    public void shouldDeleteEdge() {
        Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());
        Animal dog = getGraphTemplate().insert(new Animal("Ada"));

        EdgeEntity likes = getGraphTemplate().edge(otavio, "likes", dog);

        final Optional<EdgeEntity> edge = getGraphTemplate().edge(likes.getId());
        Assertions.assertTrue(edge.isPresent());

        getGraphTemplate().deleteEdge(likes.getId());
        assertFalse(getGraphTemplate().edge(likes.getId()).isPresent());
    }

    @Test
    public void shouldDeleteEdges() {
        Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());
        Animal dog = getGraphTemplate().insert(new Animal("Ada"));
        Book cleanCode = getGraphTemplate().insert(Book.builder().withName("Clean code").build());

        EdgeEntity likes = getGraphTemplate().edge(otavio, "likes", dog);
        EdgeEntity reads = getGraphTemplate().edge(otavio, "reads", cleanCode);

        final Optional<EdgeEntity> edge = getGraphTemplate().edge(likes.getId());
        Assertions.assertTrue(edge.isPresent());

        getGraphTemplate().deleteEdge(Arrays.asList(likes.getId(), reads.getId()));
        assertFalse(getGraphTemplate().edge(likes.getId()).isPresent());
        assertFalse(getGraphTemplate().edge(reads.getId()).isPresent());
    }

    @Test
    public void shouldReturnErrorWhenGetEdgesHasNullId() {
        assertThrows(NullPointerException.class, () -> getGraphTemplate().getEdges(null, Direction.BOTH));
    }

    @Test
    public void shouldReturnErrorWhenGetEdgesHasNullId2() {
        assertThrows(IllegalStateException.class, () -> {
            Person otavio = Person.builder().withAge().withName("Otavio").build();
            getGraphTemplate().getEdges(otavio, Direction.BOTH);
        });
    }

    @Test
    public void shouldReturnErrorWhenGetEdgesHasNullDirection() {
        assertThrows(NullPointerException.class, () -> {
            Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                    .withName("Otavio").build());
            getGraphTemplate().getEdges(otavio, null);
        });
    }

    @Test
    public void shouldReturnEmptyWhenEntityDoesNotExist() {
        Person otavio = Person.builder().withAge().withName("Otavio").withId(10L).build();
        Collection<EdgeEntity> edges = getGraphTemplate().getEdges(otavio, Direction.BOTH);
        assertTrue(edges.isEmpty());
    }


    @Test
    public void shouldReturnEdges() {
        Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        Animal dog = getGraphTemplate().insert(new Animal("dog"));
        Book cleanCode = getGraphTemplate().insert(Book.builder().withName("Clean code").build());

        EdgeEntity likes = getGraphTemplate().edge(otavio, "likes", dog);
        EdgeEntity reads = getGraphTemplate().edge(otavio, "reads", cleanCode);

        Collection<EdgeEntity> edgesById = getGraphTemplate().getEdges(otavio, Direction.BOTH);
        Collection<EdgeEntity> edgesById1 = getGraphTemplate().getEdges(otavio, Direction.BOTH, "reads");
        Collection<EdgeEntity> edgesById2 = getGraphTemplate().getEdges(otavio, Direction.BOTH, () -> "likes");
        Collection<EdgeEntity> edgesById3 = getGraphTemplate().getEdges(otavio, Direction.OUT);
        Collection<EdgeEntity> edgesById4 = getGraphTemplate().getEdges(cleanCode, Direction.IN);

        assertEquals(edgesById, edgesById3);
        assertThat(edgesById).contains(likes, reads);
        assertThat(edgesById1).contains(reads);
        assertThat(edgesById2).contains(likes);
        assertThat(edgesById4).contains(reads);

    }

    @Test
    public void shouldGetTransaction() {
        Transaction transaction = getGraphTemplate().getTransaction();
        assertNotNull(transaction);
    }

    @Test
    public void shouldExecuteQuery() {
        Person person = Person.builder().withAge()
                .withName("Otavio").build();
        getGraphTemplate().insert(person);
        List<Person> people = getGraphTemplate()
                .<Person>query("g.V().hasLabel('Person')")
                .collect(Collectors.toList());
        assertThat(people.stream().map(Person::getName).collect(toList())).contains("Otavio");
    }

    @Test
    public void shouldReturnEmpty() {
        Optional<Person> person = getGraphTemplate().singleResult("g.V().hasLabel('Person')");
        assertFalse(person.isPresent());
    }

    @Test
    public void shouldReturnOneElement() {
        Person otavio = Person.builder().withAge()
                .withName("Otavio").build();
        getGraphTemplate().insert(otavio);
        Optional<Person> person = getGraphTemplate().singleResult("g.V().hasLabel('Person')");
        assertTrue(person.isPresent());
    }

    @Test
    public void shouldReturnErrorWhenHasNoneThanOneElement() {

        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        getGraphTemplate().insert(Person.builder().withAge().withName("Poliana").build());
        assertThrows(NonUniqueResultException.class, () -> getGraphTemplate().singleResult("g.V().hasLabel('Person')"));
    }

    @Test
    public void shouldExecutePrepareStatement() {
        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        PreparedStatement prepare = getGraphTemplate().prepare("g.V().hasLabel(param)");
        prepare.bind("param", "Person");
        List<Person> people = prepare.<Person>getResult().collect(Collectors.toList());
        assertThat(people.stream().map(Person::getName).collect(toList())).contains("Otavio");
    }

    @Test
    public void shouldExecutePrepareStatementSingleton() {
        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        PreparedStatement prepare = getGraphTemplate().prepare("g.V().hasLabel(param)");
        prepare.bind("param", "Person");
        Optional<Person> otavio = prepare.getSingleResult();
        assertTrue(otavio.isPresent());
    }

    @Test
    public void shouldExecutePrepareStatementSingletonEmpty() {
        PreparedStatement prepare = getGraphTemplate().prepare("g.V().hasLabel(param)");
        prepare.bind("param", "Person");
        Optional<Person> otavio = prepare.getSingleResult();
        assertFalse(otavio.isPresent());
    }

    @Test
    public void shouldExecutePrepareStatementWithErrorWhenThereIsMoreThanOneResult() {
        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        getGraphTemplate().insert(Person.builder().withAge().withName("Poliana").build());
        PreparedStatement prepare = getGraphTemplate().prepare("g.V().hasLabel(param)");
        prepare.bind("param", "Person");
        assertThrows(NonUniqueResultException.class, prepare::getSingleResult);
    }

    @Test
    public void shouldCount() {
        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        getGraphTemplate().insert(Person.builder().withAge().withName("Poliana").build());
        assertEquals(2L, getGraphTemplate().count("Person"));
    }

    @Test
    public void shouldCountFromEntity() {
        getGraphTemplate().insert(Person.builder().withAge().withName("Otavio").build());
        getGraphTemplate().insert(Person.builder().withAge().withName("Poliana").build());
        assertEquals(2L, getGraphTemplate().count(Person.class));
    }


    @Test
    public void shouldFindById() {
        final Person otavio = getGraphTemplate().insert(Person.builder().withAge()
                .withName("Otavio").build());

        final Optional<Person> person = getGraphTemplate().find(Person.class, otavio.getId());
        assertNotNull(person);
        assertTrue(person.isPresent());
        assertEquals(otavio.getName(), person.map(Person::getName).get());
    }


    @Test
    public void shouldReturnEmptyWhenFindByIdNotFound() {

        final Optional<Person> person = getGraphTemplate().find(Person.class, -2L);
        assertNotNull(person);
        assertFalse(person.isPresent());
    }
}
