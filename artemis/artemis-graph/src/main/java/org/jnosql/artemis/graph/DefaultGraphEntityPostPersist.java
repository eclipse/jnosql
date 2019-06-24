/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Objects;

/**
 * The default implementation of {@link GraphEntityPostPersist}
 */
class DefaultGraphEntityPostPersist implements GraphEntityPostPersist {

    private final Vertex entity;

    DefaultGraphEntityPostPersist(Vertex entity) {
        this.entity = entity;
    }

    @Override
    public Vertex getVertex() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultGraphEntityPostPersist)) {
            return false;
        }
        DefaultGraphEntityPostPersist that = (DefaultGraphEntityPostPersist) o;
        return Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity);
    }

    @Override
    public String toString() {
        return  "DefaultGraphEntityPostPersist{" + "entity=" + entity +
                '}';
    }
}
