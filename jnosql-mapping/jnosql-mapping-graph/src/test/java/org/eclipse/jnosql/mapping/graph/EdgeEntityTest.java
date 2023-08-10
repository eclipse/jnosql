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
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.graph.entities.Book;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddPackages(value = {Converters.class, Transactional.class})
@AddPackages(BookRepository.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
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
        Assertions.assertThrows( EmptyResultException.class, () -> {
            Person person = Person.builder().withId(-10L).withName("Poliana").withAge().build();
            Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    public void shouldReturnEntityNotFoundWhenInBoundDidNotFound() {
        Assertions.assertThrows( EmptyResultException.class, () -> {
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

        assertEquals("reads", edge.label());
        assertEquals(person, edge.outgoing());
        assertEquals(book, edge.incoming());
        assertTrue(edge.isEmpty());
        assertNotNull(edge.id());
    }

    @Test
    public void shouldGetId() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        assertEquals("reads", edge.label());
        assertEquals(person, edge.outgoing());
        assertEquals(book, edge.incoming());
        assertTrue(edge.isEmpty());
        assertNotNull(edge.id());
        final Long id = edge.id(Long.class);
        assertNotNull(id);

        assertEquals(id, edge.id(Integer.class).longValue());

    }

    @Test
    public void shouldCreateAnEdgeWithSupplier() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, () -> "reads", book);

        assertEquals("reads", edge.label());
        assertEquals(person, edge.outgoing());
        assertEquals(book, edge.incoming());
        assertTrue(edge.isEmpty());
        assertNotNull(edge.id());
    }

    @Test
    public void shouldUseAnEdge() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        EdgeEntity sameEdge = graphTemplate.edge(person, "reads", book);

        assertEquals(edge.id(), sameEdge.id());
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

        assertEquals(edge.id(), sameEdge.id());
        assertEquals(edge, sameEdge);

        assertEquals(edge1.id(), sameEdge1.id());
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

        assertNotEquals(edge.id(), edge1.id());
        assertNotEquals(edge.id(), sameEdge1.id());

        assertNotEquals(sameEdge1.id(), sameEdge.id());
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
        assertThat(edge.properties()).contains(DefaultProperty.of("where", "Brazil"));
    }

    @Test
    public void shouldAddPropertyWithValue() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);
        edge.add("where", Value.of("Brazil"));

        assertFalse(edge.isEmpty());
        assertEquals(1, edge.size());
        assertThat(edge.properties()).contains(DefaultProperty.of("where", "Brazil"));
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
        assertNotEquals(edge.id(), newEdge.id());

        graphTemplate.deleteEdge(newEdge.id());
    }

    @Test
    public void shouldReturnErrorWhenDeleteAnEdgeWithNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.deleteById((Iterable<Object>) null));
    }

    @Test
    public void shouldDeleteAnEdge2() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());

        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        graphTemplate.deleteEdge(edge.id());

        EdgeEntity newEdge = graphTemplate.edge(person, "reads", book);
        assertNotEquals(edge.id(), newEdge.id());
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

        Optional<EdgeEntity> newEdge = graphTemplate.edge(edge.id());

        assertTrue(newEdge.isPresent());
        assertEquals(edge.id(), newEdge.get().id());

        graphTemplate.deleteEdge(edge.id());
    }

    @Test
    public void shouldNotFindAnEdge() {
        Optional<EdgeEntity> edgeEntity = graphTemplate.edge(-12L);

        assertFalse(edgeEntity.isPresent());
    }

}
