package org.eclipse.jnosql.mapping.graph;

import jakarta.inject.Inject;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.graph.entities.Car;
import org.eclipse.jnosql.mapping.graph.entities.Hero;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddPackages(value = {Convert.class, Transactional.class})
@AddPackages(BookRepository.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
class EntityConverterByContructorTest {

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