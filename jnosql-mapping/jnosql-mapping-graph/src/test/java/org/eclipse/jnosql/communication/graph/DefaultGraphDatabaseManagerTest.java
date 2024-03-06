package org.eclipse.jnosql.communication.graph;

import net.datafaker.Faker;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class DefaultGraphDatabaseManagerTest {

    private GraphDatabaseManager databaseManager;

    private final Faker faker = new Faker();

    @BeforeEach
    void setUp(){
        Graph graph = GraphSupplier.INSTANCE.get();
        this.databaseManager = DefaultGraphDatabaseManager.of(graph);
    }

    @Test
    void shouldInsertEntity(){
        String name = faker.name().fullName();
        var age = faker.number().randomDigit();
        var entity = CommunicationEntity.of("Person");
        entity.add("name", name);
        entity.add("age", age);
        var communicationEntity = databaseManager.insert(entity);
        assertNotNull(communicationEntity);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(communicationEntity.find("name", String.class)).get().isEqualTo(name);
            softly.assertThat(communicationEntity.find("age", int.class)).get().isEqualTo(age);
            softly.assertThat(communicationEntity.find(DefaultGraphDatabaseManager.ID_PROPERTY)).isPresent();
        });
    }

    @Test
    void shouldInsertEntities(){
        String name = faker.name().fullName();
        var age = faker.number().randomDigit();
        var entity = CommunicationEntity.of("Person");
        entity.add("name", name);
        entity.add("age", age);

        String name2 = faker.name().fullName();
        var age2 = faker.number().randomDigit();
        var entity2 = CommunicationEntity.of("Person");
        entity2.add("name", name2);
        entity2.add("age", age2);

        var communicationEntities = StreamSupport
                .stream(databaseManager.insert(List.of(entity, entity2)).spliterator(), false).toList();

        assertNotNull(communicationEntities);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(communicationEntities).hasSize(2);
            softly.assertThat(communicationEntities.get(0).find("name", String.class)).get().isEqualTo(name);
            softly.assertThat(communicationEntities.get(0).find("age", int.class)).get().isEqualTo(age);
            softly.assertThat(communicationEntities.get(0).find(DefaultGraphDatabaseManager.ID_PROPERTY)).isPresent();

            softly.assertThat(communicationEntities.get(1).find("name", String.class)).get().isEqualTo(name2);
            softly.assertThat(communicationEntities.get(1).find("age", int.class)).get().isEqualTo(age2);
            softly.assertThat(communicationEntities.get(1).find(DefaultGraphDatabaseManager.ID_PROPERTY)).isPresent();
        });

    }
}