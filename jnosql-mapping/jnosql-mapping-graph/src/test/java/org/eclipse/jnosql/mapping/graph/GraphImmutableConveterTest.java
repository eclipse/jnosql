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
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.graph.entities.Car;
import org.eclipse.jnosql.mapping.graph.entities.Hero;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.reflection.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

@EnableAutoWeld
@AddPackages(value = {Convert.class, Transactional.class})
@AddPackages(BookRepository.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
class GraphImmutableConveterTest {

    @Inject
    private GraphConverter converter;

    @Inject
    private Graph graph;

    @BeforeEach
    public void setUp() {
        this.graph.traversal().V().toList().forEach(Vertex::remove);
        this.graph.traversal().E().toList().forEach(Edge::remove);
    }

    @Test
    public void shouldConvertEntity() {
        Car car = new Car(null, "SF90", "Ferrari", 2023);
        Vertex vertex = this.converter.toVertex(car);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(vertex).isNotNull();
            soft.assertThat(vertex.label()).isEqualTo("Car");
            soft.assertThat(vertex.id()).isNotNull();
            soft.assertThat(vertex.property("model")).isNotNull()
                    .extracting(VertexProperty::value).isEqualTo("SF90");
            soft.assertThat(vertex.property("year")).isNotNull()
                    .extracting(VertexProperty::value).isEqualTo(2023);
            soft.assertThat(vertex.property("manufacturer")).isNotNull()
                    .extracting(VertexProperty::value).isEqualTo("Ferrari");
        });
    }

    @Test
    public void shouldConvertVertex() {
        Vertex vertex = graph.addVertex("Car");
        vertex.property("model", "SF90");
        vertex.property("manufacturer", "Ferrari");
        vertex.property("year", 2023);

        Car car = converter.toEntity(vertex);
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(car).isNotNull();
            soft.assertThat(car.model()).isEqualTo("SF90");
            soft.assertThat(car.manufacturer()).isEqualTo("Ferrari");
            soft.assertThat(car.year()).isEqualTo(2023);
        });
    }

    @Test
    public void shouldConvertToExistRecordEntity() {
        Vertex vertex = graph.addVertex("Car");
        vertex.property("model", "SF90");
        vertex.property("manufacturer", "Ferrari");
        vertex.property("year", 2023);

        Car car = new Car(null, null, null, 0);
        Car result = converter.toEntity(car, vertex);
        assertNotSame(result, car);
        assertSoftly(soft -> {
            soft.assertThat(result.model()).isNull();
            soft.assertThat(result.manufacturer()).isNull();
            soft.assertThat(result.plate()).isNull();
            soft.assertThat(result.year()).isNull();
            //
            soft.assertThat(result.model()).isEqualTo("SF90");
            soft.assertThat(result.manufacturer()).isEqualTo("Ferrari");
            soft.assertThat(result.plate()).isEqualTo(vertex.id());
            soft.assertThat(result.year()).isEqualTo(2023);
        });
    }

    @Test
    public void shouldConvertToExistEntity() {
        Vertex vertex = graph.addVertex("Hero");
        vertex.property("name", "Iron Man");
        Hero hero = new Hero(null, null);
        Hero result = converter.toEntity(hero, vertex);
        assertSame(hero, result);
        assertSoftly(soft -> {
                    soft.assertThat(hero.id()).isEqualTo(vertex.id());
                    soft.assertThat(hero.name()).isEqualTo("Iron Man");
                }
        );
    }
}
