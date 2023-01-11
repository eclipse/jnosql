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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CDIExtension
public class MapperDeleteTest {

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
    public void shouldReturnDeleteFrom() {
        template.delete(Person.class).execute();
        List<Person> people = template.select(Person.class).result();
        assertThat(people).isEmpty();
    }

    @Test
    public void shouldDeleteWhereNameEq() {
        template.delete(Person.class).where("name").eq(otavio.getName()).execute();
        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains(ada.getName(), poliana.getName());
    }

    @Test
    public void shouldThrownAnExceptionWhenDeleteWhereNameLike() {
        assertThrows(UnsupportedOperationException.class, () ->
                template.delete(Person.class).where("name").like("test"));
        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(3).map(Person::getName)
                .contains(ada.getName(), poliana.getName(), otavio.getName());
    }

    @Test
    public void shouldDeleteWhereGt() {
        template.delete(Person.class).where("age").gt(30).execute();

        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains(ada.getName(), poliana.getName());
    }

    @Test
    public void shouldDeleteWhereGte() {
        template.delete(Person.class).where("age").gte(30).execute();

        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(1).map(Person::getName)
                .contains(ada.getName());

    }

    @Test
    public void shouldDeleteWhereLt() {
        template.delete(Person.class).where("age").lt(30).execute();

        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains(otavio.getName(), poliana.getName());
    }

    @Test
    public void shouldDeleteWhereLte() {

        template.delete(Person.class).where("age").lte(30).execute();

        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(1).map(Person::getName)
                .contains(otavio.getName());
    }

    @Test
    public void shouldDeleteWhereBetween() {
        template.delete(Person.class).where("age").between(29, 40).execute();
        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(1).map(Person::getName)
                .contains(ada.getName());
    }

    @Test
    public void shouldDeleteWhereNot() {
        template.delete(Person.class).where("name").not().eq(otavio.getName()).execute();
        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(1).map(Person::getName)
                .contains(otavio.getName());
    }


    @Test
    public void shouldDeleteWhereAnd() {
        template.delete(Person.class).where("name").eq(otavio.getName())
                .and("age").gte(20).execute();
        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(2).map(Person::getName)
                .contains(ada.getName(), poliana.getName());
    }

    @Test
    public void shouldDeleteWhereOr() {
        template.delete(Person.class).where("name").eq(otavio.getName())
                .or("age").gte(20).execute();
        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(1).map(Person::getName)
                .contains(ada.getName());
    }

    @Test
    public void shouldConvertField() {
         template.delete(Person.class).where("name")
                .not().eq("Otavio").or("age").lt("30").execute();

        List<Person> people = template.select(Person.class).result();
        assertThat(people).hasSize(1).map(Person::getName)
                .contains(otavio.getName());
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
        template.delete(Worker.class).where("salary").eq(salary).execute();

        List<Worker> result = template.select(Worker.class).result();
        assertThat(result).isEmpty();
    }
}
