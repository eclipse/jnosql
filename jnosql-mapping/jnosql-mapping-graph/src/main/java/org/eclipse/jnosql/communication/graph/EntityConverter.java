package org.eclipse.jnosql.communication.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;

import java.util.function.Function;

enum EntityConverter implements Function<Vertex, CommunicationEntity>{
    INSTANCE;


    @Override
    public CommunicationEntity apply(Vertex vertex) {
        var entity = CommunicationEntity.of(vertex.label());
        vertex.properties().forEachRemaining(p -> entity.add(p.key(), p.value()));
        entity.add(DefaultGraphDatabaseManager.ID_PROPERTY, vertex.id());
        return entity;
    }
}
