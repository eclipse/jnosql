package org.eclipse.jnosql.mapping.graph;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.graph.GraphDatabaseManager;
import org.eclipse.jnosql.communication.semistructured.DatabaseManager;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.semistructured.AbstractSemistructuredTemplate;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;
import org.eclipse.jnosql.mapping.semistructured.EventPersistManager;

@Default
@ApplicationScoped
class DefaultGraphTemplate extends AbstractSemistructuredTemplate implements GraphTemplate {

    private final EntityConverter converter;

    private final GraphDatabaseManager manager;

    private final EventPersistManager eventManager;

    private final EntitiesMetadata entities;

    private final Converters converters;


    @Inject
    DefaultGraphTemplate(EntityConverter converter, GraphDatabaseManager manager,
                         EventPersistManager eventManager,
                         EntitiesMetadata entities, Converters converters) {
        this.converter = converter;
        this.manager = manager;
        this.eventManager = eventManager;
        this.entities = entities;
        this.converters = converters;
    }

    DefaultGraphTemplate() {
        this(null, null, null, null, null);
    }

    @Override
    protected EntityConverter converter() {
        return converter;
    }

    @Override
    protected DatabaseManager manager() {
        return manager;
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
