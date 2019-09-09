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
package org.eclipse.jnosql.artemis.graph;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.reflection.ClassMappings;
import org.apache.tinkerpop.gremlin.structure.Graph;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
class DefaultGraphConverter extends AbstractGraphConverter implements GraphConverter {

    private ClassMappings classMappings;

    private Converters converters;

    private Instance<Graph> graph;

    @Inject
    DefaultGraphConverter(ClassMappings classMappings, Converters converters,
                          Instance<Graph> graph) {
        this.classMappings = classMappings;
        this.converters = converters;
        this.graph = graph;
    }

    DefaultGraphConverter() {
    }

    @Override
    protected ClassMappings getClassMappings() {
        return classMappings;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }

    @Override
    protected Graph getGraph() {
        return graph.get();
    }
}
