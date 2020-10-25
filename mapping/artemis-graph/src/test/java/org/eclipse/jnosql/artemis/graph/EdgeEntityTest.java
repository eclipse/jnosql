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
package org.eclipse.jnosql.artemis.graph;

import jakarta.nosql.Value;
import jakarta.nosql.mapping.EntityNotFoundException;
import org.eclipse.jnosql.artemis.graph.model.Book;
import org.eclipse.jnosql.artemis.graph.model.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CDIExtension
public class EdgeEntityTest {


    @Inject
    private GraphTemplate graphTemplate;


    @Test
    public void shouldReturnErrorWhenInboundIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Person person = Person.builder().withName("Poliana").withAge().build();
            Book book = null;
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    public void shouldReturnErrorWhenOutboundIsNull() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            Person person = Person.builder().withName("Poliana").withAge().build();
            Book book = Book.builder().withAge(2007).withName("The Shack").build();
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    public void shouldReturnErrorWhenLabelIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Person person = Person.builder().withName("Poliana").withAge().build();
            Book book = Book.builder().withAge(2007).withName("The Shack").build();
            graphTemplate.edge(person, (String) null, book);
        });
    }

    @Test
    public void shouldReturnNullWhenInboundIdIsNull() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            Person person = Person.builder().withName("Poliana").withAge().build();
            Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
            graphTemplate.edge(person, "reads", book);
        });

    }

    @Test
    public void shouldReturnNullWhenOutboundIdIsNull() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
            Book book = Book.builder().withAge(2007).withName("The Shack").build();
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    public void shouldReturnEntityNotFoundWhenOutBoundDidNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            Person person = Person.builder().withId(-10L).withName("Poliana").withAge().build();
            Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    public void shouldReturnEntityNotFoundWhenInBoundDidNotFound() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
            Book book = Book.builder().withId(10L).withAge(2007).withName("The Shack").build();
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    public void shouldCreateAnEdge() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        assertEquals("reads", edge.getLabel());
        assertEquals(person, edge.getOutgoing());
        assertEquals(book, edge.getIncoming());
        assertTrue(edge.isEmpty());
        assertNotNull(edge.getId());
    }

    @Test
    public void shouldGetId() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        assertEquals("reads", edge.getLabel());
        assertEquals(person, edge.getOutgoing());
        assertEquals(book, edge.getIncoming());
        assertTrue(edge.isEmpty());
        assertNotNull(edge.getId());
        final Long id = edge.getId(Long.class);
        assertNotNull(id);

        assertEquals(id, edge.getId(Integer.class).longValue());

    }

    @Test
    public void shouldCreateAnEdgeWithSupplier() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, () -> "reads", book);

        assertEquals("reads", edge.getLabel());
        assertEquals(person, edge.getOutgoing());
        assertEquals(book, edge.getIncoming());
        assertTrue(edge.isEmpty());
        assertNotNull(edge.getId());
    }

    @Test
    public void shouldUseAnEdge() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        EdgeEntity sameEdge = graphTemplate.edge(person, "reads", book);

        assertEquals(edge.getId(), sameEdge.getId());
        assertEquals(edge, sameEdge);
    }

    @Test
    public void shouldUseAnEdge2() {
        Person poliana = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Person nilzete = graphTemplate.insert(Person.builder().withName("Nilzete").withAge().build());

        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(poliana, "reads", book);
        EdgeEntity edge1 = graphTemplate.edge(nilzete, "reads", book);

        EdgeEntity sameEdge = graphTemplate.edge(poliana, "reads", book);
        EdgeEntity sameEdge1 = graphTemplate.edge(nilzete, "reads", book);

        assertEquals(edge.getId(), sameEdge.getId());
        assertEquals(edge, sameEdge);

        assertEquals(edge1.getId(), sameEdge1.getId());
        assertEquals(edge1, sameEdge1);

    }

    @Test
    public void shouldUseADifferentEdge() {
        Person poliana = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Person nilzete = graphTemplate.insert(Person.builder().withName("Nilzete").withAge().build());

        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(poliana, "reads", book);
        EdgeEntity edge1 = graphTemplate.edge(nilzete, "reads", book);

        EdgeEntity sameEdge = graphTemplate.edge(poliana, "reads", book);
        EdgeEntity sameEdge1 = graphTemplate.edge(nilzete, "reads", book);

        assertNotEquals(edge.getId(), edge1.getId());
        assertNotEquals(edge.getId(), sameEdge1.getId());

        assertNotEquals(sameEdge1.getId(), sameEdge.getId());
    }

    @Test
    public void shouldReturnErrorWhenAddKeyIsNull() {
        assertThrows(NullPointerException.class, () -> {
            Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
            Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
            EdgeEntity edge = graphTemplate.edge(person, "reads", book);
            edge.add(null, "Brazil");
        });
    }

    @Test
    public void shouldReturnErrorWhenAddValueIsNull() {

        assertThrows(NullPointerException.class, () -> {
            Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
            Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
            EdgeEntity edge = graphTemplate.edge(person, "reads", book);
            edge.add("where", null);
        });
    }

    @Test
    public void shouldAddProperty() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);
        edge.add("where", "Brazil");

        assertFalse(edge.isEmpty());
        assertEquals(1, edge.size());
        assertThat(edge.getProperties(), Matchers.contains(DefaultProperty.of("where", "Brazil")));
    }

    @Test
    public void shouldAddPropertyWithValue() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);
        edge.add("where", Value.of("Brazil"));

        assertFalse(edge.isEmpty());
        assertEquals(1, edge.size());
        assertThat(edge.getProperties(), Matchers.contains(DefaultProperty.of("where", "Brazil")));
    }


    @Test
    public void shouldReturnErrorWhenRemoveNullKeyProperty() {
        assertThrows(NullPointerException.class, () -> {
            Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
            Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
            EdgeEntity edge = graphTemplate.edge(person, "reads", book);
            edge.add("where", "Brazil");


            assertFalse(edge.isEmpty());
            edge.remove(null);
        });
    }

    @Test
    public void shouldRemoveProperty() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);
        edge.add("where", "Brazil");
        assertEquals(1, edge.size());
        assertFalse(edge.isEmpty());
        edge.remove("where");
        assertTrue(edge.isEmpty());
        assertEquals(0, edge.size());
    }

    @Test
    public void shouldFindProperty() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);
        edge.add("where", "Brazil");

        Optional<Value> where = edge.get("where");
        assertTrue(where.isPresent());
        assertEquals("Brazil", where.get().get());
        assertFalse(edge.get("not").isPresent());

    }

    @Test
    public void shouldDeleteAnEdge() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);
        edge.delete();

        EdgeEntity newEdge = graphTemplate.edge(person, "reads", book);
        assertNotEquals(edge.getId(), newEdge.getId());

        graphTemplate.deleteEdge(newEdge.getId());
    }

    @Test
    public void shouldReturnErrorWhenDeleteAnEdgeWithNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.delete(null));
    }

    @Test
    public void shouldDeleteAnEdge2() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());

        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        graphTemplate.deleteEdge(edge.getId());

        EdgeEntity newEdge = graphTemplate.edge(person, "reads", book);
        assertNotEquals(edge.getId(), newEdge.getId());
    }


    @Test
    public void shouldReturnErrorWhenFindEdgeWithNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.edge(null));
    }


    @Test
    public void shouldFindAnEdge() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        Optional<EdgeEntity> newEdge = graphTemplate.edge(edge.getId());

        assertTrue(newEdge.isPresent());
        assertEquals(edge.getId(), newEdge.get().getId());

        graphTemplate.deleteEdge(edge.getId());
    }

    @Test
    public void shouldNotFindAnEdge() {
        Optional<EdgeEntity> edgeEntity = graphTemplate.edge(-12L);

        assertFalse(edgeEntity.isPresent());
    }

}
