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
import org.eclipse.jnosql.communication.semistructured.Element;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.graph.entities.Book;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableAutoWeld
@AddPackages(value = {Converters.class, EntityConverter.class, GraphTemplate.class})
@AddPackages(GraphProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
class EdgeEntityTest {


    @Inject
    private GraphTemplate graphTemplate;


    @Test
    void shouldReturnErrorWhenInboundIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Person person = Person.builder().withName("Poliana").withAge().build();
            Book book = null;
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    void shouldReturnErrorWhenOutboundIsNull() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            Person person = Person.builder().withName("Poliana").withAge().build();
            Book book = Book.builder().withAge(2007).withName("The Shack").build();
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    void shouldReturnErrorWhenLabelIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Person person = Person.builder().withName("Poliana").withAge().build();
            Book book = Book.builder().withAge(2007).withName("The Shack").build();
            graphTemplate.edge(person, (String) null, book);
        });
    }

    @Test
    void shouldReturnNullWhenInboundIdIsNull() {
        Assertions.assertThrows(EmptyResultException.class, () -> {
            Person person = Person.builder().withId(-5).withName("Poliana").withAge().build();
            Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
            graphTemplate.edge(person, "reads", book);
        });

    }

    @Test
    void shouldReturnNullWhenOutboundIdIsNull() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
            Book book = Book.builder().withAge(2007).withName("The Shack").build();
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    void shouldReturnEntityNotFoundWhenOutBoundDidNotFound() {
        Assertions.assertThrows( EmptyResultException.class, () -> {
            Person person = Person.builder().withId(-10L).withName("Poliana").withAge().build();
            Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    void shouldReturnEntityNotFoundWhenInBoundDidNotFound() {
        Assertions.assertThrows( EmptyResultException.class, () -> {
            Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
            Book book = Book.builder().withId(10L).withAge(2007).withName("The Shack").build();
            graphTemplate.edge(person, "reads", book);
        });
    }

    @Test
    void shouldCreateAnEdge() {
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
    void shouldGetId() {
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
    void shouldCreateAnEdgeWithSupplier() {
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
    void shouldUseAnEdge() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        EdgeEntity sameEdge = graphTemplate.edge(person, "reads", book);

        assertEquals(edge.id(), sameEdge.id());
        assertEquals(edge, sameEdge);
    }

    @Test
    void shouldUseAnEdge2() {
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
    void shouldUseADifferentEdge() {
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
    void shouldReturnErrorWhenAddKeyIsNull() {
        assertThrows(NullPointerException.class, () -> {
            Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
            Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
            EdgeEntity edge = graphTemplate.edge(person, "reads", book);
            edge.add(null, "Brazil");
        });
    }

    @Test
    void shouldReturnErrorWhenAddValueIsNull() {

        assertThrows(NullPointerException.class, () -> {
            Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
            Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
            EdgeEntity edge = graphTemplate.edge(person, "reads", book);
            edge.add("where", null);
        });
    }

    @Test
    void shouldAddProperty() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);
        edge.add("where", "Brazil");

        assertFalse(edge.isEmpty());
        assertEquals(1, edge.size());
        assertThat(edge.properties()).contains(Element.of("where", "Brazil"));
    }

    @Test
    void shouldAddPropertyWithValue() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);
        edge.add("where", Value.of("Brazil"));

        assertFalse(edge.isEmpty());
        assertEquals(1, edge.size());
        assertThat(edge.properties()).contains(Element.of("where", "Brazil"));
    }


    @Test
    void shouldReturnErrorWhenRemoveNullKeyProperty() {
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
    void shouldRemoveProperty() {
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
    void shouldFindProperty() {
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
    void shouldDeleteAnEdge() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);
        edge.delete();

        EdgeEntity newEdge = graphTemplate.edge(person, "reads", book);
        assertNotEquals(edge.id(), newEdge.id());

        graphTemplate.deleteEdge(newEdge.id());
    }

    @Test
    void shouldReturnErrorWhenDeleteAnEdgeWithNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.delete((Iterable<Object>) null));
    }

    @Test
    void shouldDeleteAnEdge2() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());

        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        graphTemplate.deleteEdge(edge.id());

        EdgeEntity newEdge = graphTemplate.edge(person, "reads", book);
        assertNotEquals(edge.id(), newEdge.id());
    }


    @Test
    void shouldReturnErrorWhenFindEdgeWithNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.edge(null));
    }


    @Test
    void shouldFindAnEdge() {
        Person person = graphTemplate.insert(Person.builder().withName("Poliana").withAge().build());
        Book book = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        EdgeEntity edge = graphTemplate.edge(person, "reads", book);

        Optional<EdgeEntity> newEdge = graphTemplate.edge(edge.id());

        assertTrue(newEdge.isPresent());
        assertEquals(edge.id(), newEdge.get().id());

        graphTemplate.deleteEdge(edge.id());
    }

    @Test
    void shouldNotFindAnEdge() {
        Optional<EdgeEntity> edgeEntity = graphTemplate.edge(-12L);

        assertFalse(edgeEntity.isPresent());
    }

}
