/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import jakarta.nosql.mapping.EntityPostPersist;
import jakarta.nosql.mapping.EntityPrePersist;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.DefaultEntityPostPersist;
import org.eclipse.jnosql.mapping.DefaultEntityPrePersist;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
class DefaultGraphEventPersistManager implements GraphEventPersistManager {


    @Inject
    private Event<GraphEntityPrePersist> graphEntityPrePersistEvent;

    @Inject
    private Event<GraphEntityPostPersist> graphEntityPostPersistEvent;

    @Inject
    private Event<EntityPrePersist> entityPrePersistEvent;

    @Inject
    private Event<EntityPostPersist> entityPostPersistEvent;

    @Inject
    private Event<EntityGraphPrePersist> entityGraphPrePersist;

    @Inject
    private Event<EntityGraphPostPersist> entityGraphPostPersist;

    @Override
    public void firePreGraph(Vertex entity) {
        graphEntityPrePersistEvent.fire(GraphEntityPrePersist.of(entity));
    }

    @Override
    public void firePostGraph(Vertex entity) {
        graphEntityPostPersistEvent.fire(GraphEntityPostPersist.of(entity));
    }

    @Override
    public <T> void firePreEntity(T entity) {
        entityPrePersistEvent.fire(new DefaultEntityPrePersist(entity));
    }

    @Override
    public <T> void firePostEntity(T entity) {
        entityPostPersistEvent.fire(new DefaultEntityPostPersist(entity));
    }

    @Override
    public <T> void firePreGraphEntity(T entity) {
        entityGraphPrePersist.fire(EntityGraphPrePersist.of(entity));
    }

    @Override
    public <T> void firePostGraphEntity(T entity) {
        entityGraphPostPersist.fire(EntityGraphPostPersist.of(entity));
    }
}
