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
package org.eclipse.jnosql.communication.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;

import java.util.function.Function;

public enum CommunicationEntityConverter implements Function<Vertex, CommunicationEntity>{
    INSTANCE;


    @Override
    public CommunicationEntity apply(Vertex vertex) {
        var entity = CommunicationEntity.of(vertex.label());
        vertex.properties().forEachRemaining(p -> entity.add(p.key(), p.value()));
        entity.add(DefaultGraphDatabaseManager.ID_PROPERTY, vertex.id());
        return entity;
    }
}
