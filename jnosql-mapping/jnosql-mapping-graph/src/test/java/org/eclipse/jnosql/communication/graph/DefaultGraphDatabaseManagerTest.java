package org.eclipse.jnosql.communication.graph;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultGraphDatabaseManagerTest {

    private GraphDatabaseManager databaseManager;

    @BeforeEach
    void setUp(){
        Graph graph = GraphSupplier.INSTANCE.get();
        this.databaseManager = DefaultGraphDatabaseManager.of(graph);
    }

    @Test
    void shouldInsertEntity(){
        var entity = CommunicationEntity.of("Person");
        entity.add("name", "Ada Lovelace");
        entity.add("age", 30);
        var communicationEntity = databaseManager.insert(entity);
        assertNotNull(communicationEntity);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(communicationEntity.find("name", String.class)).get().isEqualTo("Ada Lovelace");
            softly.assertThat(communicationEntity.find("age", int.class)).get().isEqualTo(30);
            softly.assertThat(communicationEntity.find(DefaultGraphDatabaseManager.ID_PROPERTY)).isPresent();
        });
    }
}