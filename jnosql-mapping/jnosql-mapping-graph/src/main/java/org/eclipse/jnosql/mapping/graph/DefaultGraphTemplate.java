package org.eclipse.jnosql.mapping.graph;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.eclipse.jnosql.communication.graph.GraphDatabaseManager;
import org.eclipse.jnosql.mapping.Database;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;
import org.eclipse.jnosql.mapping.semistructured.EventPersistManager;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.eclipse.jnosql.mapping.DatabaseType.GRAPH;

@Default
@ApplicationScoped
@Database(GRAPH)
class DefaultGraphTemplate extends AbstractGraphTemplate {

    private final EntityConverter converter;

    private final GraphDatabaseManager manager;

    private final EventPersistManager eventManager;

    private final EntitiesMetadata entities;

    private final Converters converters;
    private final Graph graph;


    @Inject
    DefaultGraphTemplate(EntityConverter converter, Graph graph,
                         EventPersistManager eventManager,
                         EntitiesMetadata entities, Converters converters) {
        this.converter = converter;
        this.graph = graph;
        this.eventManager = eventManager;
        this.entities = entities;
        this.converters = converters;
        this.manager = GraphDatabaseManager.of(graph);
    }

    DefaultGraphTemplate() {
        this(null, null, null, null, null);
    }

    @Override
    protected EntityConverter converter() {
        return converter;
    }

    @Override
    protected GraphDatabaseManager manager() {
        return manager;
    }

    @Override
    protected GraphTraversalSource traversal() {
        return graph.traversal();
    }

    @Override
    protected Graph graph() {
        return graph;
    }

    @Override
    protected EventPersistManager eventManager() {
        return eventManager;
    }

    @Override
    protected EntitiesMetadata entities() {
        return entities;
    }

    @Override
    protected Converters converters() {
        return converters;
    }
}
