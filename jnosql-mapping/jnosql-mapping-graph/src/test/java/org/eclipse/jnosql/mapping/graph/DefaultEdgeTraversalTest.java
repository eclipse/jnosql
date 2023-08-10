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

import jakarta.data.exceptions.NonUniqueResultException;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.T;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.graph.entities.Animal;
import org.eclipse.jnosql.mapping.graph.entities.Book;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddPackages(value = {Converters.class, Transactional.class})
@AddPackages(BookRepository.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
public class DefaultEdgeTraversalTest extends AbstractTraversalTest {

    @Test
    public void shouldReturnErrorWhenEdgeIdIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalEdge(null));
    }

    @Test
    public void shouldReturnEdgeId() {
        Optional<EdgeEntity> edgeEntity = graphTemplate.traversalEdge(reads.id())
                .next();

        assertTrue(edgeEntity.isPresent());
        assertEquals(reads.id(), edgeEntity.get().id());
    }

    @Test
    public void shouldReturnOutE() {
        List<EdgeEntity> edges = graphTemplate.traversalVertex().outE(READS)
                .stream()
                .collect(toList());

        assertEquals(3, edges.size());
        assertThat(edges).contains(reads, reads2, reads3);
    }

    @Test
    public void shouldReturnOutEWithSupplier() {
        List<EdgeEntity> edges = graphTemplate.traversalVertex().outE(() -> READS)
                .stream()
                .collect(toList());

        assertEquals(3, edges.size());
        assertThat(edges).contains(reads, reads2, reads3);
    }

    @Test
    public void shouldReturnErrorOutEWhenIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().outE((String) null)
                .stream()
                .collect(toList()));
    }

    @Test
    public void shouldReturnInE() {
        List<EdgeEntity> edges = graphTemplate.traversalVertex().inE(READS)
                .stream()
                .collect(toList());

        assertEquals(3, edges.size());
        assertThat(edges).contains(reads, reads2, reads3);
    }

    @Test
    public void shouldReturnInEWitSupplier() {
        List<EdgeEntity> edges = graphTemplate.traversalVertex().inE(() -> READS)
                .stream()
                .collect(toList());

        assertEquals(3, edges.size());
        assertThat(edges).contains(reads, reads2, reads3);
    }


    @Test
    public void shouldReturnErrorWhenInEIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().inE((String) null)
                .stream()
                .collect(toList()));

    }

    @Test
    public void shouldReturnBothE() {
        List<EdgeEntity> edges = graphTemplate.traversalVertex().bothE(READS)
                .stream()
                .toList();

        assertEquals(6, edges.size());
    }

    @Test
    public void shouldReturnBothEWithSupplier() {
        List<EdgeEntity> edges = graphTemplate.traversalVertex().bothE(() -> READS)
                .stream()
                .toList();

        assertEquals(6, edges.size());
    }

    @Test
    public void shouldReturnErrorWhenBothEIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().bothE((String) null)
                .stream()
                .collect(toList()));
    }


    @Test
    public void shouldReturnOut() {
        List<Person> people = graphTemplate.traversalVertex().outE(READS).outV().<Person>result().collect(toList());
        assertEquals(3, people.size());
        assertThat(people).contains(poliana, otavio, paulo);
    }

    @Test
    public void shouldReturnIn() {
        List<Book> books = graphTemplate.traversalVertex().outE(READS).inV().<Book>result().collect(toList());
        assertEquals(3, books.size());
        assertThat(books).contains(shack, effectiveJava, license);
    }


    @Test
    public void shouldReturnBoth() {
        List<Object> entities = graphTemplate.traversalVertex().outE(READS).bothV().result().collect(toList());
        assertEquals(6, entities.size());
        assertThat(entities).contains(shack, effectiveJava, license, paulo, otavio, poliana);
    }


    @Test
    public void shouldHasPropertyFromAccessor() {

        Optional<EdgeEntity> edgeEntity = graphTemplate.traversalVertex()
                .outE(READS)
                .has(T.id, "notFound").next();

        assertFalse(edgeEntity.isPresent());
    }


    @Test
    public void shouldHasProperty() {
        Optional<EdgeEntity> edgeEntity = graphTemplate.traversalVertex()
                .outE(READS)
                .has("motivation", "hobby").next();

        assertTrue(edgeEntity.isPresent());
        assertEquals(reads.id(), edgeEntity.get().id());
    }

    @Test
    public void shouldHasSupplierProperty() {
        Optional<EdgeEntity> edgeEntity = graphTemplate.traversalVertex()
                .outE(READS)
                .has(() -> "motivation", "hobby").next();

        assertTrue(edgeEntity.isPresent());
        assertEquals(reads.id(), edgeEntity.get().id());
    }

    @Test
    public void shouldHasPropertyPredicate() {

        Optional<EdgeEntity> edgeEntity = graphTemplate.traversalVertex()
                .outE(READS)
                .has("motivation", P.eq("hobby")).next();

        assertTrue(edgeEntity.isPresent());
        assertEquals(reads.id(), edgeEntity.get().id());
    }


    @Test
    public void shouldHasPropertyKeySupplierPredicate() {

        Optional<EdgeEntity> edgeEntity = graphTemplate.traversalVertex()
                .outE(READS)
                .has(() -> "motivation", P.eq("hobby")).next();

        assertTrue(edgeEntity.isPresent());
        assertEquals(reads.id(), edgeEntity.get().id());
    }


    @Test
    public void shouldReturnErrorWhenHasPropertyWhenKeyIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex()
                .outE(READS)
                .has((String) null, "hobby").next());
    }

    @Test
    public void shouldReturnErrorWhenHasPropertyWhenValueIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex()
                .outE(READS)
                .has("motivation", null).next());
    }

    @Test
    public void shouldHasNot() {
        List<EdgeEntity> edgeEntities = graphTemplate.traversalVertex()
                .outE(READS).hasNot("language")
                .stream()
                .toList();

        assertEquals(2, edgeEntities.size());
    }

    @Test
    public void shouldCount() {
        long count = graphTemplate.traversalVertex().outE(READS).count();
        assertEquals(3L, count);
    }

    @Test
    public void shouldReturnZeroWhenCountIsEmpty() {
        long count = graphTemplate.traversalVertex().outE("WRITES").count();
        assertEquals(0L, count);
    }

    @Test
    public void shouldReturnErrorWhenHasNotIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().outE(READS).hasNot((String) null));
    }


    @Test
    public void shouldDefinesLimit() {
        long count = graphTemplate.traversalEdge().limit(1L).count();
        assertEquals(1L, count);
        assertNotEquals(graphTemplate.traversalEdge().count(), count);
    }

    @Test
    public void shouldDefinesRange() {
        long count = graphTemplate.traversalEdge().range(1, 3).count();
        assertEquals(2L, count);
        assertNotEquals(graphTemplate.traversalEdge().count(), count);
    }

    @Test
    public void shouldMapValuesAsStream() {
        List<Map<String, Object>> maps = graphTemplate.traversalVertex().inE("reads")
                .valueMap("motivation").stream().toList();

        assertFalse(maps.isEmpty());
        assertEquals(3, maps.size());

        List<String> names = new ArrayList<>();

        maps.forEach(m -> names.add(m.get("motivation").toString()));

        assertThat(names).contains("hobby", "love", "job");
    }

    @Test
    public void shouldMapValuesAsStreamLimit() {
        List<Map<String, Object>> maps = graphTemplate.traversalVertex().inE("reads")
                .valueMap("motivation").next(2).toList();

        assertFalse(maps.isEmpty());
        assertEquals(2, maps.size());
    }


    @Test
    public void shouldReturnMapValueAsEmptyStream() {
        Stream<Map<String, Object>> stream = graphTemplate.traversalVertex().inE("reads")
                .valueMap("noFoundProperty").stream();
        assertTrue(stream.allMatch(m -> Objects.isNull(m.get("noFoundProperty"))));
    }

    @Test
    public void shouldReturnNext() {
        Map<String, Object> map = graphTemplate.traversalVertex().inE("reads")
                .valueMap("motivation").next();

        assertNotNull(map);
        assertFalse(map.isEmpty());
    }


    @Test
    public void shouldReturnHas() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake).add("when", "night");
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);


        Optional<EdgeEntity> result = graphTemplate.traversalEdge().has("when").next();
        assertNotNull(result);

        graphTemplate.deleteEdge(lion.getId());
    }

    @Test
    public void shouldRepeatTimesTraversal() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake).add("when", "night");
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);
        Optional<EdgeEntity> result = graphTemplate.traversalEdge().repeat().has("when").times(2).next();
        assertNotNull(result);
        assertEquals(snake, result.get().incoming());
        assertEquals(lion, result.get().outgoing());
    }

    @Test
    public void shouldRepeatUntilTraversal() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake).add("when", "night");
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);

        Optional<EdgeEntity> result = graphTemplate.traversalEdge().repeat().has("when")
                .until().has("when").next();

        assertTrue(result.isPresent());

        assertEquals(snake, result.get().incoming());
        assertEquals(lion, result.get().outgoing());

    }

    @Test
    public void shouldRepeatUntilHasValueTraversal() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake).add("when", "night");
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);

        Optional<EdgeEntity> result = graphTemplate.traversalEdge().repeat().has("when")
                .until().has("when", "night").next();

        assertTrue(result.isPresent());

        assertEquals(snake, result.get().incoming());
        assertEquals(lion, result.get().outgoing());

    }

    @Test
    public void shouldRepeatUntilHasPredicateTraversal() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake).add("when", "night");
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);

        Optional<EdgeEntity> result = graphTemplate.traversalEdge().repeat().has("when")
                .until().has("when", new P<Object>((a, b) -> true, "night")).next();

        assertTrue(result.isPresent());

        assertEquals(snake, result.get().incoming());
        assertEquals(lion, result.get().outgoing());

    }


    @Test
    public void shouldReturnErrorWhenTheOrderIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalEdge().orderBy(null));
    }

    @Test
    public void shouldReturnErrorWhenThePropertyDoesNotExist() {
       assertThrows(NoSuchElementException.class, () ->
               graphTemplate.traversalEdge().orderBy("wrong property").asc().next().get());
    }

    @Test
    public void shouldOrderAsc() {
        String property = "motivation";

        List<String> properties = graphTemplate.traversalEdge()
                .has(property)
                .orderBy(property)
                .asc().stream()
                .map(e -> e.get(property))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(v -> v.get(String.class))
                .collect(toList());

        assertThat(properties).contains("hobby", "job", "love");
    }

    @Test
    public void shouldOrderDesc() {
        String property = "motivation";

        List<String> properties = graphTemplate.traversalEdge()
                .has(property)
                .orderBy(property)
                .desc().stream()
                .map(e -> e.get(property))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(v -> v.get(String.class))
                .collect(toList());

        assertThat(properties).contains("love", "job", "hobby");
    }


    @Test
    public void shouldReturnResultAsList() {
        List<EdgeEntity> entities = graphTemplate.traversalEdge().result()
                .toList();
        assertEquals(3, entities.size());
    }

    @Test
    public void shouldReturnErrorWhenThereAreMoreThanOneInGetSingleResult() {
        assertThrows(NonUniqueResultException.class, () -> graphTemplate.traversalEdge().singleResult());
    }

    @Test
    public void shouldReturnOptionalEmptyWhenThereIsNotResultInSingleResult() {
        Optional<EdgeEntity> entity = graphTemplate.traversalEdge(-1L).singleResult();
        assertFalse(entity.isPresent());
    }

    @Test
    public void shouldReturnSingleResult() {
        String name = "Poliana";
        Optional<EdgeEntity> entity = graphTemplate.traversalEdge(reads.id()).singleResult();
        assertEquals(reads, entity.get());
    }

    @Test
    public void shouldReturnErrorWhenPredicateIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalEdge().filter(null));
    }

    @Test
    public void shouldReturnFromPredicate() {
        long count = graphTemplate.traversalEdge().filter(reads::equals).count();
        assertEquals(1L, count);
    }

    @Test
    public void shouldDedup() {

        graphTemplate.edge(otavio, "knows", paulo);
        graphTemplate.edge(paulo, "knows", otavio);
        graphTemplate.edge(otavio, "knows", poliana);
        graphTemplate.edge(poliana, "knows", otavio);
        graphTemplate.edge(poliana, "knows", paulo);
        graphTemplate.edge(paulo, "knows", poliana);

        List<EdgeEntity> edges = graphTemplate.traversalVertex()
                .hasLabel(Person.class)
                .inE("knows").result()
                .collect(Collectors.toList());

        assertEquals(6, edges.size());

        edges = graphTemplate.traversalVertex()
                .hasLabel(Person.class)
                .inE("knows")
                .dedup()
                .result()
                .collect(Collectors.toList());

        assertEquals(6, edges.size());
    }
}