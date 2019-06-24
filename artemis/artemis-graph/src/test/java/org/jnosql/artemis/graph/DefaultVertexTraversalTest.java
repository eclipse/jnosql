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

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.T;
import org.jnosql.artemis.graph.cdi.CDIExtension;
import org.jnosql.artemis.graph.model.Animal;
import org.jnosql.artemis.graph.model.Book;
import org.jnosql.artemis.graph.model.Person;
import jakarta.nosql.NonUniqueResultException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
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
public class DefaultVertexTraversalTest extends AbstractTraversalTest {


    @Test
    public void shouldReturnErrorWhenVertexIdIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex(null));
    }

    @Test
    public void shouldGetVertexFromId() {
        List<Person> people = graphTemplate.getTraversalVertex(otavio.getId(), poliana.getId()).<Person>stream()
                .collect(toList());

        assertThat(people, containsInAnyOrder(otavio, poliana));
    }

    @Test
    public void shouldDefineLimit() {
        List<Person> people = graphTemplate.getTraversalVertex(otavio.getId(), poliana.getId(), paulo.getId()).limit(1).<Person>stream()
                .collect(toList());

        assertEquals(1, people.size());
        assertThat(people, containsInAnyOrder(otavio));
    }

    @Test
    public void shouldDefineLimit2() {
        List<Person> people = graphTemplate.getTraversalVertex(otavio.getId(), poliana.getId(), paulo.getId()).
                <Person>next(2)
                .collect(toList());

        assertEquals(2, people.size());
        assertThat(people, containsInAnyOrder(otavio, poliana));
    }

    @Test
    public void shouldNext() {
        Optional<?> next = graphTemplate.getTraversalVertex().next();
        assertTrue(next.isPresent());
    }

    @Test
    public void shouldEmptyNext() {
        Optional<?> next = graphTemplate.getTraversalVertex(-12).next();
        assertFalse(next.isPresent());
    }


    @Test
    public void shouldHave() {
        Optional<Person> person = graphTemplate.getTraversalVertex().has("name", "Poliana").next();
        assertTrue(person.isPresent());
        assertEquals(person.get(), poliana);
    }

    @Test
    public void shouldReturnErrorWhenHasNullKey() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().has((String) null, "Poliana").next());
    }


    @Test
    public void shouldReturnErrorWhenHasNullValue() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().has("name", null).next());
    }

    @Test
    public void shouldHaveId() {
        Optional<Person> person = graphTemplate.getTraversalVertex().has(T.id, poliana.getId()).next();
        assertTrue(person.isPresent());
        assertEquals(person.get(), poliana);
    }

    @Test
    public void shouldReturnErrorWhenHasIdHasNullValue() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().has(T.id, null).next());
    }

    @Test
    public void shouldReturnErrorWhenHasIdHasNullAccessor() {
        assertThrows(NullPointerException.class, () -> {
            T id = null;
            graphTemplate.getTraversalVertex().has(id, poliana.getId()).next();
        });
    }


    @Test
    public void shouldHavePredicate() {
        List<?> result = graphTemplate.getTraversalVertex().has("age", P.gt(26))
                .stream()
                .collect(toList());
        assertEquals(5, result.size());
    }

    @Test
    public void shouldReturnErrorWhenHasPredicateIsNull() {
        assertThrows(NullPointerException.class, () -> {
            P<Integer> gt = null;
            graphTemplate.getTraversalVertex().has("age", gt)
                    .stream()
                    .collect(toList());
        });
    }

    @Test
    public void shouldReturnErrorWhenHasKeyIsNull() {
        assertThrows(NullPointerException.class, () -> {
            graphTemplate.getTraversalVertex().has((String) null, P.gt(26))
                    .stream()
                    .collect(toList());
        });
    }

    @Test
    public void shouldHaveLabel() {
        List<Book> books = graphTemplate.getTraversalVertex().hasLabel("Book").<Book>stream().collect(toList());
        assertEquals(3, books.size());
        assertThat(books, containsInAnyOrder(shack, license, effectiveJava));
    }

    @Test
    public void shouldHaveLabel2() {

        List<Book> books = graphTemplate.getTraversalVertex()
                .hasLabel(P.eq("Book").or(P.eq("Person")))
                .<Book>stream().collect(toList());
        assertEquals(6, books.size());
        assertThat(books, containsInAnyOrder(shack, license, effectiveJava, otavio, poliana, paulo));
    }

    @Test
    public void shouldReturnErrorWhenHasLabelHasNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().hasLabel((String) null).<Book>stream().collect(toList()));
    }

    @Test
    public void shouldIn() {
        List<Book> books = graphTemplate.getTraversalVertex().out(READS).<Book>stream().collect(toList());
        assertEquals(3, books.size());
        assertThat(books, containsInAnyOrder(shack, license, effectiveJava));
    }

    @Test
    public void shouldReturnErrorWhenInIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().out((String) null).<Book>stream().collect(toList()));
    }

    @Test
    public void shouldOut() {
        List<Person> people = graphTemplate.getTraversalVertex().in(READS).<Person>stream().collect(toList());
        assertEquals(3, people.size());
        assertThat(people, containsInAnyOrder(otavio, poliana, paulo));
    }

    @Test
    public void shouldReturnErrorWhenOutIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().in((String) null).<Person>stream().collect(toList()));
    }

    @Test
    public void shouldBoth() {
        List<?> entities = graphTemplate.getTraversalVertex().both(READS).<Person>stream().collect(toList());
        assertEquals(6, entities.size());
    }

    @Test
    public void shouldReturnErrorWhenBothIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().both((String) null).<Person>stream().collect(toList()));
    }

    @Test
    public void shouldNot() {
        List<?> result = graphTemplate.getTraversalVertex().hasNot("year").stream().collect(toList());
        assertEquals(6, result.size());
    }

    @Test
    public void shouldReturnErrorWhenHasNotIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().hasNot((String) null).stream().collect(toList()));
    }

    @Test
    public void shouldCount() {
        long count = graphTemplate.getTraversalVertex().both(READS).count();
        assertEquals(6L, count);
    }

    @Test
    public void shouldReturnZeroWhenCountIsEmpty() {
        long count = graphTemplate.getTraversalVertex().both("WRITES").count();
        assertEquals(0L, count);
    }

    @Test
    public void shouldDefinesLimit() {
        long count = graphTemplate.getTraversalVertex().limit(1L).count();
        assertEquals(1L, count);
        assertNotEquals(graphTemplate.getTraversalVertex().count(), count);
    }

    @Test
    public void shouldDefinesRange() {
        long count = graphTemplate.getTraversalVertex().range(1, 3).count();
        assertEquals(2L, count);
        assertNotEquals(graphTemplate.getTraversalVertex().count(), count);
    }

    @Test
    public void shouldMapValuesAsStream() {
        List<Map<String, Object>> maps = graphTemplate.getTraversalVertex().hasLabel("Person")
                .valueMap("name").stream().collect(toList());

        assertFalse(maps.isEmpty());
        assertEquals(3, maps.size());

        List<String> names = new ArrayList<>();

        maps.forEach(m -> names.add(((List) m.get("name")).get(0).toString()));

        assertThat(names, containsInAnyOrder("Otavio", "Poliana", "Paulo"));
    }

    @Test
    public void shouldMapValuesAsStreamLimit() {
        List<Map<String, Object>> maps = graphTemplate.getTraversalVertex().hasLabel("Person")
                .valueMap("name").next(2).collect(toList());

        assertFalse(maps.isEmpty());
        assertEquals(2, maps.size());
    }


    @Test
    public void shouldReturnMapValueAsEmptyStream() {
        Stream<Map<String, Object>> stream = graphTemplate.getTraversalVertex().hasLabel("Person")
                .valueMap("noField").stream();
        assertTrue(stream.allMatch(Map::isEmpty));
    }

    @Test
    public void shouldReturnNext() {
        Map<String, Object> map = graphTemplate.getTraversalVertex().hasLabel("Person")
                .valueMap("name").next();

        assertNotNull(map);
        assertFalse(map.isEmpty());
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
        Optional<Animal> animal = graphTemplate.getTraversalVertex().repeat().out("eats").times(3).next();
        assertTrue(animal.isPresent());
        assertEquals(plant, animal.get());

    }

    @Test
    public void shouldRepeatTimesTraversal2() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake).add("when", "night");
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);
        Optional<Animal> animal = graphTemplate.getTraversalVertex().repeat().in("eats").times(3).next();
        assertTrue(animal.isPresent());
        assertEquals(lion, animal.get());

    }

    @Test
    public void shouldRepeatUntilTraversal() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake);
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);

        Optional<Animal> animal = graphTemplate.getTraversalVertex()
                .repeat().out("eats")
                .until().has("name", "plant").next();

        assertTrue(animal.isPresent());


        assertEquals(plant, animal.get());
    }

    @Test
    public void shouldRepeatUntilTraversal2() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake);
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);

        Optional<Animal> animal = graphTemplate.getTraversalVertex()
                .repeat().in("eats")
                .until().has("name", "lion").next();

        assertTrue(animal.isPresent());


        assertEquals(lion, animal.get());
    }


    @Test
    public void shouldReturnErrorWhenTheOrderIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().orderBy(null));
    }

    @Test
    public void shouldReturnErrorWhenThePropertyDoesNotExist() {
        assertThrows(IllegalStateException.class, () -> graphTemplate.getTraversalVertex().orderBy("wrong property").asc().next());
    }

    @Test
    public void shouldOrderAsc() {
        String property = "name";

        List<String> properties = graphTemplate.getTraversalVertex()
                .hasLabel("Book")
                .has(property)
                .orderBy(property)
                .asc().<Book>stream()
                .map(Book::getName)
                .collect(toList());

        assertThat(properties, contains("Effective Java", "Software License", "The Shack"));
    }

    @Test
    public void shouldOrderDesc() {
        String property = "name";

        List<String> properties = graphTemplate.getTraversalVertex()
                .hasLabel("Book")
                .has(property)
                .orderBy(property)
                .desc().<Book>stream()
                .map(Book::getName)
                .collect(toList());

        assertThat(properties, contains("The Shack", "Software License", "Effective Java"));
    }

    @Test
    public void shouldReturnErrorWhenHasLabelStringNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().hasLabel((String) null));
    }

    @Test
    public void shouldReturnErrorWhenHasLabelSupplierNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().hasLabel((Supplier<String>) null));
    }

    @Test
    public void shouldReturnErrorWhenHasLabelEntityClassNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().hasLabel((Class<?>) null));
    }

    @Test
    public void shouldReturnHasLabel() {
        assertTrue(graphTemplate.getTraversalVertex().hasLabel("Person").stream().allMatch(Person.class::isInstance));
        assertTrue(graphTemplate.getTraversalVertex().hasLabel(() -> "Book").stream().allMatch(Book.class::isInstance));
        assertTrue(graphTemplate.getTraversalVertex().hasLabel(Animal.class).stream().allMatch(Animal.class::isInstance));
    }

    @Test
    public void shouldReturnResultAsList() {
        List<Person> people = graphTemplate.getTraversalVertex().hasLabel("Person").getResultList();
        assertEquals(3, people.size());
    }

    @Test
    public void shouldReturnErrorWhenThereAreMoreThanOneInGetSingleResult() {
        assertThrows(NonUniqueResultException.class, () -> graphTemplate.getTraversalVertex().hasLabel("Person").getSingleResult());
    }

    @Test
    public void shouldReturnOptionalEmptyWhenThereIsNotResultInSingleResult() {
        Optional<Object> entity = graphTemplate.getTraversalVertex().hasLabel("NoEntity").getSingleResult();
        assertFalse(entity.isPresent());
    }

    @Test
    public void shouldReturnSingleResult() {
        String name = "Poliana";
        Optional<Person> poliana = graphTemplate.getTraversalVertex().hasLabel("Person").
                has("name", name).getSingleResult();
        assertEquals(name, poliana.map(Person::getName).orElse(""));
    }

    @Test
    public void shouldReturnErrorWhenPredicateIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.getTraversalVertex().filter(null));
    }

    @Test
    public void shouldPredicate() {
        long count = graphTemplate.getTraversalVertex()
                .hasLabel(Person.class)
                .filter(Person::isAdult).count();
        assertEquals(3L, count);
    }
}