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
package org.jnosql.artemis.graph;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.jnosql.artemis.graph.model.Job;
import org.jnosql.artemis.graph.model.Money;
import org.jnosql.artemis.graph.model.Movie;
import org.jnosql.artemis.graph.model.Person;
import org.jnosql.artemis.graph.model.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractGraphConverterTest {

    protected abstract Graph getGraph();

    protected abstract GraphConverter getConverter();

    @BeforeEach
    public void setUp() {
        getGraph().traversal().V().toList().forEach(Vertex::remove);
        getGraph().traversal().E().toList().forEach(Edge::remove);
    }

    @Test
    public void shouldReturnErrorWhenToEntityHasNullParameter() {
        assertThrows(NullPointerException.class, () -> getConverter().toEntity(null));
    }

    @Test
    public void shouldReturnToEntity() {
        Vertex vertex = getGraph().addVertex(T.label, "Person", "age", 22, "name", "Ada");
        Person person = getConverter().toEntity(vertex);

        assertNotNull(person.getId());
        assertEquals("Ada", person.getName());
        assertEquals(Integer.valueOf(22), Integer.valueOf(person.getAge()));
    }

    @Test
    public void shouldReturnToEntityInstance() {
        Vertex vertex = getGraph().addVertex(T.label, "Person", "age", 22, "name", "Ada");
        Person person = Person.builder().build();
        Person result = getConverter().toEntity(person, vertex);

        assertSame(person, result);
        assertNotNull(person.getId());
        assertEquals("Ada", person.getName());
        assertEquals(Integer.valueOf(22), Integer.valueOf(person.getAge()));
    }

    @Test
    public void shouldReturnToEntityWithDifferentMap() {
        Vertex vertex = getGraph().addVertex(T.label, "movie", "title", "Matrix", "movie_year", "1999");
        Movie movie = getConverter().toEntity(vertex);

        assertEquals("Matrix", movie.getTitle());
        assertEquals(1999, movie.getYear());
    }

    @Test
    public void shouldReturnToEntityUsingConverter() {
        Vertex vertex = getGraph().addVertex(T.label, "Worker", "name", "James", "money", "USD 1000");
        Worker worker = getConverter().toEntity(vertex);

        assertEquals("James", worker.getName());
        assertEquals("USD", worker.getSalary().getCurrency());
        assertEquals(0, BigDecimal.valueOf(1_000).compareTo(worker.getSalary().getValue()));
    }

    @Test
    public void shouldConverterFromEmbeddable() {
        Job job = new Job();
        job.setCity("Salvador");
        job.setDescription("Java Developer");

        Worker worker = new Worker();
        worker.setName("name");
        worker.setJob(job);
        worker.setSalary(new Money("BRL", BigDecimal.TEN));
        Vertex vertex = getConverter().toVertex(worker);

        assertEquals(job.getDescription(), vertex.value("description"));
        assertEquals(job.getCity(), vertex.value("city"));
        assertEquals(worker.getName(), vertex.value("name"));
        assertEquals("BRL 10", vertex.value("money"));
    }


    @Test
    public void shouldReturnErrorWhenToVertexHasNullParameter() {
        assertThrows(NullPointerException.class, () -> getConverter().toVertex(null));
    }


    @Test
    public void shouldConvertEntityToTinkerPopVertex() {
        Person person = Person.builder().withName("Ada").withAge(22).build();
        Vertex vertex = getConverter().toVertex(person);

        assertEquals("Person", vertex.label());
        assertEquals("Ada", vertex.value("name"));
        assertEquals(Integer.valueOf(22), vertex.value("age"));
    }

    @Test
    public void shouldConvertEntityToTinkerPopVertexUsingNativeName() {
        Movie movie = new Movie("Matrix", 1999, null);
        Vertex vertex = getConverter().toVertex(movie);

        assertEquals("movie", vertex.label());
        assertEquals(1999, Number.class.cast(vertex.value("movie_year")).intValue());
        assertEquals("Matrix", vertex.value("title"));
    }


    @Test
    public void shouldConvertEntityToTinkerPopVertexUsingConverter() {
        Worker worker = new Worker();
        worker.setName("Alexandre");
        worker.setSalary(new Money("BRL", BigDecimal.valueOf(1_000L)));

        Vertex vertex = getConverter().toVertex(worker);
        assertEquals("Worker", vertex.label());
        assertEquals("BRL 1000", vertex.value("money"));
        assertEquals("Alexandre", vertex.value("name"));
    }

    @Test
    public void shouldConvertEntityWithIdExistToTinkerPopVertex() {
        Vertex adaVertex = getGraph().addVertex(T.label, "Person", "age", 22, "name", "Ada");
        Person person = Person.builder().withName("Ada").withAge(22).withId((Long) adaVertex.id()).build();
        Vertex vertex = getConverter().toVertex(person);

        assertEquals(vertex.id(), adaVertex.id());
        assertEquals("Person", vertex.label());
        assertEquals("Ada", vertex.value("name"));
        assertEquals(Integer.valueOf(22), vertex.value("age"));
    }

    @Test
    public void shouldConvertEntityWithIdDoesNotExistToTinkerPopVertex() {

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            Person person = Person.builder().withName("Ada").withAge(22).withId(10L).build();
            Vertex vertex = getConverter().toVertex(person);
        });

        assertEquals("Vertex does not support user supplied identifiers", exception.getMessage());

    }

    @Test
    public void shouldReturnErrorWhenToEdgeEntityIsNull() {
        assertThrows(NullPointerException.class, () -> getConverter().toEdgeEntity(null));
    }


    @Test
    public void shouldToEdgeEntity() {
        Vertex matrixVertex = getGraph().addVertex(T.label, "movie", "title", "Matrix", "movie_year", "1999");
        Vertex adaVertex = getGraph().addVertex(T.label, "Person", "age", 22, "name", "Ada");
        Edge edge = adaVertex.addEdge("watch", matrixVertex);
        edge.property("feel", "like");

        EdgeEntity edgeEntity = getConverter().toEdgeEntity(edge);
        Person ada = edgeEntity.getOutgoing();
        Movie matrix = edgeEntity.getIncoming();

        assertNotNull(edgeEntity);
        assertEquals("watch", edgeEntity.getLabel());
        assertNotNull(edgeEntity.getId());
        assertEquals(edge.id(), edgeEntity.getId().get());

        assertEquals("Ada", ada.getName());
        assertEquals(22, ada.getAge());

        assertEquals("Matrix", matrix.getTitle());
        assertEquals(1999L, matrix.getYear());
    }

    @Test
    public void shouldReturnToEdgeErrorWhenIsNull() {
        assertThrows(NullPointerException.class, () -> getConverter().toEdge(null));
    }

    @Test
    public void shouldReturnToEdge() {
        Vertex matrixVertex = getGraph().addVertex(T.label, "movie", "title", "Matrix", "movie_year", "1999");
        Vertex adaVertex = getGraph().addVertex(T.label, "Person", "age", 22, "name", "Ada");
        Edge edge = adaVertex.addEdge("watch", matrixVertex);

        EdgeEntity edgeEntity = getConverter().toEdgeEntity(edge);
        Edge edge1 = getConverter().toEdge(edgeEntity);

        assertEquals(edge.id(), edge1.id());
    }

    @Test
    public void shouldReturnErrorWhenGetPropertiesIsNull() {
        assertThrows(NullPointerException.class, () -> getConverter().getProperties(null));
    }

    @Test
    public void shouldGetProperties() {
        Job job = new Job();
        job.setCity("Salvador");
        job.setDescription("Java Developer");

        Worker worker = new Worker();
        worker.setName("name");
        worker.setJob(job);
        worker.setSalary(new Money("BRL", BigDecimal.TEN));

        List<Property<?>> properties = getConverter().getProperties(worker)
                .stream()
                .sorted(comparing(Property::key))
                .collect(Collectors.toList());
        assertEquals(4, properties.size());

        assertAll(() -> {
                    Property<?> property = properties.get(0);
                    assertEquals("city", property.key());
                    assertEquals("Salvador", property.value());
                }, () -> {
                    Property<?> property = properties.get(1);
                    assertEquals("description", property.key());
                    assertEquals("Java Developer", property.value());
                },
                () -> {
                    Property<?> property = properties.get(2);
                    assertEquals("money", property.key());
                    assertEquals("BRL 10", property.value());
                }, () -> {
                    Property<?> property = properties.get(3);
                    assertEquals("name", property.key());
                    assertEquals("name", property.value());
                });
    }
}
