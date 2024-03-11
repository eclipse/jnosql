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
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.graph.entities.Animal;
import org.eclipse.jnosql.mapping.graph.entities.Book;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
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
class DefaultVertexTraversalTest extends AbstractTraversalTest {


    @Test
    void shouldReturnErrorWhenVertexIdIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex(null));
    }

    @Test
    void shouldGetVertexFromId() {
        List<Person> people = graphTemplate.traversalVertex(otavio.getId(), poliana.getId()).<Person>result()
                .collect(toList());

        assertThat(people).contains(otavio, poliana);
    }

    @Test
    void shouldDefineLimit() {
        List<Person> people = graphTemplate.traversalVertex(otavio.getId(), poliana.getId(),
                        paulo.getId()).limit(1)
                .<Person>result()
                .collect(toList());

        assertEquals(1, people.size());
        assertThat(people).contains(otavio);
    }

    @Test
    void shouldDefineLimit2() {
        List<Person> people = graphTemplate.traversalVertex(otavio.getId(), poliana.getId(), paulo.getId()).
                <Person>next(2)
                .collect(toList());

        assertEquals(2, people.size());
        assertThat(people).contains(otavio, poliana);
    }

    @Test
    void shouldNext() {
        Optional<?> next = graphTemplate.traversalVertex().next();
        assertTrue(next.isPresent());
    }

    @Test
    void shouldEmptyNext() {
        Optional<?> next = graphTemplate.traversalVertex(-12).next();
        assertFalse(next.isPresent());
    }


    @Test
    void shouldHave() {
        Optional<Person> person = graphTemplate.traversalVertex().has("name", "Poliana").next();
        assertTrue(person.isPresent());
        assertEquals(person.get(), poliana);
    }

    @Test
    void shouldReturnErrorWhenHasNullKey() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex()
                .has((String) null, "Poliana")
                .next());
    }


    @Test
    void shouldReturnErrorWhenHasNullValue() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().has("name", null)
                .next());
    }

    @Test
    void shouldHaveId() {
        Optional<Person> person = graphTemplate.traversalVertex().has(T.id, poliana.getId()).next();
        assertTrue(person.isPresent());
        assertEquals(person.get(), poliana);
    }

    @Test
    void shouldReturnErrorWhenHasIdHasNullValue() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().has(T.id, null).next());
    }

    @Test
    void shouldReturnErrorWhenHasIdHasNullAccessor() {
        assertThrows(NullPointerException.class, () -> {
            T id = null;
            graphTemplate.traversalVertex().has(id, poliana.getId()).next();
        });
    }


    @Test
    void shouldHavePredicate() {
        List<?> result = graphTemplate.traversalVertex().has("age", P.gt(26))
                .result()
                .toList();
        assertEquals(5, result.size());
    }

    @Test
    void shouldReturnErrorWhenHasPredicateIsNull() {
        assertThrows(NullPointerException.class, () -> {
            P<Integer> gt = null;
            graphTemplate.traversalVertex().has("age", gt)
                    .result()
                    .collect(toList());
        });
    }

    @Test
    void shouldReturnErrorWhenHasKeyIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().has((String) null,
                        P.gt(26))
                .result()
                .collect(toList()));
    }

    @Test
    void shouldHaveLabel() {
        List<Book> books = graphTemplate.traversalVertex().hasLabel("Book").<Book>result().collect(toList());
        assertEquals(3, books.size());
        assertThat(books).contains(shack, license, effectiveJava);
    }

    @Test
    void shouldHaveLabel2() {

        List<Object> entities = graphTemplate.traversalVertex()
                .hasLabel(P.eq("Book").or(P.eq("Person")))
                .result().collect(toList());
        assertThat(entities).hasSize(6).contains(shack, license, effectiveJava, otavio, poliana, paulo);
    }

    @Test
    void shouldReturnErrorWhenHasLabelHasNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().hasLabel((String) null)
                .<Book>result().collect(toList()));
    }

    @Test
    void shouldIn() {
        List<Book> books = graphTemplate.traversalVertex().out(READS).<Book>result().collect(toList());
        assertEquals(3, books.size());
        assertThat(books).contains(shack, license, effectiveJava);
    }

    @Test
    void shouldReturnErrorWhenInIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().out((String) null).<Book>result().collect(toList()));
    }

    @Test
    void shouldOut() {
        List<Person> people = graphTemplate.traversalVertex().in(READS).<Person>result().collect(toList());
        assertEquals(3, people.size());
        assertThat(people).contains(otavio, poliana, paulo);
    }

    @Test
    void shouldReturnErrorWhenOutIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().in((String) null).<Person>result().collect(toList()));
    }

    @Test
    void shouldBoth() {
        List<?> entities = graphTemplate.traversalVertex().both(READS)
                .<Person>result().toList();
        assertEquals(6, entities.size());
    }

    @Test
    void shouldReturnErrorWhenBothIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().both((String) null)
                .<Person>result().collect(toList()));
    }

    @Test
    void shouldNot() {
        List<?> result = graphTemplate.traversalVertex().hasNot("year").result().toList();
        assertEquals(6, result.size());
    }

    @Test
    void shouldReturnErrorWhenHasNotIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().hasNot((String) null)
                .result().collect(toList()));
    }

    @Test
    void shouldCount() {
        long count = graphTemplate.traversalVertex().both(READS).count();
        assertEquals(6L, count);
    }

    @Test
    void shouldReturnZeroWhenCountIsEmpty() {
        long count = graphTemplate.traversalVertex().both("WRITES").count();
        assertEquals(0L, count);
    }

    @Test
    void shouldDefinesLimit() {
        long count = graphTemplate.traversalVertex().limit(1L).count();
        assertEquals(1L, count);
        assertNotEquals(graphTemplate.traversalVertex().count(), count);
    }

    @Test
    void shouldDefinesRange() {
        long count = graphTemplate.traversalVertex().range(1, 3).count();
        assertEquals(2L, count);
        assertNotEquals(graphTemplate.traversalVertex().count(), count);
    }

    @Test
    void shouldMapValuesAsStream() {
        List<Map<String, Object>> maps = graphTemplate.traversalVertex().hasLabel("Person")
                .valueMap("name").stream().toList();

        assertFalse(maps.isEmpty());
        assertEquals(3, maps.size());

        List<String> names = new ArrayList<>();

        maps.forEach(m -> names.add(((List) m.get("name")).get(0).toString()));

        assertThat(names).contains("Otavio", "Poliana", "Paulo");
    }

    @Test
    void shouldMapValuesAsStreamLimit() {
        List<Map<String, Object>> maps = graphTemplate.traversalVertex().hasLabel("Person")
                .valueMap("name").next(2).toList();

        assertFalse(maps.isEmpty());
        assertEquals(2, maps.size());
    }


    @Test
    void shouldReturnMapValueAsEmptyStream() {
        Stream<Map<String, Object>> stream = graphTemplate.traversalVertex().hasLabel("Person")
                .valueMap("noField").stream();
        assertTrue(stream.allMatch(m -> Objects.isNull(m.get("noFoundProperty"))));
    }

    @Test
    void shouldReturnNext() {
        Map<String, Object> map = graphTemplate.traversalVertex().hasLabel("Person")
                .valueMap("name").next();

        assertNotNull(map);
        assertFalse(map.isEmpty());
    }


    @Test
    void shouldRepeatTimesTraversal() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake).add("when", "night");
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);
        Optional<Animal> animal = graphTemplate.traversalVertex().repeat().out("eats").times(3).next();
        assertTrue(animal.isPresent());
        assertEquals(plant, animal.get());

    }

    @Test
    void shouldRepeatTimesTraversal2() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake).add("when", "night");
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);
        Optional<Animal> animal = graphTemplate.traversalVertex().repeat().in("eats").times(3).next();
        assertTrue(animal.isPresent());
        assertEquals(lion, animal.get());

    }

    @Test
    void shouldRepeatUntilTraversal() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake);
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);

        Optional<Animal> animal = graphTemplate.traversalVertex()
                .repeat().out("eats")
                .until().has("name", "plant").next();

        assertTrue(animal.isPresent());


        assertEquals(plant, animal.get());
    }

    @Test
    void shouldRepeatUntilTraversal2() {
        Animal lion = graphTemplate.insert(new Animal("lion"));
        Animal snake = graphTemplate.insert(new Animal("snake"));
        Animal mouse = graphTemplate.insert(new Animal("mouse"));
        Animal plant = graphTemplate.insert(new Animal("plant"));

        graphTemplate.edge(lion, "eats", snake);
        graphTemplate.edge(snake, "eats", mouse);
        graphTemplate.edge(mouse, "eats", plant);

        Optional<Animal> animal = graphTemplate.traversalVertex()
                .repeat().in("eats")
                .until().has("name", "lion").next();

        assertTrue(animal.isPresent());


        assertEquals(lion, animal.get());
    }


    @Test
    void shouldReturnErrorWhenTheOrderIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().orderBy(null));
    }

    @Test
    void shouldReturnErrorWhenThePropertyDoesNotExist() {
        assertThrows(NoSuchElementException.class, () ->
                graphTemplate.traversalVertex().orderBy("wrong property").asc().next().get());
    }

    @Test
    void shouldOrderAsc() {
        String property = "name";

        List<String> properties = graphTemplate.traversalVertex()
                .hasLabel("Book")
                .has(property)
                .orderBy(property)
                .asc().<Book>result()
                .map(Book::getName)
                .collect(toList());

        assertThat(properties).contains("Effective Java", "Software License", "The Shack");
    }

    @Test
    void shouldOrderDesc() {
        String property = "name";

        List<String> properties = graphTemplate.traversalVertex()
                .hasLabel("Book")
                .has(property)
                .orderBy(property)
                .desc().<Book>result()
                .map(Book::getName)
                .collect(toList());

        assertThat(properties).contains("The Shack", "Software License", "Effective Java");
    }

    @Test
    void shouldReturnErrorWhenHasLabelStringNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().hasLabel((String) null));
    }

    @Test
    void shouldReturnErrorWhenHasLabelSupplierNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().hasLabel((Supplier<String>) null));
    }

    @Test
    void shouldReturnErrorWhenHasLabelEntityClassNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().hasLabel((Class<?>) null));
    }

    @Test
    void shouldReturnHasLabel() {
        assertTrue(graphTemplate.traversalVertex().hasLabel("Person").result().allMatch(Person.class::isInstance));
        assertTrue(graphTemplate.traversalVertex().hasLabel(() -> "Book").result().allMatch(Book.class::isInstance));
        assertTrue(graphTemplate.traversalVertex().hasLabel(Animal.class).result().allMatch(Animal.class::isInstance));
    }

    @Test
    void shouldReturnResultAsList() {
        List<Person> people = graphTemplate.traversalVertex().hasLabel("Person")
                .<Person>result()
                .toList();
        assertEquals(3, people.size());
    }

    @Test
    void shouldReturnErrorWhenThereAreMoreThanOneInGetSingleResult() {
        assertThrows(NonUniqueResultException.class, () -> graphTemplate.traversalVertex().hasLabel("Person").singleResult());
    }

    @Test
    void shouldReturnOptionalEmptyWhenThereIsNotResultInSingleResult() {
        Optional<Object> entity = graphTemplate.traversalVertex().hasLabel("NoEntity").singleResult();
        assertFalse(entity.isPresent());
    }

    @Test
    void shouldReturnSingleResult() {
        String name = "Poliana";
        Optional<Person> poliana = graphTemplate.traversalVertex().hasLabel("Person").
                has("name", name).singleResult();
        assertEquals(name, poliana.map(Person::getName).orElse(""));
    }

    @Test
    void shouldReturnErrorWhenPredicateIsNull() {
        assertThrows(NullPointerException.class, () -> graphTemplate.traversalVertex().filter(null));
    }

    @Test
    void shouldPredicate() {
        long count = graphTemplate.traversalVertex()
                .hasLabel(Person.class)
                .filter(Person::isAdult).count();
        assertEquals(3L, count);
    }

    @Test
    void shouldDedup() {

        graphTemplate.edge(otavio, "knows", paulo);
        graphTemplate.edge(paulo, "knows", otavio);
        graphTemplate.edge(otavio, "knows", poliana);
        graphTemplate.edge(poliana, "knows", otavio);
        graphTemplate.edge(poliana, "knows", paulo);
        graphTemplate.edge(paulo, "knows", poliana);

        List<Person> people = graphTemplate.traversalVertex()
                .hasLabel(Person.class)
                .in("knows").<Person>result()
                .collect(Collectors.toList());

        assertEquals(6, people.size());

        people = graphTemplate.traversalVertex()
                .hasLabel(Person.class)
                .in("knows").dedup().<Person>result()
                .collect(Collectors.toList());

        assertEquals(3, people.size());
    }


}