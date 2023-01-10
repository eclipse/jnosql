/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.inject.Inject;
import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.tck.test.CDIExtension;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.graph.entities.Job;
import org.eclipse.jnosql.mapping.graph.entities.Money;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.eclipse.jnosql.mapping.graph.entities.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CDIExtension
public class MapperSelectTest {


    @Inject
    private GraphTemplate template;

    @Inject
    private Graph graph;

    private Person otavio;
    private Person ada;
    private Person poliana;

    @AfterEach
    public void after() {
        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);
    }

    @BeforeEach
    public void before() {
        otavio = template.insert(Person.builder().withName("Otavio")
                .withAge(35).build());
        ada = template.insert(Person.builder().withName("Ada")
                .withAge(12).build());
        poliana = template.insert(Person.builder().withName("Poliana")
                .withAge(30).build());
    }

    @Test
    public void shouldExecuteSelectFrom() {

        List<Person> people = template.select(Person.class).result();

        Assertions.assertNotNull(people);

        assertThat(people).hasSize(3).map(Person::getName)
                .contains(otavio.getName(), ada.getName(), poliana.getName());
    }


    @Test
    public void shouldSelectOrderAsc() {

        List<Person> people = template.select(Person.class).orderBy("name").asc().result();
        assertThat(people).hasSize(3).map(Person::getName)
                .containsExactly(ada.getName(), otavio.getName(), poliana.getName());
    }

    @Test
    public void shouldSelectOrderDesc() {

        List<Person> people = template.select(Person.class).orderBy("name").desc().result();
        assertThat(people).hasSize(3).map(Person::getName)
                .containsExactly(poliana.getName(), otavio.getName(), ada.getName());
    }

    @Test
    public void shouldSelectLimit() {
        List<Person> people = template.select(Person.class).orderBy("name").desc().limit(2).result();

        assertThat(people).hasSize(2).map(Person::getName)
                .containsExactly(poliana.getName(), otavio.getName());
    }

    @Test
    public void shouldSelectStart() {
        List<Person> people = template.select(Person.class).orderBy("name").desc().skip(1).result();

        assertThat(people).hasSize(2).map(Person::getName)
                .containsExactly(otavio.getName(), ada.getName());
    }

    @Test
    public void shouldSelectWhereNameEq() {
        Optional<Person> person = template.select(Person.class)
                .where("name").eq("Otavio").singleResult();

        Assertions.assertNotNull(person);
        Assertions.assertTrue(person.isPresent());
        assertEquals(person.map(Person::getName).orElse(""), otavio.getName());
    }

    @Test
    public void shouldSelectWhereNameGt() {
        List<Person> people = template.select(Person.class).where("age")
                .gt(30).result();
        assertThat(people).hasSize(1).map(Person::getName)
                .contains(otavio.getName());
    }

    @Test
    public void shouldSelectWhereNameGte() {
        List<Person> people = template.select(Person.class).where("age")
                .gte(30).result();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains(otavio.getName(), poliana.getName());
    }

    @Test
    public void shouldSelectWhereNameLt() {
        List<Person> people = template.select(Person.class).where("age")
                .lt(30).result();
        assertThat(people).hasSize(1).map(Person::getName)
                .contains(ada.getName());
    }

    @Test
    public void shouldSelectWhereNameLte() {
        List<Person> people = template.select(Person.class).where("age")
                .lte(30).result();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains(ada.getName(), poliana.getName());
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        List<Person> people = template.select(Person.class).where("age")
                .between(30, 40).result();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains(otavio.getName(), poliana.getName());
    }

    @Test
    public void shouldSelectWhereNameNot() {
        List<Person> people = template.select(Person.class).where("name")
                .not().eq("Otavio").result();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains(ada.getName(), poliana.getName());
    }

    @Test
    public void shouldSelectWhereNameAnd() {
        List<Person> people = template.select(Person.class).where("name")
                .not().eq("Otavio").and("age").lt(30).result();
        assertThat(people).hasSize(1).map(Person::getName)
                .contains(ada.getName());
    }

    @Test
    public void shouldSelectWhereNameOr() {
        List<Person> people = template.select(Person.class).where("name")
                .not().eq("Otavio").or("age").lt(30).result();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains(ada.getName(), poliana.getName());
    }

    @Test
    public void shouldConvertField() {
        List<Person> people = template.select(Person.class).where("name")
                .not().eq("Otavio").or("age").lt("30").result();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains(ada.getName(), poliana.getName());
    }

    @Test
    public void shouldUseAttributeConverter() {
        Job job = new Job();
        job.setCity("Salvador");
        job.setDescription("Java Developer");
        Money salary = new Money("BRL", BigDecimal.TEN);

        Worker worker = new Worker();
        worker.setName("Otavio");
        worker.setJob(job);
        worker.setSalary(salary);
        template.insert(worker);
        Optional<Worker> otavio = template.select(Worker.class).where("salary").eq(salary).singleResult();

        Assertions.assertNotNull(otavio);
        Assertions.assertTrue(otavio.isPresent());
        assertEquals(otavio.map(Worker::getName).orElse(""), worker.getName());
    }

    @Test
    public void shouldFindByIdUsingQuery() {
        Optional<Person> person = template.select(Person.class).where("id").eq(poliana.getId()).singleResult();

        Assertions.assertNotNull(person);
        Assertions.assertTrue(person.isPresent());
        assertEquals(person.map(Person::getName).orElse(""), poliana.getName());
    }

    @Test
    public void shouldResult() {
        List<Person> people = template.select(Person.class).result();
        Assertions.assertNotNull(people);
        assertThat(people).hasSize(3).map(Person::getName)
                .contains(otavio.getName(), ada.getName(), poliana.getName());
    }

    @Test
    public void shouldStream() {
        Stream<Person> people = template.select(Person.class).stream();
        Assertions.assertNotNull(people);


        assertThat(people).hasSize(3).map(Person::getName)
                .contains(otavio.getName(), ada.getName(), poliana.getName());
    }

    @Test
    public void shouldSingleResult() {
        Assertions.assertThrows(NonUniqueResultException.class, () -> template.select(Person.class).singleResult());

        Optional<Person> person = template.select(Person.class).where("name").eq("Jono").singleResult();
        Assertions.assertNotNull(person);
        Assertions.assertTrue(person.isEmpty());

        person = template.select(Person.class).where("name").eq("Otavio").singleResult();
        Assertions.assertNotNull(person);
        Assertions.assertTrue(person.isPresent());
    }


}
