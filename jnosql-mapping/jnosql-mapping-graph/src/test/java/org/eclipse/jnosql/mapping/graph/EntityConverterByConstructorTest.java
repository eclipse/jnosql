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
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.graph.entities.Car;
import org.eclipse.jnosql.mapping.graph.entities.Hero;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import java.time.Year;

@EnableAutoWeld
@AddPackages(value = {Converters.class, Transactional.class})
@AddPackages(BookRepository.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
class EntityConverterByConstructorTest {

    @Inject
    private Graph graph;

    @Inject
    private Converters converters;

    @Inject
    private EntitiesMetadata entities;

    @Test
    public void shouldCreateRecordEntity() {
        Vertex vertex = graph.addVertex("Car");
        vertex.property("model", "500");
        vertex.property("manufacturer", "Fiat");
        vertex.property("year", Year.now().getValue());
        EntityMetadata metadata = entities.get(Car.class);
        EntityConverterByContructor<Car> converter = EntityConverterByContructor.of(metadata, vertex, converters);
        Car car = converter.get();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(car).isNotNull();
            soft.assertThat(car.model()).isEqualTo("500");
            soft.assertThat(car.manufacturer()).isEqualTo("Fiat");
            soft.assertThat(car.year()).isEqualTo(Year.now().getValue());
        });
    }

    @Test
    public void shouldCreateRecordClass() {
        Vertex vertex = graph.addVertex("Hero");
        vertex.property("name", "Super man");
        EntityMetadata metadata = entities.get(Hero.class);
        EntityConverterByContructor<Hero> converter = EntityConverterByContructor.of(metadata, vertex, converters);
        Hero hero = converter.get();

        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(hero).isNotNull();
            soft.assertThat(hero.name()).isEqualTo("Super man");
        });
    }
}