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

import jakarta.nosql.NonUniqueResultException;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.T;
import org.eclipse.jnosql.artemis.graph.cdi.CDIExtension;
import org.eclipse.jnosql.artemis.graph.model.Animal;
import org.eclipse.jnosql.artemis.graph.model.Book;
import org.eclipse.jnosql.artemis.graph.model.Person;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(CDIExtension.class)
public class DefaultEdgeTraversalTest extends AbstractTraversalTest {


    @Test
    public void shouldReturnErrorWhenEdgeIdIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalEdge(null));

    }


    @Test
    public void shouldReturnEdgeId() {
        Optional<EdgeEntity> edgeEntity = graphTemplate.getTraversalEdge(reads.getId())
                .next();

        assertTrue(edgeEntity.isPresent());
        assertEquals(reads.getId(), edgeEntity.get().getId());
    }

    @Test
    public void shouldReturnOutE() {
        List<EdgeEntity> edges = graphTemplate.getTraversalVertex().outE(READS)
                .stream()
                .collect(toList());

        assertEquals(3, edges.size());
        assertThat(edges, Matchers.containsInAnyOrder(reads, reads2, reads3));
    }

    @Test
    public void shouldReturnOutEWithSupplier() {
        List<EdgeEntity> edges = graphTemplate.getTraversalVertex().outE(() -> READS)
                .stream()
                .collect(toList());

        assertEquals(3, edges.size());
        assertThat(edges, Matchers.containsInAnyOrder(reads, reads2, reads3));
    }

    @Test
    public void shouldReturnErrorOutEWhenIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().outE((String) null)
                .stream()
                .collect(toList()));


    }

    @Test
    public void shouldReturnInE() {
        List<EdgeEntity> edges = graphTemplate.getTraversalVertex().inE(READS)
                .stream()
                .collect(toList());

        assertEquals(3, edges.size());
        assertThat(edges, Matchers.containsInAnyOrder(reads, reads2, reads3));
    }

    @Test
    public void shouldReturnInEWitSupplier() {
        List<EdgeEntity> edges = graphTemplate.getTraversalVertex().inE(() -> READS)
                .stream()
                .collect(toList());

        assertEquals(3, edges.size());
        assertThat(edges, Matchers.containsInAnyOrder(reads, reads2, reads3));
    }


    @Test
    public void shouldReturnErrorWhenInEIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().inE((String) null)
                .stream()
                .collect(toList()));

    }

    @Test
    public void shouldReturnBothE() {
        List<EdgeEntity> edges = graphTemplate.getTraversalVertex().bothE(READS)
                .stream()
                .collect(toList());

        assertEquals(6, edges.size());
    }

    @Test
    public void shouldReturnBothEWithSupplier() {
        List<EdgeEntity> edges = graphTemplate.getTraversalVertex().bothE(() -> READS)
                .stream()
                .collect(toList());

        assertEquals(6, edges.size());
    }

    @Test
    public void shouldReturErrorWhennBothEIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().bothE((String) null)
                .stream()
                .collect(toList()));
    }


    @Test
    public void shouldReturnOut() {
        List<Person> people = graphTemplate.getTraversalVertex().outE(READS).outV().<Person>getResult().collect(toList());
        assertEquals(3, people.size());
        assertThat(people, containsInAnyOrder(poliana, otavio, paulo));
    }

    @Test
    public void shouldReturnIn() {
        List<Book> books = graphTemplate.getTraversalVertex().outE(READS).inV().<Book>getResult().collect(toList());
        assertEquals(3, books.size());
        assertThat(books, containsInAnyOrder(shack, effectiveJava, license));
    }


    @Test
    public void shouldReturnBoth() {
        List<?> entities = graphTemplate.getTraversalVertex().outE(READS).bothV().getResult().collect(toList());
        assertEquals(6, entities.size());
        assertThat(entities, containsInAnyOrder(shack, effectiveJava, license, paulo, otavio, poliana));
    }


    @Test
    public void shouldHasPropertyFromAccessor() {

        Optional<EdgeEntity> edgeEntity = graphTemplate.getTraversalVertex()
                .outE(READS)
                .has(T.id, "notFound").next();

        assertFalse(edgeEntity.isPresent());
    }



    @Test
    public void shouldHasProperty() {
        Optional<EdgeEntity> edgeEntity = graphTemplate.getTraversalVertex()
                .outE(READS)
                .has("motivation", "hobby").next();

        assertTrue(edgeEntity.isPresent());
        assertEquals(reads.getId(), edgeEntity.get().getId());
    }

    @Test
    public void shouldHasSupplierProperty() {
        Optional<EdgeEntity> edgeEntity = graphTemplate.getTraversalVertex()
                .outE(READS)
                .has(() -> "motivation", "hobby").next();

        assertTrue(edgeEntity.isPresent());
        assertEquals(reads.getId(), edgeEntity.get().getId());
    }

    @Test
    public void shouldHasPropertyPredicate() {

        Optional<EdgeEntity> edgeEntity = graphTemplate.getTraversalVertex()
                .outE(READS)
                .has("motivation", P.eq("hobby")).next();

        assertTrue(edgeEntity.isPresent());
        assertEquals(reads.getId(), edgeEntity.get().getId());
    }


    @Test
    public void shouldHasPropertyKeySupplierPredicate() {

        Optional<EdgeEntity> edgeEntity = graphTemplate.getTraversalVertex()
                .outE(READS)
                .has(() -> "motivation", P.eq("hobby")).next();

        assertTrue(edgeEntity.isPresent());
        assertEquals(reads.getId(), edgeEntity.get().getId());
    }


    @Test
    public void shouldReturnErrorWhenHasPropertyWhenKeyIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex()
                .outE(READS)
                .has((String) null, "hobby").next());
    }

    @Test
    public void shouldReturnErrorWhenHasPropertyWhenValueIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex()
                .outE(READS)
                .has("motivation", null).next());
    }

    @Test
    public void shouldHasNot() {
        List<EdgeEntity> edgeEntities = graphTemplate.getTraversalVertex()
                .outE(READS).hasNot("language")
                .stream()
                .collect(toList());

        assertEquals(2, edgeEntities.size());
    }

    @Test
    public void shouldCount() {
        long count = graphTemplate.getTraversalVertex().outE(READS).count();
        assertEquals(3L, count);
    }

    @Test
    public void shouldReturnZeroWhenCountIsEmpty() {
        long count = graphTemplate.getTraversalVertex().outE("WRITES").count();
        assertEquals(0L, count);
    }

    @Test
    public void shouldReturnErrorWhenHasNotIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().outE(READS).hasNot((String) null));
    }


    @Test
    public void shouldDefinesLimit() {
        long count = graphTemplate.getTraversalEdge().limit(1L).count();
        assertEquals(1L, count);
        assertNotEquals(graphTemplate.getTraversalEdge().count(), count);
    }

    @Test
    public void shouldDefinesRange() {
        long count = graphTemplate.getTraversalEdge().range(1, 3).count();
        assertEquals(2L, count);
        assertNotEquals(graphTemplate.getTraversalEdge().count(), count);
    }

    @Test
    public void shouldMapValuesAsStream() {
        List<Map<String, Object>> maps = graphTemplate.getTraversalVertex().inE("reads")
                .valueMap("motivation").stream().collect(toList());

        assertFalse(maps.isEmpty());
        assertEquals(3, maps.size());

        List<String> names = new ArrayList<>();

        maps.forEach(m -> names.add(m.get("motivation").toString()));

        assertThat(names, containsInAnyOrder("hobby", "love", "job"));
    }

    @Test
    public void shouldMapValuesAsStreamLimit() {
        List<Map<String, Object>> maps = graphTemplate.getTraversalVertex().inE("reads")
                .valueMap("motivation").next(2).collect(toList());

        assertFalse(maps.isEmpty());
        assertEquals(2, maps.size());
    }


    @Test
    public void shouldReturnMapValueAsEmptyStream() {
        Stream<Map<String, Object>> stream = graphTemplate.getTraversalVertex().inE("reads")
                .valueMap("noFoundProperty").stream();
        assertTrue(stream.allMatch(Map::isEmpty));
    }

    @Test
    public void shouldReturnNext() {
        Map<String, Object> map = graphTemplate.getTraversalVertex().inE("reads")
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


        Optional<EdgeEntity> result = graphTemplate.getTraversalEdge().has("when").next();
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
        Optional<EdgeEntity> result = graphTemplate.getTraversalEdge().repeat().has("when").times(2).next();
        assertNotNull(result);
        assertEquals(snake, result.get().getIncoming());
        assertEquals(lion, result.get().getOutgoing());
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

        Optional<EdgeEntity> result = graphTemplate.getTraversalEdge().repeat().has("when")
                .until().has("when").next();

        assertTrue(result.isPresent());


        assertEquals(snake, result.get().getIncoming());
        assertEquals(lion, result.get().getOutgoing());

    }


    @Test
    public void shouldReturnErrorWhenTheOrderIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalEdge().orderBy(null));
    }

    @Test
    public void shouldReturnErrorWhenThePropertyDoesNotExist() {
        assertThrows(IllegalStateException.class, () -> graphTemplate.getTraversalEdge().orderBy("wrong property").asc().next());
    }

    @Test
    public void shouldOrderAsc() {
        String property = "motivation";

        List<String> properties = graphTemplate.getTraversalEdge()
                .has(property)
                .orderBy(property)
                .asc().stream()
                .map(e -> e.get(property))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(v -> v.get(String.class))
                .collect(toList());

        assertThat(properties, contains("hobby", "job", "love"));
    }

    @Test
    public void shouldOrderDesc() {
        String property = "motivation";

        List<String> properties = graphTemplate.getTraversalEdge()
                .has(property)
                .orderBy(property)
                .desc().stream()
                .map(e -> e.get(property))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(v -> v.get(String.class))
                .collect(toList());

        assertThat(properties, contains("love", "job", "hobby"));
    }


    @Test
    public void shouldReturnResultAsList() {
        List<EdgeEntity> entities = graphTemplate.getTraversalEdge().getResult()
                .collect(Collectors.toList());
        assertEquals(3, entities.size());
    }

    @Test
    public void shouldReturnErrorWhenThereAreMoreThanOneInGetSingleResult() {
        assertThrows(NonUniqueResultException.class, () -> graphTemplate.getTraversalEdge().getSingleResult());
    }

    @Test
    public void shouldReturnOptionalEmptyWhenThereIsNotResultInSingleResult() {
        Optional<EdgeEntity> entity = graphTemplate.getTraversalEdge(-1L).getSingleResult();
        assertFalse(entity.isPresent());
    }

    @Test
    public void shouldReturnSingleResult() {
        String name = "Poliana";
        Optional<EdgeEntity> entity = graphTemplate.getTraversalEdge(reads.getId()).getSingleResult();
        assertEquals(reads, entity.get());
    }

    @Test
    public void shouldReturnErrorWhenPredicateIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalEdge().filter(null));
    }

    @Test
    public void shouldReturnFromPredicate() {
        long count = graphTemplate.getTraversalEdge().filter(reads::equals).count();
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

        List<EdgeEntity> edges = graphTemplate.getTraversalVertex()
                .hasLabel(Person.class)
                .inE("knows").getResult()
                .collect(Collectors.toList());

        assertEquals(6, edges.size());

        edges = graphTemplate.getTraversalVertex()
                .hasLabel(Person.class)
                .inE("knows")
                .dedup()
                .getResult()
                .collect(Collectors.toList());

        assertEquals(6, edges.size());
    }

}